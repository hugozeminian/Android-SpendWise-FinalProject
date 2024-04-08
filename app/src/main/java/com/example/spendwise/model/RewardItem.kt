package com.example.spendwise.model

import kotlinx.serialization.Serializable

@Serializable
data class RewardItem(
    val description: String,
    val amount: String
)