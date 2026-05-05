package com.example.NotesManagementSystem.demo.service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.example.NotesManagementSystem.demo.dto.tag.TagRequest;
import com.example.NotesManagementSystem.demo.dto.tag.TagResponse;
import com.example.NotesManagementSystem.demo.entity.Tag;
import com.example.NotesManagementSystem.demo.exception.ConflictOperationException;
import com.example.NotesManagementSystem.demo.exception.ResourceNotFoundException;
import com.example.NotesManagementSystem.demo.repository.TagRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TagService {

    private final TagRepository tagRepository;
    private final ApiMapper apiMapper;

    @Transactional
    public TagResponse create(TagRequest request) {
        if (tagRepository.existsByNameIgnoreCase(request.getName())) {
            throw new ConflictOperationException("Tag already exists");
        }

        Tag tag = new Tag();
        tag.setName(request.getName().trim().toLowerCase());
        return apiMapper.toTagResponse(tagRepository.save(tag));
    }

    @Transactional(readOnly = true)
    public List<TagResponse> list() {
        return tagRepository.findAll().stream()
                .map(apiMapper::toTagResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public Set<Tag> findTagsByIds(List<Long> tagIds) {
        if (tagIds == null || tagIds.isEmpty()) {
            return Set.of();
        }

        return tagIds.stream()
                .map(id -> tagRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Tag not found: " + id)))
                .collect(Collectors.toSet());
    }

    @Transactional
    public Tag findOrCreateByName(String name) {
        return tagRepository.findByNameIgnoreCase(name.trim())
                .orElseGet(() -> {
                    Tag tag = new Tag();
                    tag.setName(name.trim().toLowerCase());
                    return tagRepository.save(tag);
                });
    }
}