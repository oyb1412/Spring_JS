package kr.co.myproject.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import kr.co.myproject.entity.Resume;

@Mapper
public interface ResumeMapper {
    @Insert("INSERT INTO springboot_project_study.resume(idx, title, content, indate) VALUES (#{idx}, #{title}, #{content}, #{indate})")
    public int InsertResume(Resume resume);

    @Select("SELECT idx, title, content, indate FROM springboot_project_study.resume WHERE idx =#{idx}")
    public Resume SelectResume(int idx);
}
