package com.sideReview.side.openSearch.dto

import com.google.gson.Gson
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.annotations.JsonAdapter
import com.google.gson.reflect.TypeToken
import kotlinx.serialization.Serializable
import java.lang.reflect.Type

@JsonAdapter(PersonDtoDeserializer::class)
@Serializable
data class PersonDto(
    val id: Int,
    val name: String,
    val profilePath: String?,
    val cast: List<PersonRoleDto>?,
    val crew: List<PersonJobDto>?
)

@Serializable
data class PersonRoleDto(
    val role: String,
    val contentId: String
)

@Serializable
data class PersonJobDto(
    val job: String,
    val contentId: String
)

class PersonDtoDeserializer : JsonDeserializer<PersonDto> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): PersonDto {
        val gson = Gson()
        val jsonObject =
            json?.asJsonObject ?: throw NullPointerException("Response Json String is null")
        val typeTokenCast: Type = object : TypeToken<List<PersonRoleDeserializeDto>>() {}.type
        val typeTokenCrew: Type = object : TypeToken<List<PersonJobDeserializeDto>>() {}.type

        val id = jsonObject["id"].asInt
        val name = jsonObject["name"].asString
        val profilePath = jsonObject["profilePath"]?.asString ?: ""
        val castString =
            gson.fromJson<List<PersonRoleDeserializeDto>>(jsonObject["cast"], typeTokenCast)
        val cast: MutableList<PersonRoleDto> = mutableListOf()
        if (castString != null && castString.isNotEmpty()) {
            for (c in castString) {
                cast.add(
                    PersonRoleDto(
                        role = c.role,
                        contentId = c.contentId
                    )
                )
            }
        }
        val crewString =
            gson.fromJson<List<PersonJobDeserializeDto>>(jsonObject["crew"], typeTokenCrew)
        val crew: MutableList<PersonJobDto> = mutableListOf()
        if (crewString != null && crewString.isNotEmpty()) {
            for (c in crewString) {
                crew.add(
                    PersonJobDto(
                        job = c.job,
                        contentId = c.contentId
                    )
                )
            }
        }
        return PersonDto(
            id = id,
            name = name,
            profilePath = profilePath,
            cast = cast,
            crew = crew
        )
    }
}


@Serializable
private class PersonRoleDeserializeDto(
    val role: String,
    val contentId: String
)

@Serializable
private class PersonJobDeserializeDto(
    val job: String,
    val contentId: String
)
