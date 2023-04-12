package com.example.newapp.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.newapp.Fragments_Recruiter.RecruiterProfileFragment
import com.example.newapp.Model.Recruiter
import com.example.newapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.crashlytics.buildtools.reloc.javax.annotation.Nonnull
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class RecruiterAdapter (private var mContext: Context,
                        private var mRecruiters: List<Recruiter>,
                        private var isFragment: Boolean = false) : RecyclerView.Adapter<RecruiterAdapter.ViewHolder>()
{

    private var firebaseUser:FirebaseUser? = FirebaseAuth.getInstance().currentUser


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecruiterAdapter.ViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.recruiter_view , parent , false)
        return RecruiterAdapter.ViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecruiterAdapter.ViewHolder, position: Int) {

        val recruiter = mRecruiters[position]

        holder.recruiter_search_name.text = recruiter.getName()
        holder.recruiter_search_location.text = recruiter.getLocation()
        Picasso.get().load(recruiter.getImage()).placeholder(R.drawable.profile_picture).into(holder.recruiter_search_profile)

        checkFollowingStatus(recruiter.getUId() , holder.recruiter_search_follow)


        holder.itemView.setOnClickListener(View.OnClickListener {
            val pref = mContext.getSharedPreferences("PREFS" , Context.MODE_PRIVATE).edit()
            pref.putString("profileId" , recruiter.getUId())
            pref.apply()

            (mContext as FragmentActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.student_fragment_container , RecruiterProfileFragment()).commit()
        })


        holder.recruiter_search_follow.setOnClickListener{
            if(holder.recruiter_search_follow.text.toString() == "Follow")
            {
                firebaseUser?.uid.let { it1 ->
                    FirebaseDatabase.getInstance().reference
                        .child("Follow").child(it1.toString())
                        .child("Following").child(recruiter.getUId())
                        .setValue(true).addOnCompleteListener{task->
                            if(task.isSuccessful)
                            {
                                firebaseUser?.uid.let { it1 ->
                                    FirebaseDatabase.getInstance().reference
                                        .child("Follow").child(recruiter.getUId())
                                        .child("Followers").child(it1.toString())
                                        .setValue(true).addOnCompleteListener{task->
                                            if(task.isSuccessful)
                                            {

                                            }

                                        }
                                }
                            }

                        }
                }
            }
            else
            {
                firebaseUser?.uid.let { it1 ->
                    FirebaseDatabase.getInstance().reference
                        .child("Follow").child(it1.toString())
                        .child("Following").child(recruiter.getUId())
                        .removeValue().addOnCompleteListener{task->
                            if(task.isSuccessful)
                            {
                                firebaseUser?.uid.let { it1 ->
                                    FirebaseDatabase.getInstance().reference
                                        .child("Follow").child(recruiter.getUId())
                                        .child("Followers").child(it1.toString())
                                        .removeValue().addOnCompleteListener{task->
                                            if(task.isSuccessful)
                                            {

                                            }

                                        }
                                }
                            }

                        }
                }
            }
        }

    }



    override fun getItemCount(): Int {
        return mRecruiters.size
    }


    class ViewHolder (@Nonnull itemview: View) :RecyclerView.ViewHolder(itemview)
    {
        var recruiter_search_profile: CircleImageView = itemview.findViewById(R.id.recruiter_search_profile)
        var recruiter_search_name: TextView = itemview.findViewById(R.id.recruiter_search_name)
        var recruiter_search_location: TextView = itemview.findViewById(R.id.recruiter_search_location)
        var recruiter_search_follow: Button = itemview.findViewById(R.id.recruiter_search_follow)
    }







    private fun checkFollowingStatus(uId: String, recruiterSearchFollow: Button)
    {
        val followingRef = firebaseUser?.uid.let { it1 ->
            FirebaseDatabase.getInstance().reference
                .child("Follow").child(it1.toString())
                .child("Following")

        }


        followingRef.addValueEventListener(object : ValueEventListener
        {
            override fun onDataChange(datasnapshot: DataSnapshot)
            {
                if(datasnapshot.child(uId).exists())
                {
                    recruiterSearchFollow.text = "Following"
                }
                else
                {
                    recruiterSearchFollow.text = "Follow"
                }
            }

            override fun onCancelled(error: DatabaseError)
            {

            }
        })

    }

}