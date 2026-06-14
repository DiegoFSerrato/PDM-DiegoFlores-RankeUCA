package com.pdmcourse2026.basictemplate.data.api

import com.pdmcourse2026.basictemplate.data.api.model.ErrorResponse
import com.pdmcourse2026.basictemplate.data.api.model.OptionDto
import com.pdmcourse2026.basictemplate.data.api.model.RegisterRequest
import com.pdmcourse2026.basictemplate.data.api.model.RegisterResponse
import com.pdmcourse2026.basictemplate.data.api.model.VoteRequest
import com.pdmcourse2026.basictemplate.data.api.model.VoteResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import io.ktor.http.isSuccess

class ApiServiceImpl(private val client: HttpClient) : ApiService {

  override suspend fun register(carnet: String): RegisterResponse {
    val response = client.post("register") {
      contentType(ContentType.Application.Json)
      setBody(RegisterRequest(carnet))
    }
    return if (response.status.isSuccess()) {
      response.body<RegisterResponse>()
    } else {
      val error = response.body<ErrorResponse>()
      throw Exception(error.message)
    }
  }

  override suspend fun getOptions(apiKey: String): List<OptionDto> {
    val response = client.get("options") {
      header(HttpHeaders.Authorization, "Bearer $apiKey")
    }
    return if (response.status.isSuccess()) {
      response.body<List<OptionDto>>()
    } else {
      val error = response.body<ErrorResponse>()
      throw Exception(error.message)
    }
  }

  override suspend fun vote(apiKey: String, optionId: Int): VoteResponse {
    val response = client.post("vote") {
      header(HttpHeaders.Authorization, "Bearer $apiKey")
      contentType(ContentType.Application.Json)
      setBody(VoteRequest(optionId))
    }
    return if (response.status.isSuccess()) {
      response.body<VoteResponse>()
    } else {
      val error = response.body<ErrorResponse>()
      throw Exception(error.message)
    }
  }

  override suspend fun reset(): Boolean {
    val response = client.post("reset")
    return response.status.isSuccess()
  }
}
