package com.example.newapp.Fragments_Student

import android.app.Application
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newapp.Adapter.JobAdapter
import com.example.newapp.Adapter.NotificationAdapter
import com.example.newapp.Model.Job
import com.example.newapp.Model.Notification
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
 * Use the [NotificationsStudentFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class NotificationsStudentFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null



    private var recyclerView: RecyclerView? = null
    private var notificationAdapter: NotificationAdapter? = null
    private var mNotification: MutableList<Notification>? = null
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
        val view = inflater.inflate(R.layout.fragment_notification_student, container, false)

        student = arguments?.getParcelable<Student>("student")

        mNotification = ArrayList()

        recyclerView = view.findViewById(R.id.recycler_view_notification_student)
        recyclerView?.setHasFixedSize(true)
        recyclerView?.layoutManager = LinearLayoutManager(context)

        notificationAdapter = NotificationAdapter(requireContext() , mNotification as ArrayList<Notification> , true)

        recyclerView?.adapter = notificationAdapter

        var img: CircleImageView = view.findViewById(R.id.profile_picture)

        Picasso.get().load(student!!.getImage()).placeholder(R.drawable.profile_picture).into(img)


        retrieveNotifications()


        return view
    }


    private fun retrieveNotifications()
    {
        val notifRef = FirebaseDatabase.getInstance().getReference("Notifications")
            .orderByChild("studentUid")
            .equalTo(FirebaseAuth.getInstance().uid.toString())

        notifRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                mNotification?.clear()
                for (notification in snapshot.children) {
                    val noti = notification.getValue(Notification::class.java)
                    if(noti != null){
                        mNotification?.add(noti)
                    }
                }
                notificationAdapter?.notifyDataSetChanged()
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
         * @return A new instance of fragment NotificationStudentFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            NotificationsStudentFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}