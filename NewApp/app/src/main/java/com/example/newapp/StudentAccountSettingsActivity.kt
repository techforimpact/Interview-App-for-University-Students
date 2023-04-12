package com.example.newapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import de.hdodenhof.circleimageview.CircleImageView

class StudentAccountSettingsActivity : AppCompatActivity() {

    private lateinit var closeBtn: ImageView
    private lateinit var saveBtn: ImageView
    private lateinit var profileImageChange: CircleImageView
    private lateinit var profileName: EditText
    private lateinit var profileUniversity: EditText
    private lateinit var profileRollno: EditText
    private lateinit var logoutBtn: Button
    private lateinit var deleteBtn: Button
    private lateinit var storageRef: StorageReference

    private val REQUEST_CODE_IMAGE_PICK = 1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_account_settings)

        supportActionBar?.hide()

        closeBtn = findViewById(R.id.close_student_editprofile_btn)
        saveBtn = findViewById(R.id.save_student_editprofile_btn)
        profileImageChange = findViewById(R.id.student_editprofile_profile_picture)
        profileName = findViewById(R.id.student_editprofile_name)
        profileUniversity = findViewById(R.id.student_editprofile_university)
        profileRollno = findViewById(R.id.student_editprofile_rollno)
        logoutBtn = findViewById(R.id.student_logout)
        deleteBtn = findViewById(R.id.student_delete_account)


        closeBtn.setOnClickListener{
            finish()
        }

        profileImageChange = findViewById<CircleImageView>(R.id.student_editprofile_profile_picture)
        storageRef = Firebase.storage.reference


        //Set onClickListener for the CircleImageView
        profileImageChange.setOnClickListener {
            //Create intent to select an image from the phone's local storage
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, REQUEST_CODE_IMAGE_PICK)
        }


        saveBtn.setOnClickListener{

            val name = profileName.text.toString()
            val universtiy = profileUniversity.text.toString()
            val rollno = profileRollno.text.toString()

            if(name.isNotEmpty() || universtiy.isNotEmpty() || rollno.isNotEmpty()){

                val query = FirebaseDatabase.getInstance().getReference("Profiles")
                    .child(FirebaseAuth.getInstance().uid.toString())

                if(name.isNotEmpty())
                {
                    query.child("fullname").setValue(name)
                }
                if(universtiy.isNotEmpty()){
                    query.child("university").setValue(universtiy)
                }
                if(rollno.isNotEmpty())
                {
                    query.child("rollno").setValue(rollno)
                }

                Toast.makeText(this, "Data for the HR has been updated." , Toast.LENGTH_SHORT).show()
            }
            else
            {
                Toast.makeText(this, "Please fill in the fields to update." , Toast.LENGTH_SHORT).show()
            }

        }





        logoutBtn.setOnClickListener{
            FirebaseAuth.getInstance().signOut()

            val intent = Intent(this@StudentAccountSettingsActivity , SignIn::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }

        deleteBtn.setOnClickListener{
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Delete Account")
            builder.setMessage("Are you sure you want to delete your account?")
            builder.setPositiveButton("Yes") { dialogInterface, i ->

                // Perform actions for account deletion
                val user = FirebaseAuth.getInstance().currentUser
                val database = FirebaseDatabase.getInstance()
                val profilesRef = database.getReference("Profiles")

                user?.let {
                    val uid = it.uid

                    // Delete user's data from "Profiles" node
                    profilesRef.child(uid).removeValue()

                    // Delete user's data from "Users" node
                    database.getReference("Follow").child(uid).removeValue()

                }


                Firebase.auth.currentUser?.delete()
                // Delete user data from Firebase database here
                // Navigate to login screen
                val intent = Intent(this, SignIn::class.java)
                startActivity(intent)
                this.finish()
            }
            builder.setNegativeButton("Cancel") { dialogInterface, i ->
                // Dismiss dialog
                dialogInterface.dismiss()
            }
            val dialog = builder.create()
            dialog.show()
        }




    }

    //Handle the selected image and upload it to FirebaseStorage
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_IMAGE_PICK && resultCode == RESULT_OK) {
            //Get the selected image URI
            val imageUri = data?.data

            //Upload the selected image to FirebaseStorage
            val imageRef = storageRef.child("images/${FirebaseAuth.getInstance().uid.toString()}")
            val uploadTask = imageRef.putFile(imageUri!!)

            //Handle the upload task
            uploadTask.addOnSuccessListener { taskSnapshot ->
                //Get the download URL of the uploaded image
                imageRef.downloadUrl.addOnSuccessListener { uri ->
                    //Update the image parameter of the current user in the Firebase Realtime Database
                    val userRef = FirebaseDatabase.getInstance().getReference("Profiles").child(FirebaseAuth.getInstance().uid.toString())
                    userRef.child("image").setValue(uri.toString())
                }
            }.addOnFailureListener {
                Toast.makeText(this , "Unable to upload the file to the database." , Toast.LENGTH_SHORT).show()
            }
        }
    }



}