package com.shalenammapride.models

/**
 * GeminiRequest - The request body format for Gemini API.
 * This follows the Gemini REST API structure.
 *
 * API: POST https://generativelanguage.googleapis.com/v1beta/models/gemini-pro:generateContent
 */
data class GeminiRequest(
    val contents: List<Content>
)

data class Content(
    val parts: List<Part>
)

data class Part(
    val text: String
)

/**
 * GeminiResponse - The response format from Gemini API.
 * We extract the generated text from candidates[0].content.parts[0].text
 */
data class GeminiResponse(
    val candidates: List<Candidate>?
)

data class Candidate(
    val content: ContentResponse?
)

data class ContentResponse(
    val parts: List<PartResponse>?
)

data class PartResponse(
    val text: String?
)
