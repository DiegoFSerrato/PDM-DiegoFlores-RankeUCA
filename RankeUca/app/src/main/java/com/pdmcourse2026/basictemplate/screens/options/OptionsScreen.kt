package com.pdmcourse2026.basictemplate.screens.options

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Inbox
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OptionsScreen(
  onNavigateBack: () -> Unit,
  viewModel: OptionsViewModel = viewModel(factory = OptionsViewModel.Factory)
) {
  val options by viewModel.options.collectAsStateWithLifecycle()
  var showSheet by rememberSaveable { mutableStateOf(false) }

  Scaffold(
    containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
    topBar = {
      TopAppBar(
        title = { Text("Administrar opciones") },
        navigationIcon = {
          IconButton(onClick = onNavigateBack) {
            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Volver")
          }
        },
        actions = {
          TextButton(onClick = { showSheet = true }) {
            Icon(imageVector = Icons.Default.Add, contentDescription = "Nueva opción")
            Spacer(modifier = Modifier.width(4.dp))
            Text("Nuevo")
          }
        },
        colors = TopAppBarDefaults.topAppBarColors(
          containerColor = MaterialTheme.colorScheme.surface
        )
      )
    }
  ) { innerPadding ->
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)
        .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
      if (options.isEmpty()) {
        Column(
          modifier = Modifier.fillMaxSize(),
          verticalArrangement = Arrangement.Center,
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          Icon(
            imageVector = Icons.Default.Inbox,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.outline,
            modifier = Modifier.height(36.dp)
          )
          Spacer(modifier = Modifier.height(12.dp))
          Text(
            text = "Todavía no hay opciones",
            style = MaterialTheme.typography.titleMedium
          )
          Text(
            text = "Toca Nuevo para crear la primera.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }
      } else {
        LazyColumn(
          modifier = Modifier.fillMaxSize(),
          contentPadding = PaddingValues(vertical = 4.dp),
          verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          items(items = options, key = { it.id }) { option ->
            ElevatedCard {
              ListItem(
                headlineContent = {
                  Text(
                    text = option.name,
                    style = MaterialTheme.typography.titleMedium
                  )
                },
                supportingContent = {
                  Text(
                    text = option.imageUrl,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                  )
                },
                trailingContent = {
                  IconButton(onClick = { viewModel.deleteOption(option) }) {
                    Icon(
                      imageVector = Icons.Default.DeleteOutline,
                      contentDescription = "Borrar ${option.name}",
                      tint = MaterialTheme.colorScheme.error
                    )
                  }
                }
              )
            }
          }
        }
      }
    }
  }

  if (showSheet) {
    OptionBottomSheet(
      onSave = { name, imageUrl ->
        viewModel.addOption(name, imageUrl)
      },
      onDismiss = { showSheet = false }
    )
  }
}
