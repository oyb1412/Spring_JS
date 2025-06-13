package kr.co.myproject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import kr.co.myproject.entity.Notice;
import kr.co.myproject.entity.NoticeVote;
import kr.co.myproject.service.NoticeService;
import kr.co.myproject.service.NoticeVoteService;
import kr.co.myproject.service.UserService;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;



@Controller
public class NoticeController {
    @Autowired
    private NoticeService noticeService;

    @Autowired
    private UserService userService;

    @Autowired
    private NoticeVoteService noticeVoteService;

    


    //#region JSP
    @PostMapping("/notice-add")
    public String NoticeAdd(@ModelAttribute Notice notice, RedirectAttributes redirectAttributes) {
        if(notice == null)
        {
            redirectAttributes.addFlashAttribute("result", "글 정보가 올바르지 않습니다");
        	return "redirect:/";
        }

        int queryCount = noticeService.noticeInsert(notice);
        
        if(queryCount == 0)
        {
            redirectAttributes.addFlashAttribute("result", "글 작성에 실패했습니다");
        	return "redirect:/";
        }

        redirectAttributes.addFlashAttribute("result", "글 작성에 성공했습니다");
        return "redirect:/";
    }

    @PostMapping("/notice-delete")
    public String NoticeDelete(@RequestParam int idx, RedirectAttributes redirectAttributes) {
        if(idx == 0)
        {
            redirectAttributes.addFlashAttribute("result", "정보가 올바르지 않습니다");
        	return "redirect:/";
        }

        int queryCount = noticeService.noticeDelete(idx);

        if(queryCount == 0)
        {
            redirectAttributes.addFlashAttribute("result", "글 삭제에 실패했습니다");
        	return "redirect:/";
        }

        redirectAttributes.addFlashAttribute("result", "글 삭제에 성공했습니다");
        return "redirect:/";
    }

    @PostMapping("/notice-modify")
    public String NoticeModify(@ModelAttribute Notice notice, RedirectAttributes redirectAttributes) {
        if(notice == null)
        {
            redirectAttributes.addFlashAttribute("result", "글 정보가 올바르지 않습니다");
        	return "redirect:/";
        }

        int queryCount = noticeService.noticeUpdate(notice);

        if(queryCount == 0)
        {
            redirectAttributes.addFlashAttribute("result", "글 수정에 실패했습니다");
        	return "redirect:/";
        }
        
        redirectAttributes.addFlashAttribute("result", "글 수정에 성공했습니다");
        return "redirect:/";
    }

    @PostMapping("/notice-vote")
    public String NoticeVote(@RequestParam int idx, @RequestParam String voteType, RedirectAttributes redirectAttributes, Authentication authentication, Model model) {
        if(authentication == null || !authentication.isAuthenticated())
        {
            redirectAttributes.addFlashAttribute("result", "회원 전용 기능입니다");
        	return "redirect:/";
        }
        
        if(idx == 0)
        {
            redirectAttributes.addFlashAttribute("result", "글 정보가 올바르지 않습니다");
        	return "redirect:/";
        }

        int userIdx = userService.findByUsername(authentication.getName()).getIdx();

        if(userIdx == 0)
        {
            redirectAttributes.addFlashAttribute("result", "유저 정보가 올바르지 않습니다");
        	return "redirect:/";
        }

        int voteCount = noticeVoteService.findNoticeVoteCount(userIdx, idx);

        if(voteCount != 0)
        {
            redirectAttributes.addFlashAttribute("result", "이미 좋아요나 싫어요를 누른 글입니다");
        	return "redirect:/";
        }

        if("up".equals(voteType))
        {
            int queryCount = noticeService.plusNoticeUpCount(idx);
            if(queryCount == 0)
            {
                redirectAttributes.addFlashAttribute("result", "좋아요에 실패했습니다");
        	    return "redirect:/";
            }
            else
            {
                NoticeVote noticeVote = new NoticeVote();
                noticeVote.setUserIdx(userIdx);
                noticeVote.setNoticeIdx(idx);
                noticeVote.setVoteType("up");
                noticeVoteService.insertNoticeVote(noticeVote);
                redirectAttributes.addFlashAttribute("result", "좋아요에 성공했습니다");
        	    return "redirect:/";
            }
        }
        else if("down".equals(voteType))
        {
            int queryCount = noticeService.plusNoticeDownCount(idx);
            if(queryCount == 0)
            {
                redirectAttributes.addFlashAttribute("result", "싫어요에 실패했습니다");
        	    return "redirect:/";
            }
            else
            {
                NoticeVote noticeVote = new NoticeVote();
                noticeVote.setUserIdx(userIdx);
                noticeVote.setNoticeIdx(idx);
                noticeVote.setVoteType("down");
                noticeVoteService.insertNoticeVote(noticeVote);
                redirectAttributes.addFlashAttribute("result", "싫어요에 성공했습니다");
        	    return "redirect:/";
            }
        }
        else
        {
            redirectAttributes.addFlashAttribute("result", "예상치못한 에러가 발생했습니다");
        	return "redirect:/";
        }
    }
    //#endregion
}
