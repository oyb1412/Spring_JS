package kr.co.myproject.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import kr.co.myproject.entity.Projects;

@Mapper
public interface ProjectsMapper {
    @Insert("INSERT INTO springboot_project_study.projects(idx, title, content, indate, introduce, thumbnail) VALUES (#{idx}, #{title}, #{content}, #{indate}, #{introduce}, #{thumbnail})")
    public int InsertProjects(Projects resume);

    @Select("SELECT idx, title, content, indate, introduce, thumbnail FROM springboot_project_study.projects ORDER BY idx ASC")
    public List<Projects> SelectProjects();

    @Select("SELECT idx, title, content, indate, introduce, thumbnail FROM springboot_project_study.projects WHERE idx =#{idx} ORDER BY idx ASC")
    public Projects SelectProjectsByIdx(int idx);
}
