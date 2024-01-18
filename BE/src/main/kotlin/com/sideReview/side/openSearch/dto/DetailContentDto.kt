package com.sideReview.side.openSearch.dto

import kotlinx.serialization.Serializable

data class DetailContentDto(
    val id: String,
    val name: String,
    val originalName: String? = null,
    val originCountry: List<String>? = null,
    val platform: List<Int>?,
    val genre: List<Int>?,
    val date: String? = null,
    val synopsis: String?,
    val trailer: List<String>? = null,
    val photo: List<String>? = null,
    val poster: String? = null,
    val actors: List<Actor>? = null, // TODO : actors로 용어 수정, search랑 공통으로  #convention
    val crew: List<Crew>? = null,
    val rating: Double? = null, // TODO: total, user rating이랑 묶어서 ratingDto로 사용하기 #covention
    val totalRating: Int = 0,
    val age: Int? = null,
    var season: Season
    // TODO: ratingDto 만드는 공통 서비스 만들기!
)
data class Actor(
    val name: String,
    val id: String,
    val role: String,
    val profilePath: String
)

data class Crew(
    val name: String,
    val id: String,
    val job: String,
    val profilePath: String
)
@Serializable
data class Season(
    val now: Int,
    val list: List<String> = emptyList()
)