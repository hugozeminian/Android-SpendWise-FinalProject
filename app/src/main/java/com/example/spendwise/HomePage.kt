package com.example.spendwise

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.spendwise.ui.theme.AppViewModel

@Composable
fun HomePage(viewModel: AppViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = stringResource(id = R.string.wp_welcome_text) + uiState.loggedUser.username,
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = "Your monthly budget is: $${uiState.monthlyBudget}",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = "Your weekly budget is: ${uiState.weeklyBudget}",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

//        Text(
//            text = "$600.25 / $750.00",
//            style = MaterialTheme.typography.headlineLarge,
//            modifier = Modifier.padding(bottom = 8.dp)
//        )
//        Text(
//            text = "Of your weekly Budget",
//            style = MaterialTheme.typography.bodyMedium,
//            modifier = Modifier.padding(bottom = 8.dp)
//        )
//        Text(
//            text = "You've spent:",
//            style = MaterialTheme.typography.bodyMedium,
//            modifier = Modifier.padding(bottom = 8.dp)
//        )
//        Text(
//            text = "$2,401 / $3000.00",
//            style = MaterialTheme.typography.headlineLarge,
//            modifier = Modifier.padding(bottom = 8.dp)
//        )
//        Text(
//            text = "Of your weekly Budget",
//            style = MaterialTheme.typography.bodyMedium,
//            modifier = Modifier.padding(bottom = 8.dp)
//        )
//        Card(modifier = Modifier.padding(top = 16.dp)) {
//            Column(
//                horizontalAlignment = Alignment.CenterHorizontally,
//                modifier = Modifier.padding(16.dp)
//            ) {
//                Text(
//                    text = "You've spent 67.7% of your weekly limit!",
//                    style = MaterialTheme.typography.headlineLarge,
//                    modifier = Modifier.padding(bottom = 8.dp)
//                )
//            }
//        }
//
//        Card(modifier = Modifier.padding(top = 16.dp)) {
//            Column(
//                horizontalAlignment = Alignment.CenterHorizontally,
//                modifier = Modifier.padding(16.dp)
//            ) {
//                Text(
//                    text = "You've spent 67.7% of your monthly limit!",
//                    style = MaterialTheme.typography.headlineLarge,
//                    modifier = Modifier.padding(bottom = 8.dp)
//                )
//            }
//        }
//
//    }
    }
}
