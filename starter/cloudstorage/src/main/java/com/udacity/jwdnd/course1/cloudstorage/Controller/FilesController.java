package com.udacity.jwdnd.course1.cloudstorage.Controller;

import com.udacity.jwdnd.course1.cloudstorage.Mapper.FilesMapper;
import com.udacity.jwdnd.course1.cloudstorage.Model.Files;
import com.udacity.jwdnd.course1.cloudstorage.Model.Users;
import com.udacity.jwdnd.course1.cloudstorage.services.FilesService;
import com.udacity.jwdnd.course1.cloudstorage.services.UsersService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
public class FilesController {

    private FilesService filesService;
    private UsersService usersService;

    public FilesController(FilesService filesService, UsersService usersService) {
        this.filesService = filesService;
        this.usersService = usersService;
    }

    @GetMapping("/files/delete/{filesId}")
    public String deteleFile(@PathVariable("filesId") Integer filesId, Model model) {
        try{
            filesService.deleteFiles(filesId);
            model.addAttribute("isSuccess", true);
            model.addAttribute("successMsg", "Successfully deleted the files.");
        }catch(Exception e) {
            model.addAttribute("isError", true);
            model.addAttribute("errorMsg", "Credential couldn't be files.");
        }
        return "result";
    }

    @GetMapping(value = "/files/getFile/{filesId}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<Resource> get_file(@PathVariable("filesId") Integer filesId) {
        Files file = filesService.getFile(filesId);
        ByteArrayResource resource = new ByteArrayResource(file.getFileData());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFileName() + "\"")
                .contentType(MediaType.parseMediaType(file.getContentType())).body(resource);
    }

    @PostMapping("/files/insert")
    public String insertFile(Model model, @RequestParam("fileUpload") MultipartFile file, Authentication authentication) throws IOException {
        Users user = usersService.getUserByUsername(authentication.getPrincipal().toString());
        Integer userId = user.getUserId();

        if (file.getBytes().length == 0) {
            model.addAttribute("isError", true);
            model.addAttribute("errorMsg", "No file selected.");
            return "result";
        }

        if(!filesService.isFileNameAvailable(file.getOriginalFilename())){
            model.addAttribute("isError", true);
            model.addAttribute("errorMsg", "File with the same filename already exists");
            return "result";
        }

        try{
            filesService.insertFiles(file, userId);
            model.addAttribute("isSuccess", true);
            model.addAttribute("successMsg", "Successfully deleted the files.");
        }catch(Exception e) {
            model.addAttribute("isError", true);
            model.addAttribute("errorMsg", "Credential couldn't be files.");
        }
        return "result";
    }
}
