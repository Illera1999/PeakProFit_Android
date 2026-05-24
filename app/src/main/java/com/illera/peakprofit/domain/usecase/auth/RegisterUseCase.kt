package com.illera.peakprofit.domain.usecase.auth

import com.illera.peakprofit.domain.repository.AuthRepository
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String) {
        authRepository.register(email, password)
    }
}
