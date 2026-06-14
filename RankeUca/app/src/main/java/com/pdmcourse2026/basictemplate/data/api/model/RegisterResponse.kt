package com.pdmcourse2026.basictemplate.data.api.model

import kotlinx.serialization.Serializable

@Serializable
data class RegisterResponse(
  val ok: Boolean,
  val apiKey: String? = null
)
