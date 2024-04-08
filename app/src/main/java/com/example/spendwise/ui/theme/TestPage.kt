package com.example.spendwise.ui.theme

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun TestPage(
    viewModel: AppViewModel
){
    val uiState by viewModel.uiState.collectAsState()

    Column {
        Text("Response from server:")
        Spacer(modifier = Modifier.height(24.dp))
        Text(uiState.response.toString())
    }
}

//@Preview
//@Composable
//fun DisplayTestPage(){
//    TestPage(AppViewModel())
//}