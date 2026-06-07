package com.illera.peakprofit.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.illera.peakprofit.domain.entity.Exercise
import com.illera.peakprofit.domain.repository.SavedExerciseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class StorageSavedExerciseRepository(
    private val dataStore: DataStore<Preferences>,
    private val gson: Gson
) : SavedExerciseRepository {
    // Serializa las escrituras para no perder cambios cuando varias pantallas intentan
    // guardar o borrar ejercicios casi al mismo tiempo.
    private val mutex = Mutex()

    override suspend fun saveExercise(userId: String, exercise: Exercise) {
        mutex.withLock {
            val storage = readStorage()
            val currentByUser = storage[userId].orEmpty().associateByTo(linkedMapOf()) { it.id }
            currentByUser[exercise.id] = exercise
            persistUserExercises(userId = userId, exercises = currentByUser.values.toList())
        }
    }

    override suspend fun updateSavedExerciseNote(userId: String, exerciseId: String, note: String) {
        mutex.withLock {
            val storage = readStorage()
            val currentByUser = storage[userId].orEmpty().associateByTo(linkedMapOf()) { it.id }
            val currentExercise = currentByUser[exerciseId] ?: return
            // Solo muta la nota asociada al guardado; el resto de datos del ejercicio se conserva.
            currentByUser[exerciseId] = currentExercise.copy(savedNote = note)
            persistUserExercises(userId = userId, exercises = currentByUser.values.toList())
        }
    }

    override suspend fun removeSavedExercise(userId: String, exerciseId: String) {
        mutex.withLock {
            val storage = readStorage()
            val currentByUser = storage[userId].orEmpty().associateByTo(linkedMapOf()) { it.id }
            currentByUser.remove(exerciseId)
            persistUserExercises(userId = userId, exercises = currentByUser.values.toList())
        }
    }

    override suspend fun getSavedExerciseById(userId: String, exerciseId: String): Exercise? {
        return readStorage()[userId].orEmpty().firstOrNull { it.id == exerciseId }
    }

    override fun observeSavedExercises(userId: String): Flow<List<Exercise>> {
        return dataStore.data.map { preferences ->
            deserializeStorage(preferences[STORAGE_KEY].orEmpty())[userId].orEmpty()
        }
    }

    override fun isExerciseSaved(userId: String, exerciseId: String): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            deserializeStorage(preferences[STORAGE_KEY].orEmpty())[userId]
                .orEmpty()
                .any { it.id == exerciseId }
        }
    }

    private suspend fun persistUserExercises(userId: String, exercises: List<Exercise>) {
        val updatedStorage = readStorage().toMutableMap().apply {
            if (exercises.isEmpty()) {
                remove(userId)
            } else {
                put(userId, exercises)
            }
        }
        dataStore.edit { preferences ->
            preferences[STORAGE_KEY] = gson.toJson(updatedStorage)
        }
    }

    private suspend fun readStorage(): Map<String, List<Exercise>> {
        val raw = dataStore.data.first()[STORAGE_KEY].orEmpty()
        return deserializeStorage(raw)
    }

    private fun deserializeStorage(raw: String): Map<String, List<Exercise>> {
        if (raw.isBlank()) return emptyMap()
        val type = object : TypeToken<Map<String, List<Exercise>>>() {}.type
        return gson.fromJson<Map<String, List<Exercise>>>(raw, type) ?: emptyMap()
    }

    private companion object {
        // Una única clave almacena el mapa completo `userId -> ejercicios` para mantener
        // sincronizada la lectura reactiva desde DataStore.
        val STORAGE_KEY = stringPreferencesKey("saved_exercises_by_user")
    }
}
