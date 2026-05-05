package com.example.NotesManagementSystem.demo.controller;

import java.util.List;

import com.example.NotesManagementSystem.demo.dto.shared.ShareRequest;
import com.example.NotesManagementSystem.demo.dto.shared.SharedNoteResponse;
import com.example.NotesManagementSystem.demo.service.SharingService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ShareController {

    private final SharingService sharingService;

    @PostMapping("/share")
    public ResponseEntity<SharedNoteResponse> share(@Valid @RequestBody ShareRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(sharingService.share(request));
    }

    @GetMapping("/shared")
    public ResponseEntity<List<SharedNoteResponse>> listShared() {
        return ResponseEntity.ok(sharingService.listSharedNotesForCurrentUser());
    }
}