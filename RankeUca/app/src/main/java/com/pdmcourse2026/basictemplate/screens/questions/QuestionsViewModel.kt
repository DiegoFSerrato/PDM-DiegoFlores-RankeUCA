package com.pdmcourse2026.basictemplate.screens.questions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.pdmcourse2026.basictemplate.RankeUCAApplication
import com.pdmcourse2026.basictemplate.data.repository.QuestionRepository
import com.pdmcourse2026.basictemplate.domain.model.Question
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class QuestionsViewModel(
  private val questionRepository: QuestionRepository
) : ViewModel() {

  val questions: StateFlow<List<Question>> =
    questionRepository.getQuestions()
      .stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = emptyList()
      )

  fun addQuestion(title: String) {
    viewModelScope.launch {
      questionRepository.addQuestion(title)
    }
  }

  fun deleteQuestion(question: Question) {
    viewModelScope.launch {
      questionRepository.deleteQuestion(question)
    }
  }

  companion object {
    val Factory = viewModelFactory {
      initializer {
        val app = this[APPLICATION_KEY] as RankeUCAApplication
        QuestionsViewModel(app.appProvider.provideQuestionRepository())
      }
    }
  }
}
