package kr.co.myproject.Util;

import jakarta.servlet.http.HttpServletRequest;

public class Util {
    public static String PageRefresh(HttpServletRequest request)
    {
        String referer = request.getHeader("Referer");
        return "redirect:" + referer;
    }
}
