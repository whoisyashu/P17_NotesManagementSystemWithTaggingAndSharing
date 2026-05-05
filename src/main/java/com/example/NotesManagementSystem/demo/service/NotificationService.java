package com.example.NotesManagementSystem.demo.service;

import java.util.ArrayList;
import java.util.List;

import com.example.NotesManagementSystem.demo.dto.notification.NotificationResponse;
import com.example.NotesManagementSystem.demo.entity.Notification;
import com.example.NotesManagementSystem.demo.entity.Note;
import com.example.NotesManagementSystem.demo.entity.SharedNote;
import com.example.NotesManagementSystem.demo.entity.User;
import com.example.NotesManagementSystem.demo.entity.enums.NotificationType;
import com.example.NotesManagementSystem.demo.repository.NotificationRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final ApiMapper apiMapper;

    @Transactional
    public Notification createNotification(User recipient, Note note, NotificationType type, String message) {
        Notification notification = new Notification();
        notification.setRecipient(recipient);
        notification.setNote(note);
        notification.setType(type);
        notification.setMessage(message);
        notification.setRead(false);
        return notificationRepository.save(notification);
    }

    @Transactional
    public List<Notification> notifyNoteEdited(Note note, User actor) {
        List<Notification> created = new ArrayList<>();
        String message = "Note '" + note.getTitle() + "' was edited by " + actor.getUsername();

        if (!note.getOwner().getId().equals(actor.getId())) {
            created.add(createNotification(note.getOwner(), note, NotificationType.NOTE_UPDATED, message));
        }

        for (SharedNote sharedNote : note.getSharedNotes()) {
            if (sharedNote.getSharedWithUser() != null && !sharedNote.getSharedWithUser().getId().equals(actor.getId())) {
                created.add(createNotification(sharedNote.getSharedWithUser(), note, NotificationType.NOTE_UPDATED, message));
            }
            if (sharedNote.getSharedWithGroup() != null) {
                sharedNote.getSharedWithGroup().getMembers().stream()
                        .filter(member -> !member.getId().equals(actor.getId()))
                        .forEach(member -> created.add(createNotification(member, note, NotificationType.NOTE_UPDATED, message)));
            }
        }

        return created;
    }

    @Transactional
    public List<Notification> notifyNoteShared(Note note, User actor, SharedNote sharedNote) {
        List<Notification> created = new ArrayList<>();
        String message = "Note '" + note.getTitle() + "' was shared by " + actor.getUsername();

        if (sharedNote.getSharedWithUser() != null && !sharedNote.getSharedWithUser().getId().equals(actor.getId())) {
            created.add(createNotification(sharedNote.getSharedWithUser(), note, NotificationType.NOTE_SHARED, message));
        }
        if (sharedNote.getSharedWithGroup() != null) {
            sharedNote.getSharedWithGroup().getMembers().stream()
                    .filter(member -> !member.getId().equals(actor.getId()))
                    .forEach(member -> created.add(createNotification(member, note, NotificationType.NOTE_SHARED, message)));
        }

        return created;
    }

    @Transactional(readOnly = true)
    public List<NotificationResponse> listForUser(String username) {
        return notificationRepository.findByRecipientUsernameOrderByCreatedAtDesc(username).stream()
                .map(apiMapper::toNotificationResponse)
                .toList();
    }
}