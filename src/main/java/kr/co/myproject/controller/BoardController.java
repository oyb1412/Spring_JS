package kr.co.myproject.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;

import kr.co.myproject.entity.Board;
import kr.co.myproject.entity.BoardReport;
import kr.co.myproject.entity.BoardReported;
import kr.co.myproject.entity.BoardVote;
import kr.co.myproject.entity.ReportType;
import kr.co.myproject.entity.User;
import kr.co.myproject.service.BoardReportService;
import kr.co.myproject.service.BoardReportedService;
import kr.co.myproject.service.BoardService;
import kr.co.myproject.service.BoardVoteService;
import kr.co.myproject.service.UserService;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;







@Controller
public class BoardController {
    @Autowired
    private BoardService boardService;

    @Autowired
    private BoardVoteService boardVoteService;

    @Autowired
    private UserService userService;

    @Autowired
    private BoardReportService boardReportService;

    @Autowired
    private BoardReportedService boardReportedService;



    @PostMapping("/board-add")
    public String BoardAdd(@ModelAttribute Board board,  RedirectAttributes redirectAttributes,Authentication authentication) {
        if(authentication == null || !authentication.isAuthenticated())
        {
            redirectAttributes.addFlashAttribute("result", "회원 전용 기능입니다");
        	return "redirect:/board-list-page";
        }

        if(board == null)
        {
            redirectAttributes.addFlashAttribute("result", "글 정보가 올바르지 않습니다");
        	return "redirect:/board-list-page";
        }

        if((board.getTitle() == null || board.getTitle().isEmpty())||
        (board.getContent() == null || board.getContent().isEmpty()))
        {
            redirectAttributes.addFlashAttribute("result", "제목과 내용을 모두 작성해 주세요");
        	return "redirect:/board-list-page";
        }

        User user = userService.findByUsername(authentication.getName());
        board.setUserIdx(user.getIdx());
        int queryCount = boardService.boardInsert(board);

        if(queryCount == 0)
        {
            redirectAttributes.addFlashAttribute("result", "글 작성에 실패했습니다");
        	return "redirect:/board-list-page";
        }
        
        redirectAttributes.addFlashAttribute("result", "글 작성에 성공했습니다");
        return "redirect:/board-list-page";
    }

    @PostMapping("/board-delete")
    public String BoardDelete(@RequestParam int idx,  RedirectAttributes redirectAttributes) {
        if(idx == 0)
        {
            redirectAttributes.addFlashAttribute("result", "글 정보가 올바르지 않습니다");
        	return "redirect:/board-list-page";
        }

        int queryCount = boardService.boardDelete(idx);

        if(queryCount == 0)
        {
            redirectAttributes.addFlashAttribute("result", "글 삭제에 실패했습니다");
        	return "redirect:/board-list-page";
        }

        redirectAttributes.addFlashAttribute("result", "글 삭제에 성공했습니다");
        return "redirect:/board-list-page";
    }

    @PostMapping("/board-modify")
    public String BoardModify(@ModelAttribute Board board,  RedirectAttributes redirectAttributes) {
        if(board == null)
        {
            redirectAttributes.addFlashAttribute("result", "글 정보가 올바르지 않습니다");
        	return "redirect:/board-list-page";
        }

        if((board.getTitle() == null || board.getTitle().isEmpty())||
        (board.getContent() == null || board.getContent().isEmpty()))
        {
            redirectAttributes.addFlashAttribute("result", "제목과 내용을 모두 작성해 주세요");
        	return "redirect:/board-list-page";
        }

        int queryCount = boardService.boardUpdate(board);

        if(queryCount == 0)
        {
            redirectAttributes.addFlashAttribute("result", "글 수정에 실패했습니다");
        	return "redirect:/board-list-page";
        }

        redirectAttributes.addFlashAttribute("result", "글 수정에 성공했습니다");
        return "redirect:/board-list-page";
    }

