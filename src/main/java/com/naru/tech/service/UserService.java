package com.naru.tech.service;

import com.naru.tech.common.enums.Role;
import com.naru.tech.common.exception.UserNotFoundException;
import com.naru.tech.data.domain.User;
import com.naru.tech.data.dto.web.request.UserRequest;
import com.naru.tech.data.dto.web.response.UserResponse;
import com.naru.tech.data.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        User user = createUser(userRequest, Role.ROLE_AUTHOR);
        userRepository.save(user);
    }

    public void createAdminUser(UserRequest userRequest) {
        User user = createUser(userRequest, Role.ROLE_ADMIN);
        userRepository.save(user);
    }

    public void createNormalUser(UserRequest userRequest) {
        User user = createUser(userRequest, Role.ROLE_USER);
        userRepository.save(user);
    }

    @Transactional
    public UserResponse updateUserInfo(Long userId, UserRequest userRequest) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        String hashedPassword = passwordEncoder.encode(userRequest.originalPassword());
        user.updateByDto(userRequest, hashedPassword);
        return UserResponse.fromEntity(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException(username));
    }

    private User createUser(UserRequest userRequest, Role role) {
        String hashedPassword = passwordEncoder.encode(userRequest.originalPassword());

        return User.builder()
                .username(userRequest.username())
                .email(userRequest.email())
                .hashedPassword(hashedPassword)
                .profileImageUrl(userRequest.profileImageUrl())
                .role(role)
                .build();
    }
}
