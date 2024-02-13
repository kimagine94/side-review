package com.sideReview.side.controller

import com.sideReview.side.common.dto.UserInfoDto
import com.sideReview.side.common.util.ClientUtils
import com.sideReview.side.login.LoginUser
import com.sideReview.side.review.ReviewService
import com.sideReview.side.review.dto.ReviewCreateDto
import com.sideReview.side.review.exception.*
import io.ktor.util.logging.*
import org.slf4j.LoggerFactory
import org.springframework.data.domain.PageRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping("/review")
class ReviewController(val reviewService: ReviewService) {
    val logger = LoggerFactory.getLogger(this::class.java)!!

    @PutMapping("")
    fun createOrUpdate(
        @RequestBody body: ReviewCreateDto,
        @LoginUser(required = false) user: UserInfoDto?,
        request: HttpServletRequest
    ): ResponseEntity<Any> {
        logger.info("SSSSSSSSSSSSSSSS")
        logger.info(user.toString())
        try {
            reviewService.createOrUpdate(
                body, ClientUtils.getUserId(request, user), ClientUtils.getUserType(request, user)
            )
            return ResponseEntity(HttpStatus.OK)
        } catch (e: Exception) {
            logger.error(e.message)
            logger.error(e.stackTraceToString())
            return when (e) {
                is ReviewUpdateUserInvalidException -> {
                    logger.error("UserId : ${ClientUtils.getUserId(request, user)}")
                    ResponseEntity.badRequest().body(e.message)
                }

                is ReviewSaveDuplicateException, is ReviewGetIdInvalidException ->
                    ResponseEntity.badRequest().body(e.message)

                else -> ResponseEntity.internalServerError().body(e.message)
            }
        }
    }

//    @PutMapping("")
//    fun evaluate(@RequestBody body: ReviewEvaDto): ResponseEntity<Any> {
//        if (body.eval != 0 && body.eval != 1) return ResponseEntity(HttpStatus.BAD_REQUEST)
//        runCatching {
//            reviewService.evaluate(body)
//        }.onFailure {
//            return ResponseEntity(HttpStatus.BAD_REQUEST)
//        }
//
//        return ResponseEntity(HttpStatus.OK)
//    }

    @GetMapping("/{id}")
    fun getAllReviewsById(
        @PathVariable id: String,
        @RequestParam(required = false, defaultValue = "best") sort: String,
        @RequestParam(required = false, defaultValue = "0") spoiler: String,
        @RequestParam(required = false, defaultValue = "0") page: String,
        @RequestParam(required = false, defaultValue = "6") size: String,
        @RequestParam(required = false, defaultValue = "0") type: String,
        @LoginUser(required = false) user: UserInfoDto?

//        request: HttpServletRequest
    ): ResponseEntity<Any> {
//        val userId = ClientUtils.getUserId(request)
        val userId = user?.id
        val pageable = PageRequest.of(page.toInt(), size.toInt())
        try {
            return ResponseEntity.ok(
                reviewService.getReviewsByTargetId(
                    id,
                    sort,
                    spoiler,
                    type,
                    pageable,
                    userId ?: ""
                )
            )
        } catch (e: Exception) {
            when (e) {
                is ReviewGetAllSortException, is ReviewGetAllTypeException, is ReviewGetAllTypeException ->
                    return ResponseEntity.badRequest().build()

                else -> return ResponseEntity.internalServerError().build()
            }
        }
    }

    @GetMapping("/user/{userId}")
    fun getAllReviewsByWriterId(
        @RequestParam(required = false, defaultValue = "0") page: String,
        @RequestParam(required = false, defaultValue = "6") size: String,
        @PathVariable userId: String
    ): ResponseEntity<Any> {
        val pageable = PageRequest.of(page.toInt(), size.toInt())
        return ResponseEntity.ok(reviewService.gerReviewsByWriterId(userId, pageable))
    }
}