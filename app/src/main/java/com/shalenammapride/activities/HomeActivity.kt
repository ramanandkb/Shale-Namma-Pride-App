package com.shalenammapride.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.shalenammapride.databinding.ActivityHomeBinding
import com.shalenammapride.utils.Constants

/**
 * HomeActivity - The main dashboard shown after login.
 * Contains 4 card options: Meal, Facilities, Student Stars, Feedback.
 * Also shows a greeting with the user's role.
 */
class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get user role from SharedPreferences
        val prefs = getSharedPreferences(Constants.PREFS_NAME, MODE_PRIVATE)
        val role = prefs.getString(Constants.KEY_ROLE, "parent") ?: "parent"

        // Update the greeting based on role
        if (role == "admin") {
            binding.tvGreeting.text = "Welcome, Admin! 👋"
            binding.tvSubGreeting.text = "Manage school content from here"
        } else {
            binding.tvGreeting.text = "Welcome, Parent! 👋"
            binding.tvSubGreeting.text = "Stay connected with your child's school"
        }

        // Set up card click listeners
        setupCardClicks()

        // Logout button
        binding.btnLogout.setOnClickListener {
            auth.signOut()
            // Clear saved role
            prefs.edit().clear().apply()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    /**
     * Set up navigation when each dashboard card is clicked.
     */
    private fun setupCardClicks() {
        // Daily Meal card
        binding.cardMeal.setOnClickListener {
            startActivity(Intent(this, MealActivity::class.java))
        }

        // Facilities card
        binding.cardFacilities.setOnClickListener {
            startActivity(Intent(this, FacilitiesActivity::class.java))
        }

        // Student Stars card
        binding.cardStudentStars.setOnClickListener {
            startActivity(Intent(this, StudentStarsActivity::class.java))
        }

        // Feedback card
        binding.cardFeedback.setOnClickListener {
            startActivity(Intent(this, FeedbackActivity::class.java))
        }
    }
}
