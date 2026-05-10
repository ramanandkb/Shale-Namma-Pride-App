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
import com.shalenammapride.databinding.ActivityAdminUploadMealBinding
import com.shalenammapride.utils.Constants
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

/**
 * AdminUploadMealActivity - Only accessible to Admin.
 * Allows admin to pick a meal image and add description,
 * then save it to Firebase Storage + Firestore.
 */
class AdminUploadMealActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminUploadMealBinding

    // Firebase references
    private val db = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()

    // Selected image URI from gallery
    private var selectedImageUri: Uri? = null

    // Request code for image picker
    private val IMAGE_PICK_CODE = 1001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminUploadMealBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Back button
        binding.btnBack.setOnClickListener { finish() }

        // Choose image from gallery
        binding.btnPickImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            startActivityForResult(intent, IMAGE_PICK_CODE)
        }

        // Upload button
        binding.btnUpload.setOnClickListener {
            uploadMeal()
        }
    }

    /**
     * Called after user selects an image from gallery.
     * Shows a preview of the selected image.
     */
    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_PICK_CODE && resultCode == Activity.RESULT_OK) {
            selectedImageUri = data?.data
            binding.imgMealPreview.setImageURI(selectedImageUri)
            binding.imgMealPreview.visibility = View.VISIBLE
        }
    }

    /**
     * Uploads the meal image to Firebase Storage,
     * then saves the description + image URL to Firestore.
     */
    private fun uploadMeal() {
        val description = binding.etMealDescription.text.toString().trim()

        if (description.isEmpty()) {
            binding.etMealDescription.error = "Please enter meal description"
            return
        }

        // Show loading
        binding.progressBar.visibility = View.VISIBLE
        binding.btnUpload.isEnabled = false

        val today = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(Date())

        if (selectedImageUri != null) {
            // Upload image to Firebase Storage first
            val imageName = "meals/${UUID.randomUUID()}.jpg"
            val storageRef = storage.reference.child(imageName)

            storageRef.putFile(selectedImageUri!!)
                .addOnSuccessListener {
                    // Get the download URL after upload
                    storageRef.downloadUrl.addOnSuccessListener { url ->
                        saveMealToFirestore(description, url.toString(), today)
                    }
                }
                .addOnFailureListener { e ->
                    binding.progressBar.visibility = View.GONE
                    binding.btnUpload.isEnabled = true
                    Toast.makeText(this, "Image upload failed: ${e.message}", Toast.LENGTH_LONG).show()
                }
        } else {
            // No image selected, save with empty URL
            saveMealToFirestore(description, "", today)
        }
    }

    /**
     * Saves meal data (description + image URL + date) to Firestore.
     */
    private fun saveMealToFirestore(description: String, imageUrl: String, date: String) {
        val mealData = hashMapOf(
            "description" to description,
            "imageUrl" to imageUrl,
            "date" to date,
            "timestamp" to System.currentTimeMillis()
        )

        db.collection(Constants.COLLECTION_MEALS)
            .add(mealData)
            .addOnSuccessListener {
                binding.progressBar.visibility = View.GONE
                Toast.makeText(this, "Meal uploaded successfully! 🍽️", Toast.LENGTH_SHORT).show()
                finish()  // Go back to Meal screen
            }
            .addOnFailureListener { e ->
                binding.progressBar.visibility = View.GONE
                binding.btnUpload.isEnabled = true
                Toast.makeText(this, "Failed to save: ${e.message}", Toast.LENGTH_LONG).show()
            }
    }
}
