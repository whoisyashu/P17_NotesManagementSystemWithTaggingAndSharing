package com.example.NotesManagementSystem.demo.repository;

import java.util.List;

import com.example.NotesManagementSystem.demo.entity.Comment;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByNoteIdOrderByCreatedAtAsc(Long noteId);
}