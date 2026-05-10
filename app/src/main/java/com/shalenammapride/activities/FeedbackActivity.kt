package com.shalenammapride.activities

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.shalenammapride.databinding.ActivityFeedbackBinding
import com.shalenammapride.utils.Constants
import com.shalenammapride.api.GeminiApiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * FeedbackActivity - Parents can submit feedback about the school.
 * Supports anonymous feedback.
 * Also features the "Translate to Kannada" AI button using Gemini API.
 */
class FeedbackActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFeedbackBinding
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFeedbackBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Back button
        binding.btnBack.setOnClickListener { finish() }

        // Submit Feedback button
        binding.btnSubmitFeedback.setOnClickListener {
            submitFeedback()
        }

        // ✨ AI Translation Button - Translate feedback text to Kannada using Gemini
        binding.btnTranslate.setOnClickListener {
            translateToKannada()
        }
    }

    /**
     * Submits feedback to Firestore.
     * If "Anonymous" checkbox is checked, saves "Anonymous" as the name.
     */
    private fun submitFeedback() {
        val feedbackText = binding.etFeedback.text.toString().trim()

        if (feedbackText.isEmpty()) {
            binding.etFeedback.error = "Please write your feedback"
            return
        }

        // Determine the name to use
        val name = if (binding.cbAnonymous.isChecked) {
            "Anonymous"
        } else {
            binding.etParentName.text.toString().trim().ifEmpty { "Parent" }
        }

        binding.progressBarFeedback.visibility = View.VISIBLE
        binding.btnSubmitFeedback.isEnabled = false

        val feedbackData = hashMapOf(
            "name" to name,
            "feedback" to feedbackText,
            "isAnonymous" to binding.cbAnonymous.isChecked,
            "timestamp" to System.currentTimeMillis()
        )

        db.collection(Constants.COLLECTION_FEEDBACK)
            .add(feedbackData)
            .addOnSuccessListener {
                binding.progressBarFeedback.visibility = View.GONE
                binding.btnSubmitFeedback.isEnabled = true
                Toast.makeText(this, "Thank you for your feedback! 🙏", Toast.LENGTH_SHORT).show()
                // Clear the form
                binding.etFeedback.text?.clear()
                binding.etParentName.text?.clear()
                binding.cbAnonymous.isChecked = false
            }
            .addOnFailureListener { e ->
                binding.progressBarFeedback.visibility = View.GONE
                binding.btnSubmitFeedback.isEnabled = true
                Toast.makeText(this, "Failed to submit: ${e.message}", Toast.LENGTH_LONG).show()
            }
    }

    /**
     * 🤖 Gemini AI Feature: Translate the entered feedback text to Kannada.
     * Uses Gemini API via Retrofit.
     */
    private fun translateToKannada() {
        val textToTranslate = binding.etFeedback.text.toString().trim()

        if (textToTranslate.isEmpty()) {
            Toast.makeText(this, "Please type some text to translate", Toast.LENGTH_SHORT).show()
            return
        }

        // Show loading
        binding.progressBarTranslate.visibility = View.VISIBLE
        binding.btnTranslate.isEnabled = false
        binding.tvKannadaTranslation.visibility = View.GONE

        // Call Gemini API in background thread using Coroutines
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val translatedText = GeminiApiService.translateToKannada(textToTranslate)

                // Update UI on main thread
                withContext(Dispatchers.Main) {
                    binding.progressBarTranslate.visibility = View.GONE
                    binding.btnTranslate.isEnabled = true
                    binding.tvKannadaTranslation.visibility = View.VISIBLE
                    binding.tvKannadaTranslation.text = "🇮🇳 Kannada: $translatedText"
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    binding.progressBarTranslate.visibility = View.GONE
                    binding.btnTranslate.isEnabled = true
                    Toast.makeText(
                        this@FeedbackActivity,
                        "Translation failed: ${e.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }
}
