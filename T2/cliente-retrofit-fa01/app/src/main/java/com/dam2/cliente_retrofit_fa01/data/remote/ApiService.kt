package com.dam2.cliente_retrofit_fa01.data.remote

import com.dam2.cliente_retrofit_fa01.data.model.Item
import com.dam2.cliente_retrofit_fa01.data.model.ItemCreate
import retrofit2.http.*
import retrofit2.Response

interface ApiService {

    @GET("items/")
    suspend fun getItems(): List<Item>

    @POST("items/")
    suspend fun createItem(
        @Body item: ItemCreate
    ): Item

    @DELETE("items/{id}")
    suspend fun deleteItem(
        @Path("id") id: Int
    ): Response<Unit>
}