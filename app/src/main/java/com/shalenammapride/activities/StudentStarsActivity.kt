package com.shalenammapride.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.shalenammapride.adapters.StudentAdapter
import com.shalenammapride.databinding.ActivityStudentStarsBinding
import com.shalenammapride.models.Student
import com.shalenammapride.utils.Constants

/**
 * StudentStarsActivity - Shows student achievements fetched from Firestore.
 * Admin sees a button to add a new student achievement.
 * Parents can browse all student stars.
 */
class StudentStarsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStudentStarsBinding
    private val db = FirebaseFirestore.getInstance()
    private val studentList = mutableListOf<Student>()
    private lateinit var adapter: StudentAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStudentStarsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Back button
        binding.btnBack.setOnClickListener { finish() }

        // Check user role
        val prefs = getSharedPreferences(Constants.PREFS_NAME, MODE_PRIVATE)
        val role = prefs.getString(Constants.KEY_ROLE, "parent")

        // Show Add button only for Admin
        if (role == "admin") {
            binding.btnAddStudent.visibility = View.VISIBLE
            binding.btnAddStudent.setOnClickListener {
                startActivity(Intent(this, AdminAddStudentActivity::class.java))
            }
        }

        // Set up RecyclerView
        adapter = StudentAdapter(this, studentList)
        binding.recyclerStudents.layoutManager = LinearLayoutManager(this)
        binding.recyclerStudents.adapter = adapter

        // Load students from Firestore
        loadStudents()
    }

    override fun onResume() {
        super.onResume()
        // Reload students when returning from add screen
        loadStudents()
    }

    /**
     * Fetches all student achievements from Firestore,
     * sorted by timestamp (newest first).
     */
    private fun loadStudents() {
        binding.progressBar.visibility = View.VISIBLE

        db.collection(Constants.COLLECTION_STUDENTS)
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { documents ->
                binding.progressBar.visibility = View.GONE
                studentList.clear()

                if (documents.isEmpty) {
                    binding.tvNoStudents.visibility = View.VISIBLE
                } else {
                    binding.tvNoStudents.visibility = View.GONE
                    for (doc in documents) {
                        val student = Student(
                            id = doc.id,
                            name = doc.getString("name") ?: "",
                            achievement = doc.getString("achievement") ?: "",
                            photoUrl = doc.getString("photoUrl") ?: "",
                            className = doc.getString("className") ?: ""
                        )
                        studentList.add(student)
                    }
                }
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener { e ->
                binding.progressBar.visibility = View.GONE
                Toast.makeText(this, "Failed to load students: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
