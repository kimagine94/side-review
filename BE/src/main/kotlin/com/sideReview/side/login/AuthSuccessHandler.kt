package com.sideReview.side.login

import com.sideReview.side.common.entity.UserInfo
import com.sideReview.side.common.util.MapperUtils
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import org.springframework.stereotype.Component
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class AuthSuccessHandler : AuthenticationSuccessHandler {
    override fun onAuthenticationSuccess(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authentication: Authentication
    ) {
        val principal = authentication.principal as CustomOAuth2User
        val userInfoDto =
            MapperUtils.mapUserInfoToLoginResponseDto(principal.attributes["user"] as UserInfo)
        val targetUrl = request.requestURI
        response.addHeader("userId", userInfoDto.id)
        response.sendRedirect("${targetUrl}/login/redirect")
    }
}