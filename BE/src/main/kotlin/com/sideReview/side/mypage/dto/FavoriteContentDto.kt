package com.sideReview.side.mypage.dto

data class FavoriteContentDto(
    val id : String,
    val poster : String?,
    val name : String,
    val year : String? = null,
    var rating: String = "-",
    val provider: List<Int>,
    val rank: Int
)