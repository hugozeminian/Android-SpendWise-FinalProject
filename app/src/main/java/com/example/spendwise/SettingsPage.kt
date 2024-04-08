package com.example.spendwise

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.ui.tooling.preview.Preview
import com.example.spendwise.ui.theme.AppViewModel


@Composable
fun SettingPage(
    viewModel: AppViewModel,
    onLogout: () -> Unit,
    testPage: () -> Unit
) {
    var username by remember { mutableStateOf("") }
    var darkMode by remember { mutableStateOf(viewModel.uiState.value.isDarkMode) }
    val context = LocalContext.current.applicationContext

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "Hello, " + viewModel.GetLoggedUser().username,
            style = TextStyle(
                fontFamily = FontFamily(Font(R.font.montserrat_regular)),
                fontSize = 32.sp,
            ),
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Spacer(modifier = Modifier.width(15.dp))

        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = stringResource(id = R.string.setting_msg),
                style = TextStyle(
                    fontFamily = FontFamily(Font(R.font.montserrat_regular)),
                    fontSize = 25.sp,
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                TextField(
                    value = username,
                    label = { Text(text = "Username", style = TextStyle(
                        fontFamily = FontFamily(Font(R.font.montserrat_regular)),
                        fontSize = 25.sp,
                    )) },
                    onValueChange = { value -> username = value },
                    modifier = Modifier.weight(1f),
                    textStyle = TextStyle(fontSize = 22.sp),
                )

                Spacer(modifier = Modifier.width(8.dp))

                IconButton(onClick = {
                    if (username.isNotEmpty()) {
                        viewModel.ChangeUserName(username)
                        Toast.makeText(context, context.getString(R.string.edit_username), Toast.LENGTH_SHORT).show()
                        username = ""
                    } else {
                        Toast.makeText(context, context.getString(R.string.edit_warning), Toast.LENGTH_SHORT).show()
                    }
                }) {
                    Icon(
                        Icons.Default.FavoriteBorder,
                        tint = Color(0xFF006A68),
                        contentDescription = "Save"
                    )
                }

            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = stringResource(id = R.string.edit_darkMode),
                    style = TextStyle(
                        fontFamily = FontFamily(Font(R.font.montserrat_regular)),
                        fontSize = 25.sp,
                    )
                )

                Spacer(modifier = Modifier.weight(1f))

                Switch(checked = darkMode, onCheckedChange = { newValue ->
                    darkMode = newValue
                    viewModel.toggleDarkMode(newValue)
                })
            }

            Spacer(modifier = Modifier.height(16.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = stringResource(id = R.string.Logout),
                        style = TextStyle(
                            fontFamily = FontFamily(Font(R.font.montserrat_regular)),
                            fontSize = 25.sp,
                        )
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Icon(
                        imageVector = Icons.Default.ExitToApp,
                        contentDescription = "Logout",
                        tint = Color(0xFF006A68),
                        modifier = Modifier.clickable {

                            onLogout.invoke()
                        }
                    )
                }
        }

        UpdateButton(viewModel = viewModel)
        OpenTestPage(testPage)
    }
}

@Composable
fun UpdateButton(
    viewModel: AppViewModel
){
    Button(onClick = { viewModel.getData() }) {
        Text("Update")
    }
}

@Composable
fun OpenTestPage(
    open: ()-> Unit
){
    Button(onClick = open) {
        Text("Open test page")
    }
}

@Preview
@Composable
fun ShowSettingsPage(){
    SettingPage(
        AppViewModel(),
        {},
        {}
    )
}