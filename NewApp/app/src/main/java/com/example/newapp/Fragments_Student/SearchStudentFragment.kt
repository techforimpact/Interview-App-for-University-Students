package com.example.newapp.Fragments_Student

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newapp.Adapter.JobAdapter
import com.example.newapp.Adapter.RecruiterAdapter
import com.example.newapp.Model.Job
import com.example.newapp.Model.Recruiter
import com.example.newapp.Model.Student
import com.example.newapp.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import java.util.*
import kotlin.collections.ArrayList

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SearchStudentFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SearchStudentFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var recyclerView: RecyclerView? = null
    private var recruiterAdapter:RecruiterAdapter? = null
    private var mRecruiter: MutableList<Recruiter>? = null
    private var student: Student? = null

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
        val view =  inflater.inflate(R.layout.fragment_search_student, container, false)


        recyclerView = view.findViewById(R.id.recycler_view_student_search)
        recyclerView?.setHasFixedSize(true)
        recyclerView?.layoutManager = LinearLayoutManager(context)
        val searchbar = view.findViewById<SearchView>(R.id.student_recruiter_search_bar)




        mRecruiter = ArrayList()
        recruiterAdapter = RecruiterAdapter(requireContext(), mRecruiter as ArrayList<Recruiter>, true)


        recyclerView?.adapter = recruiterAdapter

        student = arguments?.getParcelable<Student>("student")


        var img: CircleImageView = view.findViewById(R.id.profile_picture)

        Picasso.get().load(student!!.getImage()).placeholder(R.drawable.profile_picture).into(img)

        retrieveRecruiters()


        searchbar?.setOnQueryTextListener(object: androidx.appcompat.widget.SearchView.OnQueryTextListener
        {
            override fun onQueryTextChange(newText: String?): Boolean {
                if(searchbar.query.toString() == ""){
                    retrieveRecruiters()
                }
                else{
                    retrieveRecruiters()
                    searchRecruiter(newText.toString())
                }
                return true
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                retrieveRecruiters()
                searchRecruiter(query.toString().lowercase())
                return true;
            }
        })


        return view
    }

    private fun searchRecruiter(input: String) {
        val query = FirebaseDatabase.getInstance().getReference("Profiles")
            .orderByChild("role")
            .equalTo("recruiter")

        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                mRecruiter?.clear()
                for (recruiterSnapshot in snapshot.children) {
                    val recruiter = recruiterSnapshot.getValue(Recruiter::class.java)
                    if (recruiter?.getName()?.contains(input, ignoreCase = true) == true) {
                        mRecruiter?.add(recruiter)
                    }
                }
                recruiterAdapter?.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
    }



    private fun retrieveRecruiters() {
        val jobsRef = FirebaseDatabase.getInstance().getReference().child("Profiles")
            .orderByChild("role")
            .equalTo("recruiter")

        jobsRef.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                mRecruiter?.clear()

                for (snap in snapshot.children) {
                    val recruiter = snap.getValue(Recruiter::class.java)

                    // Check if job matches search query
                    if (recruiter != null && recruiter.getName().contains(view!!.findViewById<androidx.appcompat.widget.SearchView>(R.id.student_recruiter_search_bar).query.toString(), ignoreCase = true)) {
                        mRecruiter?.add(recruiter)
                    }
                }

                recruiterAdapter?.notifyDataSetChanged()
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
         * @return A new instance of fragment SearchStudentFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SearchStudentFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}