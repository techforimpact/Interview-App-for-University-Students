package com.example.newapp.Fragments_HR

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.example.newapp.HrProfileEditActivity
import com.example.newapp.Model.HR
import com.example.newapp.Model.Student
import com.example.newapp.R
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HrProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HrProfileFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var profileView: CircleImageView
    private lateinit var profileName: TextView
    private lateinit var profileLocaiton: TextView
    private lateinit var profileAbout: TextView
    private lateinit var profileEmail: TextView
    private lateinit var profileContact: TextView
    private lateinit var profileEdit: Button

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
        var view = inflater.inflate(R.layout.fragment_hr_profile, container, false)

        profileView = view.findViewById(R.id.hr_profile_dp)
        profileName = view.findViewById(R.id.hr_profile_name)
        profileLocaiton = view.findViewById(R.id.hr_profile_location)
        profileEdit = view.findViewById(R.id.hr_editprofile_btn)
        profileAbout = view.findViewById(R.id.hr_profile_bio)
        profileEmail = view.findViewById(R.id.hr_profile_email)
        profileContact = view.findViewById(R.id.hr_profile_contact_number)


        profileEdit.setOnClickListener{
            val intent = Intent(context , HrProfileEditActivity::class.java)
            startActivity(intent)
        }

        val hr = arguments?.getParcelable<HR>("hr")

        Picasso.get().load(hr!!.getImage()).placeholder(R.drawable.profile_picture).into(profileView)

        profileName.text = hr.getName()
        profileLocaiton.text = hr.getLocation()
        profileAbout.text = hr.getBio()
        profileEmail.text = "Email : " + hr.getEmail()
        profileContact.text = "Contact : " + hr.getContact()





        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HrProfileFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HrProfileFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}