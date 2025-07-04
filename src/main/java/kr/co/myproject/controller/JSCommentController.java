package kr.co.myproject.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import kr.co.myproject.entity.BoardType;
import kr.co.myproject.entity.Comment;
import kr.co.myproject.entity.User;
import kr.co.myproject.service.BoardService;
import kr.co.myproject.service.CommentService;
import kr.co.myproject.service.NoticeService;
import kr.co.myproject.service.UserService;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;


@RestController
public class JSCommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private UserService userService;

    @Autowired
    private BoardService boardService;

    @Autowired
    private NoticeService noticeService;

    private final Logger logger = LoggerFactory.getLogger(JSCommentController.class);

    
    @DeleteMapping("/api/comment/delete")
    public Map<String, Object> commentDelete(@RequestParam("idx") int idx,
                                             @RequestParam("parentIdx") int parentIdx,
                                             @RequestParam("boardType") BoardType boardType)
    {
        Map<String, Object> result = new HashMap<>();

        if(commentService.commentDelete(idx) == 0)
        {
            result.put("error", "댓글 삭제에 실패했습니다");
            return result;
        }

        if(boardType == BoardType.FREE)
        {
            boardService.downBoardCommentCount(parentIdx);
        }
        else
        {
            noticeService.downNoticeCommentCount(parentIdx);
        }


        result.put("success", "댓글 삭제에 성공했습니다");
        return result;
    }

    @PostMapping("/api/comment/create")
    public Map<String, Object> putMethodName(@RequestBody Comment comment,
                                              Authentication authentication) {
         Map<String, Object> result = new HashMap<>();

        LocalDateTime now = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    	String formattedNow = now.format(formatter);

        comment.setIndate(formattedNow);
        if(authentication != null && authentication.isAuthenticated())
        {
            User user = userService.findByUsername(authentication.getName());
            comment.setWriter(user.getWriter());
        }
        if(commentService.commentInsert(comment) == 0)
        {
            result.put("error", "코멘트 작성에 실패했습니다");
            return result;
        }

        if(comment.getBoardType() == BoardType.FREE)
        {
            boardService.plusBoardCommentCount(comment.getParentIdx());
        }
        else if(comment.getBoardType() == BoardType.NOTICE)
        {
            noticeService.plusNoticeCommentCount(comment.getParentIdx());
        }

        result.put("success", "코멘트 작성에 성공했습니다");
        return result;
    }
}
