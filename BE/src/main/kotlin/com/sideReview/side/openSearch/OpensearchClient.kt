package com.sideReview.side.openSearch

import com.google.gson.Gson
import com.jillesvangurp.ktsearch.SearchResponse
import com.jillesvangurp.searchdsls.querydsl.*
import com.sideReview.side.common.document.ContentDocument
import com.sideReview.side.common.document.PersonDocument
import com.sideReview.side.common.util.MapperUtils
import com.sideReview.side.openSearch.dto.*
import kotlinx.coroutines.runBlocking
import org.springframework.stereotype.Service
import java.text.SimpleDateFormat
import java.util.*

@Service
class OpensearchClient(
    val openSearchGetService: OpenSearchGetService,
    val openSearchDetailService: OpenSearchDetailService
) {
    /**
     *  openSearchGetService를 통해 전체 Document 가져온다.
     *  openSearchDetailService를 통해 전체 Document 중 추가로 가져와야 할 정보를 가져온다.
     *                     + 정보를 DetailContent or DetailPerson Dto로 변경한다.
     *  OpensearchClient를 통해 DetailContent or DetailPerson Dto를 필요한 형태로 변환한 후 리턴한다.
     */

    // template
    // 사용 방법 가이드
//    fun findContents() {
//        // SearchResponse 가져오는 단계
//        val request: ContentRequestDTO = ContentRequestDTO(
//            tab = null,
//            sort = null,
//            query = "",
//            notQuery = null,
//            filter = null,
//            pagination = null
//        )
//        val query: (request: ContentRequestDTO) -> ESQuery = (
//
//                )
//        val response: SearchResponse
//        runBlocking {
//            response = openSearchGetService.search("content", request, query)
//        }
//
//        // Response 가공 단계 > 각자 알아서
//    }

    fun getOneContent(id: String, userId: String?): DetailContentDto {
        val detailContentDto: DetailContentDto
        runBlocking {
            val response: SearchResponse = openSearchGetService.findDocumentById("content", id)
            val source = response.hits?.hits?.get(0)?.source
            val document = Gson().fromJson("$source", ContentDocument::class.java)

            detailContentDto = openSearchDetailService.getContentDocumentAsDetailContentDto(document, userId)
        }
        return detailContentDto
    }

    fun getOnePerson(id: String): DetailPersonDto {
        val detailPersonDto: DetailPersonDto
        runBlocking {
            val response: SearchResponse = openSearchGetService.findDocumentById("person", id)
            if (response.hits?.hits?.size == 0) throw RuntimeException("The person does not exist in UWHOO database.")
            val source = response.hits?.hits?.get(0)?.source
            val document = Gson().fromJson("$source", PersonDocument::class.java)

            detailPersonDto = openSearchDetailService.getPersonDocument(document)
        }
        return detailPersonDto
    }

    fun getContents(request: ContentRequestDTO): List<ContentDto> {
        // SearchResponse 가져오는 단계
        if (request.tab == "main" && request.sort == "popularity") {
            // 최근 1년간의 결과만 가져오기 위해 filter 추가
            if (request.filter.isNullOrEmpty()) request.filter = mutableListOf()
            request.filter!!.add(
                ContentRequestFilterDetail(
                    "date",
                    listOf(
                        Calendar.getInstance().addDate(Calendar.YEAR, -1),
                        Calendar.getInstance().addDate(null, null)
                    )
                )
            )
        } else if (request.tab == "open") {
            // 오늘 날짜 이후만 가져오도록 filter 추가
            if (request.filter.isNullOrEmpty()) request.filter = mutableListOf()
            request.filter!!.add(
                ContentRequestFilterDetail(
                    "date",
                    listOf(
                        Calendar.getInstance().addDate(null, null),
                        ""
                    )
                )
            )
        }


        val contentDtoList: MutableList<ContentDto> = mutableListOf()
        runBlocking {
            val response = openSearchGetService.search("content", request, ::defaultQuery)
            val documentList = MapperUtils.parseToContentDocument(response)
            // Response 가공 단계
            for (doc in documentList) {
                val detailContentDto =
                    openSearchDetailService.getContentDocumentAsDetailContentDto(doc, null)

                // detail Content dto -> Content dto
                // contentDtoList.add....
                contentDtoList.add(MapperUtils.mapDetailToContent(detailContentDto))
            }
        }
        return contentDtoList
    }

    fun getMatchContents(request: ContentRequestDTO): SearchContentDto {
        // SearchResponse 가져오는 단계
        val simpleContentDtoList: MutableList<SimpleContentDto> = mutableListOf()
        var total: Int
        runBlocking {
            val response = openSearchGetService.search("content", request, ::defaultQuery)
            val documentList = MapperUtils.parseToContentDocument(response)
            total = response.hits?.total?.value?.toInt() ?: 0
            // Response 가공 단계
            for (doc in documentList) {
                val detailContentDto =
                    openSearchDetailService.getContentDocumentAsDetailContentDto(doc, null)

                // detail Content dto -> SimpleContent dto
                simpleContentDtoList.add(MapperUtils.mapDetailToSimpleContent(detailContentDto))
            }
        }
        return SearchContentDto(
            total = total,
            content = simpleContentDtoList
        )
    }

    fun getSimilarContents(request: ContentRequestDTO): SearchContentDto {
        // SearchResponse 가져오는 단계
        val simpleContentDtoList: MutableList<SimpleContentDto> = mutableListOf()
        var total: Int
        runBlocking {
            val response = openSearchGetService.search("content", request)
            val documentList = MapperUtils.parseToContentDocument(response)
            total = response.hits?.total?.value?.toInt() ?: 0
            // Response 가공 단계
            for (doc in documentList) {
                val detailContentDto =
                    openSearchDetailService.getContentDocumentAsDetailContentDto(doc, null)

                // detail Content dto -> SimpleContent dto
                simpleContentDtoList.add(MapperUtils.mapDetailToSimpleContent(detailContentDto))
            }
        }
        return SearchContentDto(
            total = total,
            content = simpleContentDtoList
        )
    }

    fun defaultQuery(request: ContentRequestDTO): ESQuery {
        val filterList = getFilterFromRequest(request)
        return SearchDSL().bool {
            if (request.filter != null) filter(filterList)
            if (!request.query.isNullOrBlank()) {
                must(SearchDSL().match("name", request.query))
            }
            if (request.notQuery != null && request.notQuery!!.isNotEmpty()) {
                mustNot(TermsQuery("id", *request.notQuery!!.toTypedArray()))
            }
        }
    }

    private fun getFilterFromRequest(request: ContentRequestDTO): MutableList<ESQuery> {
        var filterList = mutableListOf<ESQuery>()
        if (!request.query.isNullOrBlank() || !request.filter.isNullOrEmpty()) {
            // filter 파싱
            if (request.filter != null) {
                filterList = parseFilter(request.filter!!)
            }
        }
        return filterList
    }

    private fun parseFilter(filter: List<ContentRequestFilterDetail>): MutableList<ESQuery> {
        val filterList = mutableListOf<ESQuery>()

        for (filterDetail in filter) {
            // 값 중 하나가 비면 스킵
            if (filterDetail.type.isBlank() || filterDetail.value.isEmpty()) continue

            // 타입 별 filter 생성
            when (filterDetail.type) {
                "genre", "platform", "age" -> filterList.add(
                    TermsQuery(
                        filterDetail.type, *filterDetail.value.filterNotNull().toTypedArray()
                    )
                )

                "date", "rating" -> {
                    var t = filterDetail.type
                    if (filterDetail.type == "date") t = "firstAirDate"
                    filterList.add(RangeQuery(t) {
                        if (!filterDetail.value[0].isNullOrBlank()) gte = filterDetail.value[0]!!
                        if (filterDetail.value.size > 2 && !filterDetail.value[1].isNullOrBlank()) lte =
                            filterDetail.value[1]!!
                    })
                }
            }
        }
        return filterList
    }

    fun Calendar.addDate(addFun: Int?, addParam: Int?): String {
        this.timeZone = TimeZone.getTimeZone("Asia/Seoul")
        val formatter = SimpleDateFormat("yyyy-MM-dd")
        if (addFun != null && addParam != null)
            this.add(addFun, addParam)
        return formatter.format(this.time).toString()
    }
}