package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.Mapper.UsersMapper;
import com.udacity.jwdnd.course1.cloudstorage.Model.Users;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class AuthenticationService implements AuthenticationProvider {
    private UsersMapper userMapper;
    private HashService hashService;

    public AuthenticationService(UsersMapper userMapper, HashService hashService) {
        this.userMapper = userMapper;
        this.hashService = hashService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String userName = authentication.getName();
        String password = authentication.getCredentials().toString();

        Users user = userMapper.getUserByUsername(userName);
        if (user != null) {
            String encodedSalt = user.getSalt();
            String hashPassword = hashService.getHashedValue(password, encodedSalt);
            if (user.getPassword().equals(hashPassword)) {
                return new UsernamePasswordAuthenticationToken(userName, password, new ArrayList<>());
            }
        }
        return null;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
