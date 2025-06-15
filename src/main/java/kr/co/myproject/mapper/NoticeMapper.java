package kr.co.myproject.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import kr.co.myproject.entity.Notice;

@Mapper
public interface NoticeMapper {
    @Insert("INSERT INTO springboot_project_study.notice(memID, title, content, writer, indate, viewCount, commentCount) VALUES(#{memID}, #{title}, #{content}, #{writer}, #{indate} ,#{viewCount}, #{commentCount})")
	public int noticeInsert(Notice notice);
	
	@Select("SELECT idx, memID, title, content, writer, indate, viewCount, commentCount FROM springboot_project_study.notice ORDER BY idx DESC")
	public List<Notice> getList();

	@Select("SELECT idx, memID, title, content, writer, indate, viewCount, commentCount FROM springboot_project_study.notice ORDER BY idx DESC LIMIT #{start}, #{pageSize}")
	public List<Notice> getPagedList(@Param("start") int start, @Param("pageSize") int pageSize);
	
	@Select("SELECT idx, memID, title, content, writer, indate, viewCount, commentCount, upCount, downCount FROM springboot_project_study.notice WHERE idx=#{idx}")
	public Notice findNotice(int idx);
	
	@Update("UPDATE springboot_project_study.notice SET title=#{title}, content=#{content} WHERE idx=#{idx}")
	public int noticeUpdate(Notice notice);
	
	@Update("UPDATE springboot_project_study.notice SET viewCount = viewCount + 1 WHERE idx=#{idx}")
	public int plusNoticeViewCount(int idx);

	@Update("UPDATE springboot_project_study.notice SET commentCount = commentCount + 1 WHERE idx=#{idx}")
	public int plusNoticeCommentCount(int idx);

	@Update("UPDATE springboot_project_study.notice SET upCount = upCount + 1 WHERE idx=#{idx}")
	public int plusNoticeUpCount(int idx);

	@Update("UPDATE springboot_project_study.notice SET downCount = downCount + 1 WHERE idx=#{idx}")
	public int plusNoticeDownCount(int idx);
	
	@Delete("DELETE FROM springboot_project_study.notice WHERE idx=#{idx}")
	public int noticeDelete(int idx);

	@Select("""
  		SELECT * FROM springboot_project_study.notice
  		WHERE
  		 ('title' = #{searchType} AND title LIKE CONCAT('%', #{keyword}, '%')) OR
   		 ('content' = #{searchType} AND content LIKE CONCAT('%', #{keyword}, '%')) OR
   		 ('writer' = #{searchType} AND writer LIKE CONCAT('%', #{keyword}, '%')) OR
  		 ('title_content' = #{searchType} AND (title LIKE CONCAT('%', #{keyword}, '%') OR content LIKE CONCAT('%', #{keyword}, '%')))
 		 ORDER BY idx DESC
 		 LIMIT #{start}, #{pageSize}
		""")
	public List<Notice> searchNoticeListPaged(@Param("searchType") String searchType,
                                 			@Param("keyword") String keyword,
                                 			@Param("start") int start,
                                 			@Param("pageSize") int pageSize);

	@Select("""
 	 SELECT COUNT(*) FROM springboot_project_study.notice
 	 WHERE
   	 ('title' = #{searchType} AND title LIKE CONCAT('%', #{keyword}, '%')) OR
   	 ('content' = #{searchType} AND content LIKE CONCAT('%', #{keyword}, '%')) OR
   	 ('writer' = #{searchType} AND writer LIKE CONCAT('%', #{keyword}, '%')) OR
  	  ('title_content' = #{searchType} AND (title LIKE CONCAT('%', #{keyword}, '%') OR content LIKE CONCAT('%', #{keyword}, '%')))
	""")
	public int countNoticeListByType(@Param("searchType") String searchType,
									@Param("keyword") String keyword);
}
