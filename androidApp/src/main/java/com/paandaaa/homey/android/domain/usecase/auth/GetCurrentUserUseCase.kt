package com.paandaaa.homey.android.domain.usecase.auth

import com.paandaaa.homey.android.domain.model.UserModel
import com.paandaaa.homey.android.domain.repository.AuthRepository
import javax.inject.Inject


class GetCurrentUserUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(): Result<UserModel?> {
        return authRepository.getCurrentUser()
    }
}