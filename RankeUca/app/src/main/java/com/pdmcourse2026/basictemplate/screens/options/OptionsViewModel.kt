package com.pdmcourse2026.basictemplate.screens.options

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.pdmcourse2026.basictemplate.RankeUCAApplication
import com.pdmcourse2026.basictemplate.data.repository.OptionRepository
import com.pdmcourse2026.basictemplate.domain.model.Option
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class OptionsViewModel(
  private val optionRepository: OptionRepository
) : ViewModel() {

  val options: StateFlow<List<Option>> =
    optionRepository.getOptions()
      .stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = emptyList()
      )

  fun addOption(name: String, imageUrl: String) {
    viewModelScope.launch {
      optionRepository.addOption(Option(id = 0, name = name, imageUrl = imageUrl, votes = 0))
    }
  }

  fun deleteOption(option: Option) {
    viewModelScope.launch {
      optionRepository.deleteOption(option)
    }
  }

  companion object {
    val Factory = viewModelFactory {
      initializer {
        val app = this[APPLICATION_KEY] as RankeUCAApplication
        OptionsViewModel(app.appProvider.provideOptionRepository())
      }
    }
  }
}
