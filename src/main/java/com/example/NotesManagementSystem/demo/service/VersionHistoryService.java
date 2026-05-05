package com.example.NotesManagementSystem.demo.service;

import java.util.List;

import com.example.NotesManagementSystem.demo.dto.version.VersionHistoryResponse;
import com.example.NotesManagementSystem.demo.entity.Note;
import com.example.NotesManagementSystem.demo.entity.User;
import com.example.NotesManagementSystem.demo.entity.VersionHistory;
import com.example.NotesManagementSystem.demo.exception.ResourceNotFoundException;
import com.example.NotesManagementSystem.demo.repository.VersionHistoryRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class VersionHistoryService {

    private final VersionHistoryRepository versionHistoryRepository;
    private final ApiMapper apiMapper;

    @Transactional
    public VersionHistory createSnapshot(Note note, User modifiedBy) {
        int nextVersion = versionHistoryRepository.findTopByNoteIdOrderByVersionNumberDesc(note.getId())
                .map(VersionHistory::getVersionNumber)
                .orElse(0) + 1;

        VersionHistory versionHistory = new VersionHistory();
        versionHistory.setNote(note);
        versionHistory.setVersionNumber(nextVersion);
        versionHistory.setTitle(note.getTitle());
        versionHistory.setContent(note.getContent());
        versionHistory.setModifiedBy(modifiedBy);
        return versionHistoryRepository.save(versionHistory);
    }

    @Transactional(readOnly = true)
    public List<VersionHistoryResponse> listVersions(Long noteId) {
        return versionHistoryRepository.findByNoteIdOrderByVersionNumberDesc(noteId).stream()
                .map(apiMapper::toVersionResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public VersionHistory getVersion(Long versionId) {
        return versionHistoryRepository.findById(versionId)
                .orElseThrow(() -> new ResourceNotFoundException("Version not found"));
    }
}