    @PostMapping("/board-vote")
    public String BoardVote(@RequestParam int idx, @RequestParam String voteType, RedirectAttributes redirectAttributes, Authentication authentication, Model model) {
        if(authentication == null || !authentication.isAuthenticated())
        {
            redirectAttributes.addFlashAttribute("result", "회원 전용 기능입니다");
        	return "redirect:/board-list-page";
        }
        
        if(idx == 0)
        {
            redirectAttributes.addFlashAttribute("result", "글 정보가 올바르지 않습니다");
        	return "redirect:/board-list-page";
        }

        int userIdx = userService.findByUsername(authentication.getName()).getIdx();
        if(userIdx == 0)
        {
            redirectAttributes.addFlashAttribute("result", "유저 정보가 올바르지 않습니다");
        	return "redirect:/board-list-page";
        }

        int voteCount = boardVoteService.findBoardVoteCount(userIdx, idx);

        if(voteCount != 0)
        {
            redirectAttributes.addFlashAttribute("result", "이미 좋아요나 싫어요를 누른 글입니다");
        	return "redirect:/board-list-page";
        }

        if("up".equals(voteType))
        {
            int queryCount = boardService.plusBoardUpCount(idx);
            if(queryCount == 0)
            {
                redirectAttributes.addFlashAttribute("result", "좋아요에 실패했습니다");
        	    return "redirect:/board-list-page";
            }
            else
            {
                BoardVote boardVote = new BoardVote();
                boardVote.setUserIdx(userIdx);
                boardVote.setBoardIdx(idx);
                boardVote.setVoteType("up");
                boardVoteService.insertBoardVote(boardVote);
                redirectAttributes.addFlashAttribute("result", "좋아요에 성공했습니다");
        	    return "redirect:/board-list-page";
            }
        }
        else if("down".equals(voteType))
        {
            int queryCount = boardService.plusBoardDownCount(idx);
            if(queryCount == 0)
            {
                redirectAttributes.addFlashAttribute("result", "싫어요에 실패했습니다");
        	    return "redirect:/board-list-page";
            }
            else
            {
                BoardVote boardVote = new BoardVote();
                boardVote.setUserIdx(userIdx);
                boardVote.setBoardIdx(idx);
                boardVote.setVoteType("down");
                boardVoteService.insertBoardVote(boardVote);
                redirectAttributes.addFlashAttribute("result", "싫어요에 성공했습니다");
        	    return "redirect:/board-list-page";
            }
        }
        else
        {
            redirectAttributes.addFlashAttribute("result", "예상치못한 에러가 발생했습니다");
        	return "redirect:/board-list-page";
        }

        
    }

    @PostMapping("/board-report")
        public String BoardReport(@RequestParam int boardIdx,
                                  @RequestParam int reportedUserIdx,
                                  @RequestParam int userIdx,
                                  @RequestParam ReportType reportType,
                                  @RequestParam String reportContent,
                                  RedirectAttributes redirectAttributes)
        {
            BoardReport boardReport = new BoardReport();
            boardReport.setBoardIdx(boardIdx);
            boardReport.setUserIdx(userIdx);
            boardReport.setReportType(reportType);
	        LocalDateTime now = LocalDateTime.now();
		    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    	    String formattedNow = now.format(formatter);

            boardReport.setIndate(formattedNow);
            int queryCount = boardReportService.insertBoardReport(boardReport);

            //에러
            if(queryCount == 0)
            {
                redirectAttributes.addFlashAttribute("reportFail", true);
                return "redirect:/dummy";
            }

            //해당유저 신고받은 전적 체크
            int reportedCount = boardReportedService.findBoardReportedCount(reportedUserIdx, reportType);

            //전적이 없다면 새롭게 추가
            if(reportedCount == 0)
            {
                BoardReported boardReported = new BoardReported();
                boardReported.setBoardIdx(boardIdx);
                boardReported.setCount(1);
                boardReported.setReportType(reportType);
                boardReported.setIndate(formattedNow);
                boardReported.setUserIdx(reportedUserIdx);
                boardReportedService.insertBoardReported(boardReported);
            }
            else
            {
                //전적이 있다면 해당 유저 리포트 카운트 +1
                boardReportedService.plusBoardReportedCount(reportedUserIdx, reportType);
		        System.out.println("reportedCount" + reportedCount);

                //해당 유저 리포트 횟수가 4회(count+1전 기록)이면 정지
                if(reportedCount > 4)
                {
                    userService.UpdateBan(true, reportedUserIdx);
                }
            }
            

            redirectAttributes.addFlashAttribute("reportSuccess", true);
            return "redirect:/dummy";
        }
}
