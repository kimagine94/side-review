package com.sideReview.side.common.dto

import com.sideReview.side.common.entity.UserInfo
import kotlinx.serialization.Serializable

@Serializable
data class UserInfoDto @JvmOverloads constructor(
    val id: String = "",
    var nickname: String = "",
    var profile: String = "",
    val type: String = "",
    val toggle: Boolean = false
) {
    constructor(user: UserInfo) : this(
        user.userId,
        user.nickname,
        user.profile,
        user.loginType,
        user.toggle == 1
    )
}