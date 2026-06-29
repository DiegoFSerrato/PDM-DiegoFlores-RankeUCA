package com.pdmcourse2026.basictemplate

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.pdmcourse2026.basictemplate.screens.ViewModelFactory
import com.pdmcourse2026.basictemplate.screens.register.RegisterScreen
import com.pdmcourse2026.basictemplate.screens.register.RegisterViewModel
import com.pdmcourse2026.basictemplate.screens.results.ResultsScreen
import com.pdmcourse2026.basictemplate.screens.results.ResultsViewModel
import com.pdmcourse2026.basictemplate.screens.options.OptionsScreen
import com.pdmcourse2026.basictemplate.screens.questions.QuestionsScreen
import com.pdmcourse2026.basictemplate.screens.vote.VoteScreen
import com.pdmcourse2026.basictemplate.screens.vote.VoteViewModel

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.pdmcourse2026.basictemplate.screens.auth.AuthViewModel
import com.pdmcourse2026.basictemplate.screens.auth.LoginScreen

@Composable
fun RankeUCA_App() {
  val authViewModel: AuthViewModel = viewModel(factory = AuthViewModel.Factory)
  val isLoggedIn by authViewModel.isLoggedIn.collectAsState()
  val userName by authViewModel.userName.collectAsState()

  when (isLoggedIn) {
    null -> {
      Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
      ) {
        CircularProgressIndicator()
      }
    }
    false -> {
      LoginScreen(viewModel = authViewModel)
    }
    true -> {
      MainNavigation(
        userName = userName,
        onLogout = { authViewModel.logout() }
      )
    }
  }
}

@Composable
fun MainNavigation(
  userName: String?,
  onLogout: () -> Unit
) {
  val context = LocalContext.current
  val app = context.applicationContext as RankeUCAApplication
  val repository = app.repository

  val backStack = rememberNavBackStack(Routes.Vote)

  val factory = remember { ViewModelFactory(repository) }
  val voteViewModel: VoteViewModel = viewModel(factory = factory)
  val resultsViewModel: ResultsViewModel = viewModel(factory = factory)

  NavDisplay(
    backStack = backStack,
    onBack = { backStack.removeLastOrNull() },
    entryProvider = entryProvider {
      entry<Routes.Vote> {
        VoteScreen(
          viewModel = voteViewModel,
          userName = userName,
          onLogout = onLogout,
          onNavigateToResults = {
            resultsViewModel.loadResults()
            backStack.add(Routes.Results)
          },
          onNavigateToAdmin = {
            backStack.add(Routes.Questions)
          },
          onSessionExpired = {
            onLogout()
          }
        )
      }
      entry<Routes.Results> {
        ResultsScreen(
          viewModel = resultsViewModel,
          onNavigateBackToVote = {
            voteViewModel.resetVoteStatus()
            backStack.removeLastOrNull()
          },
          onSessionExpired = {
            onLogout()
          }
        )
      }
      entry<Routes.Questions> {
        QuestionsScreen(
          onNavigateBack = {
            backStack.removeLastOrNull()
          },
          onQuestionClick = { questionId ->
            backStack.add(Routes.Options(questionId = questionId))
          }
        )
      }
      entry<Routes.Options> { route ->
        OptionsScreen(
          questionId = route.questionId,
          onNavigateBack = {
            backStack.removeLastOrNull()
          }
        )
      }
    },
  )
}