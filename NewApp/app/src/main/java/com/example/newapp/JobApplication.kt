package com.example.newapp

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.newapp.Model.Applicant
import com.example.newapp.Model.Job
import com.example.newapp.Model.Student
import com.example.newapp.Model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import java.io.File

class JobApplication : AppCompatActivity() {

    private lateinit var closeBtn: Button
    private lateinit var applyBtn: Button
    private lateinit var jobTitle: TextView
    private lateinit var applicantName:EditText
    private lateinit var applicantEmail:EditText
    private lateinit var applicantContact:EditText
    private lateinit var applicantResume:EditText
    private lateinit var applicantCoverLetter:EditText

    private var student: Student? = null
    private lateinit var newApplicant: Applicant

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_job_application)

        supportActionBar?.hide()

        closeBtn = findViewById(R.id.job_app_close_btn)
        applyBtn = findViewById(R.id.job_app_apply_btn)
        jobTitle = findViewById(R.id.student_apply_job_title)
        applicantName = findViewById(R.id.student_apply_name)
        applicantEmail = findViewById(R.id.student_apply_email)
        applicantContact = findViewById(R.id.student_apply_contact)
        applicantResume = findViewById(R.id.student_apply_resume)
        applicantCoverLetter = findViewById(R.id.student_apply_cover_letter)

        newApplicant = Applicant()

        val uid = intent.getStringExtra("uid").toString()
        val jobID = intent.getStringExtra("jobUid").toString()


        val jobsRef = FirebaseDatabase.getInstance().getReference("Jobs")

        jobsRef.equalTo(jobID).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (jobSnap in snapshot.children) {
                    val job = jobSnap.getValue(Job::class.java)
                    if (job != null) {
                        jobTitle.text = job.getTitle()
                        newApplicant.setJobUid(job.getUid())
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })


        closeBtn.setOnClickListener{
            finish()
        }

        val userRef = FirebaseDatabase.getInstance().getReference("Profiles")
            .equalTo(uid)

        userRef.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for(user in snapshot.children)
                {
                    student = user.getValue(Student::class.java)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                //Student not retrieved
            }
        })


        applicantResume.setOnClickListener{
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "application/pdf"
            startActivityForResult(intent, FILE1_REQUEST_CODE)
        }


        applicantCoverLetter.setOnClickListener{
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "application/pdf"
            startActivityForResult(intent, FILE2_REQUEST_CODE)
        }



        applyBtn.setOnClickListener {

            val name = applicantName.text.toString()
            val email = applicantEmail.text.toString()
            val contact = applicantContact.text.toString()
            val resume = applicantResume.text.toString()
            val cover = applicantCoverLetter.text.toString()

            newApplicant.setStudentUid(student!!.getUId())
            newApplicant.setUniversity(student!!.getUniversity())
            newApplicant.setDegree(student!!.getDegree())
            newApplicant.setProgram(student!!.getProgram())
            newApplicant.setImage(student!!.getImage())


            if(name.isNotEmpty() && email.isNotEmpty() && contact.isNotEmpty() && resume.isNotEmpty() && cover.isNotEmpty())
            {
                newApplicant.setName(name)
                newApplicant.setEmail(email)
                newApplicant.setContact(contact)

                uploadFiles()
                applyData(newApplicant)
                Toast.makeText(this , "Your Application has been successfully submitted!" , Toast.LENGTH_SHORT).show()
            }
            else
            {
                Toast.makeText(this, "Please fill in all the fields!" , Toast.LENGTH_SHORT).show()
            }

        }




    }


    private fun applyData(applicant: Applicant)
    {

        val uid = FirebaseAuth.getInstance().uid.toString()

        val query = FirebaseDatabase.getInstance().getReference("Applicants")
            .child(uid)
            .child(applicant.getJobUid())
            .setValue(applicant)

    }


    private fun uploadFiles() {
        // Get the file paths from the EditText views
        val resume = applicantResume.text.toString()
        val coverLetter = applicantCoverLetter.text.toString()

        // Upload the files to Firebase Storage
        val resumeStorageReference = FirebaseStorage.getInstance().getReference("Resume")
        val coverLetterStorageReference = FirebaseStorage.getInstance().getReference("CoverLetter")

        resumeStorageReference.putFile(Uri.fromFile(File(resume)))
            .addOnSuccessListener {
                resumeStorageReference.downloadUrl.addOnSuccessListener { resumeUrl ->
                    newApplicant.setResume(resumeUrl.toString())
                }
            }

        coverLetterStorageReference.putFile(Uri.fromFile(File(coverLetter)))
            .addOnSuccessListener {
                coverLetterStorageReference.downloadUrl.addOnSuccessListener { coverLetterUrl ->
                    newApplicant.setCoverLetter(coverLetterUrl.toString())
                }
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK) {
            when (requestCode) {
                FILE1_REQUEST_CODE -> {
                    applicantResume.setText(data?.data.toString())
                }
                FILE2_REQUEST_CODE -> {
                    applicantCoverLetter.setText(data?.data.toString())
                }
            }
        }
    }

    companion object {
        private const val FILE1_REQUEST_CODE = 1
        private const val FILE2_REQUEST_CODE = 2
    }




}