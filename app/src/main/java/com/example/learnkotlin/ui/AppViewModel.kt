package com.example.learnkotlin.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.learnkotlin.model.Article
import com.example.learnkotlin.model.Chapter
import com.example.learnkotlin.model.learnKotlinUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.serialization.json.Json

class AppViewModel: ViewModel() {
private val _uiState = MutableStateFlow(learnKotlinUiState())
val uiState: StateFlow<learnKotlinUiState> = _uiState.asStateFlow()

    fun loadChapters(context: Context): List<Chapter> {
        return try {
            val jsonString = context.assets.open("articles.json")
                .bufferedReader()
                .use { it.readText() }

            val jsonParser = Json {
                ignoreUnknownKeys = true
                classDiscriminator = "type"
            }

            val chapters = jsonParser.decodeFromString<List<Chapter>>(jsonString)
            chapters
        } catch (e: Exception) {
            emptyList()
        }
    }
    fun getArticle(chapters: List<Chapter>, chapterId: Int, articleId: Int): Article {
        return chapters.first{it.chapterId == chapterId}.articles.first{it.id == articleId}
    }

    fun switchTheme(){
        val current = _uiState.value.isDarkTheme
        _uiState.update {currentState ->
            currentState.copy(
                isDarkTheme = !current
            )
        }
    }
}