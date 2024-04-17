package com.example.spendwise.network

import com.example.spendwise.model.RewardItem
import com.example.spendwise.model.Spending
import com.example.spendwise.model.SpendingsCategories
import kotlinx.serialization.Serializable

@Serializable
data class Response(
    val fullName: String = "",
    val userName: String = "",
    val email: String = "",
    val password: String = "",
    val income: Float = 0F,
    val monthlyBudget: Float = 0F,
    val weeklyBudget: Float = 0F,
    val budgetAlert: Float = 0F,
    val categories: List<SpendingsCategories> = listOf(),
    val spendings: List<Spending> = listOf(),
    val rewards: List<RewardItem> = listOf()
)

@Serializable
data class LoginResponse(
    val message: String
)