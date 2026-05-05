package com.example.NotesManagementSystem.demo.entity;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "notes", indexes = {
        @Index(name = "idx_notes_title", columnList = "title"),
        @Index(name = "idx_notes_owner", columnList = "owner_id")
})
public class Note extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(nullable = false, columnDefinition = "text")
    private String content;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @ToString.Exclude
    @OneToMany(mappedBy = "note", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<NoteTag> noteTags = new HashSet<>();

    @ToString.Exclude
    @OneToMany(mappedBy = "note", fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<SharedNote> sharedNotes = new HashSet<>();

    @ToString.Exclude
    @OneToMany(mappedBy = "note", fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<Comment> comments = new HashSet<>();

    @ToString.Exclude
    @OneToMany(mappedBy = "note", fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<VersionHistory> versions = new HashSet<>();
}