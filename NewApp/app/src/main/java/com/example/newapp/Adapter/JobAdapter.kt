package com.example.newapp.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.newapp.Fragments_Recruiter.RecruiterProfileFragment
import com.example.newapp.JobApplication
import com.example.newapp.Model.Job
import com.example.newapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.crashlytics.buildtools.reloc.javax.annotation.Nonnull

class JobAdapter (private var mContext: Context,
                    private var mJobs: List<Job>,
                    private var isFragment: Boolean = false) : RecyclerView.Adapter<JobAdapter.ViewHolder>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JobAdapter.ViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.student_job_post , parent , false)
        return JobAdapter.ViewHolder(view)
    }

    override fun onBindViewHolder(holder: JobAdapter.ViewHolder, position: Int) {
        val job = mJobs[position]

        holder.titleTextView.text = job.getTitle()
        holder.categoryTextView.text = "Category : " +job.getCategory()
        holder.coursesTextView.text = "Courses : " +job.getCourses()
        holder.seatsTextView.text = "Seats : " +job.getSeats()
        holder.locationTextView.text = "Location : " + job.getLocation()
        holder.deadlineTextView.text = "Deadline : " + job.getDeadline()
        holder.detailsTextView.text = "Details : " + job.getDetails()

        holder.applybtn.setOnClickListener{
            val intent = Intent(mContext, JobApplication::class.java).apply {
                putExtra("jobUid", mJobs[position].getUid())
                putExtra("uid" , FirebaseAuth.getInstance().uid)
            }
            mContext.startActivity(intent)
        }

    }

    override fun getItemCount(): Int {
        return mJobs.size
    }

    class ViewHolder (@Nonnull itemview: View) :RecyclerView.ViewHolder(itemview)
    {
        var titleTextView: TextView = itemview.findViewById(R.id.student_job_title)
        var categoryTextView: TextView = itemview.findViewById(R.id.student_job_category)
        var coursesTextView: TextView = itemview.findViewById(R.id.student_courses_required)
        var seatsTextView: TextView = itemview.findViewById(R.id.student_seats)
        var locationTextView: TextView = itemview.findViewById(R.id.student_location)
        var deadlineTextView: TextView = itemview.findViewById(R.id.student_deadline)
        var detailsTextView: TextView = itemview.findViewById(R.id.student_details)
        var applybtn: Button = itemview.findViewById(R.id.student_post_applybtn)
    }

}