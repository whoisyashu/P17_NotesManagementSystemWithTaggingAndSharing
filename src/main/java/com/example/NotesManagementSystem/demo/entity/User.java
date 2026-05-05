package com.example.NotesManagementSystem.demo.entity;

import java.util.HashSet;
import java.util.Set;

import com.example.NotesManagementSystem.demo.entity.enums.Role;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "users", indexes = {
        @Index(name = "idx_users_username", columnList = "username", unique = true),
        @Index(name = "idx_users_email", columnList = "email", unique = true)
})
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 80)
    private String username;

    @Column(nullable = false, unique = true, length = 120)
    private String email;

    @Column(nullable = false, length = 255)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private Role role = Role.USER;

    @ToString.Exclude
    @OneToMany(mappedBy = "owner", fetch = FetchType.LAZY)
    private Set<Note> notes = new HashSet<>();

    @ToString.Exclude
    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY)
    private Set<Comment> comments = new HashSet<>();

    @ToString.Exclude
    @OneToMany(mappedBy = "recipient", fetch = FetchType.LAZY)
    private Set<Notification> notifications = new HashSet<>();

    @ToString.Exclude
    @OneToMany(mappedBy = "owner", fetch = FetchType.LAZY)
    private Set<Group> ownedGroups = new HashSet<>();

    @ToString.Exclude
    @OneToMany(mappedBy = "sharedWithUser", fetch = FetchType.LAZY)
    private Set<SharedNote> receivedShares = new HashSet<>();
}