package com.example.mercedesbenzapp.di

import android.content.Context
import androidx.room.Room
import com.example.mercedesbenzapp.database.RestaurantDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    private const val DATABASE_NAME = "restaurant_database"

    @Singleton
    @Provides
    fun provideRoom(@ApplicationContext context: Context) =
        Room.databaseBuilder(context, RestaurantDatabase::class.java, DATABASE_NAME).build()

    @Singleton
    @Provides
    fun provideRestaurantDao(db: RestaurantDatabase) = db.getRestaurantsDao()
}