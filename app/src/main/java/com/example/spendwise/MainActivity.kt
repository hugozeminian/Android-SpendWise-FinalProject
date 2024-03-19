package com.example.spendwise

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.spendwise.data.navItems
import com.example.spendwise.ui.theme.AppViewModel
import com.example.spendwise.ui.theme.SpendWiseTheme
import com.example.spendwise.ui.theme.SpendingsScreen

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SpendWiseTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: AppViewModel = viewModel(),
    navController: NavHostController = rememberNavController()
){
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        bottomBar = {
            NavigationBar {
                navItems.forEachIndexed { index, item ->
                    NavigationBarItem(
                        selected = uiState.selectedIconIndex == index,
                        onClick = {
                            viewModel.SetIconIndex(index)
                            navController.navigate(item.title)
                        },
                        label = {
                            Text(item.title)
                        },
                        icon = {
                            BadgedBox(badge = {
                                if(item.badgeCount != null)
                                {
                                    Badge {
                                        Text(text = item.badgeCount.toString())
                                    }
                                }
                                else if(item.hasNews){
                                    Badge()
                                }
                            }) {
                                Icon(
                                    imageVector = if(index == uiState.selectedIconIndex){
                                        item.selectedIcon
                                    } else item.unselectedIcon,
                                    contentDescription = item.title
                                )
                            }
                        })
                }
            }
        }
    ) { innerPadding ->

        NavHost(
            navController = navController,
            startDestination = "Home",
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(innerPadding)){

            //====== Each composable will call the functions inside its scope base on the route given ======
            composable(route = "Home"){
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Text("I am the main/first/start screen")
                    Icon(imageVector = Icons.Filled.Home, contentDescription = "Home")
                    Button(onClick = {
                        viewModel.ChangeVariable()
                        }) {
                        Text("Counter")
                        }
                    Text("Counter: " + uiState.counter)
                }
            }

            composable(route = "Budget"){
                Text("Budget page goes here")
            }

            composable(route = "Spendings"){
                SpendingsScreen(viewModel = viewModel)
            }

            composable(route = "Report"){
                ReportScreen()
            }

            composable(route = "Logout"){
                Text("Logout page goes here")
            }
        }
    }
}

@Preview
@Composable
fun MainScreenPreview() {
    SpendWiseTheme(darkTheme = false) {
        MainScreen()
    }
}

//@Preview
//@Composable
//fun LightThemePreview() {
//    SpendWiseTheme(darkTheme = false) {
//        SpendWiseMainApp()
//    }
//}
//
//@Preview
//@Composable
//fun DarkThemePreview() {
//    SpendWiseTheme(darkTheme = true) {
//        SpendWiseMainApp()
//    }
//}
//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview() {
//    SpendWiseTheme {
//        SpendWiseMainApp()
//    }
//}
//
//@Composable
//fun SpendWiseMainApp(modifier: Modifier = Modifier) {
//    Text(
//        text = stringResource(id = R.string.app_greetings),
//        modifier = modifier
//    )
//}
