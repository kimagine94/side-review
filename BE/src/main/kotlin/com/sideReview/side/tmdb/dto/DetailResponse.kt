package com.sideReview.side.tmdb.dto

data class DetailResponse(
    val adult : Boolean?,
    val backdrop_path : String?,
    val created_by : List<Map<String, Any>>?,
    val episode_run_time : List<Any>?,
    val first_air_date : String?,
    val genres : List<Genre>?,
    val homepage : String?,
    val id :Int,
    val in_production : Boolean?,
    val languages : List<String>?,
    val last_air_date : String?,
    val last_episode_to_air : Episode?,
    val name: String,
    val next_episode_to_air: Episode?,
    val networks : List<Map<String, Any>>,
    val number_of_episodes : Int?,
    val number_of_seasons : Int?,
    val origin_country: List<String>,
    val original_language : String,
    val original_name : String,
    val overview : String?,
    val popularity : Float?,
    val poster_path : String?,
    val production_companies : List<ProductionCompany>?,
    val production_countries : List<ProductionCounty>?,
    val seasons : List<Season>?,
    val spoken_languages : List<Map<String, String>>?,
    val status : String?,
    val tagline: String?,
    val type: String?,
    val vote_average: Float?,
    val vote_count: Int?
)
data class Episode(
    val id: Int?,
    val name: String?,
    val overview: String?,
    val vote_average: Double?,
    val vote_count: Int?,
    val air_date: String?,
    val episode_number: Int?,
    val episode_type: String?,
    val production_code: String?,
    val runtime: Int?,
    val season_number: Int?,
    val show_id: Int?,
    val still_path: String?
)
data class Season(
    val air_date: String?,
    val episode_count: Int?,
    val id: Int,
    val name: String?,
    val overview: String?,
    val poster_path: String?,
    val season_number: Int,
    val vote_average: Double?
)

data class Genre(
    val id: Int,
    val name : String
)

data class ProductionCompany(
    val id: Int,
    val logo_path: String?,
    val name: String,
    val origin_country: String?
)

data class ProductionCounty(
    val iso_3166_1: String,
    val name: String
)