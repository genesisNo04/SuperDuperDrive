package com.udacity.jwdnd.course1.cloudstorage.Controller;

import com.udacity.jwdnd.course1.cloudstorage.Model.Credentials;
import com.udacity.jwdnd.course1.cloudstorage.Model.Notes;
import com.udacity.jwdnd.course1.cloudstorage.Model.Users;
import com.udacity.jwdnd.course1.cloudstorage.services.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class HomeController {

    private NotesService notesService;
    private UsersService usersService;
    private CredentialsService credentialsService;
    private final EncryptionService encryptionService;
    private final FilesService filesService;

    public HomeController(NotesService notesService, UsersService usersService, CredentialsService credentialsService, EncryptionService encryptionService, FilesService filesService) {
        this.notesService = notesService;
        this.usersService = usersService;
        this.credentialsService = credentialsService;
        this.encryptionService = encryptionService;
        this.filesService = filesService;
    }

    @GetMapping("/home")
    public String homeView(Model model, @ModelAttribute("Notes") Notes note, @ModelAttribute("Credentials") Credentials credentials, Authentication authentication) {
        Users user = usersService.getCurrentUser(authentication);
        Integer userId = user.getUserId();
        model.addAttribute("notes", notesService.getAllNotes(userId));
        model.addAttribute("credentials", credentialsService.getCredentialsByUser(userId));
        model.addAttribute("encryptionService", encryptionService);
        model.addAttribute("credentialsService", credentialsService);
        model.addAttribute("files", filesService.getAllFilesByUser(userId));
        return "home";
    }
}
