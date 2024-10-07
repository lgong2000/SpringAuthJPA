package com.example.springauthjpa.controller;

import com.example.springauthjpa.model.GroupDto;
import com.example.springauthjpa.model.User;
import com.example.springauthjpa.model.UserDto;
import com.example.springauthjpa.service.MyUserDetailsService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class UserController {
    private MyUserDetailsService myUserDetailsService;
    private PasswordEncoder encoder;

    public UserController(MyUserDetailsService myUserDetailsService, PasswordEncoder encoder) {
        this.myUserDetailsService = myUserDetailsService;
        this.encoder = encoder;
    }

    @PostMapping("/user")
    public void createUser(@RequestBody UserDto userDto) {
        User user = new User(userDto.username(),
                encoder.encode(userDto.password()),
                userDto.firstname(),
                userDto.lastname(),
                userDto.email());
        myUserDetailsService.createUser(user);
        myUserDetailsService.addUserToGroup(user.getUsername(), "Users");
    }

    @DeleteMapping("/user")
    public void deleteUser(@RequestBody UserDto userDto) {
        myUserDetailsService.deleteUser(userDto.username());
    }

    @PutMapping("/user")
    public void updateUser(@RequestBody UserDto userDto) {
        User user = new User(userDto.username(),
                null,
                userDto.firstname(),
                userDto.lastname(),
                userDto.email());
        myUserDetailsService.updateUser(user);
    }

    @GetMapping("/admin/groups")
    public List<String> getGroups() {
        return myUserDetailsService.findAllGroups();
    }

    @GetMapping("/admin/groupusers")
    public List<String> getGroupUsers(@RequestBody GroupDto groupDto) {
        return myUserDetailsService.findUsersInGroup(groupDto.groupName());
    }

    @PostMapping("/admin/group")
    public void createGroup(@RequestBody GroupDto groupDto) {
        List<GrantedAuthority> authorities;
        authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + groupDto.authority()));
        myUserDetailsService.createGroup(groupDto.groupName(), authorities);
    }

    @PutMapping("/admin/group")
    public void renameGroup(@RequestBody GroupDto groupDto) {
        myUserDetailsService.renameGroup(groupDto.groupName(), groupDto.newGroupName());
    }

    @DeleteMapping("/admin/group")
    public void deleteGroup(@RequestBody GroupDto groupDto) {
        myUserDetailsService.deleteGroup(groupDto.groupName());
    }

    @PostMapping("/admin/usergroup")
    public void addUserToGroup(@RequestBody UserDto userDto) {
        myUserDetailsService.addUserToGroup(userDto.username(), userDto.groupName());
    }

    @DeleteMapping("/admin/usergroup")
    public void removeUserFromGroup(@RequestBody UserDto userDto) {
        myUserDetailsService.removeUserFromGroup(userDto.username(), userDto.groupName());
    }

    @GetMapping("/admin/groupauthorities")
    public List<GrantedAuthority> getGroupAuthorities(@RequestBody GroupDto groupDto) {
        return myUserDetailsService.findGroupAuthorities(groupDto.groupName());
    }

    @PostMapping("/admin/groupauthority")
    public void addAuthorityToGroup(@RequestBody GroupDto groupDto) {
        myUserDetailsService.addGroupAuthority(groupDto.groupName(), groupDto.authority());
    }

    @DeleteMapping("/admin/groupauthority")
    public void removeAuthorityFromGroup(@RequestBody GroupDto groupDto) {
        myUserDetailsService.removeGroupAuthority(groupDto.groupName(), groupDto.authority());
    }
}
