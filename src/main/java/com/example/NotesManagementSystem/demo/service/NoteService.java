package com.example.NotesManagementSystem.demo.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.example.NotesManagementSystem.demo.dto.note.NoteCreateRequest;
import com.example.NotesManagementSystem.demo.dto.note.NoteResponse;
import com.example.NotesManagementSystem.demo.dto.note.NoteUpdateRequest;
import com.example.NotesManagementSystem.demo.entity.Note;
import com.example.NotesManagementSystem.demo.entity.NoteTag;
import com.example.NotesManagementSystem.demo.entity.Tag;
import com.example.NotesManagementSystem.demo.entity.User;
import com.example.NotesManagementSystem.demo.entity.VersionHistory;
import com.example.NotesManagementSystem.demo.exception.ForbiddenOperationException;
import com.example.NotesManagementSystem.demo.exception.ResourceNotFoundException;
import com.example.NotesManagementSystem.demo.repository.NoteRepository;
import com.example.NotesManagementSystem.demo.repository.NoteTagRepository;
import com.example.NotesManagementSystem.demo.repository.TagRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NoteService {

    private final NoteRepository noteRepository;
    private final NoteTagRepository noteTagRepository;
    private final TagRepository tagRepository;
    private final CurrentUserService currentUserService;
    private final TagService tagService;
    private final NoteAccessService noteAccessService;
    private final VersionHistoryService versionHistoryService;
    private final NotificationService notificationService;
    private final CollaborationBroadcastService collaborationBroadcastService;
    private final ApiMapper apiMapper;

    @Transactional
    public NoteResponse create(NoteCreateRequest request) {
        User currentUser = currentUserService.getCurrentUser();
        Note note = new Note();
        note.setTitle(request.getTitle().trim());
        note.setContent(request.getContent().trim());
        note.setOwner(currentUser);

        syncTags(note, request.getTagIds());
        note = noteRepository.save(note);
        
        return getById(note.getId());
    }

    @Transactional(readOnly = true)
    public Page<NoteResponse> list(Pageable pageable) {
        String username = currentUserService.getCurrentUsername();
        return noteRepository.findAccessibleNotes(username, pageable)
                .map(apiMapper::toNoteResponse);
    }

    @Transactional(readOnly = true)
    public NoteResponse getById(Long id) {
        Note note = getAccessibleNote(id);
        return apiMapper.toNoteResponse(note);
    }

    @Transactional
    public NoteResponse update(Long id, NoteUpdateRequest request) {
        Note note = getEditableNote(id);
        User currentUser = currentUserService.getCurrentUser();

        versionHistoryService.createSnapshot(note, currentUser);

        note.setTitle(request.getTitle().trim());
        note.setContent(request.getContent().trim());
        syncTags(note, request.getTagIds());
        note = noteRepository.save(note);

        NoteResponse response = apiMapper.toNoteResponse(noteRepository.findWithDetailsById(id).orElseThrow());
        collaborationBroadcastService.broadcastNoteUpdate(response);
        notificationService.notifyNoteEdited(note, currentUser);
        return response;
    }

    @Transactional
    public void delete(Long id) {
        Note note = getEditableNote(id);
        noteRepository.delete(note);
    }

    @Transactional(readOnly = true)
    public List<NoteResponse> listOwnedNotes() {
        return noteRepository.findByOwnerUsernameOrderByUpdatedAtDesc(currentUserService.getCurrentUsername()).stream()
                .map(note -> apiMapper.toNoteResponse(noteRepository.findWithDetailsById(note.getId()).orElseThrow()))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<VersionHistory> getVersions(Long noteId) {
        Note note = getAccessibleNote(noteId);
        return List.copyOf(note.getVersions());
    }

    @Transactional
    public NoteResponse restoreVersion(Long noteId, Long versionId) {
        Note note = getEditableNote(noteId);
        User currentUser = currentUserService.getCurrentUser();
        VersionHistory versionHistory = versionHistoryService.getVersion(versionId);

        if (!versionHistory.getNote().getId().equals(noteId)) {
            throw new ForbiddenOperationException("Version does not belong to the note");
        }

        versionHistoryService.createSnapshot(note, currentUser);
        note.setTitle(versionHistory.getTitle());
        note.setContent(versionHistory.getContent());
        note = noteRepository.save(note);

        NoteResponse response = apiMapper.toNoteResponse(noteRepository.findWithDetailsById(noteId).orElseThrow());
        collaborationBroadcastService.broadcastNoteUpdate(response);
        notificationService.notifyNoteEdited(note, currentUser);
        return response;
    }

    @Transactional
    public void addTag(Long noteId, Long tagId) {
        Note note = getEditableNote(noteId);
        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new ResourceNotFoundException("Tag not found"));
        if (noteTagRepository.findByNoteIdAndTagId(noteId, tagId).isPresent()) {
            return;
        }

        NoteTag noteTag = new NoteTag();
        noteTag.setNote(note);
        noteTag.setTag(tag);
        noteTagRepository.save(noteTag);
    }

    @Transactional
    public void removeTag(Long noteId, Long tagId) {
        getEditableNote(noteId);
        noteTagRepository.deleteByNoteIdAndTagId(noteId, tagId);
    }

    private Note getAccessibleNote(Long id) {
        Note note = noteRepository.findWithDetailsById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Note not found"));
        if (!noteAccessService.canView(note, currentUserService.getCurrentUsername())) {
            throw new ForbiddenOperationException("You do not have access to this note");
        }
        return note;
    }

    private Note getEditableNote(Long id) {
        Note note = noteRepository.findWithDetailsById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Note not found"));
        if (!noteAccessService.canEdit(note, currentUserService.getCurrentUsername())) {
            throw new ForbiddenOperationException("You do not have permission to edit this note");
        }
        return note;
    }

    private void syncTags(Note note, List<Long> tagIds) {
        Set<Tag> tags = new HashSet<>(tagService.findTagsByIds(tagIds));
        note.getNoteTags().clear();
        for (Tag tag : tags) {
            NoteTag noteTag = new NoteTag();
            noteTag.setNote(note);
            noteTag.setTag(tag);
            note.getNoteTags().add(noteTag);
        }
    }
}