package com.pdmcourse2026.basictemplate.screens.results

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pdmcourse2026.basictemplate.data.repository.VotingRepository
import com.pdmcourse2026.basictemplate.domain.model.Option
import kotlinx.coroutines.launch

data class ResultsUiState(
  val options: List<Option> = emptyList(),
  val isLoading: Boolean = false,
  val isRefreshing: Boolean = false,
  val error: String? = null,
  val isSessionExpired: Boolean = false
)

class ResultsViewModel(private val repository: VotingRepository) : ViewModel() {
  var state by mutableStateOf(ResultsUiState())
    private set

  init {
    if (repository.getApiKey() != null) {
      loadResults()
    }
  }

  fun loadResults() {
    state = state.copy(isLoading = true, error = null, isSessionExpired = false)
    viewModelScope.launch {
      repository.getOptions()
        .onSuccess { list ->
          // Ordenados de mayor a menor cantidad de votos
          val sortedList = list.sortedByDescending { it.votes }
          state = state.copy(options = sortedList, isLoading = false)
        }
        .onFailure { exception ->
          val msg = exception.localizedMessage ?: "Error al cargar resultados"
          val isExpired = msg.contains("401") || msg.contains("API key") || msg.contains("unauthenticated")
          if (isExpired) {
            repository.clearSession()
          }
          state = state.copy(
            isLoading = false,
            error = msg,
            isSessionExpired = isExpired
          )
        }
    }
  }

  fun refreshResults() {
    state = state.copy(isRefreshing = true, error = null, isSessionExpired = false)
    viewModelScope.launch {
      repository.getOptions()
        .onSuccess { list ->
          val sortedList = list.sortedByDescending { it.votes }
          state = state.copy(options = sortedList, isRefreshing = false)
        }
        .onFailure { exception ->
          val msg = exception.localizedMessage ?: "Error al actualizar resultados"
          val isExpired = msg.contains("401") || msg.contains("API key") || msg.contains("unauthenticated")
          if (isExpired) {
            repository.clearSession()
          }
          state = state.copy(
            isRefreshing = false,
            error = msg,
            isSessionExpired = isExpired
          )
        }
    }
  }

  fun resetError() {
    state = state.copy(error = null)
  }
}
