package com.dam2.cliente_retrofit_fa01.ui.theme

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dam2.cliente_retrofit_fa01.ui.theme.vm.ItemViewModel

@Composable
fun MainScreen(viewModel: ItemViewModel = viewModel()) {

    val items by viewModel.items.collectAsState()
    val loading by viewModel.loading.collectAsState()
    var showDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.loadItems()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Button(
            onClick = { showDialog = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Agregar Item")
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (loading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(items) { item ->
                    ItemRow(item, onDelete = {
                        viewModel.deleteItem(item.id)
                    })
                }
            }
        }
    }

    if (showDialog) {
        AddItemDialog(
            onDismiss = { showDialog = false },
            onConfirm = { nombre, descripcion, precio ->
                viewModel.addItem(nombre, descripcion, precio)
                showDialog = false
            }
        )
    }
}

@Composable
fun AddItemDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, String, Double) -> Unit
) {
    var nombre by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var precio by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Agregar Nuevo Item") },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = descripcion,
                    onValueChange = { descripcion = it },
                    label = { Text("Descripción") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = precio,
                    onValueChange = { precio = it },
                    label = { Text("Precio") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val precioDouble = precio.toDoubleOrNull() ?: 0.0
                    if (nombre.isNotBlank()) {
                        onConfirm(nombre, descripcion, precioDouble)
                    }
                }
            ) {
                Text("Agregar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}