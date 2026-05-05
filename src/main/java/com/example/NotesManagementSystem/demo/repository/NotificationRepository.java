package com.example.NotesManagementSystem.demo.repository;

import java.util.List;

import com.example.NotesManagementSystem.demo.entity.Notification;

import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByRecipientUsernameOrderByCreatedAtDesc(String username);
}