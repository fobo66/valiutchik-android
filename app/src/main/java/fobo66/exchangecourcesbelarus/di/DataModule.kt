package fobo66.exchangecourcesbelarus.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import fobo66.exchangecourcesbelarus.model.datasource.CurrencyRatesDataSource
import fobo66.exchangecourcesbelarus.model.datasource.MyfinDataSource
import fobo66.exchangecourcesbelarus.model.repository.LocationRepositoryImpl
import fobo66.exchangecourcesbelarus.model.repository.MapRepositoryImpl
import fobo66.valiutchik.core.model.repository.LocationRepository
import fobo66.valiutchik.core.model.repository.MapRepository
import fobo66.valiutchik.core.util.CurrencyRatesParser
import fobo66.valiutchik.core.util.MyfinParser

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {
  @Binds
  abstract fun provideCurrencyRatesParser(
    myfinParser: MyfinParser
  ): CurrencyRatesParser

  @Binds
  abstract fun provideCurrencyRatesDataSource(
    myfinDataSource: MyfinDataSource
  ): CurrencyRatesDataSource

  @Binds
  abstract fun provideLocationRepository(
    locationRepositoryImpl: LocationRepositoryImpl
  ): LocationRepository

  @Binds
  abstract fun provideMapRepository(
    mapRepositoryImpl: MapRepositoryImpl
  ): MapRepository
}
