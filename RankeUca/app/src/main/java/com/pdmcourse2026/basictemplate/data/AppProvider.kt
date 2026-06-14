package com.pdmcourse2026.basictemplate.data

import android.content.Context
import com.pdmcourse2026.basictemplate.data.database.AppDatabase
import com.pdmcourse2026.basictemplate.data.repository.OptionRepository
import com.pdmcourse2026.basictemplate.data.repository.OptionRepositoryImpl

class AppProvider(context: Context) {

  private val appDatabase = AppDatabase.getDatabase(context)
  private val optionDao = appDatabase.optionDao()

  private val optionRepository: OptionRepository =
    OptionRepositoryImpl(optionDao)

  fun provideOptionRepository(): OptionRepository {
    return optionRepository
  }
}
