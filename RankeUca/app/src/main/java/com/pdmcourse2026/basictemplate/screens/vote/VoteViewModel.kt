package com.pdmcourse2026.basictemplate.screens.vote

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pdmcourse2026.basictemplate.data.repository.VotingRepository
import com.pdmcourse2026.basictemplate.domain.model.Option
import kotlinx.coroutines.launch

data class VoteUiState(
  val options: List<Option> = emptyList(),
  val isLoading: Boolean = false,
  val error: String? = null,
  val selectedOptionId: Int? = null,
  val hasVoted: Boolean = false,
  val isSessionExpired: Boolean = false
)

class VoteViewModel(private val repository: VotingRepository) : ViewModel() {
  var state by mutableStateOf(VoteUiState())
    private set

  init {
    if (repository.getApiKey() != null) {
      loadOptions()
    }
  }

  fun loadOptions() {
    state = state.copy(isLoading = true, error = null, isSessionExpired = false)
    viewModelScope.launch {
      repository.getOptions()
        .onSuccess { list ->
          state = state.copy(options = list, isLoading = false)
        }
        .onFailure { exception ->
          val msg = exception.localizedMessage ?: "Error al cargar opciones"
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

  fun vote(optionId: Int) {
    if (state.hasVoted) return // Voto unico por sesion de pantalla de votacion
    state = state.copy(isLoading = true, error = null, isSessionExpired = false)
    viewModelScope.launch {
      repository.vote(optionId)
        .onSuccess {
          state = state.copy(
            isLoading = false,
            selectedOptionId = optionId,
            hasVoted = true
          )
        }
        .onFailure { exception ->
          val msg = exception.localizedMessage ?: "Error al registrar el voto"
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

  fun resetVoteStatus() {
    state = state.copy(
      selectedOptionId = null,
      hasVoted = false,
      isSessionExpired = false,
      error = null
    )
  }

  fun resetError() {
    state = state.copy(error = null)
  }
}
