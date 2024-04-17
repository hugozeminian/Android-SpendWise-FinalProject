package com.example.spendwise.data

import com.example.spendwise.model.RewardItem
import com.example.spendwise.model.Spending
import com.example.spendwise.model.SpendingsCategories
import com.example.spendwise.network.BudgetUpdate
import com.example.spendwise.network.IncomeUpdate
import com.example.spendwise.network.LoginRequest
import com.example.spendwise.network.LoginResponse
import com.example.spendwise.network.NewUserName
import com.example.spendwise.network.Response
import com.example.spendwise.network.SpendWiseApiService
import okhttp3.ResponseBody

interface SpendWiseRepository {
    suspend fun getData(email: String): Response
    suspend fun createUser(user: Response): LoginResponse
    suspend fun login(email: String, password: String): LoginResponse

    suspend fun changeUserName(email: String, userName: NewUserName)

    suspend fun addReward(user: String, rewardItem: RewardItem): ResponseBody
    suspend fun deleteReward(user: String, index: Int)

    suspend fun eraseRewards(user: String)
    suspend fun addCategory(user: String, category: SpendingsCategories): ResponseBody
    suspend fun deleteCategory(user: String, index: Int)

    suspend fun eraseCategories(user: String)
    suspend fun addSpending(user: String, spending: Spending): ResponseBody

    suspend fun deleteSpending(user: String, index: Int)
    suspend fun updateIncome(user: String, income: IncomeUpdate): ResponseBody
    suspend fun updateBudget(user: String, newBudget: BudgetUpdate): ResponseBody
}

class NetworkSpendWiseRepository(
    private val spendWiseApiService: SpendWiseApiService
): SpendWiseRepository {

    override suspend fun getData(email: String): Response{
        return spendWiseApiService.getData(email)
    }

    override suspend fun createUser(user: Response): LoginResponse {
        return spendWiseApiService.createUser(user)
    }

    override suspend fun login(email: String, password: String): LoginResponse {
        val request = LoginRequest(email, password)
        return spendWiseApiService.login(request)
    }

    override suspend fun changeUserName(email: String, userName: NewUserName) {
        spendWiseApiService.changeUserName(email, userName)
    }
    override suspend fun deleteReward(user: String, index: Int) = spendWiseApiService.deleteReward(user, index)
    override suspend fun updateIncome(user: String, income: IncomeUpdate): ResponseBody {
        return spendWiseApiService.updateIncome(user, income)
    }
    override suspend fun updateBudget(user: String, newBudget: BudgetUpdate): ResponseBody {
        return spendWiseApiService.updateBudget(user, newBudget)
    }

    override suspend fun addReward(user: String, rewardItem: RewardItem): ResponseBody {
        return spendWiseApiService.addReward(user, rewardItem)
    }

    override suspend fun addCategory(user: String, category: SpendingsCategories): ResponseBody {
        return spendWiseApiService.addCategory(user, category)
    }

    override suspend fun deleteCategory(user: String, index: Int) {
        spendWiseApiService.deleteCategory(user, index)
    }

    override suspend fun addSpending(user: String, spending: Spending): ResponseBody {
        return spendWiseApiService.addSpending(user, spending)
    }

    override suspend fun deleteSpending(user: String, index: Int) {
        spendWiseApiService.deleteSpending(user, index)
    }

    override suspend fun eraseRewards(user: String) {
        spendWiseApiService.eraseRewards(user)
    }

    override suspend fun eraseCategories(user: String) {
        spendWiseApiService.eraseCategories(user)
    }
}