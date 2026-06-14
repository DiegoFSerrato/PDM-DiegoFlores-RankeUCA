package com.pdmcourse2026.basictemplate.data.api.model

import kotlinx.serialization.Serializable

@Serializable
data class RegisterRequest(
  val carnet: String
)
