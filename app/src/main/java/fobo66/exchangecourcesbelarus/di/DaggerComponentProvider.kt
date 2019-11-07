package fobo66.exchangecourcesbelarus.di

/**
 * (c) 2019 Andrey Mukamolov <fobo66@protonmail.com>
 * Created 11/7/19.
 */
import android.app.Activity

interface DaggerComponentProvider {

  val component: ApplicationComponent
}

val Activity.injector get() = (application as DaggerComponentProvider).component