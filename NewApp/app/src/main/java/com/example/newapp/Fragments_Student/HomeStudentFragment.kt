package com.example.newapp.Fragments_Student

import android.graphics.BitmapFactory
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.SearchView.OnQueryTextListener
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import com.example.newapp.R
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newapp.Adapter.JobAdapter
import com.example.newapp.Model.Job
import com.example.newapp.Model.Student
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.NonDisposableHandle.parent

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeStudentFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeStudentFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var recyclerView: RecyclerView? = null
    private var jobAdapter: JobAdapter? = null
    private var mJob: MutableList<Job>? = null
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
        val view =  inflater.inflate(R.layout.fragment_home_student, container, false)

        recyclerView = view.findViewById(R.id.recycler_view_home_student)
        recyclerView?.setHasFixedSize(true)
        recyclerView?.layoutManager = LinearLayoutManager(context)

        mJob = ArrayList()

        jobAdapter = JobAdapter(requireContext(), mJob as ArrayList<Job>, true)


        recyclerView?.adapter = jobAdapter


        student = arguments?.getParcelable<Student>("student")


        var img: CircleImageView = view.findViewById(R.id.student_home_profile_picture)

        Picasso.get().load(student!!.getImage()).placeholder(R.drawable.profile_picture).into(img)

        retrieveJobs()

        view.findViewById<androidx.appcompat.widget.SearchView>(R.id.student_home_search).setOnQueryTextListener(object: androidx.appcompat.widget.SearchView.OnQueryTextListener
        {
            override fun onQueryTextChange(newText: String?): Boolean {
                if(view.findViewById<androidx.appcompat.widget.SearchView>(R.id.student_home_search).query.toString() == ""){
                    retrieveJobs()
                }
                else{
                    retrieveJobs()
                    searchJob(newText.toString())
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
                            view!!.findViewById<androidx.appcompat.widget.SearchView>(R.id.student_home_search).query.toString(),
                            ignoreCase = true
                        )
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
                    if (job != null && job.getTitle().contains(view!!.findViewById<androidx.appcompat.widget.SearchView>(R.id.student_home_search).query.toString(), ignoreCase = true)) {
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
         * @return A new instance of fragment HomeStudentFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeStudentFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
