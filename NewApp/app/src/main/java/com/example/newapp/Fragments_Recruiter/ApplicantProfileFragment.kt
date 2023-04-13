package com.example.newapp.Fragments_Recruiter

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newapp.Adapter.SkillsAdapter
import com.example.newapp.Model.Applicant
import com.example.newapp.Model.Student
import com.example.newapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ApplicantProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ApplicantProfileFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var applicant: Applicant? = null
    private var student: Student? = null


    private var skillsAdapter: SkillsAdapter? = null
    private var mSkills: MutableList<String>? = null



    private lateinit var profileImage: CircleImageView
    private lateinit var profileName:TextView
    private lateinit var profileUniversity: TextView
    private lateinit var skillsRecycler: RecyclerView
    private lateinit var certificatesRecycler: RecyclerView



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
        val view =  inflater.inflate(R.layout.fragment_applicant_profile, container, false)

        profileImage = view.findViewById(R.id.applicant_profile_picture)
        profileName = view.findViewById(R.id.applicant_profile_name)
        profileUniversity = view.findViewById(R.id.applicant_university)


        skillsRecycler = view.findViewById(R.id.applicant_skills_recycler_view)
        skillsRecycler.setHasFixedSize(false)
        skillsRecycler.layoutManager = LinearLayoutManager(context)

        mSkills = ArrayList()
        skillsAdapter = SkillsAdapter(requireContext() , mSkills as ArrayList , true)

        skillsRecycler.adapter = skillsAdapter


        certificatesRecycler = view.findViewById(R.id.applicant_certificates_recycler_view)
        certificatesRecycler.setHasFixedSize(false)
        certificatesRecycler.layoutManager = LinearLayoutManager(context)




        val pref = context?.getSharedPreferences("PREFS" , Context.MODE_PRIVATE)

        val jobUid = pref?.getString("profileId", "")

        applicant = arguments?.getParcelable<Applicant>("applicant")



        profileName.text = applicant!!.getName()
        profileUniversity.text = applicant!!.getUniversity()
        Picasso.get().load(applicant!!.getImage()).placeholder(R.drawable.profile_picture).into(profileImage)

        findStudent(applicant!!.getStudentUid())



        return view
    }

    private fun findStudent(uid: String)
    {
        val query = FirebaseDatabase.getInstance().getReference("Profiles")
            .orderByChild(uid)
            .equalTo(uid)

        query.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for(snap in snapshot.children)
                {
                    val user = snap.getValue(Student::class.java)
                    if (user != null)
                    {
                        student = user
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context , "Student not found!" , Toast.LENGTH_SHORT).show()
            }
        })
    }


    private fun retieveSKills() {
        val jobsRef = FirebaseDatabase.getInstance().getReference("Skills").child(student!!.getUId())

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
         * @return A new instance of fragment ApplicantProfileFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ApplicantProfileFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}