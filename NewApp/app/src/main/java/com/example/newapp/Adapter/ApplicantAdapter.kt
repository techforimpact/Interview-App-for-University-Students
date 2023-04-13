package com.example.newapp.Adapter

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.newapp.Fragments_Student.ProfileStudentFragment
import com.example.newapp.Model.Applicant
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
            val pref = mContext.getSharedPreferences("PREFS" , Context.MODE_PRIVATE).edit()
            pref.putString("profileId" , applicant.getStudentUid())
            pref.apply()

            val bundle = Bundle()
            bundle.putParcelable("applicant", applicant)
            ProfileStudentFragment().arguments = bundle


            (mContext as FragmentActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.recruiter_fragment_container , ProfileStudentFragment() ).commit()
        }


        holder.reject.setOnClickListener{

            val query = FirebaseDatabase.getInstance().getReference("Applicants")
                .orderByChild("uid")
                .equalTo(applicant.getUId())

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


        }


        holder.accept.setOnClickListener{

            val query = FirebaseDatabase.getInstance().getReference("Applicants")
                .orderByChild("uid")
                .equalTo(applicant.getUId())

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

        }

        holder.resume.setOnClickListener{
            val storageRef = FirebaseStorage.getInstance().getReferenceFromUrl(applicant.getResume())

            val localFile = File.createTempFile("temp", "pdf")
            storageRef.getFile(localFile).addOnSuccessListener {
                // File has been downloaded to local file
                // Read the file from local file using any method you prefer
                // For example, you can read the file as a byte array:
                val fileData = localFile.readBytes()

                val intent = Intent(Intent.ACTION_VIEW)
                val fileUri = Uri.fromFile(localFile)
                intent.setDataAndType(fileUri, "application/pdf") // Replace "application/pdf" with the MIME type of the file
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_GRANT_READ_URI_PERMISSION
                holder.itemView.context.startActivity(intent) // use the context of the item view

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
                val fileUri = Uri.fromFile(localFile)
                intent.setDataAndType(fileUri, "application/pdf") // Replace "application/pdf" with the MIME type of the file
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_GRANT_READ_URI_PERMISSION
                holder.itemView.context.startActivity(intent) // use the context of the item view

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