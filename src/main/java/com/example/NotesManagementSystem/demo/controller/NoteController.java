package com.example.NotesManagementSystem.demo.controller;

import com.example.NotesManagementSystem.demo.dto.note.NoteCreateRequest;
import com.example.NotesManagementSystem.demo.dto.note.NoteResponse;
import com.example.NotesManagementSystem.demo.dto.note.NoteUpdateRequest;
import com.example.NotesManagementSystem.demo.service.NoteService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/notes")
@RequiredArgsConstructor
public class NoteController {

    private final NoteService noteService;

    @GetMapping
    public ResponseEntity<Page<NoteResponse>> list(Pageable pageable) {
        return ResponseEntity.ok(noteService.list(pageable));
    }

    @PostMapping
    public ResponseEntity<NoteResponse> create(@Valid @RequestBody NoteCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(noteService.create(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<NoteResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(noteService.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<NoteResponse> update(@PathVariable Long id, @Valid @RequestBody NoteUpdateRequest request) {
        return ResponseEntity.ok(noteService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        noteService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/tags/{tagId}")
    public ResponseEntity<Void> addTag(@PathVariable Long id, @PathVariable Long tagId) {
        noteService.addTag(id, tagId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/{id}/tags/{tagId}")
    public ResponseEntity<Void> removeTag(@PathVariable Long id, @PathVariable Long tagId) {
        noteService.removeTag(id, tagId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}