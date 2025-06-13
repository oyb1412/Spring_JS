package kr.co.myproject.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import kr.co.myproject.entity.BoardReport;

@Mapper
public interface BoardReportMapper {
    @Insert("INSERT INTO springboot_project_study.boardreport(boardIdx, userIdx, reportType, indate) VALUES(#{boardIdx}, #{userIdx}, #{reportType}, #{indate})")
	public int boardReportInsert(BoardReport boardReport);

    @Select("SELECT COUNT(*) FROM springboot_project_study.boardreport WHERE userIdx=#{userIdx} and boardIdx=#{boardIdx}")
	public int findBoardReportCount(int userIdx, int boardIdx);
}
