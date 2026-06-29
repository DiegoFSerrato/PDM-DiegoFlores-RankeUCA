package com.pdmcourse2026.basictemplate

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

sealed class Routes : NavKey {
  @Serializable
  data object Vote : Routes()

  @Serializable
  data object Results : Routes()

  @Serializable
  data object Questions : Routes()

  @Serializable
  data class Options(val questionId: Int) : Routes()
}
