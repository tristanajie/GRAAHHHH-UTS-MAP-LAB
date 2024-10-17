package com.example.if570_lab_uts_muhammadtristanajibrilyannandipinto_00000078653.model

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.if570_lab_uts_muhammadtristanajibrilyannandipinto_00000078653.R
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale

class AbsensiAdapter(private val absensiList: List<Absensi>) : RecyclerView.Adapter<AbsensiAdapter.AbsensiViewHolder>() {

    inner class AbsensiViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.absensiImage)
        private val dateTextView: TextView = itemView.findViewById(R.id.absensiDate)
        private val timeTextView: TextView = itemView.findViewById(R.id.absensiTime)

        fun bind(attendance: Absensi) {
            // Handle null values for date
            if (attendance.date != null) {
                try {
                    val parsedDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(attendance.date)
                    dateTextView.text = SimpleDateFormat("dd MMMM yyyy", Locale("en", "ID")).format(parsedDate)
                } catch (e: ParseException) {
                    dateTextView.text = "Unknown date"
                    Log.e("AbsensiAdapter", "Failed to parse date: ${attendance.date}", e)
                }
            } else {
                dateTextView.text = "Unknown date"
            }

            // Handle null values for time
            timeTextView.text = attendance.time ?: "Unknown time"

            // Load image with proper null handling
            attendance.imageUrl?.let {
                Glide.with(itemView.context)
                    .load(it)
                    .placeholder(R.drawable.fikri)
                    .error(R.drawable.fikri) // Display error placeholder if image fails to load
                    .into(imageView)
            } ?: run {
                imageView.setImageResource(R.drawable.fikri) // Default image if URL is null
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AbsensiViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_absensi, parent, false)
        return AbsensiViewHolder(view)
    }

    override fun onBindViewHolder(holder: AbsensiViewHolder, position: Int) {
        holder.bind(absensiList[position])
    }

    override fun getItemCount(): Int = absensiList.size
}
