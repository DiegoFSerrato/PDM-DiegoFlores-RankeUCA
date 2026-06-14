package com.pdmcourse2026.basictemplate.screens.register

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun RegisterScreen(
  viewModel: RegisterViewModel,
  onRegisterSuccess: () -> Unit
) {
  var carnet by remember { mutableStateOf("") }
  val keyboardController = LocalSoftwareKeyboardController.current
  val uiState = viewModel.uiState

  LaunchedEffect(uiState) {
    if (uiState is RegisterUiState.Success) {
      onRegisterSuccess()
    }
  }

  Scaffold { innerPadding ->
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)
        .padding(24.dp),
      verticalArrangement = Arrangement.Center,
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      // Icono o Logo superior
      Icon(
        imageVector = Icons.Default.AccountCircle,
        contentDescription = "Registro",
        tint = MaterialTheme.colorScheme.primary,
        modifier = Modifier.size(96.dp)
      )

      Spacer(modifier = Modifier.height(16.dp))

      Text(
        text = "¡Bienvenido a RankeUCA!",
        style = MaterialTheme.typography.headlineMedium.copy(
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.primary
        ),
        textAlign = TextAlign.Center
      )

      Text(
        text = "Por favor ingresa tu número de carnet para continuar",
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        textAlign = TextAlign.Center,
        modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp)
      )

      Spacer(modifier = Modifier.height(24.dp))

      OutlinedTextField(
        value = carnet,
        onValueChange = { carnet = it },
        label = { Text("Carnet de Estudiante") },
        placeholder = { Text("Ej. 00000000") },
        leadingIcon = {
          Icon(
            imageVector = Icons.Default.AccountCircle,
            contentDescription = "Carnet"
          )
        },
        singleLine = true,
        keyboardOptions = KeyboardOptions(
          keyboardType = KeyboardType.Number,
          imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
          onDone = {
            keyboardController?.hide()
            viewModel.register(carnet)
          }
        ),
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
          focusedBorderColor = MaterialTheme.colorScheme.primary,
          unfocusedBorderColor = MaterialTheme.colorScheme.outline
        )
      )

      Spacer(modifier = Modifier.height(24.dp))

      Button(
        onClick = {
          keyboardController?.hide()
          viewModel.register(carnet)
        },
        enabled = uiState !is RegisterUiState.Loading,
        modifier = Modifier
          .fillMaxWidth()
          .height(50.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
          containerColor = MaterialTheme.colorScheme.primary,
          contentColor = MaterialTheme.colorScheme.onPrimary
        )
      ) {
        if (uiState is RegisterUiState.Loading) {
          CircularProgressIndicator(
            modifier = Modifier.size(24.dp),
            color = MaterialTheme.colorScheme.onPrimary,
            strokeWidth = 2.dp
          )
        } else {
          Text(
            text = "Registrarse",
            style = MaterialTheme.typography.labelLarge.copy(
              fontSize = 16.sp,
              fontWeight = FontWeight.Bold
            )
          )
        }
      }

      // Dialogo de Error
      if (uiState is RegisterUiState.Error) {
        AlertDialog(
          onDismissRequest = { viewModel.resetError() },
          title = { Text("Error") },
          text = { Text(uiState.message) },
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
