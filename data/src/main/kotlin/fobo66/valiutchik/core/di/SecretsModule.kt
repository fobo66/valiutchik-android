package fobo66.valiutchik.core.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import fobo66.valiutchik.core.R

@Module
@InstallIn(SingletonComponent::class)
object SecretsModule {

  @GeocoderAccessToken
  @Provides
  fun provideGeocoderAccessToken(@ApplicationContext context: Context): String =
    context.getString(R.string.mapboxGeocoderAccessToken)
}
