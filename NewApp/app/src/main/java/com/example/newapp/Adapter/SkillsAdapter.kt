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
import com.example.newapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.crashlytics.buildtools.reloc.javax.annotation.Nonnull

class SkillsAdapter(private var mContext: Context,
                    private var mSkills: List<String>,
                    private var isFragment: Boolean = false, private val onClickListener: (String) -> Unit) : RecyclerView.Adapter<SkillsAdapter.ViewHolder>()

{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SkillsAdapter.ViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.skills_list , parent , false)
        return SkillsAdapter.ViewHolder(view)
    }

    override fun onBindViewHolder(holder: SkillsAdapter.ViewHolder, position: Int) {

        holder.bind(mSkills[position], onClickListener)
        val skill = mSkills[position]

        holder.skillTextView.text = skill

    }

    override fun getItemCount(): Int {
        return mSkills.size
    }

    class ViewHolder (@Nonnull itemview: View) : RecyclerView.ViewHolder(itemview)
    {
        val skillTextView: TextView = itemview.findViewById(R.id.student_skill_view)

        fun bind(skill: String, onClickListener: (String) -> Unit) {
            skillTextView.text = skill

            // Set up OnClickListener for the skillTextView
            skillTextView.setOnClickListener {
                onClickListener(skill)
            }
        }


    }
}