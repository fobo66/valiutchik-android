package fobo66.valiutchik.core.di

import com.mapbox.search.SearchEngine
import com.mapbox.search.SearchEngineSettings
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object ThirdPartyModule {

  @Provides
  fun provideReverseGeocodingEngine(
    @GeocoderAccessToken geocoderAccessToken: String
  ): SearchEngine =
    SearchEngine.createSearchEngine(
      SearchEngineSettings(
        accessToken = geocoderAccessToken
      )
    )

  @Provides
  fun provideMoshi(): Moshi = Moshi.Builder().build()
}
