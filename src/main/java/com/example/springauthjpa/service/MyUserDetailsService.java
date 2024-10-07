package com.example.springauthjpa.service;

import com.example.springauthjpa.model.*;
import com.example.springauthjpa.repository.GroupAuthorityRepository;
import com.example.springauthjpa.repository.GroupMemberRepository;
import com.example.springauthjpa.repository.GroupRepository;
import com.example.springauthjpa.repository.UserRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.log.LogMessage;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MyUserDetailsService implements UserDetailsService {
    private PasswordEncoder encoder;
    private AuthenticationManager authenticationManager;

    private final UserRepository userRepository;
    private final GroupRepository groupRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final GroupAuthorityRepository groupAuthorityRepository;

    protected final Log logger = LogFactory.getLog(getClass());

    public MyUserDetailsService(PasswordEncoder encoder, UserRepository userRepository, GroupRepository groupRepository, GroupMemberRepository groupMemberRepository, GroupAuthorityRepository groupAuthorityRepository) {
        this.encoder = encoder;
        this.userRepository = userRepository;
        this.groupRepository = groupRepository;
        this.groupMemberRepository = groupMemberRepository;
        this.groupAuthorityRepository = groupAuthorityRepository;
    }

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        MyUserDetails userDetails = userRepository
                .findByUsername(username)
                .map(MyUserDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found: " + username));

        Long userId = findUserId(username);
        List<String> authorityList =  groupMemberRepository
                .findByUserId(userId)
                .stream()
                .map(GroupMember::getGroupId)
                .map(groupAuthorityRepository::findByGroupId)
                .flatMap(List::stream)
                .map(GroupAuthority::getAuthority)
                .toList();

        //userDetails.setAuthorities(AuthorityUtils.NO_AUTHORITIES);
        userDetails.setAuthorities(AuthorityUtils.createAuthorityList(authorityList));

        return userDetails;
    }

    public void createUser(final User user) {
        userRepository.save(user);
    }

    public void updateUser(User newUser) {
        // Not include Username change
        User user = userRepository.findByUsername(newUser.getUsername()).get();
        user.setFirstname(newUser.getFirstname());
        user.setLastname(newUser.getLastname());
        user.setEmail(newUser.getEmail());
        userRepository.save(user);
    }

    public void deleteUser(final String username) {
        Long userId = findUserId(username);
        groupMemberRepository.deleteByUserId(userId);
        userRepository.deleteById(userId);
    }

    public void changePassword(String oldPassword, String newPassword) throws AuthenticationException {
        Authentication currentUser = SecurityContextHolder.getContext().getAuthentication();
        if (currentUser == null) {
            // This would indicate bad coding somewhere
            throw new AccessDeniedException(
                    "Can't change password as no Authentication object found in context " + "for current user.");
        }
        String username = currentUser.getName();
        // If an authentication manager has been set, re-authenticate the user with the
        // supplied password.
        if (this.authenticationManager != null) {
            this.logger.debug(LogMessage.format("Reauthenticating user '%s' for password change request.", username));
            this.authenticationManager
                    .authenticate(UsernamePasswordAuthenticationToken.unauthenticated(username, oldPassword));
        }
        else {
            this.logger.debug("No authentication manager set. Password won't be re-checked.");
        }

        User user = userRepository.findByUsername(username).get();
        user.setPassword(encoder.encode(newPassword));
        userRepository.save(user);

        Authentication authentication = createNewAuthentication(currentUser, newPassword);
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
    }

    protected Authentication createNewAuthentication(Authentication currentAuth, String newPassword) {
        UserDetails user = loadUserByUsername(currentAuth.getName());
        UsernamePasswordAuthenticationToken newAuthentication = UsernamePasswordAuthenticationToken.authenticated(user,
                null, user.getAuthorities());
        newAuthentication.setDetails(currentAuth.getDetails());
        return newAuthentication;
    }

    public boolean userExists(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    public List<String> findAllGroups() {
        List<Group> groups = groupRepository.findAll();
        return groups
                .stream()
                .map(Group::getGroupName)
                .collect(Collectors.toList());
    }

    public List<String> findUsersInGroup(String groupName) {
        Long groupId = findGroupId(groupName);
        List<GroupMember> groupMembers = groupMemberRepository.findByGroupId(groupId);
        return groupMembers
                .stream()
                .map(GroupMember::getUserId)
                .map(userRepository::findById)
                .map(Optional::get)
                .map(User::getUsername)
                .collect(Collectors.toList());
    }

    public void createGroup(final String groupName, final List<GrantedAuthority> authorities) {
        Group group = new Group(groupName);
        groupRepository.save(group);

        Long groupId = findGroupId(groupName);
        for (GrantedAuthority a : authorities) {
            String authority = a.getAuthority();
            groupAuthorityRepository.save(new GroupAuthority(groupId, authority));
        }
    }

    public void deleteGroup(String groupName) {
        Long groupId = findGroupId(groupName);
        groupMemberRepository.deleteByGroupId(groupId);
        groupAuthorityRepository.deleteByGroupId(groupId);
        groupRepository.deleteById(groupId);
    }

    public void renameGroup(String oldName, String newName) {
        Group group = groupRepository.findByGroupName(oldName).get();
        group.setGroupName(newName);
        groupRepository.save(group);
    }

    public void addUserToGroup(final String username, final String groupName) {
        Long userId = findUserId(username);
        Long groupId = findGroupId(groupName);

        GroupMember groupMember = new GroupMember(userId, groupId);
        groupMemberRepository.save(groupMember);
    }

    public void removeUserFromGroup(final String username, final String groupName) {
        Long userId = findUserId(username);
        Long groupId = findGroupId(groupName);

        groupMemberRepository.deleteByUserIdAndGroupId(userId, groupId);
    }

    public List<GrantedAuthority> findGroupAuthorities(String groupName) {
        Long groupId = findGroupId(groupName);
        List<GroupAuthority> groupAuthorities = groupAuthorityRepository.findByGroupId(groupId);
        return groupAuthorities
                .stream()
                .map(GroupAuthority::getAuthority)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    public void removeGroupAuthority(String groupName, final String authorityName) {
        Long groupId = findGroupId(groupName);
        groupAuthorityRepository.deleteByGroupIdAndAuthority(groupId, ("ROLE_" + authorityName).toUpperCase());
    }

    public void addGroupAuthority(final String groupName, final String authorityName) {
        Long groupId = findGroupId(groupName);
        GroupAuthority groupAuthority = new GroupAuthority(groupId, ("ROLE_" + authorityName).toUpperCase());
        groupAuthorityRepository.save(groupAuthority);
    }

    private Long findGroupId(final String groupName) {
        return groupRepository
                .findByGroupName(groupName)
                .map(Group::getId)
                .orElseThrow(() -> new RuntimeException("Groupname not found: " + groupName));
    }

    private Long findUserId(final String username) {
        return userRepository
                .findByUsername(username)
                .map(User::getId)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found: " + username));
    }

    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

}
