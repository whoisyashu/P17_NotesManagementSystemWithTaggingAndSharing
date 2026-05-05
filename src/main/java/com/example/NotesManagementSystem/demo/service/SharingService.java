package com.example.NotesManagementSystem.demo.service;

import java.util.List;

import com.example.NotesManagementSystem.demo.dto.shared.ShareRequest;
import com.example.NotesManagementSystem.demo.dto.shared.SharedNoteResponse;
import com.example.NotesManagementSystem.demo.entity.Group;
import com.example.NotesManagementSystem.demo.entity.Note;
import com.example.NotesManagementSystem.demo.entity.SharedNote;
import com.example.NotesManagementSystem.demo.entity.User;
import com.example.NotesManagementSystem.demo.entity.enums.NotePermission;
import com.example.NotesManagementSystem.demo.exception.ForbiddenOperationException;
import com.example.NotesManagementSystem.demo.exception.ResourceNotFoundException;
import com.example.NotesManagementSystem.demo.repository.GroupRepository;
import com.example.NotesManagementSystem.demo.repository.NoteRepository;
import com.example.NotesManagementSystem.demo.repository.SharedNoteRepository;
import com.example.NotesManagementSystem.demo.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SharingService {

    private final SharedNoteRepository sharedNoteRepository;
    private final NoteRepository noteRepository;
    private final UserRepository userRepository;
    private final GroupRepository groupRepository;
    private final CurrentUserService currentUserService;
    private final NoteAccessService noteAccessService;
    private final NotificationService notificationService;
    private final ApiMapper apiMapper;

    @Transactional
    public SharedNoteResponse share(ShareRequest request) {
        Note note = noteRepository.findWithDetailsById(request.getNoteId())
                .orElseThrow(() -> new ResourceNotFoundException("Note not found"));
        User currentUser = currentUserService.getCurrentUser();

        if (!noteAccessService.canEdit(note, currentUser.getUsername())) {
            throw new ForbiddenOperationException("You do not have permission to share this note");
        }

        if ((request.getUserId() == null && request.getGroupId() == null) ||
                (request.getUserId() != null && request.getGroupId() != null)) {
            throw new ForbiddenOperationException("Share with either a user or a group");
        }

        SharedNote sharedNote = new SharedNote();
        sharedNote.setNote(note);
        sharedNote.setSharedBy(currentUser);
        sharedNote.setPermission(request.getPermission() == null ? NotePermission.VIEW : request.getPermission());

        if (request.getUserId() != null) {
            User sharedWithUser = userRepository.findById(request.getUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found"));
            sharedNote.setSharedWithUser(sharedWithUser);
        }

        if (request.getGroupId() != null) {
            Group group = groupRepository.findById(request.getGroupId())
                    .orElseThrow(() -> new ResourceNotFoundException("Group not found"));
            sharedNote.setSharedWithGroup(group);
        }

        SharedNote saved = sharedNoteRepository.save(sharedNote);
        notificationService.notifyNoteShared(note, currentUser, saved);
        return apiMapper.toSharedNoteResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<SharedNoteResponse> listSharedNotesForCurrentUser() {
        User currentUser = currentUserService.getCurrentUser();
        return sharedNoteRepository.findAll().stream()
                .filter(sharedNote -> sharedNote.getSharedWithUser() != null && sharedNote.getSharedWithUser().getId().equals(currentUser.getId())
                        || sharedNote.getSharedWithGroup() != null && sharedNote.getSharedWithGroup().getMembers().stream().anyMatch(member -> member.getId().equals(currentUser.getId())))
                .map(apiMapper::toSharedNoteResponse)
                .toList();
    }
}