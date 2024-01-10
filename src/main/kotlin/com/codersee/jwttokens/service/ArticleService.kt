package com.codersee.jwttokens.service

import com.codersee.jwttokens.model.Article
import com.codersee.jwttokens.repository.ArticleRepository
import org.springframework.stereotype.Service

@Service
class ArticleService(private val articleRepository: ArticleRepository) {

    fun findall(): List<Article> {
    return articleRepository.findAllArticle()
    }
}
