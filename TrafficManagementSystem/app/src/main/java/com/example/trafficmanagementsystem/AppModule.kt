package com.example.trafficmanagementsystem

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import androidx.room.Room
import com.example.trafficmanagementsystem.Constants.KEY_FIRST_TIME_TOGGLE_AVATAR
import com.example.trafficmanagementsystem.Constants.KEY_IMG
import com.example.trafficmanagementsystem.Constants.KEY_NAME
import com.example.trafficmanagementsystem.Constants.SHARED_PREFRENCES_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideSharedPreferences(
        @ApplicationContext app: Context
    ) = app.getSharedPreferences(SHARED_PREFRENCES_NAME , MODE_PRIVATE)

    @Singleton
    @Provides
    @Named("Name")
    fun provideName(sharedPref: SharedPreferences) = sharedPref.getString(KEY_NAME, "") ?: ""

    @Singleton
    @Provides
    @Named("Image")
    fun provideImg(sharedPref: SharedPreferences) = sharedPref.getString(
        KEY_IMG, "") ?: ""

    @Singleton
    @Provides
    @Named("First")
    fun provideFirstTimeToggleAvatar(sharedPref: SharedPreferences) = sharedPref.getBoolean(
        KEY_FIRST_TIME_TOGGLE_AVATAR, true)

}