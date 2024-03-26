package com.example.spendwise

import android.widget.Toast
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.spendwise.model.User
import com.example.spendwise.ui.theme.AppViewModel

@Composable
fun RegisterPage(onCreatingAccount: () -> Unit, viewModel: AppViewModel) {
    var fullName by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    val context = LocalContext.current.applicationContext

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 26.dp)
            .verticalScroll(rememberScrollState())
            .wrapContentHeight(align = Alignment.CenterVertically),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id= R.string.title),
            style = TextStyle(fontFamily = FontFamily(Font(R.font.montserrat_regular)),
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = stringResource(id= R.string.subtitle),
            style = TextStyle(fontFamily = FontFamily(Font(R.font.montserrat_regular)),
                fontSize = 20.sp,
                color = Color.Gray),
            modifier = Modifier.padding(bottom = 24.dp)
        )
        Card(
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier
                .padding(vertical = 24.dp)
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {

                OutlinedTextField(
                    value = fullName,
                    onValueChange = { fullName = it },
                    label = { Text(text = stringResource(id = R.string.register_Fullname)) },
                    shape = RoundedCornerShape(20.dp),
                    colors = TextFieldDefaults.colors(
                        focusedLabelColor = Color(0xFF006A68),
                        unfocusedLabelColor = Color(0xFF006A68),
                        focusedIndicatorColor = Color(0xFF006A68),
                        unfocusedIndicatorColor = Color(0xFF006A68)
                    ),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "FullName",
                            tint = Color(0xFF006A68)
                        )
                    },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = username,
                    onValueChange = { username = it },
                    label = { Text(text = stringResource(id = R.string.register_Username)) },
                    shape = RoundedCornerShape(20.dp),
                    colors = TextFieldDefaults.colors(
                        focusedLabelColor = Color(0xFF006A68),
                        unfocusedLabelColor = Color(0xFF006A68),
                        focusedIndicatorColor = Color(0xFF006A68),
                        unfocusedIndicatorColor = Color(0xFF006A68)
                    ),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.FavoriteBorder,
                            contentDescription = "Username",
                            tint = Color(0xFF006A68)
                        )
                    },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text(stringResource(id = R.string.register_Email)) },
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
                            contentDescription = "EMail",
                            tint = Color(0xFF006A68)
                        )
                    },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text(stringResource(id = R.string.register_Password)) },
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
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    label = { Text(stringResource(id = R.string.register_ConfirmPassword))},
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
                            contentDescription = "Confirm Password",
                            tint = Color(0xFF006A68)
                        )
                    },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth()
                )
                Button(
                    onClick = {
                        val errorMessage = authenticate(username, password, confirmPassword, email)
                        if (errorMessage == null) {
                            viewModel.AddUser(User(fullName, username, email, password));
                            onCreatingAccount()
                            Toast.makeText(context, "Account created successfully", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
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
                    Text(text = stringResource(id = R.string.btn_createAccount), fontSize = 22.sp)
                }
            }
        }
        Text(
            text = stringResource(id = R.string.backToLogin),
            style = TextStyle(
                fontSize = 16.sp,
                color = Color.Gray,
                textDecoration = TextDecoration.Underline
            ),
            modifier = Modifier.clickable {
                onCreatingAccount.invoke()
            }
        )
    }
}

private fun authenticate(username: String, password: String, confirmPassword: String, email: String): String? {

    val validUsername = "user"
    val validPassword = "1234"

    if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || email.isEmpty()) {
        return "Fields are required"
    }

    if (password != confirmPassword) {
        return "Passwords do not match"
    }

    val emailRegex = "^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})"
    if (!email.matches(emailRegex.toRegex())) {
        return "Email is in the wrong format"
    }

    return null
}