package com.pdmcourse2026.basictemplate.screens.vote

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VoteScreen(
  viewModel: VoteViewModel,
  onNavigateToResults: () -> Unit,
  onNavigateToAdmin: () -> Unit,
  onSessionExpired: () -> Unit
) {
  val state = viewModel.state

  LaunchedEffect(state.isSessionExpired) {
    if (state.isSessionExpired) {
      onSessionExpired()
    }
  }

  Scaffold(
    topBar = {
      TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
          containerColor = MaterialTheme.colorScheme.primaryContainer,
          titleContentColor = MaterialTheme.colorScheme.primary,
        ),
        title = {
          Column {
            Text(
              text = "RankeUCA",
              style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
            )
            Text(
              text = "¿Dónde almorzamos hoy?",
              style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
            )
          }
        },
        actions = {
          IconButton(onClick = onNavigateToAdmin) {
            Icon(
              imageVector = Icons.Default.Settings,
              contentDescription = "Administrar opciones",
              tint = MaterialTheme.colorScheme.primary
            )
          }
          IconButton(onClick = { viewModel.loadOptions() }) {
            Icon(
              imageVector = Icons.Default.Refresh,
              contentDescription = "Recargar opciones",
              tint = MaterialTheme.colorScheme.primary
            )
          }
        }
      )
    },
    bottomBar = {
      Surface(
        tonalElevation = 8.dp,
        shadowElevation = 8.dp,
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
        ) {
          Button(
            onClick = onNavigateToResults,
            enabled = state.hasVoted,
            modifier = Modifier
              .fillMaxWidth()
              .height(50.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
              containerColor = MaterialTheme.colorScheme.primary,
              contentColor = MaterialTheme.colorScheme.onPrimary
            )
          ) {
            Text(
              text = "Ir a resultados →",
              style = MaterialTheme.typography.labelLarge.copy(
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
              )
            )
          }
        }
      }
    }
  ) { innerPadding ->
    Box(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)
    ) {
      if (state.options.isEmpty() && !state.isLoading) {
        // Estado vacío o sin opciones
        Column(
          modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
          verticalArrangement = Arrangement.Center,
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          Text(
            text = "No se encontraron opciones para votar.",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
          )
          Spacer(modifier = Modifier.height(16.dp))
          Button(onClick = { viewModel.loadOptions() }) {
            Text("Reintentar")
          }
        }
      } else {
        // Grid de opciones
        LazyVerticalGrid(
          columns = GridCells.Fixed(2),
          contentPadding = PaddingValues(12.dp),
          verticalArrangement = Arrangement.spacedBy(12.dp),
          horizontalArrangement = Arrangement.spacedBy(12.dp),
          modifier = Modifier.fillMaxSize()
        ) {
          items(state.options) { option ->
            val isSelected = state.selectedOptionId == option.id
            val isEnabled = !state.hasVoted && !state.isLoading

            OptionCard(
              optionName = option.name,
              imageUrl = option.imageUrl,
              isSelected = isSelected,
              isEnabled = isEnabled,
              onClick = { viewModel.vote(option.id) }
            )
          }
        }
      }

      // Loader overlay para votar o cargar inicialmente
      if (state.isLoading) {
        Surface(
          color = Color.Black.copy(alpha = 0.2f),
          modifier = Modifier.fillMaxSize()
        ) {
          Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
          ) {
            CircularProgressIndicator(
              color = MaterialTheme.colorScheme.primary,
              modifier = Modifier.size(48.dp)
            )
          }
        }
      }

      // Dialogo de Error
      if (state.error != null && !state.isSessionExpired) {
        AlertDialog(
          onDismissRequest = { viewModel.resetError() },
          title = { Text("Error") },
          text = { Text(state.error ?: "Ocurrió un error inesperado") },
          confirmButton = {
            TextButton(onClick = { viewModel.resetError() }) {
              Text("Aceptar")
            }
          }
        )
      }
    }
  }
}

@Composable
fun OptionCard(
  optionName: String,
  imageUrl: String,
  isSelected: Boolean,
  isEnabled: Boolean,
  onClick: () -> Unit
) {
  val borderColor = if (isSelected) {
    MaterialTheme.colorScheme.primary
  } else {
    Color.Transparent
  }

  val borderStroke = if (isSelected) {
    BorderStroke(3.dp, borderColor)
  } else {
    null
  }

  Card(
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(
      containerColor = if (isSelected) {
        MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
      } else {
        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f)
      }
    ),
    border = borderStroke,
    modifier = Modifier
      .fillMaxWidth()
      .clickable(enabled = isEnabled, onClick = onClick)
  ) {
    Column {
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .aspectRatio(1.3f)
          .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
      ) {
        AsyncImage(
          model = imageUrl,
          contentDescription = optionName,
          contentScale = ContentScale.Crop,
          modifier = Modifier.fillMaxSize()
        )

        if (isSelected) {
          Surface(
            color = Color.Black.copy(alpha = 0.4f),
            modifier = Modifier.fillMaxSize()
          ) {
            Box(
              contentAlignment = Alignment.Center,
              modifier = Modifier.fillMaxSize()
            ) {
              Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = "Votado",
                tint = Color.Green,
                modifier = Modifier.size(48.dp)
              )
            }
          }
        }
      }

      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(12.dp)
      ) {
        Text(
          text = optionName,
          style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
          maxLines = 1,
          overflow = TextOverflow.Ellipsis,
          color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
          text = if (isSelected) "Tu voto registrado" else "Toca para votar",
          style = MaterialTheme.typography.bodySmall.copy(
            fontSize = 11.sp,
            color = if (isSelected) {
              MaterialTheme.colorScheme.primary
            } else {
              MaterialTheme.colorScheme.onSurfaceVariant
            }
          )
        )
      }
    }
  }
}
