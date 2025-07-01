package com.paandaaa.homey.android.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class UserModel(
    val id: String,
    val email: String,
    val name: String,
    val profilePictureUrl: String,
)