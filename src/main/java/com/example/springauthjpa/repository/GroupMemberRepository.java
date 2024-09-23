package com.example.springauthjpa.repository;

import com.example.springauthjpa.model.GroupMember;
import org.springframework.data.repository.ListCrudRepository;

public interface GroupMemberRepository extends ListCrudRepository<GroupMember, Long> {
}
