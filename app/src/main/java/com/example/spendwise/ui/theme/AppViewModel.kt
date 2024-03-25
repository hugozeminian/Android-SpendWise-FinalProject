package com.example.spendwise.ui.theme

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.spendwise.data.AppUiState
import com.example.spendwise.model.RewardItem
import com.example.spendwise.model.Spending
import com.example.spendwise.model.SpendingsCategories
import com.example.spendwise.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class AppViewModel: ViewModel(){
    private val _uiState = MutableStateFlow(AppUiState())
    val uiState: StateFlow<AppUiState> = _uiState.asStateFlow()

    var variable: Int = 0

    //Just a variable change test - counter
    fun ChangeVariable(){
        _uiState.update { currentState ->
            currentState.copy(
                counter = _uiState.value.counter + 1
            )
        }
    }

    //Navbar icon set
    fun SetIconIndex(index: Int){
        _uiState.update { currentState ->
            currentState.copy(
                selectedIconIndex = index
            )
        }
    }

    fun AddNewTransaction(
        newSpending: Spending
    ){
        _uiState.update { currentState ->
        currentState.copy(
            breakDownListSample = uiState.value.breakDownListSample + newSpending)
        }
    }

    fun SetLoggedUser(user: User){
        _uiState.update { currentState ->
            currentState.copy(loggedUser = user)
        }
    }

    fun GetTotalCategory(): Map<String, Float>{
        val sumByCategory = mutableMapOf<String, Float>()
        for (spending in _uiState.value.breakDownListSample) {
            val category = spending.category
            val amount = spending.amount
            sumByCategory[category] = (sumByCategory[category] ?: 0f) + amount
        }
        return sumByCategory
    }

    fun GetMonthlyReport(): Map<String, Float>{
        val info: Map<String,Float> = mapOf(
            Pair("Income", _uiState.value.income),
            Pair("Budget", _uiState.value.budget),
            Pair("Spendings", GetTotalSpendings()),
        )
        return info
    }

    fun GetTotalSpendings(): Float{

        var sum: Float = 0F
        for(spending in _uiState.value.breakDownListSample){
            sum = sum + spending.amount
        }

        return sum
    }

    //Special function to sort maps
    fun <K, V : Comparable<V>> Map<K, V>.sortByValue(): Map<K, V> {
        return toSortedMap(compareByDescending { this[it] })
    }

    fun GetSortedSpendings(): Map<String, Float>{
        val allTotals: Map<String, Float> =  GetTotalCategory().sortByValue()

        return allTotals
    }

    fun GetCategories(): List<String>{
        return _uiState.value.spendingsCategoriesList.map { it.name }
    }

    fun GetUsers(): List<User>{
        return _uiState.value.userListSample
    }
    fun GetLoggedUser(): User {
        return _uiState.value.loggedUser
    }

    fun AddUser(user : User) {
        _uiState.update { currentState ->
            currentState.copy(userListSample = _uiState.value.userListSample + user)
        }
    }

    fun ChangeUserName(userName : String){
        val currentName = GetLoggedUser().username

        _uiState.update { currentState ->
            currentState.copy(userListSample =
            _uiState.value.userListSample.map {
                if(it.username == currentName){
                    it.copy(username = userName)
                }else{
                    it
                }
            })
        }
        SetLoggedUser(GetLoggedUser().copy(username = userName))
    }

    fun SetLoggedUser(isLogged: Boolean){
        _uiState.update { currentState ->
            currentState.copy(isLogged = isLogged)
        }
    }

    fun toggleDarkMode(darkMode: Boolean) {
        _uiState.update { currentState ->
            currentState.copy(isDarkMode = darkMode)
        }
    }

    // Budget Page
    fun SetMonthlyIncome(income: Float){
        _uiState.update { currentState ->
            currentState.copy(income = income)
        }
    }

    fun SetMonthlySavingsGoalPercentage(savingsPercentage: Float){
        _uiState.update { currentState ->
            currentState.copy(savingsPercentage = savingsPercentage)
        }
    }

    fun AddRewardItem(rewardItem : RewardItem) {
        _uiState.update { currentState ->
            currentState.copy(rewardsList = _uiState.value.rewardsList + rewardItem)
        }
    }

    fun RemoveRewardItem(
        item: RewardItem
    ) {
        _uiState.update { currentState ->
            val updatedList = currentState.rewardsList.toMutableList()
            updatedList.remove(item)
            currentState.copy(rewardsList = updatedList)
        }
    }

    fun RemoveAllRewardItems() {
        _uiState.update { currentState ->
            currentState.copy(rewardsList = emptyList())
        }
    }

    fun SetMonthlyBudget(monthlyBudget: Float){
        _uiState.update { currentState ->
            currentState.copy(budget = monthlyBudget)
        }
    }

    fun SetWeeklyBudget(weeklyBudget: Float){
        _uiState.update { currentState ->
            currentState.copy(weeklyBudget = weeklyBudget)
        }
    }

    fun AddSpendingsCategoriesItem(spendingsCategoriesItem : SpendingsCategories) {
        _uiState.update { currentState ->
            currentState.copy(spendingsCategoriesList = _uiState.value.spendingsCategoriesList + spendingsCategoriesItem)
        }
    }

    fun RemoveSpendingsCategoriesItem(index: Int) {
        _uiState.update { currentState ->
            val updatedList = currentState.spendingsCategoriesList.toMutableList()
            updatedList.removeAt(index)
            currentState.copy(spendingsCategoriesList = updatedList)
        }
    }

    fun RemoveAllSpendingsCategoriesItem() {
        _uiState.update { currentState ->
            currentState.copy(spendingsCategoriesList = emptyList())
        }
    }

}
