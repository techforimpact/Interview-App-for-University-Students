package com.example.newapp.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.newapp.Model.Certificate
import com.example.newapp.R

class CertificatesAdapter(
    private val certificates: List<Certificate>,
    private val onItemClick: (Certificate) -> Unit
) : RecyclerView.Adapter<CertificatesAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val certificateNameTextView: TextView = itemView.findViewById(R.id.certificate_name_text_view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_certificate, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val certificate = certificates[position]
        holder.certificateNameTextView.text = certificate.getName()
        holder.itemView.setOnClickListener { onItemClick(certificate) }
    }

    override fun getItemCount() = certificates.size
}
