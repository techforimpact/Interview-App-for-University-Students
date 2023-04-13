package com.example.newapp.Adapter

import android.content.ContentValues
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.newapp.Fragments_Student.ProfileStudentFragment
import com.example.newapp.Model.Student
import com.example.newapp.R
import com.google.firebase.crashlytics.buildtools.reloc.javax.annotation.Nonnull
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class StudentApplicationAdapter (private var mContext: Context,
                                 private var mStudents: List<Student>,
                                 private var isFragment: Boolean = false) : RecyclerView.Adapter<StudentApplicationAdapter.ViewHolder>()
{

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentApplicationAdapter.ViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.recruiter_student_app , parent , false)
        return StudentApplicationAdapter.ViewHolder(view)
    }

    override fun onBindViewHolder(holder: StudentApplicationAdapter.ViewHolder, position: Int) {
        val student = mStudents[position]

        holder.studentName.text = student.getFullname()
        holder.studentRollno.text = "Roll No : " +  student.getRollno()
        Picasso.get().load(student.getImage()).placeholder(R.drawable.profile_picture).into(holder.pictureStudent)


        holder.viewProfile.setOnClickListener{
            val pref = mContext.getSharedPreferences("PREFS" , Context.MODE_PRIVATE).edit()
            pref.putString("profileId" , student.getUId())
            pref.apply()

            val bundle = Bundle()
            bundle.putParcelable("student", student)
            ProfileStudentFragment().arguments = bundle


            (mContext as FragmentActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.hr_fragment_container , ProfileStudentFragment() ).commit()
        }


        holder.rejectBtn.setOnClickListener{

            val query = FirebaseDatabase.getInstance().getReference("Profiles")
                .orderByChild("uid")
                .equalTo(student.getUId())

            query.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (profileSnapshot in dataSnapshot.children) {
                        profileSnapshot.ref.child("approved").setValue("rejected")
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Log.e(ContentValues.TAG, "onCancelled", databaseError.toException())
                }
            })


        }


        holder.acceptBtn.setOnClickListener{

            val query = FirebaseDatabase.getInstance().getReference("Profiles")
                .orderByChild("uid")
                .equalTo(student.getUId())

            query.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (profileSnapshot in dataSnapshot.children) {
                        profileSnapshot.ref.child("approved").setValue("accepted")
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Log.e(ContentValues.TAG, "onCancelled", databaseError.toException())
                }
            })

        }



    }

    override fun getItemCount(): Int {
        return mStudents.size
    }

    class ViewHolder (@Nonnull itemview: View) : RecyclerView.ViewHolder(itemview)
    {
        var pictureStudent: CircleImageView = itemview.findViewById(R.id.hr_student_app_dp)
        var studentName: TextView = itemview.findViewById(R.id.hr_student_app_name)
        var studentRollno: TextView = itemview.findViewById(R.id.hr_student_app_rollno)
        var viewProfile: TextView = itemview.findViewById(R.id.hr_student_app_profile_btn)
        var rejectBtn: Button = itemview.findViewById(R.id.hr_student_app_reject_btn)
        var acceptBtn: Button = itemview.findViewById(R.id.hr_student_app_approve_btn)
    }


}