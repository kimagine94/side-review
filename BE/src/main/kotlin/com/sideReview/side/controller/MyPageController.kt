package com.sideReview.side.controller

import com.sideReview.side.login.NicknameService
import com.sideReview.side.mypage.MyPageService
import com.sideReview.side.mypage.dto.FavoriteContentInputDto
import kotlinx.coroutines.runBlocking
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/user/{userId}")
class MyPageController(
    val nicknameService: NicknameService,
    val myPageService: MyPageService,
) {
    @GetMapping
    fun getMyPage(@PathVariable userId: String
    ): ResponseEntity<Any> {
        return ResponseEntity.ok(myPageService.getMyPage(userId))
    }

    @PutMapping("/nickname")
    fun updateNickname(
        @PathVariable userId: String,
        @RequestParam name: String
    ): ResponseEntity<Any> {
        nicknameService.editNickname(userId, name)
        return ResponseEntity(HttpStatus.OK)
    }

    @GetMapping("/contents")
    fun getContents(
        @PathVariable userId: String,
        @RequestParam keyword: String,
        @RequestParam size: Int,
        @RequestParam page: Int
    ): ResponseEntity<Any> {
        var response: ResponseEntity<Any>
        runBlocking {
            response =
                ResponseEntity.ok(myPageService.getKeywordContent(userId, keyword, page, size))
        }
        return response
    }

    @GetMapping("/person")
    fun searchFavoritePerson(
        @PathVariable userId: String,
        @RequestParam keyword: String,
        @RequestParam size: Int,
        @RequestParam page: Int
    ): ResponseEntity<Any> {
        var response: ResponseEntity<Any> = ResponseEntity(HttpStatus.BAD_REQUEST)
        runBlocking {
            response =
                ResponseEntity.ok(myPageService.getKeywordPerson(keyword, page, size))
        }
        return response
    }

    @PutMapping("/person")
    fun saveFavoritePerson(
        @PathVariable userId: String,
        @RequestParam personId: String,
    ): ResponseEntity<Any> {
        var person: Any? = null
        kotlin.runCatching {
            myPageService.saveFavoritePerson(userId, personId)
            runBlocking {
                person = myPageService.getOnePerson(personId)
            }
        }.onFailure {
            return ResponseEntity.internalServerError().body(it.message)
        }
        return ResponseEntity.ok(person)
    }

    @PutMapping("/contents")
    fun saveFavoriteContents(
        @PathVariable userId: String,
        @RequestBody contentsList: List<FavoriteContentInputDto>
    ): ResponseEntity<Any> {
        println("dd")
        return ResponseEntity.ok(myPageService.saveFavoriteContent(userId, contentsList))
    }

    @DeleteMapping("/contents")
    fun deleteFavoriteContent(
        @PathVariable userId: String,
        @RequestParam("contentId") contentId: String
    ): ResponseEntity<Any> {
        return ResponseEntity.ok(myPageService.deleteFavoriteContent(userId, contentId))
    }

    @PutMapping("/ott")
    fun saveUserOTT(
        @PathVariable userId: String,
        @RequestBody ottList: List<Integer>
    ): ResponseEntity<Any> {
        return ResponseEntity.ok(myPageService.saveOTT(userId, ottList))
    }

    @PutMapping("/genre")
    fun saveUserGenre(
        @PathVariable userId: String,
        @RequestBody genreList: List<Integer>
    ): ResponseEntity<Any> {
        return ResponseEntity.ok(myPageService.saveGenre(userId, genreList))
    }
}