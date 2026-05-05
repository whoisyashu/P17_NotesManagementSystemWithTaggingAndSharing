package com.example.NotesManagementSystem.demo.repository;

import java.util.List;

import com.example.NotesManagementSystem.demo.entity.Group;

import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupRepository extends JpaRepository<Group, Long> {

    List<Group> findByOwnerUsernameOrderByCreatedAtDesc(String username);

    List<Group> findDistinctByMembersUsernameOrderByCreatedAtDesc(String username);

    boolean existsByNameIgnoreCase(String name);
}