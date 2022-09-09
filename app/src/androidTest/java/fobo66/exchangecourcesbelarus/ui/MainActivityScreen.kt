package fobo66.exchangecourcesbelarus.ui

import androidx.compose.ui.semantics.SemanticsNode
import androidx.compose.ui.semantics.SemanticsProperties.TestTag
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import fobo66.exchangecourcesbelarus.util.LazyListItemPosition
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.github.kakaocup.compose.node.element.KNode
import io.github.kakaocup.compose.node.element.lazylist.KLazyListItemNode
import io.github.kakaocup.compose.node.element.lazylist.KLazyListNode

class MainActivityScreen(semanticsProvider: SemanticsNodeInteractionsProvider) :
  ComposeScreen<MainActivityScreen>(semanticsProvider) {

  val aboutIcon: KNode = child {
    hasTestTag("About")
  }

  val settingsIcon: KNode = child {
    hasTestTag("Settings")
  }

  val aboutDialog: KNode = child {
    isDialog()
  }

  val snackbar: KNode = child {
    hasTestTag("Snackbar")
  }

  val coursesList = KLazyListNode(
    semanticsProvider = semanticsProvider,
    viewBuilderAction = {
      hasTestTag("Courses")
    },
    itemTypeBuilder = {
      itemType(::CoursesListItem)
    },
    positionMatcher = { position -> SemanticsMatcher.expectValue(LazyListItemPosition, position) }
  )

  class CoursesListItem(
    semanticsNode: SemanticsNode,
    semanticsProvider: SemanticsNodeInteractionsProvider
  ) : KLazyListItemNode<CoursesListItem>(semanticsNode, semanticsProvider) {

    val value: KNode = child {
      hasTestTag("Currency value")
    }
  }
}
