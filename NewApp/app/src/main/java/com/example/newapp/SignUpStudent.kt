package com.example.newapp

import android.R
import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class SignUpStudent : AppCompatActivity() {

    private lateinit var dropdown:Spinner
    private lateinit var dropdownProgram:Spinner
    private lateinit var database: DatabaseReference
    private lateinit var regbtn: Button
    private lateinit var fireauth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.newapp.R.layout.activity_sign_up_student)

        supportActionBar?.hide()

        dropdown = findViewById(com.example.newapp.R.id.spinner_programs)
        dropdownProgram = findViewById(com.example.newapp.R.id.spinner_degree)
        database = Firebase.database.reference
        regbtn = findViewById(com.example.newapp.R.id.regbtnStudent)


        var degree = ""
        var program = ""


        //FUNCTION FOR THE "REGISTER" BUTTON

        regbtn.setOnClickListener {
            val fname = findViewById<EditText>(com.example.newapp.R.id.fnamestudent).text.toString()
            val lname = findViewById<EditText>(com.example.newapp.R.id.lnamestudent).text.toString()
            val university = findViewById<EditText>(com.example.newapp.R.id.University).text.toString()
            val rollno = findViewById<EditText>(com.example.newapp.R.id.rollnoStudent).text.toString()

            if(fname.isNotEmpty() && lname.isNotEmpty() && university.isNotEmpty() && degree.isNotEmpty() && program.isNotEmpty()){
                val email = intent.getStringExtra("email").toString()
                val pass = intent.getStringExtra("pass").toString()

                fireauth = FirebaseAuth.getInstance()

                val stud = Student(email , fname , lname , university , degree , program , rollno)

                val ProgressDial = ProgressDialog(this@SignUpStudent)

                ProgressDial.setMessage("Your account is being created...")
                ProgressDial.setCancelable(false)
                ProgressDial.show()


                saveStudentData(stud , ProgressDial)
            }
            else{
                Toast.makeText(this , "Please fill in all the fields!" , Toast.LENGTH_SHORT).show()
            }

        }


        //FUNCTIONS TO HANDLE THE DROP DOWN MENUS AND GRAB VALUES FROM THEM

        val adapter = ArrayAdapter.createFromResource(
            this,
            com.example.newapp.R.array.languages,
            R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(R.layout.simple_spinner_item)
        dropdown.setAdapter(adapter)


        val adapter1 = ArrayAdapter.createFromResource(
            this,
            com.example.newapp.R.array.programs,
            R.layout.simple_spinner_item
        )
        adapter1.setDropDownViewResource(R.layout.simple_spinner_item)
        dropdownProgram.setAdapter(adapter1)






        ArrayAdapter.createFromResource(
            this,
            com.example.newapp.R.array.languages,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            dropdown.adapter = adapter
        }

        ArrayAdapter.createFromResource(
            this,
            com.example.newapp.R.array.programs,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            dropdownProgram.adapter = adapter
        }

        dropdown.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: android.view.View?,
                position: Int,
                id: Long
            ) {
                // Get the selected item from the spinner
                degree = parent.getItemAtPosition(position).toString()
                Toast.makeText(this@SignUpStudent , parent.getItemAtPosition(position).toString() , Toast.LENGTH_SHORT).show()
                // Do something with the selected item
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Do nothing
            }
        }

        dropdownProgram.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: android.view.View?,
                position: Int,
                id: Long
            ) {
                // Get the selected item from the spinner
                program = parent.getItemAtPosition(position).toString()
                Toast.makeText(this@SignUpStudent , parent.getItemAtPosition(position).toString() , Toast.LENGTH_SHORT).show()
                // Do something with the selected item
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Do nothing
            }
        }




    }

    data class Student( val mail: String = "" , val firstName: String = "", val lastName: String = "", val university: String = "" , val program: String = "" , val degree: String = "" , val email: String = "", val rollNumber: String = "")

    //Function to save the student data

    private fun saveStudentData(student: Student , progress: ProgressDialog) {
        // Generate a unique key for each student to store in the database
        val currentUserID = FirebaseAuth.getInstance().currentUser!!.uid
        val studentRef: DatabaseReference = FirebaseDatabase.getInstance().reference.child("Profiles")

        val usermap = HashMap<String , Any>()

        usermap["uid"] = currentUserID
        usermap["fullname"] = student.firstName + " " + student.lastName
        usermap["email"] = student.mail
        usermap["university"] = student.university
        usermap["degree"] = student.degree
        usermap["program"] = student.program
        usermap["rollno"] = student.email
        usermap["image"] = "https://firebasestorage.googleapis.com/v0/b/smartintern-73a11.appspot.com/o/Default%20Images%2Fprofile_picture.png?alt=media&token=5c5ca692-200f-452e-aa35-786ccbaabd64"
        usermap["role"] = "student"
        usermap["approved"] = "pending"


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