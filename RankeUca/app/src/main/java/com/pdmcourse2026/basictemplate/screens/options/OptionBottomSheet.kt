package com.pdmcourse2026.basictemplate.screens.options

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OptionBottomSheet(
  onSave: (name: String, imageUrl: String) -> Unit,
  onDismiss: () -> Unit
) {
  val sheetState = rememberModalBottomSheetState()
  var name by rememberSaveable { mutableStateOf("") }
  var imageUrl by rememberSaveable { mutableStateOf("") }

  val isValid = name.isNotBlank() && imageUrl.isNotBlank()

  ModalBottomSheet(
    sheetState = sheetState,
    onDismissRequest = onDismiss
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp)
        .padding(bottom = 32.dp),
      verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
      Text(
        text = "Nueva opción",
        fontWeight = FontWeight.Bold
      )
      Text(
        text = "Agrega nombre e imagen para que aparezca en la lista."
      )

      OutlinedTextField(
        value = name,
        onValueChange = { name = it },
        label = { Text("Nombre del lugar") },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true
      )

      OutlinedTextField(
        value = imageUrl,
        onValueChange = { imageUrl = it },
        label = { Text("URL de la imagen") },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true
      )

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End
      ) {
        TextButton(onClick = onDismiss) { Text("Cancelar") }
        Spacer(modifier = Modifier.width(8.dp))
        Button(
          onClick = {
            if (isValid) {
              onSave(name.trim(), imageUrl.trim())
              onDismiss()
            }
          },
          enabled = isValid
        ) {
          Text("Guardar")
        }
      }
    }
  }
}
