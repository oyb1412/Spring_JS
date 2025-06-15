package kr.co.myproject.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import kr.co.myproject.entity.BoardType;
import kr.co.myproject.entity.Comment;

@Mapper
public interface CommentMapper {
    @Insert("INSERT INTO springboot_project_study.comment(parentIdx, writer, content, indate, boardType) VALUES(#{parentIdx}, #{writer}, #{content}, #{indate}, #{boardType})")
    public int commentInsert(Comment comment);

    @Select("SELECT idx, parentIdx, writer, content, indate, boardType FROM springboot_project_study.comment WHERE parentIdx=#{parentIdx} AND boardType=#{boardType} ORDER BY indate DESC")
	public List<Comment> findComment(@Param("parentIdx") int parentIdx, @Param("boardType") BoardType boardType);

    @Delete("DELETE FROM springboot_project_study.comment WHERE idx=#{idx}")
	public int commentDelete(int idx);
}
