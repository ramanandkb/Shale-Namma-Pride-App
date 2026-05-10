package com.shalenammapride.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.shalenammapride.databinding.ActivityMealBinding
import com.shalenammapride.utils.Constants

/**
 * MealActivity - Shows today's meal to parents.
 * Admin sees a button to upload/update the meal.
 * Uses Firebase Firestore to fetch the latest meal data.
 */
class MealActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMealBinding
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMealBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Back button
        binding.btnBack.setOnClickListener { finish() }

        // Check user role
        val prefs = getSharedPreferences(Constants.PREFS_NAME, MODE_PRIVATE)
        val role = prefs.getString(Constants.KEY_ROLE, "parent")

        // Show Upload button only for Admin
        if (role == "admin") {
            binding.btnUploadMeal.visibility = View.VISIBLE
            binding.btnUploadMeal.setOnClickListener {
                startActivity(Intent(this, AdminUploadMealActivity::class.java))
            }
        } else {
            binding.btnUploadMeal.visibility = View.GONE
        }

        // Load the latest meal from Firestore
        loadLatestMeal()
    }

    override fun onResume() {
        super.onResume()
        // Reload meal when coming back from upload screen
        loadLatestMeal()
    }

    /**
     * Fetches the most recently added meal from Firestore.
     * Meals are sorted by timestamp (newest first).
     */
    private fun loadLatestMeal() {
        binding.progressBar.visibility = View.VISIBLE
        binding.cardMealContent.visibility = View.GONE

        db.collection(Constants.COLLECTION_MEALS)
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .limit(1)
            .get()
            .addOnSuccessListener { documents ->
                binding.progressBar.visibility = View.GONE

                if (documents.isEmpty) {
                    binding.tvNoMeal.visibility = View.VISIBLE
                    binding.cardMealContent.visibility = View.GONE
                } else {
                    binding.tvNoMeal.visibility = View.GONE
                    binding.cardMealContent.visibility = View.VISIBLE

                    val doc = documents.documents[0]
                    val description = doc.getString("description") ?: "No description"
                    val imageUrl = doc.getString("imageUrl") ?: ""
                    val date = doc.getString("date") ?: "Today"

                    // Set meal info to UI
                    binding.tvMealDate.text = "📅 $date"
                    binding.tvMealDescription.text = description

                    // Load meal image using Glide
                    if (imageUrl.isNotEmpty()) {
                        Glide.with(this)
                            .load(imageUrl)
                            .placeholder(com.shalenammapride.R.drawable.ic_meal_placeholder)
                            .into(binding.imgMeal)
                    }
                }
            }
            .addOnFailureListener { e ->
                binding.progressBar.visibility = View.GONE
                Toast.makeText(this, "Failed to load meal: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
