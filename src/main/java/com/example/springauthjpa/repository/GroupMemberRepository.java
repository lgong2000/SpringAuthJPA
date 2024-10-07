package com.example.springauthjpa.repository;

import com.example.springauthjpa.model.GroupMember;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional  // For delete
public interface GroupMemberRepository extends ListCrudRepository<GroupMember, Long> {
    List<GroupMember> deleteByUserIdAndGroupId(Long userId, Long groupId);
    List<GroupMember> deleteByUserId(Long userId);
    List<GroupMember> deleteByGroupId(Long groupId);
    List<GroupMember> findByGroupId(Long groupId);
    List<GroupMember> findByUserId(Long userId);
}
