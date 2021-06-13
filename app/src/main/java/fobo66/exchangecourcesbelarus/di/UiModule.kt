package fobo66.exchangecourcesbelarus.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import fobo66.exchangecourcesbelarus.util.LocationResolver
import fobo66.exchangecourcesbelarus.util.LocationResolverImpl

@InstallIn(FragmentComponent::class)
@Module
abstract class UiModule {
  @Binds
  abstract fun provideLocationResolver(locationResolverImpl: LocationResolverImpl): LocationResolver
}
