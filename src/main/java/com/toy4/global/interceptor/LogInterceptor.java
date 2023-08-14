package com.toy4.global.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Slf4j
public class LogInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String scheme = request.getScheme();
        String method = request.getMethod();
        String requestURI = request.getRequestURI();
        String logId = UUID.randomUUID().toString();
        request.setAttribute("logId", logId);
        log.info("[STT_{}] {} {} {}", logId, scheme, method, requestURI);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        String scheme = request.getScheme();
        String method = request.getMethod();
        String requestURI = request.getRequestURI();
        String logId = (String) request.getAttribute("logId");
        log.info("[END_{}] {} {} {}", logId, scheme, method, requestURI);
    }
}
