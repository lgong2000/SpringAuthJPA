package com.example.springauthjpa.repository;

import com.example.springauthjpa.model.Group;
import org.springframework.data.repository.ListCrudRepository;

public interface GroupRepository extends ListCrudRepository<Group, Long> {
}
