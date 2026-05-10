package com.shalenammapride.activities

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.shalenammapride.databinding.ActivityAdminAddStudentBinding
import com.shalenammapride.utils.Constants
import java.util.UUID

/**
 * AdminAddStudentActivity - Admin can add a student achievement.
 * Fields: Student Name, Class, Achievement, Photo (optional).
 * Data is saved to Firestore, photo to Firebase Storage.
 */
class AdminAddStudentActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminAddStudentBinding
    private val db = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()
    private var selectedImageUri: Uri? = null
    private val IMAGE_PICK_CODE = 2001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminAddStudentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Back button
        binding.btnBack.setOnClickListener { finish() }

        // Pick photo from gallery
        binding.btnPickPhoto.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            startActivityForResult(intent, IMAGE_PICK_CODE)
        }

        // Save student button
        binding.btnSave.setOnClickListener {
            saveStudent()
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_PICK_CODE && resultCode == Activity.RESULT_OK) {
            selectedImageUri = data?.data
            binding.imgStudentPreview.setImageURI(selectedImageUri)
            binding.imgStudentPreview.visibility = View.VISIBLE
        }
    }

    /**
     * Validates inputs and saves student achievement to Firestore.
     * If a photo is selected, uploads to Firebase Storage first.
     */
    private fun saveStudent() {
        val name = binding.etStudentName.text.toString().trim()
        val className = binding.etClassName.text.toString().trim()
        val achievement = binding.etAchievement.text.toString().trim()

        if (name.isEmpty()) {
            binding.etStudentName.error = "Enter student name"
            return
        }
        if (achievement.isEmpty()) {
            binding.etAchievement.error = "Enter achievement"
            return
        }

        binding.progressBar.visibility = View.VISIBLE
        binding.btnSave.isEnabled = false

        if (selectedImageUri != null) {
            // Upload photo first, then save to Firestore
            val imageName = "students/${UUID.randomUUID()}.jpg"
            val storageRef = storage.reference.child(imageName)

            storageRef.putFile(selectedImageUri!!)
                .addOnSuccessListener {
                    storageRef.downloadUrl.addOnSuccessListener { url ->
                        saveToFirestore(name, className, achievement, url.toString())
                    }
                }
                .addOnFailureListener { e ->
                    binding.progressBar.visibility = View.GONE
                    binding.btnSave.isEnabled = true
                    Toast.makeText(this, "Photo upload failed: ${e.message}", Toast.LENGTH_LONG).show()
                }
        } else {
            // No photo, save with empty URL
            saveToFirestore(name, className, achievement, "")
        }
    }

    private fun saveToFirestore(name: String, className: String, achievement: String, photoUrl: String) {
        val studentData = hashMapOf(
            "name" to name,
            "className" to className,
            "achievement" to achievement,
            "photoUrl" to photoUrl,
            "timestamp" to System.currentTimeMillis()
        )

        db.collection(Constants.COLLECTION_STUDENTS)
            .add(studentData)
            .addOnSuccessListener {
                binding.progressBar.visibility = View.GONE
                Toast.makeText(this, "Student added! ⭐", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener { e ->
                binding.progressBar.visibility = View.GONE
                binding.btnSave.isEnabled = true
                Toast.makeText(this, "Failed: ${e.message}", Toast.LENGTH_LONG).show()
            }
    }
}
