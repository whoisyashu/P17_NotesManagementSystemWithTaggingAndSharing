package com.example.NotesManagementSystem.demo.service;

import java.util.List;

import com.example.NotesManagementSystem.demo.dto.comment.CommentCreateRequest;
import com.example.NotesManagementSystem.demo.dto.comment.CommentResponse;
import com.example.NotesManagementSystem.demo.entity.Comment;
import com.example.NotesManagementSystem.demo.entity.Note;
import com.example.NotesManagementSystem.demo.entity.User;
import com.example.NotesManagementSystem.demo.exception.ForbiddenOperationException;
import com.example.NotesManagementSystem.demo.exception.ResourceNotFoundException;
import com.example.NotesManagementSystem.demo.repository.CommentRepository;
import com.example.NotesManagementSystem.demo.repository.NoteRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final NoteRepository noteRepository;
    private final CurrentUserService currentUserService;
    private final NoteAccessService noteAccessService;
    private final ApiMapper apiMapper;

    @Transactional
    public CommentResponse addComment(CommentCreateRequest request) {
        Note note = noteRepository.findWithDetailsById(request.getNoteId())
                .orElseThrow(() -> new ResourceNotFoundException("Note not found"));
        User currentUser = currentUserService.getCurrentUser();

        if (!noteAccessService.canView(note, currentUser.getUsername())) {
            throw new ForbiddenOperationException("You do not have access to comment on this note");
        }

        Comment comment = new Comment();
        comment.setNote(note);
        comment.setAuthor(currentUser);
        comment.setContent(request.getContent().trim());
        return apiMapper.toCommentResponse(commentRepository.save(comment));
    }

    @Transactional(readOnly = true)
    public List<CommentResponse> listByNoteId(Long noteId) {
        return commentRepository.findByNoteIdOrderByCreatedAtAsc(noteId).stream()
                .map(apiMapper::toCommentResponse)
                .toList();
    }
}