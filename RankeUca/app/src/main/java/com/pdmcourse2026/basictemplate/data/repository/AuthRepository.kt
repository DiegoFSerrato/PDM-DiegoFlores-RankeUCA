package com.pdmcourse2026.basictemplate.data.repository

import kotlinx.coroutines.flow.Flow

interface AuthRepository {
  val isLoggedIn: Flow<Boolean>
  val userName: Flow<String?>
  suspend fun login(email: String, password: String)
  suspend fun logout()
}
