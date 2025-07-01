package com.paandaaa.homey.android.data.remote.model


data class GeminiResponse(
    val candidates: List<Candidate>?
)

data class Candidate(
    val content: Content?
)