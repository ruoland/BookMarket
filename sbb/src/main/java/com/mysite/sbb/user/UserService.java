package com.mysite.sbb.user;

import com.mysite.sbb.DataNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public SbbUser create(String userId, String password, String email){
        SbbUser sbbUser = new SbbUser();
        sbbUser.setUserId(userId);
        sbbUser.setPassword(passwordEncoder.encode(password));
        sbbUser.setEmail(email);
        sbbUser.setRole(UserRole.USER);
        this.userRepository.save(sbbUser);
        return sbbUser;
    }

    public SbbUser getUser(String userId) {
        Optional<SbbUser> user = this.userRepository.findByUserId(userId);
        return user.orElseThrow(() -> new DataNotFoundException("사용자를 찾을 수 없습니다: " + userId));
    }

    public SbbUser updateUser(String userId, UserRole role){
        SbbUser sbbUser = this.getUser(userId);
        sbbUser.setRole(role);
        return this.userRepository.save(sbbUser);
    }
    @Cacheable("users")
    public SbbUser getUserById(String userId) {
        return this.userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다: " + userId));
    }

    public boolean isCurrentUserAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));
    }

    public boolean canCurrentUserAccess(String author){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        return  isCurrentUserAdmin() || user.getUsername().equals(author) ;
    }
}
