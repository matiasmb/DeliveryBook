package com.deliverybook.di

import android.content.Context
import androidx.room.Room
import com.deliverybook.core.AppConstants
import com.deliverybook.data.local.DeliveryBookDatabase
import com.deliverybook.data.repository.ContactsRepositoryImpl
import com.deliverybook.domain.repository.ContactsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): DeliveryBookDatabase =
        Room.databaseBuilder(
            context,
            DeliveryBookDatabase::class.java,
            AppConstants.Db.NAME
        ).build()

    @Provides
    @Singleton
    fun provideContactsRepository(
        database: DeliveryBookDatabase
    ): ContactsRepository =
        ContactsRepositoryImpl(database)
}

