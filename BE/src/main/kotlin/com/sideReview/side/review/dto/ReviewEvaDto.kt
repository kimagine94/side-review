package com.sideReview.side.review.dto

data class ReviewEvaDto(
    val reviewId: String,
    val eval: Int // 0 = dislike, 1 = like
)