package com.example.newapp.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.newapp.JobApplication
import com.example.newapp.Model.Job
import com.example.newapp.Model.Notification
import com.example.newapp.R
import com.google.firebase.crashlytics.buildtools.reloc.javax.annotation.Nonnull
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import java.sql.Time
import java.util.*

class NotificationAdapter (private var mContext: Context,
                           private var mNotifications: List<Notification>,
                           private var isFragment: Boolean = false) : RecyclerView.Adapter<NotificationAdapter.ViewHolder>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationAdapter.ViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.notification_item , parent , false)
        return NotificationAdapter.ViewHolder(view)
    }

    override fun onBindViewHolder(holder: NotificationAdapter.ViewHolder, position: Int) {
        val noti = mNotifications[position]

        Picasso.get().load(noti.getRecruiterImage()).placeholder(R.drawable.profile_picture).into(holder.recruiterImage)
        holder.recruiterName.text = noti.getRecruiterName()
        holder.titleTextView.text = noti.getTitle()
        holder.status.text = "Status : " + noti.getStatus()


    }

    override fun getItemCount(): Int {
        return mNotifications.size
    }

    class ViewHolder (@Nonnull itemview: View) : RecyclerView.ViewHolder(itemview)
    {
        var recruiterImage: CircleImageView = itemview.findViewById(R.id.student_noti_recruiter_image)
        var recruiterName: TextView = itemview.findViewById(R.id.student_noti_recruiter_name)
        var titleTextView: TextView = itemview.findViewById(R.id.student_notification_job_title)
        var status: TextView = itemview.findViewById(R.id.student_noti_status)
    }
}