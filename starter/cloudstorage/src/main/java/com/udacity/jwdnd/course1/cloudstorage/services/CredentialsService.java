package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.Mapper.CredentialsMapper;
import com.udacity.jwdnd.course1.cloudstorage.Model.Credentials;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;

@Service
public class CredentialsService {

    private EncryptionService encryptionService;
    private CredentialsMapper credentialsMapper;

    public CredentialsService(EncryptionService encryptionService, CredentialsMapper credentialsMapper) {
        this.encryptionService = encryptionService;
        this.credentialsMapper = credentialsMapper;
    }

    public List<Credentials> getCredentialsByUser(Integer userId) {
        return credentialsMapper.getCredentialsByUserId(userId);
    }

    public Credentials getCredentials(Integer credentialid) {
        return credentialsMapper.getCredentialById(credentialid);
    }

    public int addCredentials(Credentials credentials) {
        SecureRandom random = new SecureRandom();
        byte[] key = new byte[16];
        random.nextBytes(key);
        String encryptedKey = Base64.getEncoder().encodeToString(key);
        String encryptedPassword = encryptionService.encryptValue(credentials.getPassword(), encryptedKey);
        credentials.setKey(encryptedKey);
        credentials.setPassword(encryptedPassword);
        return credentialsMapper.createCredential(credentials);
    }

    public void updateCredentials(Credentials credentials) {
        String key = this.credentialsMapper.getKeyByCredentialId(credentials.getCredentialid());
        String encodedPassword = this.encryptionService.encryptValue(credentials.getPassword(), key);
        credentials.setPassword(encodedPassword);
        this.credentialsMapper.updateCredentials(credentials);
    }

    public void deleteCredentials(Integer credentialid) {
        credentialsMapper.deleteCredentials(credentialid);
    }

    public String encryptPassword(Credentials credentials){
        return this.encryptionService.encryptValue(credentials.getPassword(), credentials.getKey());
    }

    public String decryptPassword(Credentials credentials){
        return this.encryptionService.decryptValue(credentials.getPassword(), credentials.getKey());
    }

}
