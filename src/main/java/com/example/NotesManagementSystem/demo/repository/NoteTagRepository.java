package com.example.NotesManagementSystem.demo.repository;

import java.util.Optional;

import com.example.NotesManagementSystem.demo.entity.NoteTag;

import org.springframework.data.jpa.repository.JpaRepository;

public interface NoteTagRepository extends JpaRepository<NoteTag, Long> {

    Optional<NoteTag> findByNoteIdAndTagId(Long noteId, Long tagId);

    void deleteByNoteIdAndTagId(Long noteId, Long tagId);
}