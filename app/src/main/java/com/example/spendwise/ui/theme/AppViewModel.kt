package com.example.spendwise.ui.theme

import androidx.lifecycle.ViewModel
import com.example.spendwise.data.AppUiState
import com.example.spendwise.model.Spending
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

    //CommonUi dropdown menu function - isExpanded variable
    fun SetCategoryIsExpanded(
        isExpanded: Boolean
    ){
        _uiState.update { currentState ->
            currentState.copy(
                isExpanded = isExpanded
            )
        }
    }

    //CommonUi dropdown menu function - selectedText variable
    fun SetCategorySelectedText(
        selectedText: String
    ){
        _uiState.update { currentState ->
            currentState.copy(
                selectedText = selectedText
            )
        }
    }

    fun SetRecapIsExpanded(
        isExpanded: Boolean
    ){
        _uiState.update { currentState ->
            currentState.copy(
                recapIsExpanded = isExpanded
            )
        }
    }

    fun SetRecapSelectedText(
        selectedText: String
    ){
        _uiState.update { currentState ->
            currentState.copy(
                recapSelectedText = selectedText
            )
        }
    }

    fun SetTransactionIsExpanded(
        isExpanded: Boolean
    ){
        _uiState.update { currentState ->
            currentState.copy(
                transactionIsExpanded = isExpanded
            )
        }
    }

    fun SetTransactionSelectedText(
        selectedText: String
    ){
        _uiState.update { currentState ->
            currentState.copy(
                transactionSelectedText = selectedText
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
}