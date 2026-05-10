package com.shalenammapride.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.shalenammapride.R
import com.shalenammapride.databinding.ItemStudentBinding
import com.shalenammapride.models.Student

/**
 * StudentAdapter - RecyclerView adapter for displaying student achievements.
 * Each item shows: student photo, name, class, and achievement.
 */
class StudentAdapter(
    private val context: Context,
    private val studentList: MutableList<Student>
) : RecyclerView.Adapter<StudentAdapter.StudentViewHolder>() {

    inner class StudentViewHolder(val binding: ItemStudentBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        val binding = ItemStudentBinding.inflate(
            LayoutInflater.from(context), parent, false
        )
        return StudentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        val student = studentList[position]

        holder.binding.apply {
            // Set student info
            tvStudentName.text = student.name
            tvStudentClass.text = student.className.ifEmpty { "Student" }
            tvStudentAchievement.text = student.achievement

            // Load student photo with Glide
            if (student.photoUrl.isNotEmpty()) {
                Glide.with(context)
                    .load(student.photoUrl)
                    .placeholder(R.drawable.ic_student_placeholder)
                    .circleCrop()
                    .into(imgStudentPhoto)
                imgStudentPhoto.visibility = View.VISIBLE
            } else {
                // Show default placeholder if no photo
                imgStudentPhoto.setImageResource(R.drawable.ic_student_placeholder)
                imgStudentPhoto.visibility = View.VISIBLE
            }
        }
    }

    override fun getItemCount() = studentList.size
}
