package com.example.spendwise

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.spendwise.data.navItems
import com.example.spendwise.ui.theme.AppViewModel
import com.example.spendwise.ui.theme.SpendWiseTheme


class MainActivity : ComponentActivity() {
    private val viewModel: AppViewModel by viewModels()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            SpendWiseTheme(viewModel = viewModel) {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen(
                        onLogin = {
                            viewModel.SetLoggedUser(true)
                        },
                        onLogout = {
                            viewModel.SetLoggedUser(false)
                        },
                    )
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: AppViewModel = viewModel(),
    navController: NavHostController = rememberNavController(),
    onLogin: () -> Unit,
    onLogout: () -> Unit
) {

    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        bottomBar = {
            if (uiState.isLogged) {
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
                                    if (item.badgeCount != null) {
                                        Badge {
                                            Text(text = item.badgeCount.toString())
                                        }
                                    } else if (item.hasNews) {
                                        Badge()
                                    }
                                }) {
                                    Icon(
                                        imageVector = if (index == uiState.selectedIconIndex) {
                                            item.selectedIcon
                                        } else item.unselectedIcon,
                                        contentDescription = item.title
                                    )
                                }
                            })
                    }
                }
            }
        }
    ) { innerPadding ->

        NavHost(
            navController = navController,
            startDestination = "Home",
            modifier = Modifier.padding(innerPadding)
        ) {

            //====== Each composable will call the functions inside its scope base on the route given ======
            if (!uiState.isLogged) {
                composable(route = "Home") {
                    LoginPage(onLoginSuccess = {
                        onLogin.invoke()
                        navController.navigate("home")
                    },
                        onNavigateToRegister = {
                            navController.navigate("registerPage")
                        },
                        viewModel=viewModel
                    )
                }
            }
            composable("home") {
                HomePage()
            }

            composable("registerPage") {
                RegisterPage(onCreatingAccount = {
                    navController.navigate(("Home"))
                }, viewModel = viewModel)
            }

            composable(route = "Budget") {
                BudgetInformation()
            }

            composable(route = "Spending") {
                SpendingsScreen(viewModel)
            }

            composable(route = "Report") {
                ReportScreen(viewModel)
            }

            composable(route = "Settings") {
                SettingPage(
                    onLogout = {
                        onLogout.invoke()
                        navController.navigate("Home")
                    },
                    viewModel=viewModel)
            }
        }
    }
}

@Preview
@Composable
fun MainScreenPreview() {
    val viewModel = AppViewModel()
    viewModel.toggleDarkMode(false)

    SpendWiseTheme(viewModel = viewModel) {
        MainScreen(
            onLogin = {},
            onLogout = {}
        )
    }
}