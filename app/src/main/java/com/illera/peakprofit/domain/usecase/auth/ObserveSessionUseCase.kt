package com.illera.peakprofit.domain.usecase.auth

import com.illera.peakprofit.domain.entity.UserSession
import com.illera.peakprofit.domain.repository.AuthRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.StateFlow

class ObserveSessionUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    operator fun invoke(): StateFlow<UserSession?> = authRepository.session
}
