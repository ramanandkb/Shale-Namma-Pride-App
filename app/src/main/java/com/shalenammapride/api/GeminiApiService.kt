package com.shalenammapride.api

import com.shalenammapride.models.Content
import com.shalenammapride.models.GeminiRequest
import com.shalenammapride.models.Part
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * GeminiApiService - Singleton that handles all Gemini API calls.
 *
 * How to get your API key:
 * 1. Go to https://makersuite.google.com/app/apikey
 * 2. Sign in with your Google account
 * 3. Click "Create API Key"
 * 4. Copy the key and paste it below
 *
 * IMPORTANT: For production apps, store the API key in local.properties
 * and read it via BuildConfig. Don't hardcode in production!
 */
object GeminiApiService {

    // ⚠️ REPLACE THIS WITH YOUR ACTUAL GEMINI API KEY
    private const val API_KEY = ""

    // Base URL for Gemini API
    private const val BASE_URL = "https://generativelanguage.googleapis.com/"

    // Create an OkHttp client with logging (helpful for debugging)
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .build()

    // Build Retrofit instance
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    // Create the API interface implementation
    private val geminiApi = retrofit.create(GeminiRetrofitInterface::class.java)

    /**
     * Translates English text to Kannada using Gemini AI.
     *
     * @param englishText - The text to translate
     * @return String - The Kannada translation
     * @throws Exception - If the API call fails
     */
    suspend fun translateToKannada(englishText: String): String {
        // Build the prompt for Gemini
        val prompt = "Translate the following English text to Kannada language. " +
                "Only provide the Kannada translation, nothing else: \"$englishText\""

        // Create the request body
        val request = GeminiRequest(
            contents = listOf(
                Content(
                    parts = listOf(Part(text = prompt))
                )
            )
        )

        // Make the API call (this is a suspend function, runs in background)
        val response = geminiApi.generateContent(API_KEY, request)

        // Extract the translated text from response
        return response.candidates
            ?.firstOrNull()
            ?.content
            ?.parts
            ?.firstOrNull()
            ?.text
            ?: throw Exception("No translation received from Gemini")
    }
}
