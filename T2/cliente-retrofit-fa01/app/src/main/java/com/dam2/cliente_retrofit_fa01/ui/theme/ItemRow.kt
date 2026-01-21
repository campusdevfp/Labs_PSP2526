package com.dam2.cliente_retrofit_fa01.ui.theme

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dam2.cliente_retrofit_fa01.data.model.Item

@Composable
fun ItemRow(item: Item, onDelete: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.nombre,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = item.descripcion ?: "",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Precio: $${item.precio}",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "ID: ${item.id}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
            Button(onClick = onDelete) {
                Text("Eliminar")
            }
        }
    }
}