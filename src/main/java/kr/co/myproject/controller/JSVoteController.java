package kr.co.myproject.controller;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RestController;

import kr.co.myproject.entity.BoardVote;
import kr.co.myproject.entity.NoticeVote;
import kr.co.myproject.service.BoardService;
import kr.co.myproject.service.BoardVoteService;
import kr.co.myproject.service.NoticeService;
import kr.co.myproject.service.NoticeVoteService;
import kr.co.myproject.service.UserService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
public class JSVoteController {
    @Autowired
    private BoardService boardService;

    @Autowired
    private UserService userService;

    @Autowired
    private BoardVoteService boardVoteService;

    @Autowired
    private NoticeVoteService noticeVoteService;

    @Autowired
    private NoticeService noticeService;
    
	private final Logger logger = LoggerFactory.getLogger(PageController.class);

    @PostMapping("/api/vote")
    public Map<String, Object> voteUp(@RequestParam int idx,
                                      @RequestParam String voteType,
                                      Authentication authentication) {
        Map<String, Object> result = new HashMap<>();

        if(authentication == null || !authentication.isAuthenticated())
        {
            result.put("error", "회원 전용 기능입니다");
        	return result;
        }
        
        if(idx == 0)
        {
            result.put("error", "글 정보가 올바르지 않습니다");
        	return result;
        }

        int userIdx = userService.findByUsername(authentication.getName()).getIdx();

        if(userIdx == 0)
        {
            result.put("error", "유저 정보가 올바르지 않습니다");
        	return result;
        }

        int voteCount = noticeVoteService.findNoticeVoteCount(userIdx, idx);

        if(voteCount != 0)
        {
            result.put("error", "이미 좋아요나 싫어요를 누른 글입니다");
        	return result;
        }

        if("up".equals(voteType))
        {
            int queryCount = noticeService.plusNoticeUpCount(idx);
            if(queryCount == 0)
            {
                result.put("error", "좋아요에 실패했습니다");
        	    return result;
            }
            else
            {
                NoticeVote noticeVote = new NoticeVote();
                noticeVote.setUserIdx(userIdx);
                noticeVote.setNoticeIdx(idx);
                noticeVote.setVoteType("up");
                noticeVoteService.insertNoticeVote(noticeVote);
                result.put("success", "좋아요에 성공했습니다");
        	    return result;
            }
        }
        else if("down".equals(voteType))
        {
            int queryCount = noticeService.plusNoticeDownCount(idx);
            if(queryCount == 0)
            {
                result.put("error", "싫어요에 실패했습니다");
        	    return result;
            }
            else
            {
                NoticeVote noticeVote = new NoticeVote();
                noticeVote.setUserIdx(userIdx);
                noticeVote.setNoticeIdx(idx);
                noticeVote.setVoteType("down");
                noticeVoteService.insertNoticeVote(noticeVote);
                result.put("success", "싫어요에 성공했습니다");
                return result;
            }
        }
        else
        {
                result.put("error", "예상치못한 에러가 발생했습니다");
        	    return result;
        }
    }
}
