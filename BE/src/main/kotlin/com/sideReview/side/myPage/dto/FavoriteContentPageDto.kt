package com.sideReview.side.myPage.dto

import com.sideReview.side.review.dto.PageInfo

data class FavoriteContentPageDto(
    val content: List<FavoriteContentDto>,
    val pageInfo: PageInfo
)