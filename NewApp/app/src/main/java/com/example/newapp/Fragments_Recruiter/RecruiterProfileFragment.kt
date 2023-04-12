package com.example.newapp.Fragments_Recruiter

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.example.newapp.Model.Recruiter
import com.example.newapp.Model.Student
import com.example.newapp.R
import com.example.newapp.StudentAccountSettingsActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [RecruiterProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RecruiterProfileFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var profileId: String
    private lateinit var firebaseUser: FirebaseUser
    private lateinit var editBtn: Button
    private lateinit var profileName: TextView
    private lateinit var profileLocation: TextView
    private lateinit var profileImage: CircleImageView
    private lateinit var profileBio: TextView
    private lateinit var profileEmail: TextView
    private lateinit var profilePhone: TextView
    private var recruiter: Recruiter? = null


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
        val view = inflater.inflate(R.layout.fragment_recruiter_profile, container, false)


        editBtn = view.findViewById(R.id.recruiter_editprofile_btn)
        profileName = view.findViewById(R.id.recruiter_profile_name)
        profileLocation = view.findViewById(R.id.recruiter_location)
        profileImage = view.findViewById(R.id.recruiter_profile_dp)
        profileBio = view.findViewById(R.id.recruiter_profile_company_bio)
        profileEmail = view.findViewById(R.id.recruiter_profile_email)
        profilePhone = view.findViewById(R.id.recruiter_profile_contact_number)


        recruiter = arguments?.getParcelable<Recruiter>("recruiter")


        profileName.text = recruiter!!.getName()
        profileLocation.text = recruiter!!.getLocation()
        Picasso.get().load(recruiter!!.getImage()).placeholder(R.drawable.profile_picture).into(profileImage)
        profileBio.text = recruiter!!.getBio()
        profileEmail.text = "Email: " + recruiter!!.getEmail()
        profilePhone.text = "Contact Number: " + recruiter!!.getContact()


        val pref = context?.getSharedPreferences("PREFS" , Context.MODE_PRIVATE)

        firebaseUser = FirebaseAuth.getInstance().currentUser!!
        if (pref != null)
        {
            this.profileId = pref.getString("profileId" , "none").toString()
        }

        if (profileId == firebaseUser.uid)
        {

        }



        editBtn.setOnClickListener{
            startActivity(Intent(context, StudentAccountSettingsActivity::class.java))
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
         * @return A new instance of fragment RecruiterProfileFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            RecruiterProfileFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}