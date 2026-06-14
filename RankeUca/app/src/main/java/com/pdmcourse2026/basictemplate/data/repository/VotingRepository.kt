package com.pdmcourse2026.basictemplate.data.repository

import com.pdmcourse2026.basictemplate.domain.model.Option

interface VotingRepository {
  suspend fun register(carnet: String): Result<String>
  suspend fun getOptions(): Result<List<Option>>
  suspend fun vote(optionId: Int): Result<Unit>
  fun getApiKey(): String?
  fun getCarnet(): String?
  fun clearSession()
}
