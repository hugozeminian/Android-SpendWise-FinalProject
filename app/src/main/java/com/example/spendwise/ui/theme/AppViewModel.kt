package com.example.spendwise.ui.theme

import androidx.lifecycle.ViewModel
import com.example.spendwise.data.AppUiState
import com.example.spendwise.model.Spending
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlin.math.roundToInt

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

    fun GetTotalCategory(): Map<String, Float>{

        val sumByCategory = mutableMapOf<String, Float>()
        for (spending in _uiState.value.breakDownListSample) {
            val category = spending.category
            val amount = spending.amount
            sumByCategory[category] = (sumByCategory[category] ?: 0f) + amount
        }
        return sumByCategory
    }

    fun GetCategories(): List<String>{
        val listOfCategories: List<String> = _uiState.value.breakDownListSample.map { it.category }.distinct()
        return listOfCategories
    }
}