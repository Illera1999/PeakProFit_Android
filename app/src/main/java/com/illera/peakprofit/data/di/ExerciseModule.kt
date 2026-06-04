package com.illera.peakprofit.data.di

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import dagger.hilt.android.qualifiers.ApplicationContext
import com.illera.peakprofit.BuildConfig
import com.google.gson.Gson
import com.illera.peakprofit.data.remote.ExerciseDbApi
import com.illera.peakprofit.data.repository.ExerciseDbRepository
import com.illera.peakprofit.data.repository.StorageSavedExerciseRepository
import com.illera.peakprofit.domain.repository.ExerciseRepository
import com.illera.peakprofit.domain.repository.SavedExerciseRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object ExerciseModule {
    private const val SAVED_EXERCISES_PREFS = "saved_exercises_storage"

    @Provides
    @Singleton
    fun provideExerciseHttpClient(): OkHttpClient {
        val authInterceptor = Interceptor { chain ->
            val requestBuilder = chain.request().newBuilder()
            if (BuildConfig.RAPID_API_KEY.isNotBlank()) {
                requestBuilder.addHeader("X-RapidAPI-Key", BuildConfig.RAPID_API_KEY)
            }
            requestBuilder.addHeader("X-RapidAPI-Host", BuildConfig.RAPID_API_HOST)
            chain.proceed(requestBuilder.build())
        }

        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
        }
        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(logging)
            .build()
    }

    @Provides
    @Singleton
    fun provideExerciseRetrofit(client: OkHttpClient, gson: Gson): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.RAPID_API_BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @Provides
    @Singleton
    fun provideExerciseDbApi(retrofit: Retrofit): ExerciseDbApi {
        return retrofit.create(ExerciseDbApi::class.java)
    }

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return Gson()
    }

    @Provides
    @Singleton
    fun provideExerciseRepository(api: ExerciseDbApi): ExerciseRepository {
        return ExerciseDbRepository(api)
    }

    @Provides
    @Singleton
    fun provideSavedExercisesPreferences(
        @ApplicationContext context: Context
    ): SharedPreferences {
        return context.getSharedPreferences(SAVED_EXERCISES_PREFS, Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun provideSavedExerciseRepository(
        sharedPreferences: SharedPreferences,
        gson: Gson
    ): SavedExerciseRepository {
        return StorageSavedExerciseRepository(sharedPreferences, gson)
    }
}
