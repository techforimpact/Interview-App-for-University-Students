package com.example.newapp

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.newapp.Model.Recruiter
import com.example.newapp.Model.User
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class SignIn : AppCompatActivity() {

    private lateinit var fire: FirebaseAuth
    private  lateinit var login:Button
    private lateinit var createAccount:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)




        login = findViewById(R.id.logInButton)
        createAccount = findViewById<TextView>(R.id.signUpLink)

        fire = FirebaseAuth.getInstance()

        supportActionBar?.hide()


        createAccount.setOnClickListener{
            val intent = Intent(this , SignUp::class.java)
            startActivity(intent)
            finish()
        }

        login.setOnClickListener{
            val email = findViewById<EditText>(R.id.loginEmail).text.toString()
            val pass = findViewById<EditText>(R.id.loginPass).text.toString()

            if(email.isNotEmpty() && pass.isNotEmpty()) {

                val progressDialog = ProgressDialog(this)
                progressDialog.setMessage("Please wait while you are being logged in...")
                progressDialog.setCancelable(false)
                progressDialog.show()

                fire.signInWithEmailAndPassword(email, pass)
                    .addOnCompleteListener(this, OnCompleteListener { task ->
                        if (task.isSuccessful) {

                            val query = FirebaseDatabase.getInstance().getReference()
                                .child("Profiles")
                                .orderByChild("email")
                                .equalTo(email)

                            query.addListenerForSingleValueEvent(object: ValueEventListener {
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    if (snapshot.exists()) {
                                        val user = snapshot.children.first().getValue(User::class.java)
                                        if (user != null) {
                                            when (user.getRole()) {
                                                "student" -> {
                                                    val intent = Intent(this@SignIn, StudFeed::class.java).apply {
                                                        putExtra("uid" , user.getUId())
                                                    }

                                                    val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
                                                    val editor = sharedPreferences.edit()
                                                    editor.putString("userId", user.getUId())
                                                    editor.apply()

                                                    progressDialog.dismiss()

                                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                                                    startActivity(intent)
                                                    finish()
                                                }
                                                "hr" -> {
                                                    val intent = Intent(this@SignIn, HRFeed::class.java).apply {
                                                        putExtra("uid" , user.getUId())
                                                    }

                                                    val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
                                                    val editor = sharedPreferences.edit()
                                                    editor.putString("userId", user.getUId())
                                                    editor.apply()

                                                    progressDialog.dismiss()


                                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                                                    startActivity(intent)
                                                    finish()
                                                }
                                                "recruiter" -> {
                                                    val intent = Intent(this@SignIn, RecruiterFeed::class.java).apply {
                                                        putExtra("uid" , user.getUId())
                                                    }

                                                    val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
                                                    val editor = sharedPreferences.edit()
                                                    editor.putString("userId", user.getUId())
                                                    editor.apply()

                                                    progressDialog.dismiss()


                                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                                                    startActivity(intent)
                                                    finish()
                                                }
                                                else -> {
                                                    progressDialog.dismiss()
                                                    Toast.makeText(this@SignIn , "Sorry! no such user found in the database!" , Toast.LENGTH_SHORT).show()
                                                }
                                            }
                                        }
                                    } else {
                                        progressDialog.dismiss()
                                        Toast.makeText(this@SignIn, "Incorrect Email or Password", Toast.LENGTH_LONG).show()
                                    }
                                }

                                override fun onCancelled(error: DatabaseError) {
                                    progressDialog.dismiss()
                                }
                            })
                        } else {
                            progressDialog.dismiss()
                            Toast.makeText(this, "Incorrect Email or Password", Toast.LENGTH_LONG).show()
                        }
                    })

            }
            else{
                Toast.makeText(this@SignIn , "Please fill in all the fields" , Toast.LENGTH_SHORT).show()
            }
        }


    }

}