package com.pdmcourse2026.basictemplate.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.pdmcourse2026.basictemplate.data.repository.VotingRepository
import com.pdmcourse2026.basictemplate.screens.register.RegisterViewModel
import com.pdmcourse2026.basictemplate.screens.vote.VoteViewModel
import com.pdmcourse2026.basictemplate.screens.results.ResultsViewModel

class ViewModelFactory(private val repository: VotingRepository) : ViewModelProvider.Factory {
  @Suppress("UNCHECKED_CAST")
  override fun <T : ViewModel> create(modelClass: Class<T>): T {
    return when {
      modelClass.isAssignableFrom(RegisterViewModel::class.java) -> {
        RegisterViewModel(repository) as T
      }
      modelClass.isAssignableFrom(VoteViewModel::class.java) -> {
        VoteViewModel(repository) as T
      }
      modelClass.isAssignableFrom(ResultsViewModel::class.java) -> {
        ResultsViewModel(repository) as T
      }
      else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
  }
}
