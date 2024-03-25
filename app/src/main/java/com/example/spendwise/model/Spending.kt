package com.example.spendwise.model

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

data class Spending(
    val category: String,
    val description: String,
    val date: String,
    val amount: Float
) {
    companion object {

        fun sortByDate(spendingList: List<Spending>, ascending: Boolean): List<Spending> {
            val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH)
            val sortedList = spendingList.sortedBy { dateFormat.parse(it.date) }
            return if (ascending) sortedList else sortedList.reversed()
        }

        fun getCurrentMonth(): Int {
            val calendar = Calendar.getInstance()
            return calendar.get(Calendar.MONTH) + 1
        }

        fun getWeekDays(calendar: Calendar): List<Int> {
            calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)                          // Set the calendar to the first day of the week (Sunday)
            val currentDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)                    // Get the current day of the week
            val weekDays = mutableListOf<Int>()

            repeat(7) {                                                      // Add the days of the week to the list
                val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)                    // Get the day of the month
                weekDays.add(dayOfMonth)                                                // Add the day of the month to the list
                calendar.add(Calendar.DAY_OF_MONTH, 1)                          // Move to the next day
            }

            if (currentDayOfWeek != Calendar.SUNDAY) {                                  // Adjust the list if the current day is not Sunday
                repeat(currentDayOfWeek - Calendar.SUNDAY) {                // Rotate the list to match the current day
                    val lastDay = weekDays.removeAt(weekDays.size - 1)
                    weekDays.add(0, lastDay)
                }
            }

            return weekDays
        }

    }
}


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