package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.Mapper.NotesMapper;
import com.udacity.jwdnd.course1.cloudstorage.Model.Notes;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotesService {

    private NotesMapper notesMapper;

    public NotesService(NotesMapper notesMapper) {
        this.notesMapper = notesMapper;
    }

    public List<Notes> getAllNotes(Integer userId) {
        return notesMapper.getNotesByUserId(userId);
    }

    public Notes getNote(Integer noteid) {
        return notesMapper.getNote(noteid);
    }

    public int addNotes(Notes note) {
        return notesMapper.insertNote(note);
    }

    public int updateNote(Notes note) {
        return notesMapper.updateNote(note);
    }

    public void deleteNote(Integer noteId) {
        notesMapper.deleteNotes(noteId);
    }
}
