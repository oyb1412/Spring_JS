package kr.co.myproject.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.myproject.entity.NoticeVote;
import kr.co.myproject.mapper.NoticeVoteMapper;

@Service
public class NoticeVoteService {
    @Autowired 
    private NoticeVoteMapper noticeVoteMapper;

    public int insertNoticeVote(NoticeVote noticeVote)
    {
        return noticeVoteMapper.noticeVoteInsert(noticeVote);
    }

    public int findNoticeVoteCount(int userIdx, int noticeIdx)
    {
        return noticeVoteMapper.findNoticeVoteCount(userIdx, noticeIdx);
    }
}
