package com.sideReview.side.login.entity

import java.io.Serializable
import javax.persistence.*


@Entity
@Table(name = "user_favorite_content", catalog = "meta")
@IdClass(UserFavoriteContentIdClass::class)
data class UserFavoriteContent(

    @Id
    @Column(name = "user_id", length = 50, nullable = false)
    val userId: String,

    @Id
    @Column(name = "content_id", length = 36, nullable = false)
    val contentId: String,

    @Column(name = "rank", length = 1)
    val rank: Int,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "user_Id")
    val userInfo: UserInfo
)


data class UserFavoriteContentIdClass(
    var userId: String = "",
    var contentId: String = ""
) : Serializable