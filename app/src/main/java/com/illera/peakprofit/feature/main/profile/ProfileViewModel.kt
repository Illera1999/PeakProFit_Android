package com.illera.peakprofit.feature.main.profile

import android.media.MediaPlayer
import android.media.MediaRecorder
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.illera.peakprofit.R
import com.illera.peakprofit.core.ui.UiText
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
class ProfileViewModel @Inject constructor(
    private val observeSessionUseCase: ObserveSessionUseCase,
    private val authRepository: AuthRepository,
    private val motivationalAudioRepository: MotivationalAudioRepository
) : ViewModel() {
    private companion object {
        const val MAX_RECORDING_SECONDS = 60
    }

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()
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
                        _uiState.value = ProfileUiState(
                            isAuthenticated = false,
                            isGuest = true
                        )
                    }
                    else -> {
                        stopPlayback()
                        stopRecording(saveRecording = false)
                        currentOwnerKey = null
                        _uiState.value = ProfileUiState()
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
            audioStatusMessage = UiText.StringResource(R.string.profile_audio_permission_required)
        )
    }

    fun startRecording() {
        if (!_uiState.value.isAuthenticated) return
        val ownerKey = currentOwnerKey ?: return
        if (_uiState.value.isRecordingMotivationalAudio) return
        stopPlayback()
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
                audioStatusMessage = UiText.StringResource(R.string.profile_audio_recording)
            )
            startCountdown()
        }.onFailure {
            motivationalAudioRepository.discardTempAudio(ownerKey)
            _uiState.value = _uiState.value.copy(
                audioStatusMessage = UiText.StringResource(R.string.profile_audio_recording_error)
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
            motivationalAudioRepository.commitTempAudio(ownerKey)
            _uiState.value = _uiState.value.copy(
                isRecordingMotivationalAudio = false,
                hasMotivationalAudio = motivationalAudioRepository.hasAudio(ownerKey),
                recordingSecondsRemaining = MAX_RECORDING_SECONDS,
                audioStatusMessage = UiText.StringResource(R.string.profile_audio_saved)
            )
        } else {
            motivationalAudioRepository.discardTempAudio(ownerKey)
            _uiState.value = _uiState.value.copy(
                isRecordingMotivationalAudio = false,
                recordingSecondsRemaining = MAX_RECORDING_SECONDS,
                audioStatusMessage = if (saveRecording) {
                    UiText.StringResource(R.string.profile_audio_recording_error)
                } else {
                    null
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
                audioStatusMessage = UiText.StringResource(R.string.profile_audio_playing)
            )
        }.onFailure {
            _uiState.value = _uiState.value.copy(
                isPlayingMotivationalAudio = false,
                audioStatusMessage = UiText.StringResource(R.string.profile_audio_playback_error)
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
            audioStatusMessage = UiText.StringResource(R.string.profile_audio_deleted)
        )
    }

    private fun refreshAudioState() {
        val ownerKey = currentOwnerKey ?: return
        val hasAudio = motivationalAudioRepository.hasAudio(ownerKey)
        _uiState.value = _uiState.value.copy(
            hasMotivationalAudio = hasAudio,
            isPlayingMotivationalAudio = false,
            isRecordingMotivationalAudio = false,
            recordingSecondsRemaining = MAX_RECORDING_SECONDS,
            audioStatusMessage = if (hasAudio) {
                UiText.StringResource(R.string.profile_audio_ready_saved)
            } else {
                UiText.StringResource(R.string.profile_audio_ready_empty)
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
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
        if (_uiState.value.isPlayingMotivationalAudio) {
            refreshAudioState()
        }
    }

    override fun onCleared() {
        stopPlayback()
        stopRecording(saveRecording = false)
        super.onCleared()
    }
}
