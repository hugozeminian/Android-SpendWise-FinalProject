package com.example.spendwise.model

import java.util.Date

data class Spending(
    val description: String,
    val date: String,
    val amount: Float
)

data class CategoryWeekly(
    val description: String,
    val spent: Float,
    val limit: Float
)
