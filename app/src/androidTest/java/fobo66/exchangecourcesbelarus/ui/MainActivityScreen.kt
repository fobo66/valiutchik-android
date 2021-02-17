package fobo66.exchangecourcesbelarus.ui

import android.view.View
import com.agoda.kakao.dialog.KAlertDialog
import com.agoda.kakao.recycler.KRecyclerItem
import com.agoda.kakao.recycler.KRecyclerView
import com.agoda.kakao.text.KTextView
import com.agoda.kakao.toolbar.KToolbar
import com.kaspersky.kaspresso.screens.KScreen
import fobo66.exchangecourcesbelarus.R
import org.hamcrest.Matcher

object MainScreen : KScreen<MainScreen>() {

  val toolbar = KToolbar {
    withId(R.id.toolbar)
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
  val name: KTextView = KTextView(parent) { withId(R.id.currency_name) }
  val value: KTextView = KTextView(parent) { withId(R.id.currency_value) }
  val bankName: KTextView = KTextView(parent) { withId(R.id.bank_name) }
}