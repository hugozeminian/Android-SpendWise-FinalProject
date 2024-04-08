package com.example.spendwise.ui.theme

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.spendwise.SpendWiseApplication
import com.example.spendwise.data.AppUiState
import com.example.spendwise.data.SpendWiseRepository
import com.example.spendwise.model.RewardItem
import com.example.spendwise.model.Spending
import com.example.spendwise.model.SpendingsCategories
import com.example.spendwise.model.User
import com.example.spendwise.network.BudgetUpdate
import com.example.spendwise.network.IncomeUpdate
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class AppViewModel(
    private val spendWiseRepository: SpendWiseRepository
): ViewModel(){
    private val _uiState = MutableStateFlow(AppUiState())
    val uiState: StateFlow<AppUiState> = _uiState.asStateFlow()

    val user = _uiState.value.loggedUser.email

    init{
        getData()
    }
    fun getData() {
        viewModelScope.launch {
            try {
                val dataResult = spendWiseRepository.getData()
                _uiState.update { currentState ->
                    currentState.copy(
                        loggedUser = User(
                            dataResult[0].fullName,
                            dataResult[0].userName,
                            dataResult[0].email,
                            dataResult[0].password),
                        income = dataResult[0].income,
                        monthlyBudget = dataResult[0].monthlyBudget,
                        weeklyBudget = dataResult[0].weeklyBudget,
                        budgetAlert = dataResult[0].budgetAlert,
                        spendingsCategoriesList = dataResult[0].categories,
                        breakDownListSample = dataResult[0].spendings,
                        rewardsList = dataResult[0].rewards,
                        response = dataResult
                    )
                }

            } catch (e: IOException) {
//                _uiState.update { currentState ->
//                    currentState.copy(
//                        loggedUser = User("", "","",""),
//                        income = 0.0F,
//                        monthlyBudget = 0.0F,
//                        weeklyBudget = 0.0F,
//                    )
//                }
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as SpendWiseApplication)
                val spendWiseRepository = application.container.spendWiseRepository
                AppViewModel(spendWiseRepository = spendWiseRepository)
            }
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

    //Function to add a new spending to the spending list
    fun AddNewTransaction(
        newSpending: Spending
    ){
        viewModelScope.launch {
            try{
                val user = _uiState.value.loggedUser.email
                spendWiseRepository.addSpending(user, newSpending)
                _uiState.update { currentState ->
                    currentState.copy(
                        breakDownListSample = uiState.value.breakDownListSample + newSpending)
                }
            }
            catch(e: IOException){

            }
        }
    }

    //Function to ser if the user is logged
    fun SetLoggedUser(user: User){
        _uiState.update { currentState ->
            currentState.copy(loggedUser = user)
        }
    }

    //function to sort maps used byt report graphs
    fun SortByDescendingSpendings(
        list: Map<String, Float>
    ): Map<String, Float>{
        if(list.isEmpty()) return list
        return list.sortByValue()
    }

    //Converts the list of spending into a map used by report graphs
    fun FilterList(
        period: String
    ): Map<String, Float>{

        val sumByCategory = mutableMapOf<String, Float>()

        if(period == "Monthly"){
            val spendings = _uiState.value
            val currentMonth = Spending.getCurrentMonth()
            val ascending = false
            val spendingsForCurrentMonth = getSortedSpendingsForMonth(currentMonth, ascending, spendings)

            if(!spendingsForCurrentMonth.isEmpty())
            {
                for (spending in spendingsForCurrentMonth) {
                    val category = spending.category
                    val amount = spending.amount
                    sumByCategory[category] = (sumByCategory[category] ?: 0f) + amount
                }
                return sumByCategory
            }
        }
        else
        {

            val calendar = Calendar.getInstance()
            val weekDays = Spending.getWeekDays(calendar)
            val spendings = _uiState.value
            val ascending = false

            val spendingsForCurrentWeek = getSortedSpendingsForWeek(weekDays, calendar, ascending, spendings)

            if(!spendingsForCurrentWeek.isEmpty()) {
                for (spending in spendingsForCurrentWeek) {
                    val category = spending.category
                    val amount = spending.amount
                    sumByCategory[category] = (sumByCategory[category] ?: 0f) + amount
                }
                return sumByCategory
            }
        }

        return sumByCategory
    }

    //Returns a complete report used in report screen
    fun GetMonthlyReport(): Map<String, Float>{
        val info: Map<String,Float> = mapOf(
            Pair("Income", _uiState.value.income),
            Pair("Budget", _uiState.value.monthlyBudget),
            Pair("Spendings", GetTotalSpendingsMounth()),
        )
        return info
    }

    //Returns the total spent in the current month
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

    //Returns the total spent in the current week
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

    //Returns a list of only distinct categories
    fun GetCategories(): List<String>{
        return _uiState.value.spendingsCategoriesList.map { it.name }
    }

    //Returns te user list
    fun GetUsers(): List<User>{
        return _uiState.value.userListSample
    }

    //returns logged user
    fun GetLoggedUser(): User {
        return _uiState.value.loggedUser
    }

    //Function to delete a spending from spending list
    fun DeleteSpending(
        item: Spending
    ){
        viewModelScope.launch {
            try{
                val user = _uiState.value.loggedUser.email
                val index = _uiState.value.breakDownListSample.indexOf(item)
                val response = spendWiseRepository.deleteSpending(user, index)
                _uiState.update { currentState ->
                    val updatedList = currentState.breakDownListSample.toMutableList()
                    updatedList.remove(item)
                    currentState.copy(breakDownListSample = updatedList)
                }
            }
            catch (e: IOException) {

            }
        }
    }

    //Function to add a new user
    fun AddUser(user : User) {
        _uiState.update { currentState ->
            currentState.copy(userListSample = _uiState.value.userListSample + user)
        }
    }

    //Function to change user name
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

    //Function to set if user is logged
    fun SetLoggedUser(isLogged: Boolean){
        _uiState.update { currentState ->
            currentState.copy(isLogged = isLogged)
        }
    }

    //Function to set dark mode
    fun toggleDarkMode(darkMode: Boolean) {
        _uiState.update { currentState ->
            currentState.copy(isDarkMode = darkMode)
        }
    }

    //Function to set monthly income value
    fun SetMonthlyIncome(income: Float){
        viewModelScope.launch {
            try{
                val user = _uiState.value.loggedUser.email
                val response = spendWiseRepository.updateIncome(user, IncomeUpdate(income))
                _uiState.update { currentState ->
                    currentState.copy(income = income)
                }
            }
            catch (e: IOException) {

            }
        }
    }

    fun UpdateAllBudget(
        monthly: Float,
        weekly: Float,
        alert: Float){
        viewModelScope.launch {
            try{
                val user = _uiState.value.loggedUser.email
                spendWiseRepository.updateBudget(user, BudgetUpdate(monthly, weekly, alert))
                _uiState.update { currentState ->
                    currentState.copy(
                        monthlyBudget = monthly,
                        weeklyBudget = weekly,
                        budgetAlert = alert
                    )
                }
            }
            catch (e: IOException) {

            }
        }
    }

    //Function to add a new reward item in the list of rewards
    fun AddRewardItem(rewardItem : RewardItem) {

        viewModelScope.launch{
            val user = _uiState.value.loggedUser.email
            spendWiseRepository.addReward(user, rewardItem)
            _uiState.update { currentState ->
                currentState.copy(rewardsList = _uiState.value.rewardsList + rewardItem)
            }
        }
    }

    //function to removed a reward form the list of rewards
    fun RemoveRewardItem(
        item: RewardItem
    ) {
        val index = _uiState.value.rewardsList.indexOf(item)

        viewModelScope.launch{
            val user = _uiState.value.loggedUser.email
            spendWiseRepository.deleteReward(user, index)
            _uiState.update { currentState ->
                val updatedList = currentState.rewardsList.toMutableList()
                updatedList.remove(item)
                currentState.copy(rewardsList = updatedList)
            }
        }
    }

    //Function to clear up rewards list
    fun RemoveAllRewardItems() {

        viewModelScope.launch {
            try{
                val user = _uiState.value.loggedUser.email
                spendWiseRepository.eraseRewards(user)
                _uiState.update { currentState ->
                    currentState.copy(rewardsList = emptyList())
                }
            }
            catch (e: IOException) {

            }
        }
    }

    //Sets the weekly budget value
    fun SetWeeklyBudget(weeklyBudget: Float){
        _uiState.update { currentState ->
            currentState.copy(weeklyBudget = weeklyBudget)
        }
    }

    //Sets the budget alert
    fun SetBudgetAlert(budgetAlert: Float){
        _uiState.update { currentState ->
            currentState.copy(budgetAlert = budgetAlert)
        }
    }

    //Function to add a new category to categories list
    fun AddSpendingsCategoriesItem(category : SpendingsCategories) {

        viewModelScope.launch {
            val user = _uiState.value.loggedUser.email
            spendWiseRepository.addCategory(user, category)
            _uiState.update { currentState ->
                currentState.copy(spendingsCategoriesList = _uiState.value.spendingsCategoriesList + category)
            }
        }

    }

    //Function to remove a category from categories list
    fun RemoveSpendingsCategoriesItem(
        category: SpendingsCategories
    ) {
        viewModelScope.launch {
            val user = _uiState.value.loggedUser.email
            val index = _uiState.value.spendingsCategoriesList.indexOf(category)
            spendWiseRepository.deleteCategory(user, index)
            _uiState.update { currentState ->
                val updatedList = currentState.spendingsCategoriesList.toMutableList()
                updatedList.remove(category)
                currentState.copy(spendingsCategoriesList = updatedList)
            }
        }
    }

    //Function to clear up categories list
    fun RemoveAllSpendingsCategoriesItem() {

        viewModelScope.launch {
            try{
                val user = _uiState.value.loggedUser.email
                spendWiseRepository.eraseCategories(user)
                _uiState.update { currentState ->
                    currentState.copy(spendingsCategoriesList = emptyList())
                }
            }
            catch (e: IOException) {

            }
        }
    }

    //Function to set monthly budget
    fun SetMonthlyBudget(monthlyBudget: Float) {
        _uiState.update { currentState ->
            currentState.copy(monthlyBudget = monthlyBudget)
        }
    }

    //Function to set spending recap list
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

    //Function to sort and return list of spending in the current month
    fun getSortedSpendingsForMonth(currentMonth: Int, ascending: Boolean, uiState: AppUiState): List<Spending> {
        val spendings = getSortedSpendingList(ascending, uiState)
        return spendings.filter { spending ->
            val spendingMonth = SimpleDateFormat("MMM", Locale.ENGLISH).format(Date(spending.date)).toUpperCase()
            spendingMonth == getMonthAbbreviation(currentMonth)
        }
    }

    //Function to sort and return list of spending in the current week
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

    //Function to format dates
    private fun getMonthAbbreviation(month: Int): String {
        return SimpleDateFormat("MMM", Locale.ENGLISH).format(Calendar.getInstance().apply { set(Calendar.MONTH, month - 1) }.time).toUpperCase()
    }
}





