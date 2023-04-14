package com.example.newapp

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class SignUpRecruiter : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    private lateinit var regbtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.newapp.R.layout.activity_sign_up_recruiter)

        supportActionBar?.hide()

        database = Firebase.database.reference
        regbtn = findViewById(com.example.newapp.R.id.registerButton_recruiter)


        //FUNCTION FOR THE "REGISTER" BUTTON

        regbtn.setOnClickListener {
            val name = findViewById<EditText>(com.example.newapp.R.id.company_name).text.toString()
            val bio = findViewById<EditText>(com.example.newapp.R.id.company_bio).text.toString()
            val location = findViewById<EditText>(com.example.newapp.R.id.company_location).text.toString()
            val contact = findViewById<EditText>(com.example.newapp.R.id.contactno_recruiter).text.toString()

            if(name.isNotEmpty() && bio.isNotEmpty() && location.isNotEmpty() && contact.isNotEmpty()){
                val email = intent.getStringExtra("email").toString()

                val hr = Recruiter(email , name, bio , location , contact)

                val ProgressDial = ProgressDialog(this@SignUpRecruiter)

                ProgressDial.setMessage("Your account is being created...")
                ProgressDial.setCancelable(false)
                ProgressDial.show()


                saveRecruiterData(hr , ProgressDial)

                Toast.makeText(this , "Account successfully created!" , Toast.LENGTH_SHORT).show()

                val intent = Intent(this , HRFeed::class.java)
                startActivity(intent)
                finish()
            }
            else{
                Toast.makeText(this , "Please fill in all the fields!" , Toast.LENGTH_SHORT).show()
            }

        }


    }

    data class Recruiter( val mail: String = "" , val companyName: String = "", val companyBio: String = "", val companyLocation: String = "" , val companyContact: String = "" )



    //Function to save the student data

    private fun saveRecruiterData(student: Recruiter, progress: ProgressDialog) {
        // Generate a unique key for each student to store in the database
        val currentUserID = FirebaseAuth.getInstance().currentUser!!.uid
        val studentRef: DatabaseReference = FirebaseDatabase.getInstance().reference.child("Profiles")

        val usermap = HashMap<String , Any>()

        usermap["uid"] = currentUserID
        usermap["name"] = student.companyName
        usermap["bio"] = student.companyBio
        usermap["location"] = student.companyLocation
        usermap["contact"] = student.companyContact
        usermap["email"] = student.mail
        usermap["image"] = "https://firebasestorage.googleapis.com/v0/b/smartintern-73a11.appspot.com/o/Default%20Images%2Fprofile_picture.png?alt=media&token=5c5ca692-200f-452e-aa35-786ccbaabd64"
        usermap["role"] = "recruiter"


        studentRef.child(currentUserID).setValue(usermap)
            .addOnCompleteListener{task ->
                if (task.isSuccessful){
                    progress.dismiss()
                    Toast.makeText(this , "Account has been Successfully created!" , Toast.LENGTH_SHORT).show()

                    var intent = Intent(this , SignIn::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    finish()
                }
                else {
                    // Error occurred while saving data
                    val message = task.exception!!.toString()
                    Toast.makeText(this , "Error saving student data: ${message}" , Toast.LENGTH_SHORT).show()
                    FirebaseAuth.getInstance().signOut()
                    progress.dismiss()
                }
            }

    }


}