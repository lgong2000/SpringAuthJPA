package com.example.springauthjpa.repository;

import com.example.springauthjpa.model.GroupAuthority;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;

public interface GroupAuthorityRepository extends ListCrudRepository<GroupAuthority, Long> {
    void deleteByGroupId(Long groupId);
    void deleteByGroupIdAndAuthority(Long groupId, String authority);
    List<GroupAuthority> findByGroupId(Long groupId);
}
