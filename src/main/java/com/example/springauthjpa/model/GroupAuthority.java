package com.example.springauthjpa.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "group_authorities")
public class GroupAuthority {
    @Id
    @GeneratedValue
    private Long id;

    private Long groupId;

    private String authority;
}
