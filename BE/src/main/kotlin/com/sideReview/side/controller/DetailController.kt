package com.sideReview.side.controller

import com.sideReview.side.openSearch.OpenSearchDetailService
import kotlinx.coroutines.runBlocking
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class DetailController @Autowired constructor(private val openSearchDetailService: OpenSearchDetailService){
    @GetMapping("/contents/{id}")
    fun getContentDetail(@PathVariable id : String) : ResponseEntity<Any> {
        var response: ResponseEntity<Any>
        runBlocking{
            response = ResponseEntity.ok(openSearchDetailService.getContentDocument(openSearchDetailService.findDocumentById("content", id)))
        }
        return response
    }
}