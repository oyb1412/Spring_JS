package kr.co.myproject.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.myproject.entity.BoardReported;
import kr.co.myproject.entity.ReportType;
import kr.co.myproject.mapper.BoardReportedMapper;

@Service
public class BoardReportedService {
    @Autowired 
    private BoardReportedMapper boardReportedMapper;

    public int insertBoardReported(BoardReported boardReport)
    {
        return boardReportedMapper.boardReportedInsert(boardReport);
    }

    public Integer findBoardReportedCount(int userIdx, ReportType reportType)
    {
        return boardReportedMapper.findBoardReportedCount(userIdx, reportType);
    }

    public int plusBoardReportedCount(int userIdx, ReportType reportType)
    {
        return boardReportedMapper.plusBoardReportedCount(userIdx, reportType);
    }
}
