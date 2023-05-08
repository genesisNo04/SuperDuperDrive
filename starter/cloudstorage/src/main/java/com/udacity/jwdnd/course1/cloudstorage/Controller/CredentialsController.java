package com.udacity.jwdnd.course1.cloudstorage.Controller;

import com.udacity.jwdnd.course1.cloudstorage.Model.Credentials;
import com.udacity.jwdnd.course1.cloudstorage.Model.Users;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialsService;
import com.udacity.jwdnd.course1.cloudstorage.services.UsersService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class CredentialsController {

    private CredentialsService credentialsService;
    private UsersService usersService;

    public CredentialsController(CredentialsService credentialsService, UsersService usersService) {
        this.credentialsService = credentialsService;
        this.usersService = usersService;
    }

    @PostMapping("/credentials/insert")
    public String createUpdateCredentials(Authentication authentication, @ModelAttribute("Credentials") Credentials credentials, Model model) {
        Users user = usersService.getUserByUsername(authentication.getPrincipal().toString());
        credentials.setUserId(user.getUserId());
        if (credentials.getCredentialid() == null) {
            try {
                credentialsService.addCredentials(credentials);
                model.addAttribute("isSuccess", true);
                model.addAttribute("successMsg", "Credential has been successfully inserted.");
            } catch (Exception e) {
                System.out.println("Cause: " + e.getCause() + ". Message: " + e.getMessage());
                model.addAttribute("isError", true);
                model.addAttribute("errorMsg", "Credential has not been inserted.");
            }
        } else {
            try {
                credentialsService.updateCredentials(credentials);
                model.addAttribute("isSuccess", true);
                model.addAttribute("successMsg", "Credential has been successfully updated.");
            } catch (Exception e) {
                System.out.println("Cause: " + e.getCause() + ". Message: " + e.getMessage());
                model.addAttribute("isError", true);
                model.addAttribute("errorMsg", "Credential has not been updated.");
            }
        }
        return "result";
    }

    @GetMapping("/credentials/deleteCredendtials/{credentialid}")
    public String deleteCredentials(@PathVariable("credentialid") Integer credentialid, Model model) {
        try{
            credentialsService.deleteCredentials(credentialid);
            model.addAttribute("isSuccess", true);
            model.addAttribute("successMsg", "Successfully deleted the credential.");
        }catch(Exception e) {
            System.out.println("Cause: " + e.getCause() + ". Message: " + e.getMessage());
            model.addAttribute("isError", true);
            model.addAttribute("errorMsg", "Credential couldn't be deleted.");
        }
        return "result";
    }
}
