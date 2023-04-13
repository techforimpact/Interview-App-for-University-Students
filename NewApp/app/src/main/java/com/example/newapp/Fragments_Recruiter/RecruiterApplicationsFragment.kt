package com.example.newapp.Fragments_Recruiter

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newapp.Adapter.ApplicantAdapter
import com.example.newapp.Adapter.RecruiterJobAdapter
import com.example.newapp.Model.Applicant
import com.example.newapp.Model.Student
import com.example.newapp.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [RecruiterApplicationsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RecruiterApplicationsFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var recyclerView: RecyclerView
    private lateinit var applicantAdapter: ApplicantAdapter
    private lateinit var mApplicants: MutableList<Applicant>

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
        val view = inflater.inflate(R.layout.fragment_recruiter_applications, container, false)

        recyclerView = view.findViewById(R.id.recycler_view_student_applications)
        recyclerView?.setHasFixedSize(true)
        recyclerView?.layoutManager = LinearLayoutManager(context)

        mApplicants = ArrayList()

        applicantAdapter = ApplicantAdapter(requireContext() , mApplicants as ArrayList , true)
        recyclerView.adapter = applicantAdapter

        val pref = context?.getSharedPreferences("PREFS" , Context.MODE_PRIVATE)

        val jobUid = pref?.getString("jobid", "")
        val uid = pref?.getString("uid", "")


        retrieveApplicants(uid.toString() , jobUid.toString())


        return view
    }


    private fun retrieveApplicants(uid: String , jobuid: String)
    {
        val query = FirebaseDatabase.getInstance().getReference("Applicants")
            .child(jobuid)
            .orderByChild("uid")
            .equalTo(uid)

        query.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                mApplicants?.clear()

                for (snap in snapshot.children) {
                    val applicant = snap.getValue(Applicant::class.java)

                    // Check if student matches search query
                    if (applicant != null ) {
                        mApplicants?.add(applicant)
                    }
                }

                applicantAdapter?.notifyDataSetChanged()
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
         * @return A new instance of fragment RecruiterApplicationsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            RecruiterApplicationsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}