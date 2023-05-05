package com.udacity.jwdnd.course1.cloudstorage.Controller;

import com.udacity.jwdnd.course1.cloudstorage.Model.Users;
import com.udacity.jwdnd.course1.cloudstorage.services.UsersService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RegisterController {

    private UsersService usersService;

    public RegisterController(UsersService usersService) {
        this.usersService = usersService;
    }

    @GetMapping("/register")
    public String registerView() {
        return "signup";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute Users user, Model model) {
        String registerError = null;
        if(!usersService.isUsernameAvailable(user.getUsername())) {
            registerError = "The username already exists.";
        }

        if (registerError == null) {
            int rowsAdded = usersService.registerUser(user);
            if (rowsAdded < 0) {
                registerError = "There was an error signing you up. Please try again.";
            }
        }

        if(registerError == null) {
            model.addAttribute("signupSuccess", true);
        } else {
            model.addAttribute("signupError", registerError);
        }
        return "signup";
    }
}
