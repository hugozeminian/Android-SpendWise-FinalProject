package com.example.spendwise.ui.theme

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class AppViewModel: ViewModel(){
    private val _uiState = MutableStateFlow(AppUiState())
    val uiState: StateFlow<AppUiState> = _uiState.asStateFlow()

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

    fun SortByDescendingSpendings(
        list: Map<String, Float>
    ): Map<String, Float>{
        return list.sortByValue()
    }

    fun FilterList(
        period: String
    ): Map<String, Float>{

        val sumByCategory = mutableMapOf<String, Float>()

        if(period == "Monthly"){
            val spendings = _uiState.value
            val currentMonth = Spending.getCurrentMonth()
            val ascending = false
            val spendingsForCurrentMonth = getSortedSpendingsForMonth(currentMonth, ascending, spendings)

            for (spending in spendingsForCurrentMonth) {
                val category = spending.category
                val amount = spending.amount
                sumByCategory[category] = (sumByCategory[category] ?: 0f) + amount
            }
            return sumByCategory
        }
        else
        {

            val calendar = Calendar.getInstance()
            val weekDays = Spending.getWeekDays(calendar)
            val spendings = _uiState.value
            val ascending = false

            val spendingsForCurrentWeek = getSortedSpendingsForWeek(weekDays, calendar, ascending, spendings)
            for (spending in spendingsForCurrentWeek) {
                val category = spending.category
                val amount = spending.amount
                sumByCategory[category] = (sumByCategory[category] ?: 0f) + amount
            }
            return sumByCategory
        }
    }

    fun GetMonthlyReport(): Map<String, Float>{
        val info: Map<String,Float> = mapOf(
            Pair("Income", _uiState.value.income),
            Pair("Budget", _uiState.value.monthlyBudget),
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

    fun GetTotalSpendingsMounth(): Float{
        val spendings = _uiState.value
        val currentMonth = Spending.getCurrentMonth()
        val ascending = false
        val spendingsForCurrentMonth = getSortedSpendingsForMonth(currentMonth, ascending, spendings)

        var sum: Float = 0F
        for(spending in spendingsForCurrentMonth){
            sum = sum + spending.amount
        }

        return sum
    }

    fun GetTotalSpendingsWeek(): Float{
        val calendar = Calendar.getInstance()
        val weekDays = Spending.getWeekDays(calendar)
        val spendings = _uiState.value
        val ascending = false
        val spendingsForCurrentWeek = getSortedSpendingsForWeek(weekDays, calendar, ascending, spendings)

        var sum: Float = 0F
        for(spending in spendingsForCurrentWeek){
            sum = sum + spending.amount
        }

        return sum
    }


    //Special function to sort maps
    fun <K, V : Comparable<V>> Map<K, V>.sortByValue(): Map<K, V> {
        return toSortedMap(compareByDescending { this[it] })
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

    fun DeleteSpending(
        item: Spending
    ){
        _uiState.update { currentState ->
            val updatedList = currentState.breakDownListSample.toMutableList()
            updatedList.remove(item)
            currentState.copy(breakDownListSample = updatedList)
        }
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

    fun SetWeeklyBudget(weeklyBudget: Float){
        _uiState.update { currentState ->
            currentState.copy(weeklyBudget = weeklyBudget)
        }
    }

    fun SetBudgetAlert(budgetAlert: Float){
        _uiState.update { currentState ->
            currentState.copy(budgetAlert = budgetAlert)
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
    fun SetMonthlyBudget(monthlyBudget: Float) {
        _uiState.update { currentState ->
            currentState.copy(monthlyBudget = monthlyBudget)
        }
    }

    fun SetSpendingRecap(
        set: String
    ){
        _uiState.update { currentState ->
            currentState.copy(spendingRecap = set)
        }
    }

    //Date manipulation functions
    fun getSortedSpendingList(ascending: Boolean = false, uiState: AppUiState): List<Spending> {
        val originalList = uiState.breakDownListSample
        return Spending.sortByDate(originalList, ascending)
    }

    fun getSortedSpendingsForMonth(currentMonth: Int, ascending: Boolean, uiState: AppUiState): List<Spending> {
        val spendings = getSortedSpendingList(ascending, uiState)
        return spendings.filter { spending ->
            val spendingMonth = SimpleDateFormat("MMM", Locale.ENGLISH).format(Date(spending.date)).toUpperCase()
            spendingMonth == getMonthAbbreviation(currentMonth)
        }
    }

    fun getSortedSpendingsForWeek(weekDays: List<Int>, calendar: Calendar, ascending: Boolean, uiState: AppUiState): List<Spending> {
        val spendings = getSortedSpendingList(ascending, uiState)
        return spendings.filter { spending ->
            val spendingCalendar = Calendar.getInstance().apply {
                time = SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH).parse(spending.date) ?: Date()
            }
            val spendingDay = spendingCalendar.get(Calendar.DAY_OF_MONTH)
            val spendingMonth = spendingCalendar.get(Calendar.MONTH)
            val spendingYear = spendingCalendar.get(Calendar.YEAR)
            val currentYear = calendar.get(Calendar.YEAR)
            val currentMonth = calendar.get(Calendar.MONTH)
            val currentDay = calendar.get(Calendar.DAY_OF_MONTH)

            currentYear == spendingYear &&
                    currentMonth == spendingMonth &&
                    weekDays.contains(spendingDay)
        }
    }

    private fun getMonthAbbreviation(month: Int): String {
        return SimpleDateFormat("MMM", Locale.ENGLISH).format(Calendar.getInstance().apply { set(Calendar.MONTH, month - 1) }.time).toUpperCase()
    }
}



