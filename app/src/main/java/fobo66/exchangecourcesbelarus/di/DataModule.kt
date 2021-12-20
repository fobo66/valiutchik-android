package fobo66.exchangecourcesbelarus.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import fobo66.exchangecourcesbelarus.model.datasource.CurrencyRatesDataSource
import fobo66.exchangecourcesbelarus.model.datasource.CurrencyRatesDataSourceImpl
import fobo66.exchangecourcesbelarus.model.repository.CurrencyRatesTimestampRepository
import fobo66.exchangecourcesbelarus.model.repository.CurrencyRatesTimestampRepositoryImpl
import fobo66.exchangecourcesbelarus.model.repository.LocationRepositoryImpl
import fobo66.exchangecourcesbelarus.model.repository.MapRepositoryImpl
import fobo66.valiutchik.core.model.repository.LocationRepository
import fobo66.valiutchik.core.model.repository.MapRepository
import fobo66.valiutchik.core.util.CurrencyRatesParser
import fobo66.valiutchik.core.util.CurrencyRatesParserImpl

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {
  @Binds
  abstract fun provideCurrencyRatesParser(
    currencyRatesParserImpl: CurrencyRatesParserImpl
  ): CurrencyRatesParser

  @Binds
  abstract fun provideCurrencyRatesDataSource(
    currencyRatesDataSourceImpl: CurrencyRatesDataSourceImpl
  ): CurrencyRatesDataSource

  @Binds
  abstract fun provideCurrencyRatesTimestampRepository(
    currencyRatesTimestampRepositoryImpl: CurrencyRatesTimestampRepositoryImpl
  ): CurrencyRatesTimestampRepository

  @Binds
  abstract fun provideLocationRepository(
    locationRepositoryImpl: LocationRepositoryImpl
  ): LocationRepository

  @Binds
  abstract fun provideMapRepository(
    mapRepositoryImpl: MapRepositoryImpl
  ): MapRepository
}
