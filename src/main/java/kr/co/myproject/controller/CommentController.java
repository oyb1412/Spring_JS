package kr.co.myproject.controller;

import java.security.Principal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import kr.co.myproject.Util.Util;
import kr.co.myproject.entity.Comment;
import kr.co.myproject.entity.User;
import kr.co.myproject.service.CommentService;
import kr.co.myproject.service.UserService;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpServletRequest;


@Controller
public class CommentController {
    @Autowired CommentService commentService;

    @Autowired UserService userService;

    @PostMapping("/comment-add")
    public String commentInsert(@ModelAttribute Comment comment, RedirectAttributes redirectAttributes, HttpServletRequest request, Principal principal) {
        if(comment == null)
        {
            redirectAttributes.addFlashAttribute("result", "댓글 정보가 올바르지 않습니다");
        	return Util.PageRefresh(request);
        }

        if(comment.getContent() == null || comment.getContent().isEmpty())
        {
            redirectAttributes.addFlashAttribute("result", "내용을 입력해 주세요");
        	return Util.PageRefresh(request);
        }
        
        String username = principal.getName();

        if(username == null || username.isEmpty())
        {
            redirectAttributes.addFlashAttribute("result", "유저 정보가 올바르지 않습니다");
        	return Util.PageRefresh(request);
        }

        User user = userService.findByUsername(username);

        if(user == null)
        {
            redirectAttributes.addFlashAttribute("result", "유저 정보가 올바르지 않습니다");
        	return Util.PageRefresh(request);
        }

        comment.setWriter(user.getWriter());

        LocalDateTime now = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    	String formattedNow = now.format(formatter);

        comment.setIndate(formattedNow);

        int queryCount = commentService.commentInsert(comment);

        if(queryCount == 0)
        {
            redirectAttributes.addFlashAttribute("result", "코멘트 추가에 실패했습니다");
        	return Util.PageRefresh(request);
        }


        redirectAttributes.addFlashAttribute("result", "코멘트 추가에 성공했습니다");
        return Util.PageRefresh(request);
    }

    @PostMapping("/comment-delete")
    public String commentDelete(@ModelAttribute Comment comment, RedirectAttributes redirectAttributes, HttpServletRequest request) {
        if(comment == null)
        {
            redirectAttributes.addFlashAttribute("result", "댓글 정보가 올바르지 않습니다");
        	return Util.PageRefresh(request);
        }

        int queryCount = commentService.commentDelete(comment.getIdx());

        if(queryCount == 0)
        {
            redirectAttributes.addFlashAttribute("result", "코멘트 삭제에 실패했습니다");
        	return Util.PageRefresh(request);
        }

        
        redirectAttributes.addFlashAttribute("result", "코멘트 삭제에 성공했습니다");
        return Util.PageRefresh(request);
    }
    
}
