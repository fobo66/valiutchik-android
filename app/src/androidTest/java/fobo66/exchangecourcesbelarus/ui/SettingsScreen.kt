package fobo66.exchangecourcesbelarus.ui

import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.github.kakaocup.compose.node.element.KNode

class SettingsScreen(semanticsProvider: SemanticsNodeInteractionsProvider) :
  ComposeScreen<SettingsScreen>(semanticsProvider) {
  val settings: KNode = child {
    hasTestTag("Settings")
  }
}
