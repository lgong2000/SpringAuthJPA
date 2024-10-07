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

    public GroupAuthority() {
    }

    public GroupAuthority(Long groupId, String authority) {
        this.groupId = groupId;
        this.authority = authority;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }
}
