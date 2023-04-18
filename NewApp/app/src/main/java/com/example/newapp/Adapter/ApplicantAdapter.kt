package com.example.newapp.Adapter

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.core.content.FileProvider
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.newapp.Fragments_Recruiter.RecruiterApplicationsFragment
import com.example.newapp.Fragments_Recruiter.RecrutierApplicantProfileFragment
import com.example.newapp.Fragments_Student.ProfileStudentFragment
import com.example.newapp.Model.Applicant
import com.example.newapp.Model.Notification
import com.example.newapp.Model.Student
import com.example.newapp.R
import com.google.firebase.crashlytics.buildtools.reloc.javax.annotation.Nonnull
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import java.io.File

class ApplicantAdapter(private var mContext: Context,
                       private var mApplicants: List<Applicant>,
                       private var isFragment: Boolean = false) : RecyclerView.Adapter<ApplicantAdapter.ViewHolder>()
{

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ApplicantAdapter.ViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.recruiter_student_app , parent , false)
        return ApplicantAdapter.ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ApplicantAdapter.ViewHolder, position: Int) {
        val applicant = mApplicants[position]

        holder.applicantName.text = applicant.getName()
        holder.applicantUniversity.text = "University : " +  applicant.getUniversity()
        Picasso.get().load(applicant.getImage()).placeholder(R.drawable.profile_picture).into(holder.applicantimage)


        holder.viewProfile.setOnClickListener{

            val bundle = Bundle().apply {
                putString("jobUid", applicant.getJobUid())
                putString("studentUid", applicant.getStudentUid())
            }

            val fragment = RecrutierApplicantProfileFragment().apply {
                arguments = bundle
            }

            (mContext as FragmentActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.recruiter_fragment_container, fragment)
                .commit()
        }


        holder.reject.setOnClickListener{

            val query = FirebaseDatabase.getInstance().getReference("Applicants")
                .child(applicant.getJobUid())
                .orderByChild("studentUid")
                .equalTo(applicant.getStudentUid())

            query.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (profileSnapshot in dataSnapshot.children) {
                        profileSnapshot.ref.child("status").setValue("rejected")
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Log.e(ContentValues.TAG, "onCancelled", databaseError.toException())
                }
            })

            val notificationref = FirebaseDatabase.getInstance().getReference("Notifications")
            val notiref = FirebaseDatabase.getInstance().getReference("Notifications")
                .orderByChild("studentUid").equalTo(applicant.getStudentUid())

            notiref.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (notificationSnap in snapshot.children) {
                        val notification = notificationSnap.getValue(Notification::class.java)
                        if (notification?.getJobUid() == applicant.getJobUid()) {
                            notification.setStatus("rejected")
                            notificationref.child(notificationSnap.key ?: "").setValue(notification)
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle error
                }
            })


        }


        holder.accept.setOnClickListener{

            val query = FirebaseDatabase.getInstance().getReference("Applicants")
                .child(applicant.getJobUid())
                .orderByChild("studentUid")
                .equalTo(applicant.getStudentUid())

            query.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (profileSnapshot in dataSnapshot.children) {
                        profileSnapshot.ref.child("status").setValue("accepted")
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Log.e(ContentValues.TAG, "onCancelled", databaseError.toException())
                }
            })

            val notificationref = FirebaseDatabase.getInstance().getReference("Notifications")
            val notiref = FirebaseDatabase.getInstance().getReference("Notifications")
                .orderByChild("studentUid").equalTo(applicant.getStudentUid())

            notiref.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (notificationSnap in snapshot.children) {
                        val notification = notificationSnap.getValue(Notification::class.java)
                        if (notification?.getJobUid() == applicant.getJobUid()) {
                            notification.setStatus("accepted")
                            notificationref.child(notificationSnap.key ?: "").setValue(notification)
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle error
                }
            })



        }

        holder.resume.setOnClickListener{
            val storageRef = FirebaseStorage.getInstance().getReferenceFromUrl(applicant.getResume())
            val localFile = File.createTempFile("temp", "pdf")
            storageRef.getFile(localFile).addOnSuccessListener {
                // File has been downloaded to local file
                // Read the file from local file using any method you prefer
                // For example, you can read the file as a byte array:
                val fileData = localFile.readBytes()

                // Create a content:// Uri for the file
                val fileProviderUri = FileProvider.getUriForFile(holder.itemView.context, "com.example.newapp.fileprovider", localFile)

                val intent = Intent(Intent.ACTION_VIEW)
                intent.setDataAndType(fileProviderUri, "application/pdf") // Replace "application/pdf" with the MIME type of the file
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_GRANT_READ_URI_PERMISSION

                // Add read permission for the Uri to the Intent
                val resInfoList = holder.itemView.context.packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)
                for (resolveInfo in resInfoList) {
                    val packageName = resolveInfo.activityInfo.packageName
                    holder.itemView.context.grantUriPermission(packageName, fileProviderUri, Intent.FLAG_GRANT_READ_URI_PERMISSION)
                }

                // Start the activity with the Intent
                if (intent.resolveActivity(holder.itemView.context.packageManager) != null) {
                    holder.itemView.context.startActivity(intent)
                } else {
                    Toast.makeText(holder.itemView.context, "No app available to handle the file", Toast.LENGTH_SHORT).show()
                }

            }.addOnFailureListener {
                // Handle any errors that occur during file download
            }
        }


        holder.coverLetter.setOnClickListener{
            val storageRef = FirebaseStorage.getInstance().getReferenceFromUrl(applicant.getCoverLetter())

            val localFile = File.createTempFile("temp", "pdf")
            storageRef.getFile(localFile).addOnSuccessListener {
                // File has been downloaded to local file
                // Read the file from local file using any method you prefer
                val intent = Intent(Intent.ACTION_VIEW)
                val fileProviderUri = FileProvider.getUriForFile(holder.itemView.context, "com.example.newapp.fileprovider", localFile)
                intent.setDataAndType(fileProviderUri, "application/pdf") // Replace "application/pdf" with the MIME type of the file
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_GRANT_READ_URI_PERMISSION

                // Check if there is an app available to open the document
                val resInfoList = holder.itemView.context.packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)
                if (resInfoList.isNotEmpty()) {
                    // Add read permission for the Uri to the Intent
                    for (resolveInfo in resInfoList) {
                        val packageName = resolveInfo.activityInfo.packageName
                        holder.itemView.context.grantUriPermission(packageName, fileProviderUri, Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    }

                    // Start the activity with the Intent
                    holder.itemView.context.startActivity(intent)
                } else {
                    Toast.makeText(holder.itemView.context, "No app available to open this document", Toast.LENGTH_SHORT).show()
                }
            }.addOnFailureListener {
                // Handle any errors that occur during file download
            }
        }



    }

    override fun getItemCount(): Int {
        return mApplicants.size
    }

    class ViewHolder (@Nonnull itemview: View) : RecyclerView.ViewHolder(itemview)
    {
        var applicantimage: CircleImageView = itemview.findViewById(R.id.recruiter_student_app_dp)
        var applicantName: TextView = itemview.findViewById(R.id.recruiter_student_app_name)
        var applicantUniversity: TextView = itemview.findViewById(R.id.recruiter_student_app_university)
        var viewProfile: Button = itemview.findViewById(R.id.recruiter_student_app_profile_btn)
        var resume: Button = itemview.findViewById(R.id.recruiter_student_app_resume)
        var coverLetter: Button = itemview.findViewById(R.id.recruiter_student_app_cover_letter)
        var accept: Button = itemview.findViewById(R.id.recruiter_student_app_approve_btn)
        var reject: Button = itemview.findViewById(R.id.recruiter_student_app_reject_btn)
    }
}