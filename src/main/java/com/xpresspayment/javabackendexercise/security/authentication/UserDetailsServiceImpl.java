package com.xpresspayment.javabackendexercise.security.authentication;

import com.xpresspayment.javabackendexercise.exception.customException.ResourceNotFoundException;
import com.xpresspayment.javabackendexercise.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findUserByEmail(username).orElseThrow(()-> new ResourceNotFoundException("User does not exist"));
    }
}
