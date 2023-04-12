package com.example.practice2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.practice2.databinding.ActivitySignInBinding
import com.google.firebase.auth.FirebaseAuth


class SignUpActiivity : AppCompatActivity() {

    private lateinit var bind:ActivitySignInBinding
    private lateinit var fire:FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up_actiivity)


        bind = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(bind.root)

        fire = FirebaseAuth.getInstance()

        supportActionBar?.hide()

        bind.button.setOnClickListener{
            val email = bind.emailEt.text.toString()
            val pass = bind.passET.text.toString()
            val confirmpass = bind.confirmPassEt.text.toString()

            if(email.isNotEmpty() && pass.isNotEmpty() && confirmpass.isNotEmpty()){
                if(pass.equals(confirmpass)){
                    fire.createUserWithEmailAndPassword(email , pass).addOnCompleteListener{
                        if(it.isSuccessful){
                            val intent = Intent(this , SignIn::class.java)
                            startActivity(intent)
                        }
                        else{
                            Toast.makeText(this, it.exception.toString() , Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                else{
                    Toast.makeText(this, "Passwords are not matching" , Toast.LENGTH_SHORT).show()
                }
            }
            else{
                Toast.makeText(this, "Please fill all the fields" , Toast.LENGTH_SHORT).show()
            }

        }


    }



}