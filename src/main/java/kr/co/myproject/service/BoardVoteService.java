package kr.co.myproject.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.myproject.entity.BoardVote;
import kr.co.myproject.mapper.BoardVoteMapper;

@Service
public class BoardVoteService {
    @Autowired 
    private BoardVoteMapper boardVoteMapper;

    public int insertBoardVote(BoardVote boardVote)
    {
        return boardVoteMapper.boardVoteInsert(boardVote);
    }

    public int findBoardVoteCount(int userIdx, int boardIdx)
    {
        return boardVoteMapper.findBoardVoteCount(userIdx, boardIdx);
    }
}
