package com.byeboo.app.data.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.byeboo.app.data.datasource.local.FcmLocalDataSource
import com.byeboo.app.data.datasource.local.UserLocalDataSource
import com.byeboo.app.data.datasourceimpl.local.FcmLocalDataSourceImpl
import com.byeboo.app.data.datasourceimpl.local.UserLocalDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

private const val HEARTZ_DATASTORE = "heartz_datastore"
private const val FCM_DATASTORE = "fcm_datastore"
private val Context.heartzDataStore: DataStore<Preferences> by preferencesDataStore(
    name = HEARTZ_DATASTORE,
)

private val Context.fcmDataStore: DataStore<Preferences> by preferencesDataStore(
    name = FCM_DATASTORE,
)

@Module
@InstallIn(SingletonComponent::class)
object HeartzDataStoreModule {
    @Provides
    @Singleton
    fun provideHeartzDataStore(
        @ApplicationContext context: Context,
    ): UserLocalDataSource = UserLocalDataSourceImpl(context.heartzDataStore)

    @Provides
    @Singleton
    fun provideFcmDataStore(
        @ApplicationContext context: Context,
    ): FcmLocalDataSource = FcmLocalDataSourceImpl(context.fcmDataStore)
}
