package com.example.NotesManagementSystem.demo.repository;

import java.util.List;

import com.example.NotesManagementSystem.demo.entity.SharedNote;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SharedNoteRepository extends JpaRepository<SharedNote, Long> {

    List<SharedNote> findByNoteId(Long noteId);

    List<SharedNote> findBySharedWithUserId(Long userId);

    boolean existsByNoteIdAndSharedWithUserId(Long noteId, Long userId);
}