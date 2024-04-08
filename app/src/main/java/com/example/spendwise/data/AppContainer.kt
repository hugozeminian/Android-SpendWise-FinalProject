package com.example.spendwise.data

import com.example.spendwise.network.SpendWiseApiService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

interface AppContainer {
    val spendWiseRepository: SpendWiseRepository
}

class DefaultAppContainer: AppContainer{

    private val BASE_URL = "http://192.168.1.70:3000"

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
        .build()

    private val retrofitService: SpendWiseApiService by lazy {
        retrofit.create(SpendWiseApiService::class.java)
    }

    override val spendWiseRepository: SpendWiseRepository by lazy {
        NetworkSpendWiseRepository(retrofitService)
    }
}