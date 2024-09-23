package com.example.springauthjpa.service;

import com.example.springauthjpa.model.MyUserDetails;
import com.example.springauthjpa.repository.GroupAuthorityRepository;
import com.example.springauthjpa.repository.GroupMemberRepository;
import com.example.springauthjpa.repository.GroupRepository;
import com.example.springauthjpa.repository.UserRepository;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {
    private UserRepository userRepository;
    private GroupRepository groupRepository;
    private GroupMemberRepository groupMemberRepository;
    private GroupAuthorityRepository groupAuthorityRepository;

    public MyUserDetailsService(UserRepository userRepository, GroupRepository groupRepository, GroupMemberRepository groupMemberRepository, GroupAuthorityRepository groupAuthorityRepository) {
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

        //userDetails.setAuthorities(AuthorityUtils.NO_AUTHORITIES);
        userDetails.setAuthorities(AuthorityUtils.createAuthorityList("ROLE_USER","ROLE_ADMIN"));

        return userDetails;

    }
}
