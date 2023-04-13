package com.example.newapp.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.newapp.Fragments_Recruiter.RecruiterApplicationsFragment
import com.example.newapp.Fragments_Recruiter.RecruiterProfileFragment
import com.example.newapp.Model.Job
import com.example.newapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.crashlytics.buildtools.reloc.javax.annotation.Nonnull

class RecruiterJobAdapter(private var mContext: Context,
                          private var mJobs: List<Job>,
                          private var isFragment: Boolean = false) : RecyclerView.Adapter<RecruiterJobAdapter.ViewHolder>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecruiterJobAdapter.ViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.recruiter_job_view , parent , false)
        return RecruiterJobAdapter.ViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecruiterJobAdapter.ViewHolder, position: Int) {
        val job = mJobs[position]

        holder.titleTextView.text = job.getTitle()
        holder.categoryTextView.text = "Category : " +job.getCategory()
        holder.coursesTextView.text = "Courses : " +job.getCourses()
        holder.seatsTextView.text = "Seats : " +job.getSeats()
        holder.locationTextView.text = "Location : " + job.getLocation()
        holder.deadlineTextView.text = "Deadline : " + job.getDeadline()
        holder.detailsTextView.text = "Details : " + job.getDetails()

        holder.seeApplications.setOnClickListener(View.OnClickListener {
            val pref = mContext.getSharedPreferences("PREFS" , Context.MODE_PRIVATE).edit()
            pref.putString("jobId" , job.getUid())
            pref.putString("uid" , FirebaseAuth.getInstance().uid)
            pref.apply()

            (mContext as FragmentActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.recruiter_fragment_container , RecruiterApplicationsFragment()).commit()
        })

    }

    override fun getItemCount(): Int {
        return mJobs.size
    }

    class ViewHolder (@Nonnull itemview: View) :RecyclerView.ViewHolder(itemview)
    {
        var titleTextView: TextView = itemview.findViewById(R.id.recruiter_job_title)
        var categoryTextView: TextView = itemview.findViewById(R.id.recruiter_job_category)
        var coursesTextView: TextView = itemview.findViewById(R.id.recruiter_courses_required)
        var seatsTextView: TextView = itemview.findViewById(R.id.recruiter_seats)
        var locationTextView: TextView = itemview.findViewById(R.id.recruiter_location)
        var deadlineTextView: TextView = itemview.findViewById(R.id.recruiter_deadline)
        var detailsTextView: TextView = itemview.findViewById(R.id.recruiter_details)
        var seeApplications: Button = itemview.findViewById(R.id.recruiter_see_applications)
    }

}