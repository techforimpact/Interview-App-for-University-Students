package com.example.newapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.newapp.Fragments_Recruiter.*
import com.example.newapp.Model.Recruiter
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class RecruiterFeed : AppCompatActivity() {

    private lateinit var student: Recruiter


    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.menu_show_posts -> {
                moveToFragement(HomeRecruiterFragment(), student)
                return@OnNavigationItemSelectedListener true
            }

            R.id.menu_add_post_recruiter -> {
                val intent = Intent(this, PostJobActivity::class.java)
                startActivity(intent)
                return@OnNavigationItemSelectedListener true
            }

            R.id.menu_recruiter_profile -> {
                moveToFragement(RecruiterProfileFragment(), student)
                return@OnNavigationItemSelectedListener true
            }
        }

        false
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recruiter_feed)

        supportActionBar?.hide()


        supportActionBar?.setDisplayShowTitleEnabled(false)

        val navView: BottomNavigationView = findViewById(R.id.bottom_navigation_view_recruiter)

        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)



        val uid = intent.getStringExtra("uid")

        val query = FirebaseDatabase.getInstance().getReference()
            .child("Profiles")

        var userFound = false

        query.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                for (snap in snapshot.children) {
                    val user = snap.getValue(Recruiter::class.java)

                    // Check if job matches search query
                    if (user != null && user.getUId() == uid)
                    {
                        student = user
                        userFound = true
                        break
                    }
                }

                if (userFound) {
                    moveToFragement(HomeRecruiterFragment() , student)
                } else {
                    Toast.makeText(this@RecruiterFeed , "You are logged out because your id has not been found." , Toast.LENGTH_SHORT).show()
                    var intent = Intent(this@RecruiterFeed , SignIn::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    finish()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })

    }

    private fun moveToFragement(fragement: Fragment, student: Recruiter){
        val bundle = Bundle()
        bundle.putParcelable("recruiter", student)
        fragement.arguments = bundle


        val frangmenttrans = supportFragmentManager.beginTransaction()
        frangmenttrans.replace(R.id.recruiter_fragment_container , fragement)
        frangmenttrans.commit()
    }

}

