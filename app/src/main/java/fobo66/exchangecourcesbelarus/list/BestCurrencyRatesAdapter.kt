package fobo66.exchangecourcesbelarus.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import fobo66.exchangecourcesbelarus.R.layout
import fobo66.exchangecourcesbelarus.entities.BestCurrencyRate

/**
 * (c) 2019 Andrey Mukamolov <fobo66@protonmail.com>
 * Created 11/26/19.
 */
class BestCurrencyRatesAdapter :
  ListAdapter<BestCurrencyRate, BestCurrencyRatesViewHolder>(
    BestCurrencyRateDiffUtilItemCallback()
  ) {
  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BestCurrencyRatesViewHolder {
    val v =
      LayoutInflater.from(parent.context).inflate(layout.currency_card, parent, false)
    return BestCurrencyRatesViewHolder(v)
  }

  override fun onBindViewHolder(holder: BestCurrencyRatesViewHolder, position: Int) {
    holder.bind(getItem(position))
  }
}