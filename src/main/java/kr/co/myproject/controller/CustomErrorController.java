package kr.co.myproject.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class CustomErrorController implements ErrorController{
	//404에러가 뜰때 특정 액션을 취할수 있게 해주는 인터페이스
	
	@GetMapping("/error")
	public String handleError(HttpServletRequest request, 
                              RedirectAttributes redirectAttributes)
	{
		int statusCode = (int)request.getAttribute("jakarta.servlet.error.status_code");
		
		if (statusCode == 404) {
        redirectAttributes.addFlashAttribute("result", "페이지를 찾을 수 없습니다 (404)");
        return "redirect:/";
    }

    if (statusCode == 403) {
        redirectAttributes.addFlashAttribute("result", "접근 권한이 없습니다 (403)");
        return "redirect:/";
    }

    redirectAttributes.addFlashAttribute("result", "알 수 없는 오류가 발생했습니다.");
    return "redirect:/";
	}
}


