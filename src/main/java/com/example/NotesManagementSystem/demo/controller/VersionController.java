package com.example.NotesManagementSystem.demo.controller;

import java.util.List;

import com.example.NotesManagementSystem.demo.dto.note.NoteResponse;
import com.example.NotesManagementSystem.demo.dto.version.RestoreVersionRequest;
import com.example.NotesManagementSystem.demo.dto.version.VersionHistoryResponse;
import com.example.NotesManagementSystem.demo.service.NoteService;
import com.example.NotesManagementSystem.demo.service.VersionHistoryService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/notes")
@RequiredArgsConstructor
public class VersionController {

    private final VersionHistoryService versionHistoryService;
    private final NoteService noteService;

    @GetMapping("/{id}/versions")
    public ResponseEntity<List<VersionHistoryResponse>> listVersions(@PathVariable Long id) {
        return ResponseEntity.ok(versionHistoryService.listVersions(id));
    }

    @PostMapping("/{id}/restore")
    public ResponseEntity<NoteResponse> restore(@PathVariable Long id, @Valid @RequestBody RestoreVersionRequest request) {
        return ResponseEntity.ok(noteService.restoreVersion(id, request.getVersionId()));
    }
}