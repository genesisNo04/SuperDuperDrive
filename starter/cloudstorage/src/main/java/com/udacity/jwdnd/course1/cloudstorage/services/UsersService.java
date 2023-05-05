package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.Mapper.UsersMapper;
import com.udacity.jwdnd.course1.cloudstorage.Model.Users;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;

@Service
public class UsersService {

    private UsersMapper usersMapper;
    private HashService hashService;

    public UsersService(UsersMapper usersMapper, HashService hashService) {
        this.usersMapper = usersMapper;
        this.hashService = hashService;
    }

    public Users getUserByUsername(String username) {
        Users user = usersMapper.getUserByUsername(username);
        if(user == null) {
            throw new UsernameNotFoundException("");
        }
        return user;
    }

    public int registerUser(Users user) {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        String encodedSalt = Base64.getEncoder().encodeToString(salt);
        String hashedPassword = hashService.getHashedValue(user.getPassword(), encodedSalt);
        user.setPassword(hashedPassword);
        user.setSalt(encodedSalt);
        return usersMapper.insertUser(user);
    }

    public boolean isUsernameAvailable(String username) {
        return usersMapper.getUserByUsername(username) == null;
    }

    public Users getCurrentUser(Authentication auth) {
        return usersMapper.getUserByUsername(auth.getName());
    }
}
