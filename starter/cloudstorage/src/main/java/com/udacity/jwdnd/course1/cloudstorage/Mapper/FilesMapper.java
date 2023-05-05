package com.udacity.jwdnd.course1.cloudstorage.Mapper;

import com.udacity.jwdnd.course1.cloudstorage.Model.Files;
import org.apache.ibatis.annotations.*;

import java.util.List;


@Mapper
public interface FilesMapper {
    @Select("INSERT INTO FILES (filename, contenttype, filesize, userid, filedata)" +
            "VALUES(#{fileName}," + "#{contentType}, #{fileSize}, #{userId}, #{fileData})")
    @Options(useGeneratedKeys = true, keyProperty = "fileId")
    Integer insert(Files file);

    @Select("SELECT * FROM FILES WHERE userid = #{userId}")
    List<Files> getAllFiles(Integer userId);

    @Select("SELECT * FROM FILES WHERE filename = #{fileName} AND userid = #{userId}")
    Files getFileNameWithId(String fileName, Integer userId);

    @Select("SELECT * FROM FILES WHERE filename = #{fileName}")
    Files getFilename(String fileName);

    @Select("SELECT * FROM FILES WHERE fileId = #{fileId}")
    Files getFileById(Integer fileId);

    @Delete("DELETE FROM FILES WHERE fileId = #{fileId}")
    int delete(Integer fileId);

}
