package com.example.newapp.Fragments_Recruiter

import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.renderscript.Sampler.Value
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newapp.Adapter.CertificatesAdapter
import com.example.newapp.Adapter.SkillsAdapter
import com.example.newapp.Model.Applicant
import com.example.newapp.Model.Certificate
import com.example.newapp.Model.Student
import com.example.newapp.Model.User
import com.example.newapp.R
import com.example.newapp.StudentAccountSettingsActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [RecrutierApplicantProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RecrutierApplicantProfileFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null


    private var applicant: Applicant? = null
    private var student: Student? = null


    private var profileName: TextView? = null
    private var profileUniversity: TextView? = null
    private lateinit var skillsRecyclerView: RecyclerView

    private lateinit var certificatesRecyclerView: RecyclerView
    private lateinit var certificatesAdapter: CertificatesAdapter

    private var skillsAdapter: SkillsAdapter? = null
    private var mSkills: MutableList<String>? = null

    private lateinit var verificationIcon: ImageView


    private var jobUid: String? = ""
    private var studentUid: String? = ""

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
        val view =  inflater.inflate(R.layout.fragment_recrutier_applicant_profile, container, false)

        arguments?.let {
            jobUid = it.getString("jobUid")
            studentUid = it.getString("studentUid")
        }


        getApplicant()



        verificationIcon = view.findViewById(R.id.applicant_verification_image)

        var img: CircleImageView = view.findViewById(R.id.applicant_profile_dp)

        Picasso.get().load(student?.getImage()).placeholder(R.drawable.profile_picture).into(img)



        if(applicant?.getApproved() == "pending")
        {
            val resourceId = resources.getIdentifier("icon_pending", "drawable", "com.example.newapp")
            verificationIcon.setImageResource(resourceId)
        }
        else if (applicant?.getApproved() == "accepted")
        {
            val resourceId = resources.getIdentifier("icon_verified", "drawable", "com.example.newapp")
            verificationIcon.setImageResource(resourceId)
        }
        else
        {
            verificationIcon.setImageResource(android.R.color.transparent)
        }

        profileName = view.findViewById(R.id.applicant_profile_name)
        profileName!!.text = applicant?.getName()

        profileUniversity = view.findViewById(R.id.applicant_education_level)
        profileUniversity!!.text = applicant?.getUniversity()




        skillsRecyclerView = view.findViewById(R.id.applicant_skills_recycler_view)
        skillsRecyclerView.setHasFixedSize(false)
        skillsRecyclerView.layoutManager = LinearLayoutManager(context)

        mSkills = ArrayList()

        skillsAdapter = SkillsAdapter(requireContext() , mSkills as ArrayList , true)


        skillsRecyclerView.adapter = skillsAdapter

        retieveSKills()

        certificatesRecyclerView = view.findViewById(R.id.applicant_certificates_recycler_view)
        certificatesRecyclerView.layoutManager = LinearLayoutManager(requireContext())


        retrieveCertificates(studentUid!!)


        return view
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



    private fun getApplicant() {
        val query = FirebaseDatabase.getInstance().getReference()
            .child("Applicants")
            .child(jobUid.toString())
            .orderByChild("studentUid")
            .equalTo(studentUid.toString())

        query.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val user = snapshot.children.first().getValue(Applicant::class.java)
                    if (user != null) {
                        applicant = user
                    }
                } else {
                    Toast.makeText(context, "Student not found!", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "Student data not retrieved from the database!", Toast.LENGTH_SHORT).show()
            }
        })

    }




    private fun retieveSKills() {
        val jobsRef = FirebaseDatabase.getInstance().getReference("Skills").child(studentUid.toString())

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


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment RecrutierApplicantProfileFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            RecrutierApplicantProfileFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}