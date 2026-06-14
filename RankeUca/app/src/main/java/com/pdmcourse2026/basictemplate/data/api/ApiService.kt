package com.pdmcourse2026.basictemplate.data.api

import com.pdmcourse2026.basictemplate.data.api.model.OptionDto
import com.pdmcourse2026.basictemplate.data.api.model.RegisterResponse
import com.pdmcourse2026.basictemplate.data.api.model.VoteResponse

interface ApiService {
  suspend fun register(carnet: String): RegisterResponse
  suspend fun getOptions(apiKey: String): List<OptionDto>
  suspend fun vote(apiKey: String, optionId: Int): VoteResponse
  suspend fun reset(): Boolean
}
