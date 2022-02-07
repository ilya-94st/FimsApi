package com.example.films.di
import android.content.Context
import androidx.room.Room
import com.example.films.common.Constants
import com.example.films.common.Constants.DB_NAME
import com.example.films.data.api.ApiFilms
import com.example.films.data.db.DataBaseFilms
import com.example.films.data.repository.FilmsRepositoryImp
import com.example.films.domain.repository.FilmsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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
    fun provideFilmsRepository(api: ApiFilms, db: DataBaseFilms, @ApplicationContext app: Context): FilmsRepository = FilmsRepositoryImp(db,api, app)

    @Provides
    @Singleton
    fun provideFilmsDatabase(
        @ApplicationContext app: Context
    ) = Room.databaseBuilder(app, DataBaseFilms::class.java, DB_NAME).build()

}
