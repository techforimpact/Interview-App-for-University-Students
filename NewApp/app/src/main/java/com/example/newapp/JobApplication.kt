package com.example.newapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.newapp.Model.Student
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class JobApplication : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_job_application)

        supportActionBar?.hide()


        val uid = intent.getStringExtra("uid").toString()
        val jobID = intent.getStringExtra("jobUid").toString()





        val userRef = FirebaseDatabase.getInstance().getReference("Profiles").child(uid)

        userRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val student = snapshot.getValue(Student::class.java)





            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })



    }




}