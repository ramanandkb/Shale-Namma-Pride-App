package com.shalenammapride.models

/**
 * Facility - Data model for a school facility card.
 * Displayed in a grid in FacilitiesActivity.
 */
data class Facility(
    val name: String,              // Name of the facility (e.g., "Library")
    val description: String,       // Brief description
    val emoji: String,             // Emoji icon for visual appeal
    val colorResId: Int            // Background color resource ID
)
