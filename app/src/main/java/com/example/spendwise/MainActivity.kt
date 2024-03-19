package com.example.spendwise

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.spendwise.ui.theme.SpendWiseTheme

class MainActivity : ComponentActivity() {
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


//====== Add your new pages here and give them a name ===========
enum class CurrentScreen() {
    StartScreen,
    ReportScreen
}

@Composable
fun MainScreen(
    navController: NavHostController = rememberNavController()
){
    NavHost(navController = navController, startDestination = CurrentScreen.StartScreen.name){

        //====== Each composable will call the functions inside its scope base on the route given ======
        composable(route = CurrentScreen.StartScreen.name){
            Column {
                Text("I am the main/first/start screen")
                Button(onClick = {navController.navigate(CurrentScreen.ReportScreen.name)}) {
                    Text("Report")
                }
            }
        }

        composable(route = CurrentScreen.ReportScreen.name){
            ReportScreen()
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
