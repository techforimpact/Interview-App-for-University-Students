package com.example.newapp

import android.app.Dialog
import android.app.ProgressDialog
import android.content.ContentValues.TAG
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.net.toUri
import androidx.lifecycle.lifecycleScope
import com.example.newapp.Model.*
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
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

    private var job: Job? = null
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


        val jobsRef = FirebaseDatabase.getInstance().getReference("Jobs").child(jobID)

        jobsRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    job = snapshot.getValue(Job::class.java)
                    if (job != null) {
                        jobTitle.text = job?.getTitle()
                        newApplicant.setJobUid(job?.getJobUid().toString())
                    }
                } else {
                    Log.e("Firebase", "Data not found for job ID: $jobID")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Firebase", "Error reading data: ${error.message}")
            }
        })


        closeBtn.setOnClickListener{
            finish()
        }

        val userRef = FirebaseDatabase.getInstance().getReference("Profiles")

        userRef.orderByChild("uid").equalTo(FirebaseAuth.getInstance().uid.toString()).limitToFirst(1).get().addOnSuccessListener { snapshot ->
            for (user in snapshot.children) {
                student = user.getValue(Student::class.java)
            }
        }.addOnFailureListener { exception ->
            Log.e("Firebase", "Error reading data: ${exception.message}")
        }


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
                newApplicant.setStatus("pending")
                newApplicant.setApproved(student!!.getApproved())

                uploadFiles()


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
            .child(applicant.getJobUid())
            .child(uid)
            .setValue(applicant)

        query.addOnSuccessListener {
            Toast.makeText(this , "Your Application has been successfully submitted!" , Toast.LENGTH_SHORT).show()
        }.addOnFailureListener { exception ->
            Toast.makeText(this , "Error: ${exception.message.toString()}" , Toast.LENGTH_SHORT).show()
        }

    }

    private fun createNotification() {

        val newNoti = Notification(
            student?.getUId().toString(),
            job?.getTitle().toString(),
            newApplicant.getStatus(),
            job?.getRecruiterImage().toString(),
            job?.getRecruiterName().toString(),
            job?.getJobUid().toString()
        )


        val notiRef = FirebaseDatabase.getInstance().getReference("Notifications")
        val query = notiRef.orderByChild("studentUid").equalTo(newApplicant.getStudentUid())
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var found = false
                for (notification in dataSnapshot.children) {
                    val noti = notification.getValue(Notification::class.java)
                    if (noti?.getJobUid() == newApplicant.getJobUid()) {
                        // Update the existing notification with the new notification's data
                        notiRef.child(notification.key!!).setValue(newNoti)
                        found = true
                        break
                    }
                }
                if (!found) {
                    // Push the new notification if no existing notification was found
                    notiRef.push().setValue(newNoti)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle error
            }
        })

    }

    private fun uploadFiles() {
        // Get the file paths from the EditText views
        val resumeUri = applicantResume.text.toString().toUri()
        val coverLetterUri = applicantCoverLetter.text.toString().toUri()


        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Please wait while your application is being submitted...")
        progressDialog.setCancelable(false)
        progressDialog.show()

        // Upload the files to Firebase Storage
        val resumeStorageReference = FirebaseStorage.getInstance().getReference("Jobs").child(job!!.getJobUid()).child(newApplicant.getStudentUid()).child("Resume")
        val coverLetterStorageReference = FirebaseStorage.getInstance().getReference("Jobs").child(job!!.getJobUid()).child(newApplicant.getStudentUid()).child("Cover Letter")

        // Use coroutines to wait for the upload tasks to complete
        lifecycleScope.launch {
            // Upload the resume file
            val resumeTask = resumeStorageReference.putFile(resumeUri)
            // Get the download URL of the resume file
            val resumeUrl = resumeTask.await().storage.downloadUrl.await()
            newApplicant.setResume(resumeUrl.toString())

            // Upload the cover letter file
            val coverLetterTask = coverLetterStorageReference.putFile(coverLetterUri)
            // Get the download URL of the cover letter file
            val coverLetterUrl = coverLetterTask.await().storage.downloadUrl.await()
            newApplicant.setCoverLetter(coverLetterUrl.toString())

            applyData(newApplicant)
            createNotification()
            progressDialog.dismiss()
            // Close the activity after the URLs of the uploaded files are stored
            finish()
        }.invokeOnCompletion { throwable ->
            if (throwable != null) {
                Log.e(TAG, "Failed to upload files: ${throwable.message}")
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
        const val TAG = "JobApplicationActivity"
        const val FILE1_REQUEST_CODE = 1
        const val FILE2_REQUEST_CODE = 2
    }

    private fun String.toUri(): Uri {
        return Uri.parse(this)
    }






}