package com.sideReview.side.openSearch.dto

import com.google.gson.Gson
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.annotations.JsonAdapter
import com.google.gson.reflect.TypeToken
import com.sideReview.side.review.dto.ReviewDTO
import kotlinx.serialization.Serializable
import java.lang.reflect.Type

@JsonAdapter(ContentDeserializer::class)
@Serializable
data class ContentDto(
    //TODO : actor, rating, age
    val id: String,
    val name: String,
    val platform: List<String>,
    val genre: List<String>? = null,
    val year: String? = null,
    var synopsis: String? = null,
    val trailer: String? = null,
    var poster: String? = null,
    val rating: Double? = null,
    val actors: List<String>? = null,
    val age: Int? = null,
    val season: List<String>? = null,
    var review: ReviewDTO? = null
)

class ContentDeserializer : JsonDeserializer<ContentDto> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): ContentDto {
        val jsonObject =
            json?.asJsonObject ?: throw NullPointerException("Response Json String is null")
        val gson = Gson()
        val typeToken: Type = object : TypeToken<List<String>>() {}.type

        val id = jsonObject["id"].asString
        val name = jsonObject["name"].asString
        val platform = gson.fromJson<List<String>>(jsonObject["platform"], typeToken)
        val genre = gson.fromJson<List<String>>(jsonObject["genre"], typeToken)
        val year = jsonObject["firstAirDate"]?.asString?.substring(0, 4)
        val synopsis = jsonObject["synopsis"]?.asString
        val trailer_ = gson.fromJson<List<String>>(jsonObject["trailer"], typeToken)
        var trailer = ""
        if (trailer_.isNotEmpty()) {
            trailer = trailer_[0]
        }
        val poster = jsonObject["poster"]?.asString
        val rating = jsonObject["rating"]?.asDouble
        val season = gson.fromJson<List<String>>(jsonObject["season"], typeToken)

        return ContentDto(
            id = id,
            name = name,
            platform = platform,
            genre = genre,
            rating = rating,
            year = year,
            synopsis = synopsis,
            trailer = trailer,
            poster = poster,
            actors = null,
            age = null,
            season = season,
            review = null
        )
    }

}