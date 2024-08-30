package com.sideReview.side.login

import org.slf4j.LoggerFactory
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler
import org.springframework.stereotype.Component
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class LogoutSuccessHandler : LogoutSuccessHandler {
    override fun onLogoutSuccess(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authentication: Authentication
    ) {
        val logger = LoggerFactory.getLogger(this::class.java)!!

//        val targetUrl = request.requestURL.split("/api/")[0]
        val targetUrl = request.getHeader("origin").split("/api/")[0]

        logger.info("logout hander : $targetUrl")
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Credentials", "false");
        response.sendRedirect("$targetUrl/redirect")
    }
}