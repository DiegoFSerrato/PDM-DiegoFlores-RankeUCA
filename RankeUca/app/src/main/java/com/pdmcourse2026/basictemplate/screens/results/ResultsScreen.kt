package com.pdmcourse2026.basictemplate.screens.results

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
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
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
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
fun ResultsScreen(
  viewModel: ResultsViewModel,
  onNavigateBackToVote: () -> Unit,
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
          Text(
            text = "Resultados Actuales",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
          )
        },
        navigationIcon = {
          IconButton(onClick = onNavigateBackToVote) {
            Icon(
              imageVector = Icons.Default.ArrowBack,
              contentDescription = "Volver a votar"
            )
          }
        },
        actions = {
          IconButton(onClick = { viewModel.refreshResults() }) {
            Icon(
              imageVector = Icons.Default.Refresh,
              contentDescription = "Refrescar"
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
            onClick = onNavigateBackToVote,
            modifier = Modifier
              .fillMaxWidth()
              .height(50.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
              containerColor = MaterialTheme.colorScheme.secondary,
              contentColor = MaterialTheme.colorScheme.onSecondary
            )
          ) {
            Text(
              text = "Nuevo (volver a votar)",
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
        Column(
          modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
          verticalArrangement = Arrangement.Center,
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          Text(
            text = "No hay resultados disponibles.",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
          )
          Spacer(modifier = Modifier.height(16.dp))
          Button(onClick = { viewModel.loadResults() }) {
            Text("Reintentar")
          }
        }
      } else {
        PullToRefreshBox(
          isRefreshing = state.isRefreshing,
          onRefresh = { viewModel.refreshResults() },
          modifier = Modifier.fillMaxSize()
        ) {
          LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
          ) {
            itemsIndexed(state.options) { index, option ->
              ResultRow(
                rank = index + 1,
                optionName = option.name,
                imageUrl = option.imageUrl,
                votes = option.votes
              )
            }
          }
        }
      }

      // Carga inicial
      if (state.isLoading && !state.isRefreshing) {
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

      // Dialogo de Error
      if (state.error != null && !state.isSessionExpired) {
        AlertDialog(
          onDismissRequest = { viewModel.resetError() },
          title = { Text("Error") },
          text = { Text(state.error ?: "Ocurrió un error al cargar los votos") },
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
fun ResultRow(
  rank: Int,
  optionName: String,
  imageUrl: String,
  votes: Int
) {
  val badgeColor = when (rank) {
    1 -> Color(0xFFFFD700) // Oro
    2 -> Color(0xFFC0C0C0) // Plata
    3 -> Color(0xFFCD7F32) // Bronce
    else -> MaterialTheme.colorScheme.surfaceVariant
  }

  val textColor = when (rank) {
    1, 2, 3 -> Color.Black
    else -> MaterialTheme.colorScheme.onSurfaceVariant
  }

  val badgeText = when (rank) {
    1 -> "🥇"
    2 -> "🥈"
    3 -> "🥉"
    else -> rank.toString()
  }

  Card(
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(
      containerColor = MaterialTheme.colorScheme.surface
    ),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    modifier = Modifier.fillMaxWidth()
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(12.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      // Badge de Posición
      Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
          .size(40.dp)
          .clip(CircleShape)
          .padding(4.dp)
      ) {
        Text(
          text = badgeText,
          style = MaterialTheme.typography.titleMedium.copy(
            fontWeight = FontWeight.Bold,
            fontSize = if (rank <= 3) 20.sp else 16.sp
          ),
          color = textColor,
          textAlign = TextAlign.Center
        )
      }

      Spacer(modifier = Modifier.width(8.dp))

      // Imagen redonda de Coil
      AsyncImage(
        model = imageUrl,
        contentDescription = optionName,
        contentScale = ContentScale.Crop,
        modifier = Modifier
          .size(56.dp)
          .clip(RoundedCornerShape(8.dp))
      )

      Spacer(modifier = Modifier.width(16.dp))

      // Nombre del Lugar
      Column(
        modifier = Modifier.weight(1f)
      ) {
        Text(
          text = optionName,
          style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
          maxLines = 1,
          overflow = TextOverflow.Ellipsis,
          color = MaterialTheme.colorScheme.onSurface
        )
      }

      Spacer(modifier = Modifier.width(8.dp))

      // Conteo de Votos
      Column(
        horizontalAlignment = Alignment.End
      ) {
        Text(
          text = votes.toString(),
          style = MaterialTheme.typography.headlineSmall.copy(
            fontWeight = FontWeight.Black,
            color = MaterialTheme.colorScheme.primary
          )
        )
        Text(
          text = if (votes == 1) "voto" else "votos",
          style = MaterialTheme.typography.labelSmall.copy(
            fontSize = 9.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        )
      }
    }
  }
}
