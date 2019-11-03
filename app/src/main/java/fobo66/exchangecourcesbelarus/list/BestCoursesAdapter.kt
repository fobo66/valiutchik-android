package fobo66.exchangecourcesbelarus.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import fobo66.exchangecourcesbelarus.R.layout
import fobo66.exchangecourcesbelarus.entities.BestCourse

/**
 * Adapter for currency cards
 * Created by fobo66 on 23.08.2015.
 */
class BestCoursesAdapter(courses: List<BestCourse>) :
  Adapter<CurrencyViewHolder>() {
  private val courses: MutableList<BestCourse> = ArrayList()
  private var buyOrSell = false
  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyViewHolder {
    val v =
      LayoutInflater.from(parent.context).inflate(layout.currency_card, parent, false)
    return CurrencyViewHolder(v)
  }

  override fun onBindViewHolder(holder: CurrencyViewHolder, position: Int) {
    val bestCourse = courses[position]
    if (bestCourse.isBuy == buyOrSell) {
      holder.bind(bestCourse)
    }
  }

  override fun getItemCount(): Int {
    return courses.size
  }

  fun onDataUpdate(newCourses: List<BestCourse>) {
    courses.clear()
    courses.addAll(newCourses)
    notifyDataSetChanged()
  }

  fun setBuyOrSell(buyOrSell: Boolean) {
    this.buyOrSell = buyOrSell
  }

  init {
    this.courses.addAll(courses)
  }
}