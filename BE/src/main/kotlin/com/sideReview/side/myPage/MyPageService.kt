package com.sideReview.side.myPage

import com.google.gson.Gson
import com.jillesvangurp.ktsearch.SearchResponse
import com.sideReview.side.common.document.ContentDocument
import com.sideReview.side.common.dto.PageInfo
import com.sideReview.side.common.entity.UserFavoritePerson
import com.sideReview.side.common.repository.UserInfoRepository
import com.sideReview.side.common.util.MapperUtils
import com.sideReview.side.myPage.dto.FavoriteContentDto
import com.sideReview.side.myPage.dto.FavoriteContentPageDto
import com.sideReview.side.myPage.dto.FavoritePersonDto
import com.sideReview.side.myPage.repository.UserFavoritePersonRepository
import com.sideReview.side.openSearch.OpenSearchDetailService
import com.sideReview.side.openSearch.OpenSearchGetService
import com.sideReview.side.person.PersonService
import com.sideReview.side.review.UserStarRatingRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MyPageService(
    val openSearchGetService: OpenSearchGetService,
    val openSearchDetailService: OpenSearchDetailService,
    val userStarRatingRepository: UserStarRatingRepository,
    val userFavoritePersonRepository: UserFavoritePersonRepository,
    val userInfoRepository: UserInfoRepository,
    val personService: PersonService
) {
    suspend fun getKeywordContent(
        userId: String,
        keyword: String,
        page: Int,
        size: Int
    ): FavoriteContentPageDto {
        val response: SearchResponse =
            openSearchGetService.findDocumentByKeyword(keyword, page, size)
        val hits = response.hits?.hits
        val total = response.hits?.total?.value?.toInt() ?: 0
        val contentList: MutableList<FavoriteContentDto> = mutableListOf()
        val totalPages = if (total % size == 0) total / size else total / size + 1

        hits?.forEach {
            val source = it.source
            val document = Gson().fromJson("$source", ContentDocument::class.java)
            val personList = MapperUtils.parseToPersonDocument(
                openSearchDetailService.findDirectorByContentId(document.id)
            )
            val rating =
                userStarRatingRepository.findOneByTargetIdAndWriterId(document.id, userId)?.rating
            val userFavoriteContentDto = FavoriteContentDto(
                id = document.id,
                poster = document.poster,
                name = document.name,
                year = if (document.firstAirDate == null) "unknown" else document.firstAirDate.substring(
                    0,
                    4
                )!!,
                director = if (personList == null) emptyList() else openSearchDetailService.filterCreditInfo(
                    personList,
                    document.id
                ).second.map { it.name },
                genre = document.genre,
                country = document.production?.country ?: emptyList(),
                rating = rating?.toString() ?: "-"
            )
            contentList.add(userFavoriteContentDto)
        }
        return FavoriteContentPageDto(contentList, PageInfo(total, totalPages, page))
    }

    suspend fun getKeywordPerson(
        keyword: String,
        page: Int,
        size: Int
    ): FavoritePersonDto {
        val matchPerson =
            personService.searchMatch(keyword, (page - 1) * size, size)
        val matchPersonDto = MapperUtils.parseToPersonDto(matchPerson)
        val personDtoList = openSearchDetailService.fillCastCrew(matchPersonDto)
        val total = matchPerson.hits?.total?.value?.toInt() ?: 0
        val totalPages = if (total % size == 0) total / size else total / size + 1
        return FavoritePersonDto(personDtoList, PageInfo(total, totalPages, page))
    }

    @Transactional
    fun saveFavoritePerson(userId: String, personId: String): UserFavoritePerson {
        val user = userInfoRepository.getReferenceById(userId)
        return userFavoritePersonRepository.save(
            UserFavoritePerson(personId = personId, userInfo = user)
        )
    }
}