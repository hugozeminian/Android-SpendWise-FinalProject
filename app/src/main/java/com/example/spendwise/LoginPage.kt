package com.example.spendwise

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import com.example.spendwise.ui.theme.AppViewModel

@Composable
fun LoginPage(
    viewModel: AppViewModel,
    onLoginSuccess: () -> Unit,
    onNavigateToRegister: () -> Unit) {
    var userEmail by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val context = LocalContext.current.applicationContext
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 26.dp)
            .wrapContentHeight(align = Alignment.CenterVertically),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "App Logo",
                    modifier = Modifier
                    .fillMaxWidth()
                .height(200.dp)
                .padding(bottom = 5.dp)
        )
//        Text(
//            text = stringResource(id= R.string.title),
//            style = TextStyle( fontFamily = FontFamily(Font(R.font.montserrat_regular)),
//                fontSize = 40.sp,
//                fontWeight = FontWeight.Bold),
//            modifier = Modifier.padding(bottom = 8.dp)
//        )
//        Text(
//            text = stringResource(id = R.string.subtitle),
//            style = TextStyle( fontFamily = FontFamily(Font(R.font.montserrat_regular)),
//                fontSize = 20.sp,
//                color = Color.Gray),
//            modifier = Modifier.padding(bottom = 24.dp)
//        )
        Card(
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier
                .padding(vertical = 24.dp)
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                OutlinedTextField(
                    value = userEmail,
                    onValueChange = { userEmail = it },
                    label = { Text(text = stringResource(id = R.string.email)) },
                    shape = RoundedCornerShape(20.dp),
                    colors = TextFieldDefaults.colors(
                        focusedLabelColor = Color(0xFF006A68),
                        unfocusedLabelColor = Color(0xFF006A68),
                        focusedIndicatorColor = Color(0xFF006A68),
                        unfocusedIndicatorColor = Color(0xFF006A68)
                    ),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Email,
                            contentDescription = "Email",
                            tint = Color(0xFF006A68)
                        )
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text(text = stringResource(id = R.string.password)) },
                    shape = RoundedCornerShape(20.dp),
                    colors = TextFieldDefaults.colors(
                        focusedLabelColor = Color(0xFF006A68),
                        unfocusedLabelColor = Color(0xFF006A68),
                        focusedIndicatorColor = Color(0xFF006A68),
                        unfocusedIndicatorColor = Color(0xFF006A68)
                    ),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = "Password",
                            tint = Color(0xFF006A68)
                        )
                    },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth()
                )
                Button(
                    onClick = {
                        if (authenticate(userEmail, password, viewModel)) {
                            onLoginSuccess()
                            Toast.makeText(context, context.getString(R.string.login_success), Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(context, context.getString(R.string.login_fail), Toast.LENGTH_SHORT).show()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(Color(0xFF006A68)),
                    contentPadding = PaddingValues(
                        start = 60.dp,
                        end = 60.dp,
                        top = 8.dp,
                        bottom = 8.dp
                    ),
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 24.dp)
                ) {
                    Text(text = stringResource(id = R.string.btn_login), fontSize = 22.sp)
                }
            }
        }

        Text(
            text = stringResource(id = R.string.text_register),
            style = TextStyle(
                fontSize = 16.sp,
                color = Color.Gray,
                textDecoration = TextDecoration.Underline
            ),
            modifier = Modifier.clickable {
                onNavigateToRegister()
            }
        )
    }
}
private fun authenticate(userEmail: String, password: String, viewModel: AppViewModel): Boolean {
    val user = viewModel.GetUsers().find { u -> u.email == userEmail && u.password == password }
    if(user != null){
        viewModel.SetLoggedUser(user)
        return true
    }
    return false
}