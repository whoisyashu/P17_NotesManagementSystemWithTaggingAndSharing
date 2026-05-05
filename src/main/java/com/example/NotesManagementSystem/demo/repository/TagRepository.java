package com.example.NotesManagementSystem.demo.repository;

import java.util.Optional;

import com.example.NotesManagementSystem.demo.entity.Tag;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag, Long> {

    Optional<Tag> findByNameIgnoreCase(String name);

    boolean existsByNameIgnoreCase(String name);
}