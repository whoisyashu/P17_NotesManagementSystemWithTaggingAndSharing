package com.example.NotesManagementSystem.demo.service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.example.NotesManagementSystem.demo.dto.group.GroupCreateRequest;
import com.example.NotesManagementSystem.demo.dto.group.GroupResponse;
import com.example.NotesManagementSystem.demo.entity.Group;
import com.example.NotesManagementSystem.demo.entity.User;
import com.example.NotesManagementSystem.demo.exception.ConflictOperationException;
import com.example.NotesManagementSystem.demo.exception.ResourceNotFoundException;
import com.example.NotesManagementSystem.demo.repository.GroupRepository;
import com.example.NotesManagementSystem.demo.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GroupService {

    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final CurrentUserService currentUserService;
    private final ApiMapper apiMapper;

    @Transactional
    public GroupResponse create(GroupCreateRequest request) {
        if (groupRepository.existsByNameIgnoreCase(request.getName())) {
            throw new ConflictOperationException("Group already exists");
        }

        User owner = currentUserService.getCurrentUser();
        Group group = new Group();
        group.setName(request.getName().trim());
        group.setOwner(owner);
        group.getMembers().add(owner);

        if (request.getMemberIds() != null) {
            Set<User> members = request.getMemberIds().stream()
                    .map(id -> userRepository.findById(id)
                            .orElseThrow(() -> new ResourceNotFoundException("User not found: " + id)))
                    .collect(Collectors.toSet());
            group.getMembers().addAll(members);
        }

        return apiMapper.toGroupResponse(groupRepository.save(group));
    }

    @Transactional(readOnly = true)
    public List<GroupResponse> listMyGroups() {
        String username = currentUserService.getCurrentUsername();
        return groupRepository.findByOwnerUsernameOrderByCreatedAtDesc(username).stream()
                .map(apiMapper::toGroupResponse)
                .toList();
    }

    @Transactional
    public Group addMember(Long groupId, Long userId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new ResourceNotFoundException("Group not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        group.getMembers().add(user);
        return groupRepository.save(group);
    }

    @Transactional(readOnly = true)
    public Group getGroup(Long groupId) {
        return groupRepository.findById(groupId)
                .orElseThrow(() -> new ResourceNotFoundException("Group not found"));
    }
}