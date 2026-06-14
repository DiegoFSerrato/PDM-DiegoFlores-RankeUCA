package com.pdmcourse2026.basictemplate.data.api.model

import kotlinx.serialization.Serializable

@Serializable
data class ErrorResponse(
  val ok: Boolean,
  val message: String
)
