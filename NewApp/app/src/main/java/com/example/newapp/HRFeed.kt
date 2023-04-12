package com.example.newapp

import android.content.Context
import android.content.Intent
import android.graphics.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.example.newapp.Fragments_HR.HomeHrFragment
import com.example.newapp.Fragments_HR.HrProfileFragment
import com.example.newapp.Fragments_HR.PendingRequestFragment
import com.example.newapp.Fragments_Student.HomeStudentFragment
import com.example.newapp.Fragments_Student.NotificationStudentFragment
import com.example.newapp.Fragments_Student.ProfileStudentFragment
import com.example.newapp.Fragments_Student.SearchStudentFragment
import com.example.newapp.Model.HR
import com.example.newapp.Model.Student
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.tabs.TabLayout
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class HRFeed : AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var hr: HR

    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.menu_approved_student_list -> {
                moveToFragment(HomeHrFragment() , hr)
                return@OnNavigationItemSelectedListener true
            }
            R.id.menu_pending_student_list -> {
                moveToFragment(PendingRequestFragment() , hr)
                return@OnNavigationItemSelectedListener true
            }
            R.id.menu_university_profile -> {
                moveToFragment(HrProfileFragment() , hr)
                return@OnNavigationItemSelectedListener true
            }
        }

        false
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hrfeed)

/*        val toolbar: Toolbar = findViewById(R.id.toolbar)

        setSupportActionBar(toolbar)*/
        supportActionBar?.hide()

        supportActionBar?.setDisplayShowTitleEnabled(false)


        bottomNavigationView = findViewById(R.id.bottom_navigation_view_hr)

        // Set the initial menu item as selected
        bottomNavigationView.selectedItemId = R.id.menu_approved_student_list
        val uid = intent.getStringExtra("uid")

        val query = FirebaseDatabase.getInstance().getReference()
            .child("Profiles")

        var userFound = false

        query.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                for (snap in snapshot.children) {
                    val user = snap.getValue(HR::class.java)

                    // Check if job matches search query
                    if (user != null && user.getUId() == uid)
                    {
                        hr = user
                        userFound = true
                        break
                    }
                }

                if (userFound) {
                    moveToFragment(HomeHrFragment() , hr)
                } else {
                    Toast.makeText(this@HRFeed , "You are logged out because your id has not been found." , Toast.LENGTH_SHORT).show()
                    var intent = Intent(this@HRFeed , SignIn::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    finish()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })


        bottomNavigationView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)


    }

    private fun moveToFragment(fragment: Fragment, hr: HR) {
        val bundle = Bundle()
        bundle.putParcelable("hr", hr)
        fragment.arguments = bundle

        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.hr_fragment_container, fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }


}
