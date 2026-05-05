package com.example.NotesManagementSystem.demo.service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.example.NotesManagementSystem.demo.dto.comment.CommentResponse;
import com.example.NotesManagementSystem.demo.dto.group.GroupResponse;
import com.example.NotesManagementSystem.demo.dto.notification.NotificationResponse;
import com.example.NotesManagementSystem.demo.dto.note.NoteResponse;
import com.example.NotesManagementSystem.demo.dto.shared.SharedNoteResponse;
import com.example.NotesManagementSystem.demo.dto.tag.TagResponse;
import com.example.NotesManagementSystem.demo.dto.user.UserResponse;
import com.example.NotesManagementSystem.demo.dto.version.VersionHistoryResponse;
import com.example.NotesManagementSystem.demo.entity.Comment;
import com.example.NotesManagementSystem.demo.entity.Group;
import com.example.NotesManagementSystem.demo.entity.Notification;
import com.example.NotesManagementSystem.demo.entity.Note;
import com.example.NotesManagementSystem.demo.entity.NoteTag;
import com.example.NotesManagementSystem.demo.entity.SharedNote;
import com.example.NotesManagementSystem.demo.entity.Tag;
import com.example.NotesManagementSystem.demo.entity.User;
import com.example.NotesManagementSystem.demo.entity.VersionHistory;

import org.springframework.stereotype.Component;

@Component
public class ApiMapper {

    public UserResponse toUserResponse(User user) {
        if (user == null) {
            return null;
        }
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole())
                .createdAt(user.getCreatedAt())
                .build();
    }

    public TagResponse toTagResponse(Tag tag) {
        if (tag == null) {
            return null;
        }
        return TagResponse.builder()
                .id(tag.getId())
                .name(tag.getName())
                .createdAt(tag.getCreatedAt())
                .build();
    }

    public SharedNoteResponse toSharedNoteResponse(SharedNote sharedNote) {
        if (sharedNote == null) {
            return null;
        }
        return SharedNoteResponse.builder()
                .id(sharedNote.getId())
                .noteId(sharedNote.getNote() != null ? sharedNote.getNote().getId() : null)
                .sharedWithUser(toUserResponse(sharedNote.getSharedWithUser()))
                .sharedWithGroup(sharedNote.getSharedWithGroup() == null ? null : SharedNoteResponse.GroupSummary.builder()
                        .id(sharedNote.getSharedWithGroup().getId())
                        .name(sharedNote.getSharedWithGroup().getName())
                        .build())
                .sharedBy(toUserResponse(sharedNote.getSharedBy()))
                .permission(sharedNote.getPermission())
                .createdAt(sharedNote.getCreatedAt())
                .build();
    }

    public NoteResponse toNoteResponse(Note note) {
        List<TagResponse> tags = note.getNoteTags().stream()
                .map(NoteTag::getTag)
                .map(this::toTagResponse)
                .distinct()
                .toList();

        List<SharedNoteResponse> shares = note.getSharedNotes().stream()
                .map(this::toSharedNoteResponse)
                .toList();

        return NoteResponse.builder()
                .id(note.getId())
                .title(note.getTitle())
                .content(note.getContent())
                .owner(toUserResponse(note.getOwner()))
                .tags(tags)
                .shares(shares)
                .commentsCount(note.getComments().size())
                .versionsCount(note.getVersions().size())
                .createdAt(note.getCreatedAt())
                .updatedAt(note.getUpdatedAt())
                .build();
    }

    public CommentResponse toCommentResponse(Comment comment) {
        return CommentResponse.builder()
                .id(comment.getId())
                .noteId(comment.getNote().getId())
                .author(toUserResponse(comment.getAuthor()))
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt())
                .build();
    }

    public VersionHistoryResponse toVersionResponse(VersionHistory versionHistory) {
        return VersionHistoryResponse.builder()
                .id(versionHistory.getId())
                .noteId(versionHistory.getNote().getId())
                .versionNumber(versionHistory.getVersionNumber())
                .title(versionHistory.getTitle())
                .content(versionHistory.getContent())
                .modifiedBy(toUserResponse(versionHistory.getModifiedBy()))
                .createdAt(versionHistory.getCreatedAt())
                .build();
    }

    public NotificationResponse toNotificationResponse(Notification notification) {
        return NotificationResponse.builder()
                .id(notification.getId())
                .recipientId(notification.getRecipient().getId())
                .noteId(notification.getNote() == null ? null : notification.getNote().getId())
                .type(notification.getType())
                .message(notification.getMessage())
                .read(notification.isRead())
                .createdAt(notification.getCreatedAt())
                .build();
    }

    public GroupResponse toGroupResponse(Group group) {
        Set<UserResponse> members = group.getMembers().stream()
                .map(this::toUserResponse)
                .collect(Collectors.toSet());

        return GroupResponse.builder()
                .id(group.getId())
                .name(group.getName())
                .owner(toUserResponse(group.getOwner()))
                .members(members)
                .createdAt(group.getCreatedAt())
                .build();
    }
}