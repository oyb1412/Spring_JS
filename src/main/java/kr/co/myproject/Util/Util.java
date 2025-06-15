package kr.co.myproject.Util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.servlet.http.HttpServletRequest;
import kr.co.myproject.controller.JSPageController;

public class Util {
    private static final Logger logger = LoggerFactory.getLogger(JSPageController.class);

    public static String PageRefresh(HttpServletRequest request)
    {
        String referer = request.getHeader("Referer");
        return "redirect:" + referer;
    }

    public static void ResetFinalURL(HttpServletRequest request, String url)
    {
        logger.info("finalUrl 리셋 ");
        request.getSession().setAttribute("finalUrl", url);
    }

    public static void SaveFinalURL(HttpServletRequest request)
    {
        String uri = request.getRequestURI();

        if (!"GET".equalsIgnoreCase(request.getMethod())) return;

        if (uri.equals("/") && request.getQueryString() == null) return;

        if (uri.endsWith(".css") || uri.endsWith(".js") || uri.endsWith(".png") || uri.endsWith(".ico")) {
            return; // 정적 리소스면 무시
        }

        String query = request.getQueryString();
        String fullUrl = uri + (query != null ? "?" + query : "");
        logger.info("finalUrl 저장 : " + fullUrl);
        request.getSession().setAttribute("finalUrl", fullUrl);
    }

    public static String RedirectFinalURL(HttpServletRequest request){
        String finalUrl = (String) request.getSession().getAttribute("finalUrl");
        if (finalUrl != null) {
            request.getSession().removeAttribute("finalUrl");
            logger.info("finalUrl 호출 : " + finalUrl);

            return "redirect:" + finalUrl;
        }
        return "redirect:/";
    }

    public static String GetFinalURL(HttpServletRequest request){
        String finalUrl = (String) request.getSession().getAttribute("finalUrl");
        if (finalUrl != null) {
            logger.info("finalUrl get : " + finalUrl);
           return finalUrl;
        }
        logger.info("finalUrl get : " + "/");
        return "/";
    }
}