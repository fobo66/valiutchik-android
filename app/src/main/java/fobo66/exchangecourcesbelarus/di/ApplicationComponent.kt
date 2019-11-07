package fobo66.exchangecourcesbelarus.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import fobo66.exchangecourcesbelarus.ui.MainActivity

/**
 * (c) 2019 Andrey Mukamolov <fobo66@protonmail.com>
 * Created 11/7/19.
 */
@Component(modules = [NetworkModule::class])
interface ApplicationComponent {

  @Component.Factory
  interface Factory {
    // With @BindsInstance, the Context passed in will be available in the graph
    fun create(@BindsInstance context: Context): ApplicationComponent
  }

  fun inject(mainActivity: MainActivity)
}