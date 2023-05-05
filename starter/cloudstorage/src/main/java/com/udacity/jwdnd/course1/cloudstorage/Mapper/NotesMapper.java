package com.udacity.jwdnd.course1.cloudstorage.Mapper;

import com.udacity.jwdnd.course1.cloudstorage.Model.Notes;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface NotesMapper {

    @Select("SELECT * FROM NOTES WHERE noteid = #{noteId}")
    Notes getNote(Integer noteId);

    @Select("SELECT * FROM NOTES")
    List<Notes> getAllNotes();

    @Select("SELECT * FROM NOTES WHERE userid = #{userId}")
    List<Notes> getNotesByUserId(Integer userId);

    @Insert("INSERT INTO NOTES (noteid, notetitle, notedescription, userid) VALUES (#{noteid}, #{noteTitle}, #{noteDescription}, #{userId})")
    @Options(useGeneratedKeys = true, keyProperty = "noteid")
    int insertNote(Notes note);

    @Delete("DELETE FROM NOTES WHERE noteid = #{noteId}")
    void deleteNotes(Integer noteId);

    @Update("UPDATE NOTES SET notetitle = #{noteTitle}, notedescription = #{noteDescription} WHERE noteid = #{noteid}")
    int updateNote(Notes note);

}
