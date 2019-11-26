package fobo66.exchangecourcesbelarus.list

import androidx.recyclerview.widget.DiffUtil
import fobo66.exchangecourcesbelarus.entities.BestCurrencyRate

/**
 * (c) 2019 Andrey Mukamolov <fobo66@protonmail.com>
 * Created 11/26/19.
 */
class BestCurrencyRateDiffUtilCallback(
  private val oldList: List<BestCurrencyRate>,
  private val newList: List<BestCurrencyRate>
) : DiffUtil.Callback() {

  override fun getOldListSize(): Int = oldList.size

  override fun getNewListSize(): Int = newList.size

  override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
    return oldList[oldItemPosition].id == newList[newItemPosition].id
  }

  override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
    return oldList[oldItemPosition].currencyValue == newList[newItemPosition].currencyValue
  }
}