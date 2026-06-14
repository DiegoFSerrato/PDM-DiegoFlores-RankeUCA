package com.pdmcourse2026.basictemplate.data.repository

import com.pdmcourse2026.basictemplate.data.database.dao.OptionDao
import com.pdmcourse2026.basictemplate.data.database.entities.toEntity
import com.pdmcourse2026.basictemplate.data.database.entities.toModel
import com.pdmcourse2026.basictemplate.domain.model.Option
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class OptionRepositoryImpl(
  private val optionDao: OptionDao
) : OptionRepository {

  override fun getOptions(): Flow<List<Option>> {
    return optionDao.getAllOptions().map { entities ->
      entities.map { it.toModel() }
    }
  }

  override suspend fun addOption(option: Option) {
    optionDao.insertOption(option.toEntity())
  }

  override suspend fun deleteOption(option: Option) {
    optionDao.deleteOption(option.toEntity())
  }
}
