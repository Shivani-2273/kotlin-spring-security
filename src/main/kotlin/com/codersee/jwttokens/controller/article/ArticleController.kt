package com.codersee.jwttokens.controller.article

import com.codersee.jwttokens.model.Article
import com.codersee.jwttokens.service.ArticleService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/articles")
class ArticleController(private val articleService: ArticleService) {

    @GetMapping
    fun getAllArticles(): List<ArticleResponse> =
        articleService.findall()
            .map { it.toResponse() }

        private fun Article.toResponse(): ArticleResponse =
            ArticleResponse(
                id = this.id,
                title = this.title,
                content = this.content
            )


}
