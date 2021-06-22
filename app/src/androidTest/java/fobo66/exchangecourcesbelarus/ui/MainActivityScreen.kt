package fobo66.exchangecourcesbelarus.ui

import android.view.View
import com.kaspersky.kaspresso.screens.KScreen
import fobo66.exchangecourcesbelarus.R
import io.github.kakaocup.kakao.common.views.KView
import io.github.kakaocup.kakao.dialog.KAlertDialog
import io.github.kakaocup.kakao.recycler.KRecyclerItem
import io.github.kakaocup.kakao.recycler.KRecyclerView
import io.github.kakaocup.kakao.text.KTextView
import org.hamcrest.Matcher

object MainScreen : KScreen<MainScreen>() {

  val aboutIcon = KView {
    withId(R.id.action_about)
  }

  val settingsIcon = KView {
    withId(R.id.action_settings)
  }

  val aboutDialog = KAlertDialog()

  val coursesList = KRecyclerView(
    builder = { this.withId(R.id.courses_list) },
    itemTypeBuilder = { itemType(::CoursesListItem) }
  )
  override val layoutId = R.layout.activity_main
  override val viewClass = MainActivity::class.java
}

class CoursesListItem(parent: Matcher<View>) : KRecyclerItem<CoursesListItem>(parent) {
  val value: KTextView = KTextView(parent) { withId(R.id.currency_value) }
}
