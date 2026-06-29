package com.pdmcourse2026.basictemplate.data.repository

import com.pdmcourse2026.basictemplate.data.database.dao.QuestionDao
import com.pdmcourse2026.basictemplate.data.database.entities.QuestionEntity
import com.pdmcourse2026.basictemplate.data.database.entities.toModel
import com.pdmcourse2026.basictemplate.domain.model.Question
import com.pdmcourse2026.basictemplate.domain.model.toEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class QuestionRepositoryImpl(
  private val questionDao: QuestionDao
) : QuestionRepository {

  override fun getQuestions(): Flow<List<Question>> {
    return questionDao.getQuestionsWithOptions().map { list ->
      list.map { it.toModel() }
    }
  }

  override suspend fun addQuestion(title: String) {
    questionDao.insertQuestion(QuestionEntity(title = title))
  }

  override suspend fun deleteQuestion(question: Question) {
    questionDao.deleteQuestion(question.toEntity())
  }
}
