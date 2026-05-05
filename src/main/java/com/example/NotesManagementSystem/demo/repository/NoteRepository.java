package com.example.NotesManagementSystem.demo.repository;

import java.util.List;
import java.util.Optional;

import com.example.NotesManagementSystem.demo.entity.Note;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface NoteRepository extends JpaRepository<Note, Long> {

    @EntityGraph(attributePaths = {"owner", "noteTags", "noteTags.tag", "sharedNotes", "sharedNotes.sharedWithUser", "sharedNotes.sharedWithGroup", "comments", "versions"})
    Optional<Note> findWithDetailsById(Long id);

    @Query(value = """
            select distinct n from Note n
            left join n.sharedNotes sn
            left join sn.sharedWithGroup sg
            left join sg.members gm
            where n.owner.username = :username
               or sn.sharedWithUser.username = :username
               or gm.username = :username
            """,
            countQuery = """
            select count(distinct n) from Note n
            left join n.sharedNotes sn
            left join sn.sharedWithGroup sg
            left join sg.members gm
            where n.owner.username = :username
               or sn.sharedWithUser.username = :username
               or gm.username = :username
            """)
    Page<Note> findAccessibleNotes(@Param("username") String username, Pageable pageable);

    @Query(value = """
            select distinct n from Note n
            left join n.noteTags nt
            left join nt.tag t
            left join n.sharedNotes sn
            left join sn.sharedWithGroup sg
            left join sg.members gm
            where (
                lower(n.title) like lower(concat('%', :query, '%'))
                or lower(n.content) like lower(concat('%', :query, '%'))
                or lower(t.name) like lower(concat('%', :query, '%'))
            )
            and (
                n.owner.username = :username
                or sn.sharedWithUser.username = :username
                or gm.username = :username
            )
            """,
            countQuery = """
            select count(distinct n) from Note n
            left join n.noteTags nt
            left join nt.tag t
            left join n.sharedNotes sn
            left join sn.sharedWithGroup sg
            left join sg.members gm
            where (
                lower(n.title) like lower(concat('%', :query, '%'))
                or lower(n.content) like lower(concat('%', :query, '%'))
                or lower(t.name) like lower(concat('%', :query, '%'))
            )
            and (
                n.owner.username = :username
                or sn.sharedWithUser.username = :username
                or gm.username = :username
            )
            """)
    Page<Note> searchAccessibleNotes(@Param("username") String username, @Param("query") String query, Pageable pageable);

    List<Note> findByOwnerUsernameOrderByUpdatedAtDesc(String username);
}