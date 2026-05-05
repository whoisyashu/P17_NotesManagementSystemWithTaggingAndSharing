package com.example.NotesManagementSystem.demo.service;

import com.example.NotesManagementSystem.demo.dto.note.NoteResponse;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CollaborationBroadcastService {

    private final SimpMessagingTemplate messagingTemplate;

    public void broadcastNoteUpdate(NoteResponse noteResponse) {
        messagingTemplate.convertAndSend("/topic/notes/" + noteResponse.getId(), noteResponse);
    }
}