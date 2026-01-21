package com.dam2.cliente_retrofit_fa01.ui.theme.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dam2.cliente_retrofit_fa01.data.model.Item
import com.dam2.cliente_retrofit_fa01.data.model.ItemCreate
import com.dam2.cliente_retrofit_fa01.data.remote.RetrofitClient
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ItemViewModel : ViewModel() {

    private val api = RetrofitClient.api

    private val _items = MutableStateFlow<List<Item>>(emptyList())
    val items: StateFlow<List<Item>> = _items

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    fun loadItems() {
        viewModelScope.launch {
            _loading.value = true
            try {
                _items.value = api.getItems()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            _loading.value = false
        }
    }

    fun addItem(nombre: String, descripcion: String?, precio: Double) {
        viewModelScope.launch {
            try {
                api.createItem(ItemCreate(nombre, descripcion, precio))
                loadItems()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun deleteItem(id: Int) {
        viewModelScope.launch {
            api.deleteItem(id)
            loadItems()
        }
    }
}