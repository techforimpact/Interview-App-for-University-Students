package com.example.newapp.Adapter

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.newapp.Fragments_Recruiter.RecruiterProfileFragment
import com.example.newapp.Fragments_Student.ProfileStudentFragment
import com.example.newapp.JobApplication
import com.example.newapp.Model.Student
import com.example.newapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.crashlytics.buildtools.reloc.javax.annotation.Nonnull
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class StudentAdapter(private var mContext: Context,
                     private var mStudents: List<Student>,
                     private var isFragment: Boolean = false) : RecyclerView.Adapter<StudentAdapter.ViewHolder>()
{

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentAdapter.ViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.hr_approved_student , parent , false)
        return StudentAdapter.ViewHolder(view)
    }

    override fun onBindViewHolder(holder: StudentAdapter.ViewHolder, position: Int) {
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


    }

    override fun getItemCount(): Int {
        return mStudents.size
    }

    class ViewHolder (@Nonnull itemview: View) : RecyclerView.ViewHolder(itemview)
    {
        var pictureStudent: CircleImageView = itemview.findViewById(R.id.hr_student_approved_dp)
        var studentName: TextView = itemview.findViewById(R.id.hr_student_approved_name)
        var studentRollno: TextView = itemview.findViewById(R.id.hr_student_approved_rollno)
        var viewProfile: TextView = itemview.findViewById(R.id.hr_student_approved_profile_btn)
    }

}