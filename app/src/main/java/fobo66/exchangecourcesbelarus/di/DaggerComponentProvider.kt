package fobo66.exchangecourcesbelarus.di

/**
 * (c) 2019 Andrey Mukamolov <fobo66@protonmail.com>
 * Created 11/7/19.
 */
import android.app.Activity
import android.app.Service

interface DaggerComponentProvider {

  val component: ApplicationComponent
}

val Activity.injector get() = (application as DaggerComponentProvider).component
val Service.injector get() = (application as DaggerComponentProvider).component