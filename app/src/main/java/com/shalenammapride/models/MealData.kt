package com.shalenammapride.models

/**
 * MealData - Data model for a daily meal entry.
 * Matches the structure stored in Firestore's "meals" collection.
 */
data class MealData(
    val description: String = "",  // Meal description (e.g., "Rice, dal, vegetables")
    val imageUrl: String = "",     // URL to meal image in Firebase Storage
    val date: String = "",         // Date string (e.g., "10 May 2026")
    val timestamp: Long = 0L       // Unix timestamp for sorting
)
