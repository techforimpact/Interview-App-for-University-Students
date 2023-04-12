package com.example.newapp

import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.*


class PostJobActivity : AppCompatActivity() {

    private val myCalendar = Calendar.getInstance()
    private lateinit var editText: EditText
    private lateinit var details: EditText
    private lateinit var postbtn: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_job)

        supportActionBar?.hide()

        postbtn = findViewById(R.id.add_post_post_btn)

        postbtn.setOnClickListener{
            val title = findViewById<EditText>(R.id.add_post_title).text.toString()
            val courses = findViewById<EditText>(R.id.add_post_courses).text.toString()
            val category = findViewById<EditText>(R.id.add_post_Category).text.toString()
            val deadline = findViewById<EditText>(R.id.add_post_deadline).text.toString()
            val seats = findViewById<EditText>(R.id.add_post_seats).text.toString()
            val details = findViewById<EditText>(R.id.add_post_details).text.toString()
            val location = findViewById<EditText>(R.id.add_post_location).text.toString()

            if(title.isNotEmpty() && courses.isNotEmpty() && category.isNotEmpty() && deadline.isNotEmpty() && seats.isNotEmpty() && details.isNotEmpty() && location.isNotEmpty())
            {
                var job = Job(title , category , courses , seats , location , deadline , details)

                val ProgressDial = ProgressDialog(this@PostJobActivity)

                saveJobData(job , ProgressDial)
            }
            else{
                Toast.makeText(this , "Please fill in all the fields!" , Toast.LENGTH_SHORT).show()
            }

        }



        editText = findViewById(R.id.add_post_deadline)
        val date = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, monthOfYear)
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateLabel()
        }

        editText.setOnClickListener {
            DatePickerDialog(
                this@PostJobActivity,
                date,
                myCalendar.get(Calendar.YEAR),
                myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }


        details = findViewById(R.id.add_post_details)

        details.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                details.height = (details.getLineCount() + 1) * details.getLineHeight()
            }
        })


    }
    private fun updateLabel() {
        val myFormat = "MM/dd/yy"
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        editText.setText(sdf.format(myCalendar.time))
    }

    data class Job(val title: String = "" , val category: String = "" , val courses: String = "" , val seats: String = "" , val location: String = "" , val deadline: String = "" , val details: String = "")


    private fun saveJobData(job: Job, progress: ProgressDialog) {
        // Generate a unique key for each student to store in the database
        val currentUserID = FirebaseAuth.getInstance().currentUser!!.uid
        val studentRef: DatabaseReference = FirebaseDatabase.getInstance().reference.child("Jobs")

        val usermap = HashMap<String , Any>()

        usermap["uid"] = currentUserID
        usermap["title"] = job.title.lowercase()
        usermap["category"] = job.category
        usermap["courses"] = job.courses
        usermap["seats"] = job.seats
        usermap["location"] = job.location
        usermap["deadline"] = job.deadline
        usermap["details"] = job.details
        usermap["status"] = "active"


        studentRef.child(currentUserID).setValue(usermap)
            .addOnCompleteListener{task ->
                if (task.isSuccessful){
                    progress.dismiss()
                    Toast.makeText(this , "Post has been Successfully created!" , Toast.LENGTH_SHORT).show()

                    finish()
                }
                else {
                    // Error occurred while saving data
                    val message = task.exception!!.toString()
                    Toast.makeText(this , "Error saving post data: ${message}" , Toast.LENGTH_SHORT).show()
                    FirebaseAuth.getInstance().signOut()
                    progress.dismiss()
                }
            }

    }


}