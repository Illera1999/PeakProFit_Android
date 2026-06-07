package com.illera.peakprofit.domain.repository

import java.io.File

/**
 * Gestiona el audio motivacional almacenado localmente para un usuario concreto.
 *
 * La clave `ownerKey` identifica al usuario autenticado dentro del dispositivo.
 * No sincroniza archivos entre dispositivos ni con backend.
 */
interface MotivationalAudioRepository {
    fun getAudioFile(ownerKey: String): File
    fun getTempAudioFile(ownerKey: String): File
    fun hasAudio(ownerKey: String): Boolean
    fun commitTempAudio(ownerKey: String)
    fun discardTempAudio(ownerKey: String)
    fun deleteAudio(ownerKey: String)
}
