package fobo66.exchangecourcesbelarus.ui

import com.kaspersky.kaspresso.screens.KScreen
import fobo66.exchangecourcesbelarus.R
import io.github.kakaocup.kakao.common.views.KView

object SettingsScreen : KScreen<SettingsScreen>() {
  val settings = KView {
    withId(android.R.id.list_container)
  }

  override val layoutId = R.layout.activity_main
  override val viewClass = OldMainActivity::class.java
}
