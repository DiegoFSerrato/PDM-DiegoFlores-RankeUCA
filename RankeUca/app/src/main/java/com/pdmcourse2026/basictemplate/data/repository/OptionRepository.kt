package com.pdmcourse2026.basictemplate.data.repository

import com.pdmcourse2026.basictemplate.domain.model.Option
import kotlinx.coroutines.flow.Flow

interface OptionRepository {
  fun getOptions(questionId: Int): Flow<List<Option>>
  suspend fun addOption(name: String, imageUrl: String, questionId: Int)
  suspend fun deleteOption(option: Option)
}
