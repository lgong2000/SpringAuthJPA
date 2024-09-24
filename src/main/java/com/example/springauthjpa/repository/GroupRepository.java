package com.example.springauthjpa.repository;

import com.example.springauthjpa.model.Group;
import org.springframework.data.repository.ListCrudRepository;

import java.util.Optional;

public interface GroupRepository extends ListCrudRepository<Group, Long> {
    Optional<Group> findByGroupName(String groupName);
}
