package com.shalenammapride.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.shalenammapride.databinding.ActivityLoginBinding
import com.shalenammapride.utils.Constants

/**
 * LoginActivity - Handles both Admin and Parent login using Firebase Authentication.
 * Admin: admin@shalenamma.com  (you can change in Firebase Console)
 * Parent: any registered email/password
 */
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val auth = FirebaseAuth.getInstance()

    // Track which login mode is selected: "admin" or "parent"
    private var selectedRole = "parent"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRoleSelection()
        setupLoginButton()
    }

    /**
     * Set up the Admin / Parent toggle buttons.
     * When Admin is clicked, show the admin email hint.
     */
    private fun setupRoleSelection() {
        // Parent button clicked
        binding.btnParent.setOnClickListener {
            selectedRole = "parent"
            binding.btnParent.alpha = 1.0f
            binding.btnAdmin.alpha = 0.5f
            binding.etEmail.hint = "Parent Email"
            binding.tvRoleInfo.text = "Login as Parent to view school updates"
        }

        // Admin button clicked
        binding.btnAdmin.setOnClickListener {
            selectedRole = "admin"
            binding.btnAdmin.alpha = 1.0f
            binding.btnParent.alpha = 0.5f
            binding.etEmail.hint = "Admin Email (admin@shalenamma.com)"
            binding.tvRoleInfo.text = "Login as Admin to manage school content"
        }
    }

    /**
     * Handle login button click.
     * Validate inputs and authenticate with Firebase.
     */
    private fun setupLoginButton() {
        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            // Basic validation
            if (email.isEmpty()) {
                binding.etEmail.error = "Please enter your email"
                return@setOnClickListener
            }
            if (password.isEmpty()) {
                binding.etPassword.error = "Please enter your password"
                return@setOnClickListener
            }

            // Show loading spinner
            binding.progressBar.visibility = View.VISIBLE
            binding.btnLogin.isEnabled = false

            // Sign in with Firebase Authentication
            auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    binding.progressBar.visibility = View.GONE

                    // Save the user role in SharedPreferences for later use
                    val prefs = getSharedPreferences(Constants.PREFS_NAME, MODE_PRIVATE)
                    prefs.edit().putString(Constants.KEY_ROLE, selectedRole).apply()

                    // Navigate to Home Dashboard
                    startActivity(Intent(this, HomeActivity::class.java))
                    finish()
                }
                .addOnFailureListener { e ->
                    binding.progressBar.visibility = View.GONE
                    binding.btnLogin.isEnabled = true
                    Toast.makeText(this, "Login failed: ${e.message}", Toast.LENGTH_LONG).show()
                }
        }

        // Register new user (for demo/testing)
        binding.tvRegister.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Enter email and password to register", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            binding.progressBar.visibility = View.VISIBLE
            auth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(this, "Account created! Please login.", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { e ->
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(this, "Registration failed: ${e.message}", Toast.LENGTH_LONG).show()
                }
        }
    }
}
