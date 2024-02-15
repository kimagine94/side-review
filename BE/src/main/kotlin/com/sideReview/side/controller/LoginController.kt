package com.sideReview.side.controller

import com.sideReview.side.login.LoginService
import com.sideReview.side.login.google.GoogleClientAuth
import com.sideReview.side.login.google.GoogleClientProfile
import com.sideReview.side.login.google.dto.GoogleRequest
import com.sideReview.side.login.kakao.KakaoClient
import com.sideReview.side.login.naver.NaverClientAuth
import com.sideReview.side.login.naver.NaverClientProfile
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler
import org.springframework.session.web.http.CookieSerializer
import org.springframework.session.web.http.DefaultCookieSerializer
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@RestController
class LoginController(
    val googleClientProfile: GoogleClientProfile,
    val googleClientAuth: GoogleClientAuth,
    val naverClientAuth: NaverClientAuth,
    val naverClientProfile: NaverClientProfile,
    val kakaoClient: KakaoClient,
    val loginService: LoginService,
    private val cookieSerializer: DefaultCookieSerializer
) {

    @GetMapping("/login/naver")
    fun getNaverProfile(
        @RequestParam code: String,
        @RequestParam state: String,
        request: HttpServletRequest,
        response: HttpServletResponse

    ): ResponseEntity<String> {
        val auth = naverClientAuth.getAuth(code, state).access_token
        val profile = naverClientProfile.getProfile("Bearer $auth")
        val saveUser = loginService.saveUser("naver", profile)
        return loginService.createOrUpdateSession(saveUser, request, response)
    }

    @GetMapping("/login/google")
    fun getGoogleProfile(
        @RequestParam code: String,
        @RequestParam uri: String,
        request: HttpServletRequest,
        response: HttpServletResponse
    ): ResponseEntity<String> {
        val auth = googleClientAuth.getAuth(
            GoogleRequest(
                code = code, redirect_uri = uri
            )
        )
        val profile = googleClientProfile.getProfile(auth.access_token)
        val saveUser = loginService.saveUser("google", profile)
        return loginService.createOrUpdateSession(saveUser, request, response)
    }

    @GetMapping("/login/kakao")
    fun getKakaoProfile(
        @RequestParam code: String,
        @RequestParam uri: String,
        request: HttpServletRequest,
        response: HttpServletResponse

    ): ResponseEntity<String> {
        val auth = kakaoClient.getAuth(uri, code)
        val profile = kakaoClient.getProfile(
            "${auth.token_type} ${auth.access_token}"
        )
        val saveUser = loginService.saveUser("kakao", profile)
        return loginService.createOrUpdateSession(saveUser, request, response)
    }

    @GetMapping("/logout")
    fun logout(request: HttpServletRequest, response: HttpServletResponse):ResponseEntity<String> {

        // 세션 삭제
        request.getSession(false).invalidate()

        // 기존 쿠키 삭제
        val cookieNames = arrayOf("JSESSIONID") // 여러 쿠키 이름이 있다면 추가
        for (cookieName in cookieNames) {
            val cookie = Cookie(cookieName, null)
            cookie.path = "/"
            cookie.maxAge = 0
            cookie.secure = true
            cookie.isHttpOnly = true
            cookieSerializer.setSameSite("Lax")
            cookieSerializer.writeCookieValue(
                CookieSerializer.CookieValue(
                    request,
                    response,
                    cookie.value
                )
            )
        }

        // 사용자 인증 정보 삭제
        SecurityContextHolder.getContext().authentication = null
        val securityContextLogoutHandler = SecurityContextLogoutHandler()
        securityContextLogoutHandler.logout(request, response, null)


        // 새로운 쿠키 생성 및 응답에 추가
//        val newCookie = Cookie("JSESSIONID", "") // 여러 쿠키가 있다면 추가
//        newCookie.path = "/"
//        newCookie.secure = true
//        newCookie.isHttpOnly = true
        // SameSite 설정을 삭제하고 싶다면 아래 주석 처리된 라인을 사용하세요.
        // newCookie.sameSite = "None"
//        response.addCookie(newCookie)
        return ResponseEntity.ok("logout success")
    }
}
