package com.example.NotesManagementSystem.demo.service;

import com.example.NotesManagementSystem.demo.entity.Note;
import com.example.NotesManagementSystem.demo.entity.SharedNote;
import com.example.NotesManagementSystem.demo.entity.enums.NotePermission;

import org.springframework.stereotype.Service;

@Service
public class NoteAccessService {

    public boolean canView(Note note, String username) {
        return canAccess(note, username, false);
    }

    public boolean canEdit(Note note, String username) {
        return canAccess(note, username, true);
    }

    private boolean canAccess(Note note, String username, boolean editRequired) {
        if (note.getOwner().getUsername().equals(username)) {
            return true;
        }

        for (SharedNote sharedNote : note.getSharedNotes()) {
            boolean sharedWithUser = sharedNote.getSharedWithUser() != null && sharedNote.getSharedWithUser().getUsername().equals(username);
            boolean sharedWithGroup = sharedNote.getSharedWithGroup() != null && sharedNote.getSharedWithGroup().getMembers().stream()
                    .anyMatch(member -> member.getUsername().equals(username));

            if ((sharedWithUser || sharedWithGroup) && (!editRequired || sharedNote.getPermission() == NotePermission.EDIT)) {
                return true;
            }
        }

        return false;
    }
}