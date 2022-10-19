/*
 *    Copyright 2022 Andrey Mukamolov
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package fobo66.exchangecourcesbelarus.ui

import androidx.compose.ui.semantics.SemanticsNode
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import fobo66.exchangecourcesbelarus.util.LazyListItemPosition
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.github.kakaocup.compose.node.element.KNode
import io.github.kakaocup.compose.node.element.lazylist.KLazyListItemNode
import io.github.kakaocup.compose.node.element.lazylist.KLazyListNode

class CurrenciesScreen(semanticsProvider: SemanticsNodeInteractionsProvider) :
  ComposeScreen<CurrenciesScreen>(semanticsProvider) {

  val emptyListIndicator: KNode = child {
    hasTestTag("No rates")
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
