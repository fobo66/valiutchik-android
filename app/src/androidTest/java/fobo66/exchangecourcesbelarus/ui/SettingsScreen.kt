package fobo66.exchangecourcesbelarus.ui

import com.agoda.kakao.common.views.KView
import com.kaspersky.kaspresso.screens.KScreen
import fobo66.exchangecourcesbelarus.R
import fobo66.exchangecourcesbelarus.R.id

object SettingsScreen : KScreen<SettingsScreen>() {
  val settings = KView {
    withId(id.settings_container)

  }

  override val layoutId = R.layout.activity_settings
  override val viewClass = SettingsActivity::class.java
}