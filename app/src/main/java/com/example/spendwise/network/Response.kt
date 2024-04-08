package com.example.spendwise.network

import com.example.spendwise.model.RewardItem
import com.example.spendwise.model.Spending
import com.example.spendwise.model.SpendingsCategories
import kotlinx.serialization.Serializable

@Serializable
data class Response(
    val fullName: String,
    val userName: String,
    val email: String,
    val password: String,
    val income: Float,
    val monthlyBudget: Float,
    val weeklyBudget: Float,
    val budgetAlert: Float,
    val categories: List<SpendingsCategories>,
    val spendings: List<Spending>,
    val rewards: List<RewardItem>
)