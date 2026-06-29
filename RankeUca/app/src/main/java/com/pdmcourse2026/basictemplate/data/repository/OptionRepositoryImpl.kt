package com.pdmcourse2026.basictemplate.data.repository

import com.pdmcourse2026.basictemplate.data.database.dao.OptionDao
import com.pdmcourse2026.basictemplate.data.database.entities.OptionEntity
import com.pdmcourse2026.basictemplate.data.database.entities.toEntity
import com.pdmcourse2026.basictemplate.data.database.entities.toModel
import com.pdmcourse2026.basictemplate.domain.model.Option
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class OptionRepositoryImpl(
  private val optionDao: OptionDao
) : OptionRepository {

  override fun getOptions(questionId: Int): Flow<List<Option>> {
    return optionDao.getOptionsForQuestion(questionId).map { entities ->
      entities.map { it.toModel() }
    }
  }

  override suspend fun addOption(name: String, imageUrl: String, questionId: Int) {
    optionDao.insertOption(
      OptionEntity(name = name, imageUrl = imageUrl, questionId = questionId)
    )
  }

  override suspend fun deleteOption(option: Option) {
    optionDao.deleteOption(option.toEntity())
  }
}
