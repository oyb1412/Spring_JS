package kr.co.myproject.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import kr.co.myproject.entity.Comment;
import kr.co.myproject.entity.User;
import kr.co.myproject.service.CommentService;
import kr.co.myproject.service.UserService;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;


@RestController
public class JSCommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private UserService userService;
    
    @DeleteMapping("/api/comment/delete")
    public Map<String, Object> commentDelete(@RequestParam int idx)
    {
        Map<String, Object> result = new HashMap<>();

        if(commentService.commentDelete(idx) == 0)
        {
            result.put("error", "댓글 삭제에 실패했습니다");
            return result;
        }

        result.put("success", "댓글 삭제에 성공했습니다");
        return result;
    }

    @PostMapping("/api/comment/create")
    public Map<String, Object> putMethodName(@RequestBody Comment comment, Authentication authentication) {
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

        result.put("success", "코멘트 작성에 성공했습니다");
        return result;
    }
}
