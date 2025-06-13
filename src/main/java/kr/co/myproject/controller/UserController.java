package kr.co.myproject.controller;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import kr.co.myproject.entity.Role;
import kr.co.myproject.entity.User;
import kr.co.myproject.service.UserService;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.RequestParam;







@Controller
public class UserController {
	@Autowired
	private UserService userService;
	@Autowired
	private PasswordEncoder passwordEncoder;
	// @PostMapping("/register")
	// public String register(@ModelAttribute User user,  RedirectAttributes redirectAttributes,Model model) {
	// 	if(user == null)
	// 	{
	// 		redirectAttributes.addFlashAttribute("result", "유저 정보가 올바르지 않습니다");
    //     	return "redirect:/register-page";
	// 	}

	// 	if(userService.countByUsername(user.getUsername()) > 0) {
    //     	redirectAttributes.addFlashAttribute("result", "이미 존재하는 아이디입니다");
    //     	return "redirect:/register-page";
    // 	}

	// 	LocalDateTime now = LocalDateTime.now();
	// 	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    // 	String formattedNow = now.format(formatter);

	// 	user.setIndate(formattedNow);
	// 	String userPassword = user.getPassword();
	// 	user.setRole(Role.MEMBER);
	// 	String passwordEncoded = passwordEncoder.encode(userPassword);
	// 	user.setPassword(passwordEncoded);
	// 	int queryCount = userService.insertUser(user);

	// 	if(queryCount == 0)
	// 	{
	// 		redirectAttributes.addFlashAttribute("result", "회원가입에 실패했습니다");
    //     	return "redirect:/register-page";
	// 	}

	// 	redirectAttributes.addFlashAttribute("result", "회원가입에 성공했습니다");
	// 	return "redirect:/login-page";
	// }

	@PostMapping("myPage-Modify")
	public String myPageModify(@ModelAttribute User user, RedirectAttributes redirectAttributes,Model model) {
		//유저 값 예외처리
		if(user.getUsername() == null || user.getUsername().isEmpty())
		{
			redirectAttributes.addFlashAttribute("result", "유저 정보가 올바르지 않습니다");
        	return "redirect:/my-page";
		}
		
		//모든 칸이 비워져있나 체크
		if((user.getPassword() == null || user.getPassword().isEmpty()) && 
		(user.getWriter() == null || user.getWriter().isEmpty()))
		{
			redirectAttributes.addFlashAttribute("result", "수정할 내용을 적어주세요");
        	return "redirect:/my-page";
		}

		boolean success = true;

		//비밀번호를 수정하는 상황
		if (user.getPassword() != null && !user.getPassword().isEmpty()) {
    		String userPassword = user.getPassword();
			String passwordEncoded = passwordEncoder.encode(userPassword);
			user.setPassword(passwordEncoded);

			//쿼리 성공 예외처리
			int updateQuery = userService.updatePassword(user);

			if(updateQuery == 0)
			{
				//쿼리 실패
				success = false;
			}
		}

		//작성자 수정 쿼리
		if (user.getWriter() != null && !user.getWriter().isEmpty()) {
			//쿼리 성공 예외처리
			int updateQuery = userService.UpdateWriter(user);
			
			if(updateQuery == 0)
			{
				//쿼리 실패
				success = false;
			}
		}

		if(!success)
		{
			redirectAttributes.addFlashAttribute("result", "수정에 실패했습니다");
        	return "redirect:/my-page";
		}

		//마이페이지 수정 완료
		redirectAttributes.addFlashAttribute("result", "수정에 성공했습니다");
        return "redirect:/my-page";
	}

	@PostMapping("/find-password")
	public String findPassword(@RequestParam String username, @RequestParam String password, @RequestParam String passwordConfirm, RedirectAttributes redirectAttributes) {
		if((username == null || username.isEmpty()) ||
		(password == null || password.isEmpty()) ||
		(passwordConfirm == null || passwordConfirm.isEmpty()))
		{
			redirectAttributes.addFlashAttribute("result", "입력 정보가 올바르지 않습니다");
        	return "redirect:/find-password-page"; 
		}

		if (!password.equals(passwordConfirm)) {
        	redirectAttributes.addFlashAttribute("result", "비밀번호가 서로 다릅니다");
        	return "redirect:/find-password-page"; 
    	}

		User user = userService.findByUsername(username);

		if(user == null)
		{
			redirectAttributes.addFlashAttribute("result", "존재하지 않는 유저입니다");
		 	return "redirect:/find-password-page"; 
		}

		String passwordEncoded = passwordEncoder.encode(password);
		user.setPassword(passwordEncoded);
		int queryCount = userService.updatePassword(user);

		if(queryCount == 0)
		{
			redirectAttributes.addFlashAttribute("result", "비밀번호변경 실패");
		 	return "redirect:/find-password-page"; 
		}

		redirectAttributes.addFlashAttribute("result", "비밀번호변경 성공");

		 return "redirect:/find-password-page"; 
	}

	@PostMapping("/admin-ban")
	public String adminBan(@RequestParam int userIdx,
						   @RequestParam boolean ban,
						   RedirectAttributes redirectAttributes) {
		User user = userService.findByIdx(userIdx);

		if(user.isBan() == ban)
		{
			if(ban)
			{
				redirectAttributes.addFlashAttribute("result", "이미 정지당한 유저입니다");
				return "redirect:/admin-page";
			}
			else
			{
				redirectAttributes.addFlashAttribute("result", "정지당하지 않은 유저입니다");
				return "redirect:/admin-page";
			}
		}

		int queryCount = userService.UpdateBan(ban, userIdx);

		if(queryCount == 0)
		{
			redirectAttributes.addFlashAttribute("result", "유저 정보가 올바르지 않습니다");
			return "redirect:/admin-page";
		}

		if(ban)
		{
			redirectAttributes.addFlashAttribute("result", "해당 유저를 정지시켰습니다");
		}
		else
		{
			redirectAttributes.addFlashAttribute("result", "해당 유저의 정지를 해제했습니다");
		}
							
		return "redirect:/admin-page";
	}
	
}
