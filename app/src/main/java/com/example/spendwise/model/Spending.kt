package com.example.spendwise.model

data class Spending(
    val category: String,
    val description: String,
    val date: String,
    val amount: Float
)

data class CategoryWeekly(
    val description: String,
    val spent: Float,
    val limit: Float
)

data class User(
    val fullName: String,
    val username: String,
    val email: String,
    val password: String
)

data class SpendingsCategories(
    val name: String,
    val weeklyLimit: Float,
)