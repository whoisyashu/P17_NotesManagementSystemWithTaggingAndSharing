package com.example.NotesManagementSystem.demo.service;

import java.util.List;

import com.example.NotesManagementSystem.demo.dto.note.NoteResponse;
import com.example.NotesManagementSystem.demo.repository.NoteRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final NoteRepository noteRepository;
    private final CurrentUserService currentUserService;
    private final ApiMapper apiMapper;

    @Transactional(readOnly = true)
    public List<NoteResponse> search(String query) {
        String username = currentUserService.getCurrentUsername();
        return noteRepository.searchAccessibleNotes(username, query, PageRequest.of(0, 50)).stream()
                .map(apiMapper::toNoteResponse)
                .toList();
    }
}