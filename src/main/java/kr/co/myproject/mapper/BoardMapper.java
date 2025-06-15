package kr.co.myproject.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import kr.co.myproject.entity.Board;

@Mapper
public interface BoardMapper {
	
	@Insert("INSERT INTO springboot_project_study.board(memID, title, content, writer, indate, viewCount, commentCount, userIdx) VALUES(#{memID}, #{title}, #{content}, #{writer}, #{indate} ,#{viewCount}, #{commentCount}, #{userIdx})")
	public int boardInsert(Board board);
	
	@Select("SELECT idx, memID, title, content, writer, indate, viewCount, commentCount FROM springboot_project_study.board ORDER BY idx DESC")
	public List<Board> getList();

	@Select("SELECT idx, memID, title, content, writer, indate, viewCount, commentCount FROM springboot_project_study.board ORDER BY viewCount DESC")
	public List<Board> getBoardListDescViewCount();

	@Select("SELECT idx, memID, title, content, writer, indate, viewCount, commentCount FROM springboot_project_study.board ORDER BY commentCount DESC")
	public List<Board> getBoardListDescCommentCount();

	@Select("SELECT idx, memID, title, content, writer, indate, viewCount, commentCount FROM springboot_project_study.board ORDER BY indate DESC")
	public List<Board> getBoardListIndate();

	@Select("SELECT idx, memID, title, content, writer, indate, viewCount, commentCount FROM springboot_project_study.board ORDER BY idx DESC LIMIT #{start}, #{pageSize}")
	public List<Board> getPagedList(@Param("start") int start,@Param("pageSize") int pageSize);
	
	@Select("SELECT idx, memID, title, content, writer, indate, viewCount, commentCount, upCount, downCount, userIdx FROM springboot_project_study.board WHERE idx=#{idx}")
	public Board findBoard(int idx);

	@Update("UPDATE springboot_project_study.board SET title=#{title}, content=#{content} WHERE idx=#{idx}")
	public int boardUpdate(Board board);
	
	@Update("UPDATE springboot_project_study.board SET viewCount = viewCount + 1 WHERE idx=#{idx}")
	public int plusBoardViewCount(int idx);

	@Update("UPDATE springboot_project_study.board SET commentCount = commentCount + 1 WHERE idx=#{idx}")
	public int plusBoardCommentCount(int idx);

	@Update("UPDATE springboot_project_study.board SET commentCount = commentCount - 1 WHERE idx=#{idx}")
	public int downBoardCommentCount(int idx);

	@Update("UPDATE springboot_project_study.board SET upCount = upCount + 1 WHERE idx=#{idx}")
	public int plusBoardUpCount(int idx);

	@Update("UPDATE springboot_project_study.board SET downCount = downCount + 1 WHERE idx=#{idx}")
	public int plusBoardDownCount(int idx);
	
	@Delete("DELETE FROM springboot_project_study.board WHERE idx=#{idx}")
	public int boardDelete(int idx);

	@Select("""
  		SELECT * FROM springboot_project_study.board
  		WHERE
  		 ('title' = #{searchType} AND title LIKE CONCAT('%', #{keyword}, '%')) OR
   		 ('content' = #{searchType} AND content LIKE CONCAT('%', #{keyword}, '%')) OR
   		 ('writer' = #{searchType} AND writer LIKE CONCAT('%', #{keyword}, '%')) OR
  		 ('title_content' = #{searchType} AND (title LIKE CONCAT('%', #{keyword}, '%') OR content LIKE CONCAT('%', #{keyword}, '%')))
 		 ORDER BY idx DESC
		""")
	public List<Board> searchBoardListPaged(@Param("searchType") String searchType,
                                 			@Param("keyword") String keyword);

												@Select("""
  		SELECT * FROM springboot_project_study.board
  		WHERE
  		 ('title' = #{searchType} AND title LIKE CONCAT('%', #{keyword}, '%')) OR
   		 ('content' = #{searchType} AND content LIKE CONCAT('%', #{keyword}, '%')) OR
   		 ('writer' = #{searchType} AND writer LIKE CONCAT('%', #{keyword}, '%')) OR
  		 ('title_content' = #{searchType} AND (title LIKE CONCAT('%', #{keyword}, '%') OR content LIKE CONCAT('%', #{keyword}, '%')))
 		 ORDER BY viewCount DESC
		""")
	public List<Board> searchBoardListPagedDescViewCount(@Param("searchType") String searchType,
                                 			@Param("keyword") String keyword);

												@Select("""
  		SELECT * FROM springboot_project_study.board
  		WHERE
  		 ('title' = #{searchType} AND title LIKE CONCAT('%', #{keyword}, '%')) OR
   		 ('content' = #{searchType} AND content LIKE CONCAT('%', #{keyword}, '%')) OR
   		 ('writer' = #{searchType} AND writer LIKE CONCAT('%', #{keyword}, '%')) OR
  		 ('title_content' = #{searchType} AND (title LIKE CONCAT('%', #{keyword}, '%') OR content LIKE CONCAT('%', #{keyword}, '%')))
 		 ORDER BY commentCount DESC
		""")
	public List<Board> searchBoardListPagedDescCommentCount(@Param("searchType") String searchType,
                                 			@Param("keyword") String keyword);

												@Select("""
  		SELECT * FROM springboot_project_study.board
  		WHERE
  		 ('title' = #{searchType} AND title LIKE CONCAT('%', #{keyword}, '%')) OR
   		 ('content' = #{searchType} AND content LIKE CONCAT('%', #{keyword}, '%')) OR
   		 ('writer' = #{searchType} AND writer LIKE CONCAT('%', #{keyword}, '%')) OR
  		 ('title_content' = #{searchType} AND (title LIKE CONCAT('%', #{keyword}, '%') OR content LIKE CONCAT('%', #{keyword}, '%')))
 		 ORDER BY indate DESC
		""")
	public List<Board> searchBoardListPagedDescIndate(@Param("searchType") String searchType,
                                 			@Param("keyword") String keyword);

	@Select("""
 	 SELECT COUNT(*) FROM springboot_project_study.board
 	 WHERE
   	 ('title' = #{searchType} AND title LIKE CONCAT('%', #{keyword}, '%')) OR
   	 ('content' = #{searchType} AND content LIKE CONCAT('%', #{keyword}, '%')) OR
   	 ('writer' = #{searchType} AND writer LIKE CONCAT('%', #{keyword}, '%')) OR
  	  ('title_content' = #{searchType} AND (title LIKE CONCAT('%', #{keyword}, '%') OR content LIKE CONCAT('%', #{keyword}, '%')))
	""")
	public int countBoardListByType(@Param("searchType") String searchType,
									@Param("keyword") String keyword);

}
