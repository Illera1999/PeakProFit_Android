package com.illera.peakprofit.data.di

import com.illera.peakprofit.data.remote.ExerciseDbApi
import com.illera.peakprofit.data.repository.ExerciseDbRepository
import com.illera.peakprofit.domain.repository.ExerciseRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object ExerciseModule {

    @Provides
    @Singleton
    fun provideExerciseHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
        }
        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
    }

    @Provides
    @Singleton
    fun provideExerciseRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://oss.exercisedb.dev/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideExerciseDbApi(retrofit: Retrofit): ExerciseDbApi {
        return retrofit.create(ExerciseDbApi::class.java)
    }

    @Provides
    @Singleton
    fun provideExerciseRepository(api: ExerciseDbApi): ExerciseRepository {
        return ExerciseDbRepository(api)
    }
}
