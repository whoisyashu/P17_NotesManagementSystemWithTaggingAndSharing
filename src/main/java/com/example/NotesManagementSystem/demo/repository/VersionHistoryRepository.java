package com.example.NotesManagementSystem.demo.repository;

import java.util.List;
import java.util.Optional;

import com.example.NotesManagementSystem.demo.entity.VersionHistory;

import org.springframework.data.jpa.repository.JpaRepository;

public interface VersionHistoryRepository extends JpaRepository<VersionHistory, Long> {

    List<VersionHistory> findByNoteIdOrderByVersionNumberDesc(Long noteId);

    Optional<VersionHistory> findTopByNoteIdOrderByVersionNumberDesc(Long noteId);
}