package com.example.newapp.Fragments_HR

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newapp.Adapter.JobAdapter
import com.example.newapp.Adapter.StudentAdapter
import com.example.newapp.Model.HR
import com.example.newapp.Model.Job
import com.example.newapp.Model.Student
import com.example.newapp.R
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
 * Use the [HomeHrFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeHrFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var recyclerView: RecyclerView? = null
    private var studentAdapter: StudentAdapter? = null
    private var mStudents: MutableList<Student>? = null
    private lateinit var profileImage: CircleImageView




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
        val view =  inflater.inflate(R.layout.fragment_home_hr, container, false)


        val hr = arguments?.getParcelable<HR>("hr")

        profileImage = view.findViewById(R.id.hr_home_profile_picture)

        Picasso.get().load(hr!!.getImage()).placeholder(R.drawable.profile_picture).into(profileImage)

        recyclerView = view.findViewById(R.id.recycler_view_home_hr)
        recyclerView?.setHasFixedSize(true)
        recyclerView?.layoutManager = LinearLayoutManager(context)

        mStudents = ArrayList()

        studentAdapter = StudentAdapter(requireContext(), mStudents as ArrayList<Student>, true)

        recyclerView?.adapter = studentAdapter


        retrieveStudents(hr!!)

        return view
    }



    private fun retrieveStudents(hr: HR) {
        val query = FirebaseDatabase.getInstance().getReference("Profiles")
            .orderByChild("role")
            .equalTo("student")

        query.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                mStudents?.clear()

                for (snap in snapshot.children) {
                    val student = snap.getValue(Student::class.java)

                    // Check if student matches search query
                    if (student != null && student.getUniversity().contains(hr.getName(), ignoreCase = true) && student.getApproved() == "accepted") {
                        mStudents?.add(student)
                    }
                }

                studentAdapter?.notifyDataSetChanged()
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
         * @return A new instance of fragment HomeHrFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeHrFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }




}