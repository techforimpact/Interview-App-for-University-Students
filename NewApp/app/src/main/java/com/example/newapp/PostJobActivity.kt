package com.example.newapp

import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.newapp.Model.Recruiter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import java.text.SimpleDateFormat
import java.util.*


class PostJobActivity : AppCompatActivity() {

    private val myCalendar = Calendar.getInstance()
    private lateinit var editText: EditText
    private lateinit var details: EditText
    private lateinit var postbtn: TextView
    private lateinit var recruiterImage: CircleImageView

    private var recruiter: Recruiter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_job)

        supportActionBar?.hide()

        val currentUser = FirebaseAuth.getInstance().currentUser
        val profileRef = FirebaseDatabase.getInstance().getReference("Profiles").child(currentUser!!.uid)

        profileRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val recru = snapshot.getValue(Recruiter::class.java)
                    recruiter = recru
                } else {
                    // Profile data not found
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })

        postbtn = findViewById(R.id.add_post_post_btn)
        val closeBtn = findViewById<ImageView>(R.id.add_post_close_btn)

        recruiterImage = findViewById(R.id.add_post_recruiter_image)
        Picasso.get().load(recruiter?.getImage()).placeholder(R.drawable.profile_picture).into(recruiterImage)

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
                var job = Job(recruiter!!.getImage() , recruiter!!.getName() ,title , category , courses , seats , location , deadline , details)

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

        closeBtn.setOnClickListener{
            this.finish()
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

    data class Job(val recruiterImage: String = "" , val recruiterName: String = "", val title: String = "" , val category: String = "" , val courses: String = "" , val seats: String = "" , val location: String = "" , val deadline: String = "" , val details: String = "")


    private fun saveJobData(job: Job, progress: ProgressDialog) {
        // Generate a unique key for each student to store in the database
        val currentUserID = FirebaseAuth.getInstance().currentUser!!.uid
        val jobsRef = FirebaseDatabase.getInstance().getReference("Jobs")

        val newJobRef = jobsRef.push()

        val usermap = HashMap<String , Any>()

        usermap["jobUid"] = newJobRef.key.toString()
        usermap["uid"] = currentUserID
        usermap["recruiterImage"] = job.recruiterImage
        usermap["recruiterName"] = job.recruiterName
        usermap["title"] = job.title.lowercase()
        usermap["category"] = job.category
        usermap["courses"] = job.courses
        usermap["seats"] = job.seats
        usermap["location"] = job.location
        usermap["deadline"] = job.deadline
        usermap["details"] = job.details
        usermap["status"] = "active"


        newJobRef.setValue(usermap)
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
                    progress.dismiss()
                }
            }

    }


}