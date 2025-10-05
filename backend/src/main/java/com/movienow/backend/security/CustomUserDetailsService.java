package com.movienow.backend.security;

import com.movienow.backend.dtos.user.UserDetailsDTO;
import com.movienow.backend.mappers.UserMapper;
import com.movienow.backend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CustomUserDetailsService  implements UserDetailsService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String email) {

        UserDetailsDTO userDetailsDTO = userRepository.findUserByEmail(email).map(user -> userMapper.toUserDetailsDTO(user))
                .orElseThrow(() -> new BadCredentialsException("Bad credentials"));

        return new CustomUserDetails(userDetailsDTO);
    }




}






