package com.shalenammapride.utils

/**
 * Constants - Centralized place for all constant values used in the app.
 * This makes it easy to change values in one place if needed.
 */
object Constants {

    // SharedPreferences name (used to store user role locally)
    const val PREFS_NAME = "ShalePridePrefs"

    // Key for storing user role (admin or parent) in SharedPreferences
    const val KEY_ROLE = "user_role"

    // Firestore collection names
    const val COLLECTION_MEALS = "meals"         // Daily meal data
    const val COLLECTION_STUDENTS = "students"   // Student achievements
    const val COLLECTION_FEEDBACK = "feedback"   // Parent feedback

    // User roles
    const val ROLE_ADMIN = "admin"
    const val ROLE_PARENT = "parent"
}
