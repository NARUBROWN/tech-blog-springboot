package com.naru.tech.service;

import com.naru.tech.common.enums.Role;
import com.naru.tech.common.exception.UserNotFoundException;
import com.naru.tech.data.domain.User;
import com.naru.tech.data.dto.request.UserRequest;
import com.naru.tech.data.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 작가 가입하는 메서드입니다.
     * @param userRequest 유저 정보가 담긴 DTO
     */
    public void createAuthorUser(UserRequest userRequest) {
        String hashedPassword = passwordEncoder.encode(userRequest.originalPassword());
        User user = User.builder()
                .username(userRequest.username())
                .email(userRequest.email())
                .hashedPassword(hashedPassword)
                .profileImageUrl(userRequest.profileImageUrl())
                .role(Role.ROLE_AUTHOR)
                .build();
        userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException(username));
    }
}
