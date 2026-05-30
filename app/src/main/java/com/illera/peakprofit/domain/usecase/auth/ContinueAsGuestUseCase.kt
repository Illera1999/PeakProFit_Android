package com.illera.peakprofit.domain.usecase.auth

import com.illera.peakprofit.domain.repository.AuthRepository
import javax.inject.Inject

class ContinueAsGuestUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    operator fun invoke() {
        authRepository.continueAsGuest()
    }
}
