package com.example.springauthjpa.repository;

import com.example.springauthjpa.model.GroupAuthority;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional  // For delete
public interface GroupAuthorityRepository extends ListCrudRepository<GroupAuthority, Long> {
    List<GroupAuthority> deleteByGroupId(Long groupId);
    List<GroupAuthority> deleteByGroupIdAndAuthority(Long groupId, String authority);
    List<GroupAuthority> findByGroupId(Long groupId);
}
