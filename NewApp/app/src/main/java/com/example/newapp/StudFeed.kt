package com.example.newapp

import android.content.Context
import android.content.Intent
import android.graphics.*
import android.graphics.ColorSpace.Model
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import com.example.newapp.Fragments_Student.HomeStudentFragment
import com.example.newapp.Fragments_Student.NotificationStudentFragment
import com.example.newapp.Fragments_Student.ProfileStudentFragment
import com.example.newapp.Fragments_Student.SearchStudentFragment
import com.example.newapp.Model.Student
import com.example.newapp.Model.User
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class StudFeed : AppCompatActivity() {

    private lateinit var student: Student

    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.menu_job_postings -> {
                moveToFragment(HomeStudentFragment() , student)
                return@OnNavigationItemSelectedListener true
            }
            R.id.menu_student_search -> {
                moveToFragment(SearchStudentFragment() , student)
                return@OnNavigationItemSelectedListener true
            }
            R.id.menu_personal_profile -> {
                moveToFragment(ProfileStudentFragment() , student)
                return@OnNavigationItemSelectedListener true
            }
            R.id.menu_notifications -> {
                moveToFragment(NotificationStudentFragment() , student)
                return@OnNavigationItemSelectedListener true
            }
        }

        false
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stud_feed)

/*        val toolbar: Toolbar = findViewById(R.id.toolbar)

        setSupportActionBar(toolbar)*/
        supportActionBar?.hide()


        supportActionBar?.setDisplayShowTitleEnabled(false)



        val uid = intent.getStringExtra("uid")

        val query = FirebaseDatabase.getInstance().getReference()
            .child("Profiles")

        var userFound = false

        query.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                for (snap in snapshot.children) {
                    val user = snap.getValue(Student::class.java)

                    // Check if job matches search query
                    if (user != null && user.getUId() == uid)
                    {
                        student = user
                        userFound = true
                        break
                    }
                }

                if (userFound) {
                    moveToFragment(HomeStudentFragment() , student)
                } else {
                    Toast.makeText(this@StudFeed , "You are logged out because your id has not been found." , Toast.LENGTH_SHORT).show()
                    var intent = Intent(this@StudFeed , SignIn::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    finish()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })



        val navView: BottomNavigationView = findViewById(R.id.bottom_navigation_view)

        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)






    }


    private fun moveToFragment(fragment: Fragment, student: Student) {
        val bundle = Bundle()
        bundle.putParcelable("student", student)
        fragment.arguments = bundle

        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.student_fragment_container, fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }
}
