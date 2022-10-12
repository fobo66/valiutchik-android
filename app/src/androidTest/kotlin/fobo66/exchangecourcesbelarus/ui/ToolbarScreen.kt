package fobo66.exchangecourcesbelarus.ui

import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.github.kakaocup.compose.node.element.KNode

class ToolbarScreen(semanticsProvider: SemanticsNodeInteractionsProvider) :
  ComposeScreen<ToolbarScreen>(semanticsProvider) {

  val backIcon: KNode = child {
    hasTestTag("Back")
  }

  val settingsIcon: KNode = child {
    hasTestTag("Settings")
  }

  val title: KNode = child {
    hasTestTag("Title")
  }
}
