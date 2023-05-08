package com.udacity.jwdnd.course1.cloudstorage.Controller;

import com.udacity.jwdnd.course1.cloudstorage.Model.Notes;
import com.udacity.jwdnd.course1.cloudstorage.Model.Users;
import com.udacity.jwdnd.course1.cloudstorage.services.NotesService;
import com.udacity.jwdnd.course1.cloudstorage.services.UsersService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class NotesController {

    private NotesService notesService;
    private UsersService usersService;

    public NotesController(NotesService notesService, UsersService usersService) {
        this.notesService = notesService;
        this.usersService = usersService;
    }

    @PostMapping("/notes/insert")
    public String addNotes(@ModelAttribute("Notes") Notes note, Authentication authentication, Model model) {
        Users user = usersService.getUserByUsername(authentication.getPrincipal().toString());
        note.setUserId(user.getUserId());
        if(note.getNoteid() == null) {
            try {
                notesService.addNotes(note);
                model.addAttribute("isSuccess", true);
                model.addAttribute("successMsg", "Note has been successfully inserted.");
            } catch (Exception e) {
                System.out.println("Cause: " + e.getCause() + ". Message: " + e.getMessage());
                model.addAttribute("isError", true);
                model.addAttribute("errorMsg", "Note could not be inserted.");
            }

        } else {
            try {
                notesService.updateNote(note);
                model.addAttribute("isSuccess", true);
                model.addAttribute("successMsg", "Note has been successfully updated.");
            } catch (Exception e) {
                System.out.println("Cause: " + e.getCause() + ". Message: " + e.getMessage());
                model.addAttribute("isError", true);
                model.addAttribute("errorMsg", "Note could not be updated.");
            }

        }

        return "result";
    }

    @GetMapping("/notes/deleteNote/{noteid}")
    public String deleteNote(@PathVariable("noteid") Integer noteid, Model model) {
        try{
            notesService.deleteNote(noteid);
            model.addAttribute("isSuccess", true);
            model.addAttribute("successMsg", "Successfully deleted note");
        }catch(Exception e) {
            System.out.println("Cause: " + e.getCause() + ". Message: " + e.getMessage());
            System.out.println("Cause: " + e.getCause() + ". Message: " + e.getMessage());
            model.addAttribute("isError", true);
            model.addAttribute("errorMsg", "Note couldn't be deleted");
        }

        return "result";
    }
}
