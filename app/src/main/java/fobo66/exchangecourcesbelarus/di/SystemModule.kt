package fobo66.exchangecourcesbelarus.di

import android.content.Context
import androidx.preference.PreferenceManager
import dagger.Module
import dagger.Provides

/**
 * (c) 2019 Andrey Mukamolov <fobo66@protonmail.com>
 * Created 11/8/19.
 */
@Module
object SystemModule {

  @Provides
  fun provideSharedPreferences(context: Context) =
    PreferenceManager.getDefaultSharedPreferences(context)
}
