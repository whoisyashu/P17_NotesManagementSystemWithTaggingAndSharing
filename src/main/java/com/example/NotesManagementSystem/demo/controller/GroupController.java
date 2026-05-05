package com.example.NotesManagementSystem.demo.controller;

import java.util.List;

import com.example.NotesManagementSystem.demo.dto.group.GroupCreateRequest;
import com.example.NotesManagementSystem.demo.dto.group.GroupResponse;
import com.example.NotesManagementSystem.demo.service.GroupService;
import com.example.NotesManagementSystem.demo.service.ApiMapper;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/groups")
@RequiredArgsConstructor
public class GroupController {

    private final GroupService groupService;
    private final ApiMapper apiMapper;

    @PostMapping
    public ResponseEntity<GroupResponse> create(@Valid @RequestBody GroupCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(groupService.create(request));
    }

    @GetMapping
    public ResponseEntity<List<GroupResponse>> listMine() {
        return ResponseEntity.ok(groupService.listMyGroups());
    }

    @PostMapping("/{groupId}/members/{userId}")
    public ResponseEntity<GroupResponse> addMember(@PathVariable Long groupId, @PathVariable Long userId) {
        return ResponseEntity.ok(apiMapper.toGroupResponse(groupService.addMember(groupId, userId)));
    }
}