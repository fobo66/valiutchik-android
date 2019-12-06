package fobo66.exchangecourcesbelarus.di

import android.content.Context
import com.google.android.gms.location.LocationServices
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * (c) 2019 Andrey Mukamolov <fobo66@protonmail.com>
 * Created 11/7/19.
 */
@Module
object LocationModule {

  @Provides
  @Singleton
  fun provideFusedLocationProviderClient(context: Context) =
    LocationServices.getFusedLocationProviderClient(context)
}
