package com.example.spendwise.data

import com.example.spendwise.model.RewardItem
import com.example.spendwise.model.Spending
import com.example.spendwise.model.SpendingsCategories
import com.example.spendwise.model.User

data class AppUiState(
    //Navbar icons state
    val selectedIconIndex: Int = 0,

    //Main page counter - test
    val counter: Int = 0,

    //Sample list to show items in "Spendings" screen - first list
    val breakDownListSample: List<Spending> = listOf(
        Spending("Groceries","Walmart", "Feb 18, 2024", 54F),
        Spending("Groceries","Costco", "Feb 12, 2024", 56.12F),
        Spending("Groceries","Safeway - Pharmacy", "Feb 12, 2024", 54.98F),
        Spending("Utilities","Enmax", "Feb 6, 2024", 114.99F),
        Spending("Utilities","Telus", "Feb 9, 2024", 69.5F),
        Spending("Entertainment","Skate park", "Feb 25, 2024", 75.30F)
    ),

    val userListSample: List<User> = listOf(
        User ("Aurora Wang", "Aurora","aurorawang@gmail.com", "1234" ),
        User("User", "User", "user@gmail.com", "123"),
        User("John Doe", "john.doe", "john@gmail.com", "12"),
        User("H C", "hc", "h@c.com", "hc"),
    ),

    val loggedUser : User = User("","","",""),

    val isLogged: Boolean = true,

    //Other Settings Data
    val isDarkMode: Boolean = false,

    //Monthly income
    val income: Float = 2500F,
    val savingsPercentage: Float = 0F,

    //Monthly budget
    val budget: Float = 2000F,
    val weeklyBudget: Float = 500F,

    //Alert Limit
    val showAlert: Boolean = false,

    //Spendings Categories
    val spendingsCategoriesList: List<SpendingsCategories> = listOf(
        SpendingsCategories("Groceries", 100F),
        SpendingsCategories("Utilities", 300F),
        SpendingsCategories("Entertainment", 200F),
    ),

    //Rewards
    val rewardsBalance: Float = 0F,
    val rewardsList: List<RewardItem> = listOf(
        RewardItem("Reward-1", "10"),
        RewardItem("Reward-2", "20"),
        RewardItem("Reward-3", "30"),
    ),
)
