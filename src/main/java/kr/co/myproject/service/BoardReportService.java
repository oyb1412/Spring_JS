package kr.co.myproject.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.myproject.entity.BoardReport;
import kr.co.myproject.mapper.BoardReportMapper;

@Service
public class BoardReportService {
    @Autowired 
    private BoardReportMapper boardReportMapper;

    public int insertBoardReport(BoardReport boardReport)
    {
        return boardReportMapper.boardReportInsert(boardReport);
    }

    public int findBoardReportCount(int userIdx, int boardIdx)
    {
        return boardReportMapper.findBoardReportCount(userIdx, boardIdx);
    }
}
