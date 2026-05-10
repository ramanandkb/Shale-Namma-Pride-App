package com.shalenammapride.api

import com.shalenammapride.models.GeminiRequest
import com.shalenammapride.models.GeminiResponse
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

/**
 * GeminiRetrofitInterface - Defines the Retrofit API endpoint for Gemini.
 * The API key is passed as a query parameter.
 */
interface GeminiRetrofitInterface {

    /**
     * POST request to Gemini's generateContent endpoint.
     *
     * @param apiKey - Your Gemini API key from Google AI Studio
     * @param request - The request body containing the prompt
     * @return GeminiResponse - Contains the generated text
     */
    @POST("v1beta/models/gemini-pro:generateContent")
    suspend fun generateContent(
        @Query("key") apiKey: String,
        @Body request: GeminiRequest
    ): GeminiResponse
}
