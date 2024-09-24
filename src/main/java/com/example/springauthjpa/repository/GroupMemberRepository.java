package com.example.springauthjpa.repository;

import com.example.springauthjpa.model.GroupMember;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;
import java.util.Optional;

public interface GroupMemberRepository extends ListCrudRepository<GroupMember, Long> {
    Optional<GroupMember> findByUserIdAndGroupId(Long userId, Long groupId);
    void deleteByGroupId(Long groupId);
    List<GroupMember> findByGroupId(Long groupId);
    List<GroupMember> findByUserId(Long userId);
}
