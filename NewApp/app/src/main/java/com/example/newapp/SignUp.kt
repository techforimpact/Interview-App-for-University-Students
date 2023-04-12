package com.example.newapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


class SignUp : AppCompatActivity() {

    private lateinit var fire:FirebaseAuth
    private  lateinit var loginLink:TextView
    private lateinit var signUpbtn:Button
    private lateinit var radioarr:RadioGroup
    private lateinit var radiobtn:RadioButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        fire = FirebaseAuth.getInstance()
        loginLink = findViewById(R.id.login_link)
        signUpbtn = findViewById(R.id.signUpbutton)
        radioarr = findViewById(R.id.radioGroup)

        supportActionBar?.hide()


        signUpbtn.setOnClickListener {
            val email = findViewById<TextView>(R.id.emailEt).text.toString()
            val pass = findViewById<TextView>(R.id.passSignUp).text.toString()
            val confirmpass = findViewById<TextView>(R.id.confirmPassEt).text.toString()

            if (email.isNotEmpty() && pass.isNotEmpty() && confirmpass.isNotEmpty() && radiobtn.isChecked) {
                if (pass.equals(confirmpass)) {
                    fire.createUserWithEmailAndPassword(email, pass).addOnCompleteListener {
                        if (it.isSuccessful) {

                            if(radiobtn.text == "As a Student") {
                                val intent = Intent(this, SignUpStudent::class.java).apply {
                                    putExtra("email" , email)
                                    putExtra("pass" , pass)
                                }
                                startActivity(intent)
                                finish()
                            }
                            else if (radiobtn.text == "As a University HR") {
                                val intent = Intent(this , SignUpHR::class.java).apply {
                                    putExtra("email" , email)
                                    putExtra("pass" , pass)
                                }
                                startActivity(intent)
                                finish()
                            }
                            else{
                                val intent = Intent(this , SignUpRecruiter::class.java).apply {
                                    putExtra("email" , email)
                                    putExtra("pass" , pass)
                                }
                                startActivity(intent)
                                finish()
                            }
                        } else {
                            Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(this, "Passwords are not matching", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show()
            }

        }

        loginLink.setOnClickListener {
            var intent = Intent(this, SignIn::class.java)
            startActivity(intent)
        }


    }

    fun checkRadio(v: View?) {
        val radioId: Int = radioarr.checkedRadioButtonId
        radiobtn = findViewById(radioId)
        Toast.makeText(
            this, "Selected Radio Button: " + radiobtn.text,
            Toast.LENGTH_SHORT
        ).show()
    }


    private suspend fun isUserRegistered(email: String): Boolean {
        val auth = FirebaseAuth.getInstance()
        return try {
            auth.fetchSignInMethodsForEmail(email).await().toString().isNotEmpty()
        } catch (e: FirebaseAuthInvalidUserException) {
            false
        }
    }

    private suspend fun isEmailRegistered(email: String): Boolean {
        val auth = FirebaseAuth.getInstance()
        return try {
            auth.createUserWithEmailAndPassword(email, "password").await()
            false
        } catch (e: FirebaseAuthUserCollisionException) {
            true
        }
    }

}