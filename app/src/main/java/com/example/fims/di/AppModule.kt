package com.example.fims.di

import com.example.fims.common.Constants
import com.example.fims.data.api.ApiFilms
import com.example.fims.data.repository.FilmsRepositoryImp
import com.example.fims.domain.repository.FilmsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun getSearchFilms(): ApiFilms = Retrofit.Builder()
        .baseUrl(Constants.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ApiFilms::class.java)

    @Provides
    @Singleton
    fun provideRepositoryFilms(api: ApiFilms): FilmsRepository = FilmsRepositoryImp(api)
}