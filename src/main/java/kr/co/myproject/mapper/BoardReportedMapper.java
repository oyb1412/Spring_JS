package kr.co.myproject.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import kr.co.myproject.entity.BoardReported;
import kr.co.myproject.entity.ReportType;


@Mapper
public interface BoardReportedMapper {
    //insert
    //유저idx,reportType으로 검색해서 카운트 반환
    //유저idx,reportType으로 검색해서 카운트 + 1
    
    @Insert("INSERT INTO springboot_project_study.boardreported(boardIdx, userIdx, reportType, count, indate) VALUES(#{boardIdx}, #{userIdx}, #{reportType}, #{count},#{indate})")
	public int boardReportedInsert(BoardReported boardReported);

    @Select("SELECT count FROM springboot_project_study.boardreported WHERE userIdx=#{userIdx} and reportType=#{reportType}")
	public Integer findBoardReportedCount(@Param("userIdx") int userIdx, @Param("reportType") ReportType reportType);

    @Update("UPDATE springboot_project_study.boardreported SET count = count + 1 WHERE userIdx=#{userIdx} AND reportType =#{reportType}")
	public int plusBoardReportedCount(@Param("userIdx") int userIdx, @Param("reportType") ReportType reportType);
}
