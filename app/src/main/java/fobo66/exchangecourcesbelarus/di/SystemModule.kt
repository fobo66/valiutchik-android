package fobo66.exchangecourcesbelarus.di

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import fobo66.exchangecourcesbelarus.R

/**
 * (c) 2019 Andrey Mukamolov <fobo66@protonmail.com>
 * Created 11/8/19.
 */
@InstallIn(SingletonComponent::class)
@Module
object SystemModule {

  @Provides
  fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences =
    PreferenceManager.getDefaultSharedPreferences(context)

  @Provides
  @GeocoderAccessToken
  fun provideGeocoderAccessToken(@ApplicationContext context: Context): String =
    context.getString(R.string.mapboxGeocoderAccessToken)
}
