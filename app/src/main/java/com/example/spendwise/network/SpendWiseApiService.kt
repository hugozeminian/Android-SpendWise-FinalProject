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

@Serializable
data class IncomeUpdate(val income: Float)

@Serializable
data class BudgetUpdate(
    val monthlyBudget: Float,
    val weeklyBudget: Float,
    val budgetAlert: Float
)

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