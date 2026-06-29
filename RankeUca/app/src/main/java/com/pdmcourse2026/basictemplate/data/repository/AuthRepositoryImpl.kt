package com.pdmcourse2026.basictemplate.data.repository

import com.pdmcourse2026.basictemplate.data.api.KtorClient
import com.pdmcourse2026.basictemplate.data.api.model.LoginRequestDto
import com.pdmcourse2026.basictemplate.data.api.model.LoginResponseDto
import com.pdmcourse2026.basictemplate.data.session.SessionManager
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AuthRepositoryImpl(
  private val session: SessionManager
) : AuthRepository {

  override val isLoggedIn: Flow<Boolean> = session.token.map { it != null }
  override val userName: Flow<String?> = session.userName

  override suspend fun login(email: String, password: String) {
    val response: LoginResponseDto = KtorClient.client.post("auth/login") {
      contentType(ContentType.Application.Json)
      setBody(LoginRequestDto(email, password))
    }.body()

    KtorClient.authToken = response.token
    session.save(response.token, response.user.name)
  }

  override suspend fun logout() {
    KtorClient.authToken = null
    session.clear()
  }
}
