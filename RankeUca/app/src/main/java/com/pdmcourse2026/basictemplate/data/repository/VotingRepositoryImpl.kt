package com.pdmcourse2026.basictemplate.data.repository

import com.pdmcourse2026.basictemplate.data.api.ApiService
import com.pdmcourse2026.basictemplate.data.local.PreferencesManager
import com.pdmcourse2026.basictemplate.domain.model.Option

import com.pdmcourse2026.basictemplate.data.api.KtorClient

class VotingRepositoryImpl(
  private val apiService: ApiService,
  private val preferencesManager: PreferencesManager
) : VotingRepository {

  override suspend fun register(carnet: String): Result<String> {
    return runCatching {
      val response = apiService.register(carnet)
      val apiKey = response.apiKey ?: throw Exception("No se recibió la API key")
      preferencesManager.saveCarnet(carnet)
      preferencesManager.saveApiKey(apiKey)
      apiKey
    }
  }

  override suspend fun getOptions(): Result<List<Option>> {
    val token = KtorClient.authToken
      ?: return Result.failure(Exception("Usuario no autenticado (falta token)"))
    return runCatching {
      val dtos = apiService.getOptions(token)
      dtos.map { dto ->
        Option(
          id = dto.id,
          name = dto.name,
          imageUrl = dto.imageUrl ?: "",
          votes = dto.votes
        )
      }
    }
  }

  override suspend fun vote(optionId: Int): Result<Unit> {
    val token = KtorClient.authToken
      ?: return Result.failure(Exception("Usuario no autenticado (falta token)"))
    return runCatching {
      val response = apiService.vote(token, optionId)
      if (!response.ok) {
        throw Exception(response.message ?: "Error al registrar el voto")
      }
    }
  }

  override fun getApiKey(): String? = KtorClient.authToken

  override fun getCarnet(): String? = "00029823"

  override fun clearSession() {
    preferencesManager.clear()
  }
}
