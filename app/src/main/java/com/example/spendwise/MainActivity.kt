package com.example.spendwise

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
import com.example.spendwise.data.BottomNavigationItem
import com.example.spendwise.data.navItems
import com.example.spendwise.ui.theme.SpendWiseTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
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

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    navController: NavHostController = rememberNavController()
){
    var selectedItemIndex by rememberSaveable { mutableStateOf(0)}
    Scaffold(
        bottomBar = {
            NavigationBar {
                navItems.forEachIndexed { index, item ->
                    NavigationBarItem(
                        selected = selectedItemIndex == index,
                        onClick = {
                            selectedItemIndex = index
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
                                    imageVector = if(index == selectedItemIndex){
                                        item.selectedIcon
                                    } else item.unselectedIcon,
                                    contentDescription = item.title
                                )
                            }
                        })
                }
            }
        }
    ) {
        NavHost(navController = navController, startDestination = "Home"){

            //====== Each composable will call the functions inside its scope base on the route given ======
            composable(route = "Home"){
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Text("I am the main/first/start screen")
                    Icon(imageVector = Icons.Filled.Home, contentDescription = "Home")
                }
            }

            composable(route = "Budget"){
                Text("Budget page goes here")
            }

            composable(route = "Spendings"){
                Text("Spendings page goes here")
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
