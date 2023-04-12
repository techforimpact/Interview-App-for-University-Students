package com.example.newapp.Fragments_Recruiter

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newapp.Adapter.JobAdapter
import com.example.newapp.Adapter.RecruiterJobAdapter
import com.example.newapp.Model.Job
import com.example.newapp.R
import com.google.firebase.auth.FirebaseAuth
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
 * Use the [HomeRecruiterFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeRecruiterFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null


    private var recyclerView: RecyclerView? = null
    private var jobAdapter: RecruiterJobAdapter? = null
    private var mJob: MutableList<Job>? = null



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
        val view = inflater.inflate(R.layout.fragment_home_recruiter, container, false)


        recyclerView = view.findViewById(R.id.recycler_view_home_recruiter)
        recyclerView?.setHasFixedSize(true)
        recyclerView?.layoutManager = LinearLayoutManager(context)

        mJob = ArrayList()

        jobAdapter = RecruiterJobAdapter(requireContext(), mJob as ArrayList<Job>, true)


        recyclerView?.adapter = jobAdapter




        retrieveJobs()

        view.findViewById<androidx.appcompat.widget.SearchView>(R.id.recruiter_home_search_view).setOnQueryTextListener(object: androidx.appcompat.widget.SearchView.OnQueryTextListener
        {
            override fun onQueryTextChange(newText: String?): Boolean {
                if(view.findViewById<androidx.appcompat.widget.SearchView>(R.id.recruiter_home_search_view).query.toString() == ""){
                    retrieveJobs()
                }
                else{
                    retrieveJobs()
                    searchJob(newText.toString().lowercase())
                }
                return true
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                retrieveJobs()
                searchJob(query.toString().lowercase())
                return true
            }

        })





        return view
    }



    private fun searchJob(input: String)
    {
        val query = FirebaseDatabase.getInstance().getReference()
            .child("Jobs")
            .orderByChild("title")
            .startAt(input)
            .endAt(input + "\uf8ff")


        query.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                mJob?.clear()

                for (snap in snapshot.children) {
                    val job = snap.getValue(Job::class.java)

                    // Check if job matches search query
                    if ((job != null) && job.getTitle().contains(
                            view!!.findViewById<androidx.appcompat.widget.SearchView>(R.id.recruiter_home_search_view).query.toString(),
                            ignoreCase = true
                        ) && job.getUid() == FirebaseAuth.getInstance().uid
                    ) {
                        mJob?.add(job)
                    }
                }

                jobAdapter?.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
    }



    private fun retrieveJobs() {
        val jobsRef = FirebaseDatabase.getInstance().getReference().child("Jobs")

        jobsRef.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                mJob?.clear()

                for (snap in snapshot.children) {
                    val job = snap.getValue(Job::class.java)

                    // Check if job matches search query
                    if (job != null && job.getTitle().contains(view!!.findViewById<androidx.appcompat.widget.SearchView>(R.id.recruiter_home_search_view).query.toString(), ignoreCase = true) && job.getUid() == FirebaseAuth.getInstance().uid) {
                        mJob?.add(job)
                    }
                }

                jobAdapter?.notifyDataSetChanged()
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
         * @return A new instance of fragment HomeRecruiterFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeRecruiterFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}