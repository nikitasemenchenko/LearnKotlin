package com.example.learnkotlin.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Article(
    val id: Int,
    val title: String,
    @SerialName("blocks")
    val content: List<Block>
)

@Serializable
data class Chapter(
    val chapterId: Int,
    @SerialName("chapterTitle")
    val title: String,
    @SerialName("articles")
    val articles: List<Article>
)

@Serializable
sealed class Block {
    @Serializable
    @SerialName("text")
    data class Text(
        @SerialName("content")
        val text: String
    ) : Block()

    @Serializable
    @SerialName("code")
    data class Code(
        @SerialName("content")
        val code: String
    ) : Block()

    @Serializable
    @SerialName("image")
    data class Image(
        @SerialName("content")
        val img: String
    ) : Block()

    @Serializable
    @SerialName("subtitle")
    data class Subtitle(
        @SerialName("content")
        val text: String
    ) : Block()

    @Serializable
    @SerialName("url")
    data class Url(
        @SerialName("content")
        val url: String
    ): Block()
}
