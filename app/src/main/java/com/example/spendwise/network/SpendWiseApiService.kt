package com.example.spendwise.network

import com.example.spendwise.model.RewardItem
import com.example.spendwise.model.Spending
import com.example.spendwise.model.SpendingsCategories
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
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

    @POST("/addReward/{user}")
    suspend fun addReward(@Path("user") user: String, @Body reward: RewardItem): ResponseBody

    @DELETE("deleteReward/{user}/{index}")
    suspend fun deleteReward(@Path("user") user: String, @Path("index") index: Int)

    @DELETE("eraseRewards/{user}")
    suspend fun eraseRewards(@Path("user") user: String)

    @POST("/addCategory/{user}")
    suspend fun addCategory(@Path("user") user: String, @Body category: SpendingsCategories): ResponseBody

    @DELETE("deleteCategory/{user}/{index}")
    suspend fun deleteCategory(@Path("user") user: String, @Path("index") index: Int)

    @DELETE("eraseCategories/{user}")
    suspend fun eraseCategories(@Path("user") user: String)
    @POST("/addSpending/{user}")
    suspend fun addSpending(@Path("user") user: String, @Body spending: Spending) : ResponseBody

    @DELETE("deleteSpending/{user}/{index}")
    suspend fun deleteSpending(@Path("user") user: String, @Path("index") index: Int)

    @PUT("updateIncome/{user}")
    suspend fun updateIncome(@Path("user") user: String, @Body income: IncomeUpdate): ResponseBody

    @PUT("updateBudget/{user}")
    suspend fun updateBudget(@Path("user") user: String, @Body newBudget: BudgetUpdate): ResponseBody
}