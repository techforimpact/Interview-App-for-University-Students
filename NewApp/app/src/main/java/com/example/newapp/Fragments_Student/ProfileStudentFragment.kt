package com.example.newapp.Fragments_Student

import android.app.Activity.RESULT_OK
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newapp.Adapter.CertificatesAdapter
import com.example.newapp.Adapter.JobAdapter
import com.example.newapp.Adapter.SkillsAdapter
import com.example.newapp.Model.Certificate
import com.example.newapp.Model.Job
import com.example.newapp.Model.Student
import com.example.newapp.R
import com.example.newapp.StudentAccountSettingsActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProfileStudentFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileStudentFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var student: Student? = null
    private var profileName: TextView? = null
    private var profileUniversity: TextView? = null
    private lateinit var skillsRecyclerView: RecyclerView

    private lateinit var certificatesRecyclerView: RecyclerView
    private lateinit var certificatesAdapter: CertificatesAdapter

    private var skillsAdapter: SkillsAdapter? = null
    private var mSkills: MutableList<String>? = null

    private lateinit var verificationIcon: ImageView
    private  lateinit var newSkill: Button
    private lateinit var newCertificate: Button

    private lateinit var selectedSkill: String

    private var selectedFileUri: Uri? = null

    private val PICK_FILE_REQUEST_CODE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile_student, container, false)

        val editProfileButton = view.findViewById<Button>(R.id.student_editprofile_btn)
        editProfileButton.setOnClickListener {
            startActivity(Intent(context, StudentAccountSettingsActivity::class.java))
        }

        student = arguments?.getParcelable<Student>("student")


        verificationIcon = view.findViewById(R.id.verification_image)

        var img: CircleImageView = view.findViewById(R.id.student_profile_dp)

        Picasso.get().load(student!!.getImage()).placeholder(R.drawable.profile_picture).into(img)


        newSkill = view.findViewById(R.id.add_skill_button)
        newCertificate = view.findViewById(R.id.new_certificates_button)

        if(student?.getApproved() == "pending")
        {
            val resourceId = resources.getIdentifier("icon_pending", "drawable", "com.example.newapp")
            verificationIcon.setImageResource(resourceId)
        }
        else if (student?.getApproved() == "accepted")
        {
            val resourceId = resources.getIdentifier("icon_verified", "drawable", "com.example.newapp")
            verificationIcon.setImageResource(resourceId)
        }
        else
        {
            verificationIcon.setImageResource(android.R.color.transparent)
        }

        profileName = view.findViewById(R.id.student_profile_name)
        profileName!!.text = student!!.getFullname()

        profileUniversity = view.findViewById(R.id.student_education_level)
        profileUniversity!!.text = student!!.getUniversity()




        skillsRecyclerView = view.findViewById(R.id.skills_recycler_view)
        skillsRecyclerView.setHasFixedSize(false)
        skillsRecyclerView.layoutManager = LinearLayoutManager(context)

        mSkills = ArrayList()

        skillsAdapter = SkillsAdapter(requireContext() , mSkills as ArrayList , true)


        skillsRecyclerView.adapter = skillsAdapter

        retieveSKills()


        newSkill.setOnClickListener{
            // Create a dialog to add a new skill
            val dialog = Dialog(requireContext())
            dialog.setContentView(R.layout.dialog_add_skill)

            // Set up the dialog layout
            val skillEditText = dialog.findViewById<EditText>(R.id.skill_edit_text)
            val addButton = dialog.findViewById<Button>(R.id.add_skill_button)
            val cancelButton = dialog.findViewById<Button>(R.id.cancel_button)

            dialog.show()
            // Set up the Add button
            addButton.setOnClickListener {
                // Get the skill text and add it to the database
                val skill = skillEditText.text.toString().trim()
                if (skill.isNotEmpty()) {
                    val uid = FirebaseAuth.getInstance().currentUser?.uid ?: ""
                    val skillsRef = FirebaseDatabase.getInstance().getReference("Skills").child(uid).push()
                    skillsRef.setValue(skill)
                    dialog.dismiss()
                } else {
                    skillEditText.error = "Please enter a skill"
                }
            }

            // Set up the Cancel button
            cancelButton.setOnClickListener {
                dialog.dismiss()
            }
        }

        newCertificate.setOnClickListener {
            // Create a dialog to add a new certificate
            val dialog = Dialog(requireContext())
            dialog.setContentView(R.layout.dialog_add_certificate)

            // Set up the dialog layout
            val certificateNameEditText = dialog.findViewById<EditText>(R.id.file_name_box)
            val selectFileButton = dialog.findViewById<Button>(R.id.select_button)
            val uploadButton = dialog.findViewById<Button>(R.id.upload_button)
            val cancelButton = dialog.findViewById<Button>(R.id.cancel_button)

            selectFileButton.setOnClickListener {
                // Create an intent to select a file
                val intent = Intent(Intent.ACTION_GET_CONTENT)
                intent.type = "*/*"
                val mimeTypes = arrayOf("application/pdf", "image/png")
                intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
                startActivityForResult(intent, PICK_FILE_REQUEST_CODE)

                uploadButton.isEnabled = true
            }

            dialog.show()

            // Set up the Upload button
            uploadButton.setOnClickListener {
                // Get the certificate name and file URI and upload to Firebase storage
                val certificateName = certificateNameEditText.text.toString().trim()
                if (certificateName.isNotEmpty()) {
                    if (selectedFileUri != null) {
                        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: ""
                        val storageRef = FirebaseStorage.getInstance().getReference("certificates").child(uid).child("$certificateName.pdf")
                        storageRef.putFile(selectedFileUri!!).addOnSuccessListener {
                            Toast.makeText(requireContext(), "Certificate uploaded successfully", Toast.LENGTH_SHORT).show()
                            dialog.dismiss()
                        }.addOnFailureListener {
                            Toast.makeText(requireContext(), "Failed to upload certificate", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(requireContext(), "Please select a file", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    certificateNameEditText.error = "Please enter a certificate name"
                }
            }

            // Set up the Cancel button
            cancelButton.setOnClickListener {
                dialog.dismiss()
            }
        }


        certificatesRecyclerView = view.findViewById(R.id.certificates_recycler_view)
        certificatesRecyclerView.layoutManager = LinearLayoutManager(requireContext())


        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: ""

        retrieveCertificates(uid)

        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_FILE_REQUEST_CODE && resultCode == RESULT_OK) {
            data?.data?.let {
                selectedFileUri = it
            }
        }
    }


    private fun retrieveCertificates(uid: String) {
        // Retrieve the certificates from Firebase Storage
        val storageRef = FirebaseStorage.getInstance().getReference("certificates").child(uid)
        storageRef.listAll()
            .addOnSuccessListener { listResult ->
                val certificates = listResult.items.map {
                    Certificate(it.name, it.downloadUrl.toString())
                }
                certificatesAdapter = CertificatesAdapter(certificates) { certificate ->
                    // Open the file when the user clicks on the certificate name
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data = Uri.parse(certificate.getDownloadUrl())
                    if (intent.resolveActivity(requireActivity().packageManager) != null) {
                        startActivity(intent)
                    } else {
                        Toast.makeText(requireContext(), "No app found to open the file", Toast.LENGTH_SHORT).show()
                    }
                }
                certificatesRecyclerView.adapter = certificatesAdapter
            }
            .addOnFailureListener {
                // Handle any errors that occur while retrieving the certificates
            }
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ProfileFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProfileStudentFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }



    private fun retieveSKills() {
        val jobsRef = FirebaseDatabase.getInstance().getReference("Skills").child(FirebaseAuth.getInstance().currentUser?.uid.toString())

        jobsRef.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                mSkills?.clear()

                for (skillSnapshot in snapshot.children) {
                    val skillValue = skillSnapshot.getValue(String::class.java)

                    if (skillValue != null) {
                        mSkills?.add(skillValue)
                    }

                }

                skillsAdapter?.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
    }
}