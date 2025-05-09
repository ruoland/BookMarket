package com.mysite.sbb.user.signup;

import com.mysite.sbb.user.SbbUser;
import com.mysite.sbb.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserSecurityService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<SbbUser> _siteUser = this.userRepository.findByUserId(username);
        SbbUser sbbUser = _siteUser.orElseThrow(() ->
                new UsernameNotFoundException("사용자를 찾을 수 없습니다")
        );

        List<GrantedAuthority> authorityList = new ArrayList<>();
        if(username.equals("admin")){
            authorityList.add(new SimpleGrantedAuthority(UserRole.ADMIN.getValue()));
            sbbUser.setRole(UserRole.ADMIN);
        }
        else{
            authorityList.add(new SimpleGrantedAuthority(UserRole.USER.getValue()));
            sbbUser.setRole(UserRole.USER);
        }
        String role = username.equals("admin") ?
                UserRole.ADMIN.getValue() : UserRole.USER.getValue();
        return User.builder()
                .username(sbbUser.getUserId())
                .password(sbbUser.getPassword())
                .roles(role.replace("ROLE_", "")) // ✅ Spring Security 역할 형식
                .build();
    }
}
