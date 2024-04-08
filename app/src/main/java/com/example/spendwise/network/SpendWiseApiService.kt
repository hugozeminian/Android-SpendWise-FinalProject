package com.example.spendwise.network

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

private const val BASE_URL = "http://192.168.1.70:3000"

@Serializable
data class IncomeUpdate(val income: Float)

@Serializable
data class BudgetUpdate(
    val monthlyBudget: Float,
    val weeklyBudget: Float,
    val budgetAlert: Float
)

private val retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
    .build()

interface SpendWiseApiService{
    @GET("data")
    suspend fun getData(): List<Response>

    @DELETE("deleteReward/{user}/{index}")
    suspend fun deleteReward(@Path("user") user: String, @Path("index") index: Int)

    @PUT("updateIncome/{user}")
    suspend fun updateIncome(@Path("user") user: String, @Body income: IncomeUpdate): ResponseBody

    @PUT("updateBudget/{user}")
    suspend fun updateBudget(@Path("user") user: String, @Body newBudget: BudgetUpdate): ResponseBody
}

object SpendWiseApi{
    val retrofitService: SpendWiseApiService by lazy {
        retrofit.create(SpendWiseApiService::class.java)
    }
}