package com.illera.peakprofit.data.repository

import android.content.Context
import com.illera.peakprofit.domain.repository.MotivationalAudioRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalMotivationalAudioRepository @Inject constructor(
    @param:ApplicationContext private val context: Context
) : MotivationalAudioRepository {
    // Cada usuario autenticado guarda su audio en un fichero local distinto.
    override fun getAudioFile(ownerKey: String): File {
        return File(audioDir(), "${sanitize(ownerKey)}.m4a")
    }

    // Se graba primero en temporal para no corromper el audio anterior si la grabación falla.
    override fun getTempAudioFile(ownerKey: String): File {
        return File(audioDir(), "${sanitize(ownerKey)}.tmp.m4a")
    }

    override fun hasAudio(ownerKey: String): Boolean {
        return getAudioFile(ownerKey).exists()
    }

    override fun commitTempAudio(ownerKey: String) {
        val tempFile = getTempAudioFile(ownerKey)
        if (!tempFile.exists()) return
        val targetFile = getAudioFile(ownerKey)
        if (targetFile.exists()) {
            targetFile.delete()
        }
        tempFile.renameTo(targetFile)
    }

    override fun discardTempAudio(ownerKey: String) {
        getTempAudioFile(ownerKey).delete()
    }

    override fun deleteAudio(ownerKey: String) {
        getAudioFile(ownerKey).delete()
        discardTempAudio(ownerKey)
    }

    private fun audioDir(): File {
        // Se mantiene dentro del sandbox privado de la app.
        return File(context.filesDir, "motivational_audio").apply { mkdirs() }
    }

    private fun sanitize(value: String): String {
        return value.replace(Regex("[^a-zA-Z0-9._-]"), "_")
    }
}
