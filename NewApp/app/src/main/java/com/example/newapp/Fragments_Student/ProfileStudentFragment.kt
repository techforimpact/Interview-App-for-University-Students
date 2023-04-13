package com.example.newapp.Fragments_Student

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newapp.Adapter.JobAdapter
import com.example.newapp.Adapter.SkillsAdapter
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
    private lateinit var seeAllButton: Button
    private var skillsAdapter: SkillsAdapter? = null
    private var mSkills: MutableList<String>? = null
    private lateinit var editSkillsBtn: Button
    private lateinit var verificationIcon: ImageView
    private  lateinit var newSkill: Button
    private lateinit var newCertificate: Button

    private lateinit var selectedSkill: String

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

        newCertificate.setOnClickListener{
            // Create a dialog to add a new skill
            val dialog = Dialog(requireContext())
            dialog.setContentView(R.layout.dialog_add_skill)

            // Set up the dialog layout
            val skillEditText = dialog.findViewById<EditText>(R.id.skill_edit_text)
            val addButton = dialog.findViewById<Button>(R.id.add_skill_button)
            val cancelButton = dialog.findViewById<Button>(R.id.cancel_button)

            addButton.text = "Add Certificate"

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




        return view
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