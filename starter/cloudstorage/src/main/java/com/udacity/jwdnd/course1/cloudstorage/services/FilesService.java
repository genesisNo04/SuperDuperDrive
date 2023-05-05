package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.Mapper.FilesMapper;
import com.udacity.jwdnd.course1.cloudstorage.Model.Files;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class FilesService {
    private FilesMapper filesMapper;

    public FilesService(FilesMapper filesMapper) {
        this.filesMapper = filesMapper;
    }

    public List<Files> getAllFilesByUser(Integer userId) {
        return filesMapper.getAllFiles(userId);
    }

    public int deleteFiles(Integer fileId) {
        return filesMapper.delete(fileId);
    }

    public Files getFile(Integer fileId) {
        return filesMapper.getFileById(fileId);
    }

    public Files getFileWithUserId(String fileName, Integer userId) {
        return filesMapper.getFileNameWithId(fileName, userId);
    }

    public Files getFileWithFileName(String fileName) {
        return filesMapper.getFilename(fileName);
    }

    public Integer insertFiles(MultipartFile file, Integer userId) throws IOException {
        String filename = file.getOriginalFilename();
        String fileSize = String.valueOf(file.getSize());
        String fileType = file.getContentType();
        return filesMapper.insert(new Files(null, filename, fileType, fileSize, userId, file.getBytes()));
    }

    public boolean isFileNameAvailable(String fileName) {
        return filesMapper.getFilename(fileName) == null;
    }
}
