package com.shalenammapride.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.shalenammapride.adapters.FacilityAdapter
import com.shalenammapride.databinding.ActivityFacilitiesBinding
import com.shalenammapride.models.Facility

/**
 * FacilitiesActivity - Displays school facilities in a grid.
 * For demo purposes, facility data is hardcoded with sample images.
 * In production, you can fetch this from Firebase Firestore.
 */
class FacilitiesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFacilitiesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFacilitiesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Back button
        binding.btnBack.setOnClickListener { finish() }

        // Create a list of facilities (demo data)
        val facilityList = getFacilitiesList()

        // Set up RecyclerView with a 2-column grid
        val adapter = FacilityAdapter(this, facilityList)
        binding.recyclerFacilities.layoutManager = GridLayoutManager(this, 2)
        binding.recyclerFacilities.adapter = adapter
    }

    /**
     * Returns a hardcoded list of school facilities for demo.
     * You can replace these with real Firebase data later.
     */
    private fun getFacilitiesList(): List<Facility> {
        return listOf(
            Facility(
                name = "Classrooms",
                description = "Well-lit, ventilated classrooms",
                emoji = "🏫",
                // Using a placeholder color resource for demo
                colorResId = com.shalenammapride.R.color.facilityBlue
            ),
            Facility(
                name = "Computer Lab",
                description = "30 computers with internet",
                emoji = "💻",
                colorResId = com.shalenammapride.R.color.facilityGreen
            ),
            Facility(
                name = "Library",
                description = "500+ books for students",
                emoji = "📚",
                colorResId = com.shalenammapride.R.color.facilityOrange
            ),
            Facility(
                name = "Playground",
                description = "Spacious outdoor playground",
                emoji = "⚽",
                colorResId = com.shalenammapride.R.color.facilityPurple
            ),
            Facility(
                name = "Science Lab",
                description = "Fully equipped science lab",
                emoji = "🔬",
                colorResId = com.shalenammapride.R.color.facilityRed
            ),
            Facility(
                name = "Canteen",
                description = "Hygienic mid-day meals",
                emoji = "🍱",
                colorResId = com.shalenammapride.R.color.facilityYellow
            )
        )
    }
}
