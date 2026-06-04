package com.illera.peakprofit.data.repository

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.illera.peakprofit.domain.entity.Exercise
import com.illera.peakprofit.domain.repository.SavedExerciseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class StorageSavedExerciseRepository(
    private val sharedPreferences: SharedPreferences,
    private val gson: Gson
) : SavedExerciseRepository {
    private val mutex = Mutex()
    private val storage = MutableStateFlow(readStorage())

    override suspend fun saveExercise(userId: String, exercise: Exercise) {
        mutex.withLock {
            val currentByUser = storage.value[userId].orEmpty().associateByTo(linkedMapOf()) { it.id }
            currentByUser[exercise.id] = exercise
            persistUserExercises(userId = userId, exercises = currentByUser.values.toList())
        }
    }

    override suspend fun removeSavedExercise(userId: String, exerciseId: String) {
        mutex.withLock {
            val currentByUser = storage.value[userId].orEmpty().associateByTo(linkedMapOf()) { it.id }
            currentByUser.remove(exerciseId)
            persistUserExercises(userId = userId, exercises = currentByUser.values.toList())
        }
    }

    override suspend fun getSavedExerciseById(userId: String, exerciseId: String): Exercise? {
        return storage.value[userId].orEmpty().firstOrNull { it.id == exerciseId }
    }

    override fun observeSavedExercises(userId: String): Flow<List<Exercise>> {
        return storage.map { savedByUser -> savedByUser[userId].orEmpty() }
    }

    override fun isExerciseSaved(userId: String, exerciseId: String): Flow<Boolean> {
        return storage.map { savedByUser ->
            savedByUser[userId].orEmpty().any { it.id == exerciseId }
        }
    }

    private fun persistUserExercises(userId: String, exercises: List<Exercise>) {
        val updatedStorage = storage.value.toMutableMap().apply {
            if (exercises.isEmpty()) {
                remove(userId)
            } else {
                put(userId, exercises)
            }
        }
        storage.value = updatedStorage
        sharedPreferences.edit()
            .putString(STORAGE_KEY, gson.toJson(updatedStorage))
            .apply()
    }

    private fun readStorage(): Map<String, List<Exercise>> {
        val raw = sharedPreferences.getString(STORAGE_KEY, null).orEmpty()
        if (raw.isBlank()) return emptyMap()

        val type = object : TypeToken<Map<String, List<Exercise>>>() {}.type
        return gson.fromJson<Map<String, List<Exercise>>>(raw, type) ?: emptyMap()
    }

    private companion object {
        const val STORAGE_KEY = "saved_exercises_by_user"
    }
}
