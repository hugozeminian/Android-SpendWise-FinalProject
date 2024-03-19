package com.example.spendwise.data

import androidx.compose.foundation.layout.width
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.spendwise.ui.theme.AppViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryDropdownMenu(
    viewModel: AppViewModel,
    modifier: Modifier = Modifier
){
    val uiState by viewModel.uiState.collectAsState()

    ExposedDropdownMenuBox(
        expanded = uiState.isExpanded,
        onExpandedChange = { viewModel.SetCategoryIsExpanded(!uiState.isExpanded)}
    ) {
        TextField(
            modifier = Modifier
                .menuAnchor()
                .width(180.dp),
            value = uiState.selectedText,
            onValueChange = {},
            readOnly = true,
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = uiState.isExpanded)
            }
        )

        ExposedDropdownMenu(
            expanded = uiState.isExpanded,
            onDismissRequest = { viewModel.SetCategoryIsExpanded(false) }
        ) {
            uiState.list.forEachIndexed { index, text ->
                DropdownMenuItem(
                    text = { Text(text = text) },
                    onClick = {
                        viewModel.SetCategorySelectedText(uiState.list[index])
                        viewModel.SetCategoryIsExpanded(false)},
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                )
            }
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun AddTransactionDropdownMenu(
    viewModel: AppViewModel,
    modifier: Modifier = Modifier
){
    val uiState by viewModel.uiState.collectAsState()

    ExposedDropdownMenuBox(
        expanded = uiState.transactionIsExpanded,
        onExpandedChange = { viewModel.SetTransactionIsExpanded(!uiState.transactionIsExpanded)}
    ) {
        TextField(
            modifier = Modifier
                .menuAnchor()
                .width(180.dp),
            value = uiState.transactionSelectedText,
            onValueChange = {},
            readOnly = true,
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = uiState.transactionIsExpanded)
            }
        )

        ExposedDropdownMenu(
            expanded = uiState.transactionIsExpanded,
            onDismissRequest = { viewModel.SetTransactionIsExpanded(false) }
        ) {
            uiState.transactionList.forEachIndexed { index, text ->
                DropdownMenuItem(
                    text = { Text(text = text) },
                    onClick = {
                        viewModel.SetTransactionSelectedText(uiState.list[index])
                        viewModel.SetTransactionIsExpanded(false)},
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpendingDropdownMenu(
    viewModel: AppViewModel,
    modifier: Modifier = Modifier
){
    val uiState by viewModel.uiState.collectAsState()

    ExposedDropdownMenuBox(
        expanded = uiState.recapIsExpanded,
        onExpandedChange = { viewModel.SetRecapIsExpanded(!uiState.isExpanded)}
    ) {
        TextField(
            modifier = Modifier
                .menuAnchor()
                .width(180.dp),
            value = uiState.recapSelectedText,
            onValueChange = {},
            readOnly = true,
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = uiState.recapIsExpanded)
            }
        )

        ExposedDropdownMenu(
            expanded = uiState.recapIsExpanded,
            onDismissRequest = { viewModel.SetRecapIsExpanded(false) }
        ) {
            uiState.recapList.forEachIndexed { index, text ->
                DropdownMenuItem(
                    text = { Text(text = text) },
                    onClick = {
                        viewModel.SetRecapSelectedText(uiState.recapList[index])
                        viewModel.SetRecapIsExpanded(false)},
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                )
            }
        }
    }
}