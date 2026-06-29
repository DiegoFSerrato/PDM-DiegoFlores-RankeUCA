package com.pdmcourse2026.basictemplate.data.repository

import com.pdmcourse2026.basictemplate.domain.model.Question
import kotlinx.coroutines.flow.Flow

interface QuestionRepository {
  fun getQuestions(): Flow<List<Question>>
  suspend fun addQuestion(title: String)
  suspend fun deleteQuestion(question: Question)
}
