package com.example.NotesManagementSystem.demo.controller;

import java.util.List;

import com.example.NotesManagementSystem.demo.dto.notification.NotificationResponse;
import com.example.NotesManagementSystem.demo.service.NotificationService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;
    private final com.example.NotesManagementSystem.demo.service.CurrentUserService currentUserService;

    @GetMapping
    public ResponseEntity<List<NotificationResponse>> listMine() {
        return ResponseEntity.ok(notificationService.listForUser(currentUserService.getCurrentUsername()));
    }
}