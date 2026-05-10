package com.shalenammapride.models

/**
 * Student - Data model for a student achievement.
 * This is what gets stored in and retrieved from Firestore.
 */
data class Student(
    val id: String = "",           // Firestore document ID
    val name: String = "",         // Student's full name
    val achievement: String = "",  // What they achieved (e.g., "Won state chess championship")
    val photoUrl: String = "",     // URL to their photo in Firebase Storage
    val className: String = ""     // Class they are in (e.g., "Grade 5")
)
