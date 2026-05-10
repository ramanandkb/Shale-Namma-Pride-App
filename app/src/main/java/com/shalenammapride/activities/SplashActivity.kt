package com.shalenammapride.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.shalenammapride.R
import com.shalenammapride.databinding.ActivitySplashBinding

/**
 * SplashActivity - The first screen shown when the app launches.
 * Displays the app logo and name for 2.5 seconds, then navigates to Login or Home.
 */
class SplashActivity : AppCompatActivity() {

    // View binding for easy access to UI elements
    private lateinit var binding: ActivitySplashBinding

    // Firebase Authentication instance
    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Load and start the bounce animation for the logo
        val bounceAnim = AnimationUtils.loadAnimation(this, R.anim.bounce)
        binding.imgLogo.startAnimation(bounceAnim)

        // Load and start the fade-in animation for the title text
        val fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        binding.tvAppName.startAnimation(fadeIn)
        binding.tvTagline.startAnimation(fadeIn)

        // Wait 2.5 seconds, then decide which screen to go to
        Handler(Looper.getMainLooper()).postDelayed({
            navigateToNextScreen()
        }, 2500)
    }

    /**
     * Check if a user is already logged in.
     * If yes, go to Home. If no, go to Login.
     */
    private fun navigateToNextScreen() {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            // User already logged in -> go to Home
            startActivity(Intent(this, HomeActivity::class.java))
        } else {
            // No user logged in -> go to Login
            startActivity(Intent(this, LoginActivity::class.java))
        }
        // Close splash so user can't press Back to return here
        finish()
    }
}
