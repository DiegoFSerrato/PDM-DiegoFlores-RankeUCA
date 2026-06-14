package com.pdmcourse2026.basictemplate

import android.app.Application
import com.pdmcourse2026.basictemplate.data.AppProvider
import com.pdmcourse2026.basictemplate.data.api.ApiServiceImpl
import com.pdmcourse2026.basictemplate.data.api.KtorClient
import com.pdmcourse2026.basictemplate.data.local.PreferencesManager
import com.pdmcourse2026.basictemplate.data.repository.VotingRepository
import com.pdmcourse2026.basictemplate.data.repository.VotingRepositoryImpl

class RankeUCAApplication : Application() {
  lateinit var repository: VotingRepository
    private set

  val appProvider by lazy { AppProvider(this) }

  override fun onCreate() {
    super.onCreate()
    val apiService = ApiServiceImpl(KtorClient.client)
    val preferencesManager = PreferencesManager(this)
    repository = VotingRepositoryImpl(apiService, preferencesManager)
  }
}
