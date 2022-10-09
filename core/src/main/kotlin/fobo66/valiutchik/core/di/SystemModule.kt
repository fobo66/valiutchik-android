package fobo66.valiutchik.core.di

import android.content.Context
import android.content.SharedPreferences
import android.content.res.AssetManager
import androidx.preference.PreferenceManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

/**
 * (c) 2019 Andrey Mukamolov <fobo66@protonmail.com>
 * Created 11/8/19.
 */
@Module
@InstallIn(SingletonComponent::class)
object SystemModule {

  @Provides
  fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences =
    PreferenceManager.getDefaultSharedPreferences(context)

  @Provides
  fun provideAssetManager(@ApplicationContext context: Context): AssetManager = context.assets
}
