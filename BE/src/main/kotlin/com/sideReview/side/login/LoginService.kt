package com.sideReview.side.login

import com.fasterxml.jackson.databind.ObjectMapper
import com.sideReview.side.common.dto.UserInfoDto
import com.sideReview.side.common.entity.UserInfo
import com.sideReview.side.common.repository.UserInfoRepository
import com.sideReview.side.login.google.dto.GoogleProfileResponse
import com.sideReview.side.login.kakao.dto.KakaoProfileResponse
import com.sideReview.side.login.naver.dto.NaverProfileResponse
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.session.SessionRegistry
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


@Service
class LoginService(
    val userInfoRepository: UserInfoRepository,
    val nicknameService: NicknameService,
    val sessionRegistry: SessionRegistry
) {
    val logger = LoggerFactory.getLogger(this::class.java)!!

    @Transactional
    fun saveUser(type: String, response: Any): UserInfo {
        var id: String = ""
        var name: String = ""
        var profile: String = ""
        var email: String = ""
        when (type) {
            "naver" -> {
                val dto = response as NaverProfileResponse
                id = dto.response.id
                name = nicknameService.makeNickname(0)
                profile = dto.response.profile_image
                email = dto.response.email
            }

            "google" -> {
                val dto = response as GoogleProfileResponse
                id = dto.id
                name = nicknameService.makeNickname(2)
                profile = dto.picture
                email = dto.email
            }

            "kakao" -> {
                val dto = response as KakaoProfileResponse
                id = "${dto.id}"
                name = nicknameService.makeNickname(1)
                profile = if (dto.kakao_account.profile.thumbnail_image_url.isNullOrBlank()) ""
                else dto.kakao_account.profile.thumbnail_image_url
                email = dto.kakao_account.email
            }
        }
        val user = userInfoRepository.findById(id)
        return if (user.isPresent) {
//            if (user.get().nickname != name) user.get().nickname = name
            if (user.get().profile != profile) user.get().profile = profile
            if (user.get().email != email) user.get().email = email
            user.get()
        } else
            userInfoRepository.save(
                UserInfo(
                    id,
                    type,
                    name,
                    profile,
                    email,
                    null,
                    null,
                    0,
                    null,
                    null
                )
            )
    }

    fun authenticateUser(id: String, type: String): Boolean {
        return userInfoRepository.existsByUserIdAndLoginType(id, type)
    }

    fun createOrUpdateSession(
        saveUser: UserInfo,
        request: HttpServletRequest,
        response: HttpServletResponse,
        token: String
    ): ResponseEntity<String> {
        val userInfoDto = UserInfoDto(saveUser, token)

        // 기존 세션 중 동일한 유저 정보를 가지고 있는 세션이 있으면 삭제
        val principals = sessionRegistry.allPrincipals
        for (principal in principals) {
            if (principal is UserDetails) {
                if (principal.username.equals(userInfoDto.id)) {
                    val sessions = sessionRegistry.getAllSessions(principal, false)
                    for (sessionInformation in sessions) {
                        // 각 세션에 대한 작업 수행
                        logger.info("동일 유저 이전 session 삭제 : ${sessionInformation.sessionId}")
                        sessionInformation.expireNow()
                    }
                }
            }
        }

        // 새 세션 만들어서 리턴
        val authorities: List<GrantedAuthority> = listOf(SimpleGrantedAuthority("ROLE_USER"))
        val authentication: Authentication =
            UsernamePasswordAuthenticationToken(userInfoDto, null, authorities)
        SecurityContextHolder.getContext().authentication = authentication

        val httpSession = request.getSession(true)
        httpSession.setAttribute("user", userInfoDto)
        httpSession.maxInactiveInterval = 86400;

        val logAuth = SecurityContextHolder.getContext().authentication
        if (logAuth != null && logAuth.principal is UserInfoDto) {
            val logInfo = logAuth.principal as UserInfoDto
            // 이제 userInfoDto를 사용할 수 있음
            logger.info("session principal에 저장 : $logInfo")
        }

        val obj: MutableMap<String, Any> = HashMap<String, Any>()
        obj["userInfoDto"] = userInfoDto
        obj["sessionId"] = httpSession.id
        return ResponseEntity.ok(ObjectMapper().writeValueAsString(obj))
    }

    @Transactional
    fun changeToggle(user: UserInfoDto, toggle: Boolean) {
        if (!userInfoRepository.existsById(user.id)) throw LoginToggleUserIdInvalidException("Cannot change user parameter toggle. User Id Invalid.")
        userInfoRepository.findById(user.id).get().toggle = if (toggle) 1 else 0
    }

    fun isOttTrue(userId: String): Boolean {
        val user = userInfoRepository.findById(userId)
        return user.isPresent && userInfoRepository.findById(userId).get().toggle == 1
    }

    fun getUser(userId: String): UserInfo {
        return userInfoRepository.findById(userId).get()
    }

}