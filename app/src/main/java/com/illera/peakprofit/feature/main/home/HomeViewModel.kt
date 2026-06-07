package com.illera.peakprofit.feature.main.home

import android.media.MediaPlayer
import android.media.MediaRecorder
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.illera.peakprofit.R
import com.illera.peakprofit.domain.entity.AuthState
import com.illera.peakprofit.domain.repository.AuthRepository
import com.illera.peakprofit.domain.repository.MotivationalAudioRepository
import com.illera.peakprofit.domain.usecase.auth.ObserveSessionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val observeSessionUseCase: ObserveSessionUseCase,
    private val authRepository: AuthRepository,
    private val motivationalAudioRepository: MotivationalAudioRepository
) : ViewModel() {
    private companion object {
        const val MAX_RECORDING_SECONDS = 60
    }

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()
    private var currentOwnerKey: String? = null
    private var mediaRecorder: MediaRecorder? = null
    private var mediaPlayer: MediaPlayer? = null
    private var countdownJob: Job? = null

    init {
        viewModelScope.launch {
            observeSessionUseCase().collect { authState ->
                when (authState) {
                    is AuthState.Authenticated -> {
                        val session = authState.session
                        val displayName = session.email?.substringBefore("@")
                            ?.takeIf { it.isNotBlank() }
                            .orEmpty()
                        currentOwnerKey = session.uid
                        _uiState.value = _uiState.value.copy(
                            isAuthenticated = true,
                            isGuest = false,
                            userName = displayName,
                            userEmail = session.email ?: session.uid
                        )
                        refreshAudioState()
                    }
                    AuthState.Guest -> {
                        stopPlayback()
                        stopRecording(saveRecording = false)
                        currentOwnerKey = null
                        _uiState.value = _uiState.value.copy(
                            isAuthenticated = false,
                            isGuest = true,
                            userName = "",
                            userEmail = "",
                            hasMotivationalAudio = false,
                            isRecordingMotivationalAudio = false,
                            isPlayingMotivationalAudio = false,
                            recordingSecondsRemaining = MAX_RECORDING_SECONDS,
                            audioStatusMessage = ""
                        )
                    }
                    else -> {
                        stopPlayback()
                        stopRecording(saveRecording = false)
                        currentOwnerKey = null
                        _uiState.value = _uiState.value.copy(
                            isAuthenticated = false,
                            isGuest = false,
                            userName = "",
                            userEmail = "",
                            hasMotivationalAudio = false,
                            isRecordingMotivationalAudio = false,
                            isPlayingMotivationalAudio = false,
                            recordingSecondsRemaining = MAX_RECORDING_SECONDS,
                            audioStatusMessage = ""
                        )
                    }
                }
            }
        }
    }

    fun logout() {
        authRepository.signOut()
    }

    fun onRecordPermissionDenied() {
        if (!_uiState.value.isAuthenticated) return
        _uiState.value = _uiState.value.copy(
            audioStatusMessage = "Necesitas permitir el micrófono para grabar tu mensaje."
        )
    }

    fun startRecording() {
        if (!_uiState.value.isAuthenticated) return
        val ownerKey = currentOwnerKey ?: return
        if (_uiState.value.isRecordingMotivationalAudio) return
        stopPlayback()
        // Se reemplaza cualquier temporal previo antes de iniciar una nueva captura.
        motivationalAudioRepository.discardTempAudio(ownerKey)
        val tempFile = motivationalAudioRepository.getTempAudioFile(ownerKey)

        runCatching {
            @Suppress("DEPRECATION")
            MediaRecorder().apply {
                setAudioSource(MediaRecorder.AudioSource.MIC)
                setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
                setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
                setOutputFile(tempFile.absolutePath)
                setMaxDuration(MAX_RECORDING_SECONDS * 1_000)
                setOnInfoListener { _, what, _ ->
                    if (what == MediaRecorder.MEDIA_RECORDER_INFO_MAX_DURATION_REACHED) {
                        viewModelScope.launch { stopRecording(saveRecording = true) }
                    }
                }
                prepare()
                start()
            }
        }.onSuccess { recorder ->
            mediaRecorder = recorder
            _uiState.value = _uiState.value.copy(
                isRecordingMotivationalAudio = true,
                isPlayingMotivationalAudio = false,
                recordingSecondsRemaining = MAX_RECORDING_SECONDS,
                audioStatusMessage = "Grabando mensaje motivacional..."
            )
            startCountdown()
        }.onFailure {
            motivationalAudioRepository.discardTempAudio(ownerKey)
            _uiState.value = _uiState.value.copy(
                audioStatusMessage = "No se pudo iniciar la grabación."
            )
        }
    }

    fun stopRecording(saveRecording: Boolean = true) {
        if (!_uiState.value.isAuthenticated && mediaRecorder == null) return
        val ownerKey = currentOwnerKey ?: return
        val recorder = mediaRecorder ?: return
        mediaRecorder = null
        countdownJob?.cancel()
        countdownJob = null

        val stopSucceeded = runCatching {
            recorder.stop()
        }.isSuccess
        recorder.reset()
        recorder.release()

        if (saveRecording && stopSucceeded) {
            // Solo se promociona el temporal a definitivo cuando `stop()` ha cerrado bien el contenedor.
            motivationalAudioRepository.commitTempAudio(ownerKey)
            _uiState.value = _uiState.value.copy(
                isRecordingMotivationalAudio = false,
                hasMotivationalAudio = motivationalAudioRepository.hasAudio(ownerKey),
                recordingSecondsRemaining = MAX_RECORDING_SECONDS,
                audioStatusMessage = "Mensaje motivacional guardado."
            )
        } else {
            motivationalAudioRepository.discardTempAudio(ownerKey)
            _uiState.value = _uiState.value.copy(
                isRecordingMotivationalAudio = false,
                recordingSecondsRemaining = MAX_RECORDING_SECONDS,
                audioStatusMessage = if (saveRecording) {
                    "No se pudo guardar la grabación."
                } else {
                    ""
                }
            )
            refreshAudioState()
        }
    }

    fun togglePlayback() {
        if (!_uiState.value.isAuthenticated) return
        val ownerKey = currentOwnerKey ?: return
        if (_uiState.value.isRecordingMotivationalAudio) return
        if (_uiState.value.isPlayingMotivationalAudio) {
            stopPlayback()
            return
        }

        val audioFile = motivationalAudioRepository.getAudioFile(ownerKey)
        if (!audioFile.exists()) return

        runCatching {
            MediaPlayer().apply {
                setDataSource(audioFile.absolutePath)
                setOnCompletionListener {
                    stopPlayback()
                }
                prepare()
                start()
            }
        }.onSuccess { player ->
            mediaPlayer = player
            _uiState.value = _uiState.value.copy(
                isPlayingMotivationalAudio = true,
                audioStatusMessage = "Reproduciendo tu mensaje motivacional."
            )
        }.onFailure {
            _uiState.value = _uiState.value.copy(
                isPlayingMotivationalAudio = false,
                audioStatusMessage = "No se pudo reproducir el audio."
            )
        }
    }

    fun deleteMotivationalAudio() {
        if (!_uiState.value.isAuthenticated) return
        val ownerKey = currentOwnerKey ?: return
        stopPlayback()
        if (_uiState.value.isRecordingMotivationalAudio) {
            stopRecording(saveRecording = false)
        }
        motivationalAudioRepository.deleteAudio(ownerKey)
        _uiState.value = _uiState.value.copy(
            hasMotivationalAudio = false,
            isPlayingMotivationalAudio = false,
            recordingSecondsRemaining = MAX_RECORDING_SECONDS,
            audioStatusMessage = "Mensaje motivacional eliminado."
        )
    }

    private fun refreshAudioState() {
        val ownerKey = currentOwnerKey ?: return
        _uiState.value = _uiState.value.copy(
            hasMotivationalAudio = motivationalAudioRepository.hasAudio(ownerKey),
            isPlayingMotivationalAudio = false,
            isRecordingMotivationalAudio = false,
            recordingSecondsRemaining = MAX_RECORDING_SECONDS,
            audioStatusMessage = if (motivationalAudioRepository.hasAudio(ownerKey)) {
                "Ya tienes un mensaje motivacional guardado."
            } else {
                "Graba un mensaje de hasta 1 minuto."
            }
        )
    }

    private fun startCountdown() {
        countdownJob?.cancel()
        countdownJob = viewModelScope.launch {
            for (remaining in MAX_RECORDING_SECONDS - 1 downTo 0) {
                delay(1_000)
                _uiState.value = _uiState.value.copy(recordingSecondsRemaining = remaining)
                if (!_uiState.value.isRecordingMotivationalAudio) return@launch
                if (remaining == 0) {
                    stopRecording(saveRecording = true)
                }
            }
        }
    }

    private fun stopPlayback() {
        mediaPlayer?.runCatching {
            stop()
        }
        mediaPlayer?.release()
        mediaPlayer = null
        if (_uiState.value.isPlayingMotivationalAudio) {
            _uiState.value = _uiState.value.copy(
                isPlayingMotivationalAudio = false,
                audioStatusMessage = if (_uiState.value.hasMotivationalAudio) {
                    "Tu mensaje está listo para reproducirse o regrabarse."
                } else {
                    ""
                }
            )
        }
    }

    override fun onCleared() {
        // Libera recursos nativos si la pantalla desaparece en mitad de una grabación/reproducción.
        stopPlayback()
        stopRecording(saveRecording = false)
        super.onCleared()
    }
}
