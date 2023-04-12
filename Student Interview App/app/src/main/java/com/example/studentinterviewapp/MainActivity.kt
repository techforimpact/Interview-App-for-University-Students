package com.example.studentinterviewapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var rememberMe: Switch
    private lateinit var loginButton: Button
    private lateinit var createAccount: TextView
    private lateinit var forgetPassword: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Remove the title bar
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        supportActionBar?.hide()

// Remove the status bar
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        setContentView(R.layout.activity_main)

        email = findViewById(R.id.email)
        password = findViewById(R.id.password)
        rememberMe = findViewById(R.id.remember_me)
        loginButton = findViewById(R.id.login_button)
        createAccount = findViewById(R.id.create_account)
        forgetPassword = findViewById(R.id.forget_password)

        loginButton.setOnClickListener {
            // Add your login logic here
        }

//        createAccount.setOnClickListener {
//            val intent = Intent(this, CreateAccountActivity::class.java)
//            startActivity(intent)
//        }

//        forgetPassword.setOnClickListener {
//            val intent = Intent(this, ForgetPasswordActivity::class.java)
//            startActivity(intent)
//        }
    }
}
