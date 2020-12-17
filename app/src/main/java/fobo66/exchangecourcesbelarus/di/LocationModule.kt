package fobo66.exchangecourcesbelarus.di

import android.content.Context
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * (c) 2019 Andrey Mukamolov <fobo66@protonmail.com>
 * Created 11/7/19.
 */
@Module
@InstallIn(SingletonComponent::class)
object LocationModule {

  @Provides
  @Singleton
  fun provideFusedLocationProviderClient(@ApplicationContext context: Context): FusedLocationProviderClient =
    LocationServices.getFusedLocationProviderClient(context)
}