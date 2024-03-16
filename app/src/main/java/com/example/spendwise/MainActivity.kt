package com.example.spendwise

import android.content.Intent
import android.hardware.lights.Light
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.spendwise.databinding.LoginPageBinding
import com.example.spendwise.ui.theme.SpendWiseTheme

class MainActivity : ComponentActivity() {
    private lateinit var binding: LoginPageBinding

    lateinit var username : EditText
    lateinit var password : EditText
    lateinit var loginButton: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = LoginPageBinding.inflate(layoutInflater)

        binding.loginButton.setOnClickListener(View.OnClickListener {
            if (binding.username.text.toString() == "user" && binding.password.text.toString() == "1234") {
                //Toast.makeText(this, "Login Successfull", Toast.LENGTH_SHORT).show()
                val activity = Intent(this, WelcomeActivity::class.java)
                startActivity(activity)

            } else {
                Toast.makeText(this, "Login Fail", Toast.LENGTH_SHORT).show()
            }
        })

        binding.txtCreateAccount.setOnClickListener {
            val activity = Intent(this, CreateAccount::class.java)
            startActivity(activity)
        }

        setContentView(binding.root)
    }

    @Preview
    @Composable
    fun LightThemePreview() {
        SpendWiseTheme(darkTheme = false) {
            SpendWiseMainApp()
        }
    }

    @Preview
    @Composable
    fun DarkThemePreview() {
        SpendWiseTheme(darkTheme = true) {
            SpendWiseMainApp()
        }
    }
    @Preview(showBackground = true)
    @Composable
    fun GreetingPreview() {
        SpendWiseTheme {
            SpendWiseMainApp()
        }
    }

    @Composable
    fun SpendWiseMainApp(modifier: Modifier = Modifier) {
        Text(
            text = stringResource(id = R.string.app_greetings),
            modifier = modifier
        )
    }}
