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
import com.pdmcourse2026.basictemplate.screens.vote.VoteScreen
import com.pdmcourse2026.basictemplate.screens.vote.VoteViewModel

@Composable
fun RankeUCA_App() {
  val context = LocalContext.current
  val app = context.applicationContext as RankeUCAApplication
  val repository = app.repository

  // Determinar la ruta inicial si existe la apiKey guardada
  val startDestination = remember {
    if (repository.getApiKey() != null) Routes.Vote else Routes.Register
  }

  val backStack = rememberNavBackStack(startDestination)

  val factory = remember { ViewModelFactory(repository) }
  val registerViewModel: RegisterViewModel = viewModel(factory = factory)
  val voteViewModel: VoteViewModel = viewModel(factory = factory)
  val resultsViewModel: ResultsViewModel = viewModel(factory = factory)

  NavDisplay(
    backStack = backStack,
    onBack = { backStack.removeLastOrNull() },
    entryProvider = entryProvider {
      entry<Routes.Register> {
        RegisterScreen(
          viewModel = registerViewModel,
          onRegisterSuccess = {
            voteViewModel.loadOptions()
            voteViewModel.resetVoteStatus()
            backStack.clear()
            backStack.add(Routes.Vote)
          }
        )
      }
      entry<Routes.Vote> {
        VoteScreen(
          viewModel = voteViewModel,
          onNavigateToResults = {
            resultsViewModel.loadResults()
            backStack.add(Routes.Results)
          },
          onNavigateToAdmin = {
            backStack.add(Routes.Options)
          },
          onSessionExpired = {
            backStack.clear()
            backStack.add(Routes.Register)
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
            backStack.clear()
            backStack.add(Routes.Register)
          }
        )
      }
      entry<Routes.Options> {
        OptionsScreen(
          onNavigateBack = {
            backStack.removeLastOrNull()
          }
        )
      }
    },
  )
}