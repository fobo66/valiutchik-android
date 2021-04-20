package fobo66.exchangecourcesbelarus.ui

import com.agoda.kakao.common.views.KView
import com.kaspersky.kaspresso.screens.KScreen
import fobo66.exchangecourcesbelarus.R

object SettingsScreen : KScreen<SettingsScreen>() {
  val settings = KView {
    withId(android.R.id.list_container)
  }

  override val layoutId = R.layout.activity_main
  override val viewClass = MainActivity::class.java
}