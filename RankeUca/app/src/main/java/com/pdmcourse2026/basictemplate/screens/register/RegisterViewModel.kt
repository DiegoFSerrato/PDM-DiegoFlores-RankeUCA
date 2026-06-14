package com.pdmcourse2026.basictemplate.screens.register

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pdmcourse2026.basictemplate.data.repository.VotingRepository
import kotlinx.coroutines.launch

sealed interface RegisterUiState {
  data object Idle : RegisterUiState
  data object Loading : RegisterUiState
  data object Success : RegisterUiState
  data class Error(val message: String) : RegisterUiState
}

class RegisterViewModel(private val repository: VotingRepository) : ViewModel() {
  var uiState: RegisterUiState by mutableStateOf(RegisterUiState.Idle)
    private set

  fun register(carnet: String) {
    if (carnet.trim().isEmpty()) {
      uiState = RegisterUiState.Error("El número de carnet es obligatorio")
      return
    }
    uiState = RegisterUiState.Loading
    viewModelScope.launch {
      repository.register(carnet.trim())
        .onSuccess {
          uiState = RegisterUiState.Success
        }
        .onFailure { exception ->
          uiState = RegisterUiState.Error(exception.localizedMessage ?: "Error de red o carnet inválido")
        }
    }
  }

  fun resetError() {
    uiState = RegisterUiState.Idle
  }
}
