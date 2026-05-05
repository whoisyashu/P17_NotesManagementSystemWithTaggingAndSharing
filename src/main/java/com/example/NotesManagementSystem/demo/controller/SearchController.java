package com.example.NotesManagementSystem.demo.controller;

import java.util.List;

import com.example.NotesManagementSystem.demo.dto.note.NoteResponse;
import com.example.NotesManagementSystem.demo.service.SearchService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;

    @GetMapping
    public ResponseEntity<List<NoteResponse>> search(@RequestParam("q") String query) {
        return ResponseEntity.ok(searchService.search(query));
    }
}