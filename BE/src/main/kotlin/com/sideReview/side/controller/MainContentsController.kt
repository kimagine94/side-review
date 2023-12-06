package com.sideReview.side.controller

import com.sideReview.side.common.util.ContentUtils
import com.sideReview.side.common.util.MapperUtil
import com.sideReview.side.openSearch.OpenSearchGetService
import com.sideReview.side.openSearch.dto.*
import com.sideReview.side.person.PersonService
import com.sideReview.side.review.ReviewService
import kotlinx.coroutines.runBlocking
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class MainContentsController @Autowired constructor(
    private val openSearchGetService: OpenSearchGetService,
    private val personService: PersonService,
    private val reviewService: ReviewService
) {

    @PostMapping("/contents")
    fun getContents(
        @RequestBody request: ContentRequestDTO
    ): ResponseEntity<Any> {
        var response: ResponseEntity<Any> = ResponseEntity(HttpStatus.BAD_REQUEST)
        runBlocking {
            when (request.tab) {
                "main" -> {
                    val popular = reviewService.fillReview(
                        ContentUtils.fill(
                            MapperUtil.parseToContentDto(
                                openSearchGetService.get(request.tab, "popularity", request)
                            )
                        )
                    )

                    val latest = reviewService.fillReview(
                        ContentUtils.fill(
                            MapperUtil.parseToContentDto(
                                openSearchGetService.get(request.tab, "new", request)
                            )
                        )
                    )
                    response = ResponseEntity.ok(
                        MainContentDto(
                            popular,
                            latest
                        )
                    )
                }

                "popularity" -> {
                    response = ResponseEntity.ok(
                        reviewService.fillReview(
                            ContentUtils.fill(
                                MapperUtil.parseToContentDto(
                                    openSearchGetService.get(request.tab, "popularity", request)
                                )
                            )
                        )
                    )
                }

                "new" -> {
                    response = ResponseEntity.ok(
                        reviewService.fillReview(
                            ContentUtils.fill(
                                MapperUtil.parseToContentDto(
                                    openSearchGetService.get(request.tab, "new", request)
                                )
                            )
                        )
                    )
                }

                "search" -> {
                    if (request.query.isNullOrBlank()) {
                        response = ResponseEntity.ok(
                            ContentUtils.fill(
                                MapperUtil.parseToContentDto(
                                    openSearchGetService.get(request.tab, "popularity", request)
                                )
                            )
                        )
                    } else {
                        response = ResponseEntity.ok(
                            SearchContentDto(
                                match = MatchDto(
                                    content =
                                    ContentUtils.fill(
                                        MapperUtil.parseToContentDto(
                                            openSearchGetService.get(
                                                "search",
                                                request.sort,
                                                request
                                            )
                                        )
                                    ),
                                    person = MapperUtil.parseToPersonDto(
                                        personService.searchMatch(request.query)
                                    )
                                ),
                                similar = ContentUtils.fill(
                                    MapperUtil.parseToContentDto(
                                        openSearchGetService.search(request.sort, request)
                                    )
                                )
                            )
                        )
                    }
                }
            }
        }
        return response
    }
}
