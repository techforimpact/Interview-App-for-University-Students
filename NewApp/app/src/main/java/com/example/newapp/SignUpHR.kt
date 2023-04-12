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


class SignUpHR : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    private lateinit var regbtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.newapp.R.layout.activity_sign_up_hr)

        supportActionBar?.hide()

        database = Firebase.database.reference
        regbtn = findViewById(com.example.newapp.R.id.registerButton_hr)


        //FUNCTION FOR THE "REGISTER" BUTTON

        regbtn.setOnClickListener {
            val name = findViewById<EditText>(com.example.newapp.R.id.univeristy_name).text.toString()
            val bio = findViewById<EditText>(com.example.newapp.R.id.university_bio).text.toString()
            val empno = findViewById<EditText>(com.example.newapp.R.id.university_emp_no).text.toString()
            val contact = findViewById<EditText>(com.example.newapp.R.id.contactno_hr).text.toString()
            val location = findViewById<EditText>(com.example.newapp.R.id.hr_location).text.toString()


            if(name.isNotEmpty() && bio.isNotEmpty() && empno.isNotEmpty() && contact.isNotEmpty() && location.isNotEmpty()){
                val email = intent.getStringExtra("email").toString()

                val ProgressDial = ProgressDialog(this)


                ProgressDial.setMessage("Your account is being created...")
                ProgressDial.setCancelable(false)
                ProgressDial.show()

                val hr = HR(email , name , bio , empno, contact)
                saveHRData(hr , ProgressDial)



            }
            else{
                Toast.makeText(this , "Please fill in all the fields!" , Toast.LENGTH_SHORT).show()
            }

        }


    }

    data class HR( val mail: String = "" , val universityName: String = "", val universityInfo: String = "", val universityEmployeeNumber: String = "" , val universityContact: String = "" )



    //Function to save the student data

    private fun saveHRData(student: HR, progress: ProgressDialog) {
        // Generate a unique key for each student to store in the database
        val currentUserID = FirebaseAuth.getInstance().currentUser!!.uid
        val studentRef: DatabaseReference = FirebaseDatabase.getInstance().reference.child("Profiles")


        val usermap = HashMap<String , Any>()

        usermap["uid"] = currentUserID
        usermap["name"] = student.universityName
        usermap["bio"] = student.universityInfo
        usermap["employeeNo"] = student.universityEmployeeNumber
        usermap["contact"] = student.universityContact
        usermap["email"] = student.mail
        usermap["image"] = "https://firebasestorage.googleapis.com/v0/b/smart-intern-5c025.appspot.com/o/Default%20Images%2Fprofile_picture.png?alt=media&token=75c41c40-5c8c-4ac8-862b-5040c7aa2464"
        usermap["role"] = "hr"


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