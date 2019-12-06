package fobo66.exchangecourcesbelarus.list

import androidx.recyclerview.widget.DiffUtil
import fobo66.exchangecourcesbelarus.entities.BestCurrencyRate

/**
 * (c) 2019 Andrey Mukamolov <fobo66@protonmail.com>
 * Created 11/26/19.
 */
class BestCurrencyRateDiffUtilItemCallback : DiffUtil.ItemCallback<BestCurrencyRate>() {
  override fun areItemsTheSame(oldItem: BestCurrencyRate, newItem: BestCurrencyRate): Boolean {
    return oldItem.id == newItem.id
  }

  override fun areContentsTheSame(oldItem: BestCurrencyRate, newItem: BestCurrencyRate): Boolean {
    return oldItem == newItem
  }
}
