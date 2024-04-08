package com.example.spendwise.data

import com.example.spendwise.network.BudgetUpdate
import com.example.spendwise.network.IncomeUpdate
import com.example.spendwise.network.Response
import com.example.spendwise.network.SpendWiseApiService
import okhttp3.ResponseBody

interface SpendWiseRepository {
    suspend fun getData(): List<Response>
    suspend fun deleteReward(user: String, index: Int)
    suspend fun updateIncome(user: String, income: IncomeUpdate): ResponseBody
    suspend fun updateBudget(user: String, newBudget: BudgetUpdate): ResponseBody
}

class NetworkSpendWiseRepository(
    private val spendWiseApiService: SpendWiseApiService
): SpendWiseRepository {

    override suspend fun getData(): List<Response> = spendWiseApiService.getData()
    override suspend fun deleteReward(user: String, index: Int) = spendWiseApiService.deleteReward(user, index)
    override suspend fun updateIncome(user: String, income: IncomeUpdate): ResponseBody {
        return spendWiseApiService.updateIncome(user, income)
    }
    override suspend fun updateBudget(user: String, newBudget: BudgetUpdate): ResponseBody {
        return spendWiseApiService.updateBudget(user, newBudget)
    }
}