package fobo66.exchangecourcesbelarus.list

import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.google.android.material.card.MaterialCardView
import fobo66.exchangecourcesbelarus.databinding.CurrencyCardBinding
import fobo66.valiutchik.core.entities.BestCurrencyRate

/**
 * (c) 2017 Andrey Mukamolov
 * Created 10/19/17.
 */
class BestCurrencyRatesViewHolder(private val binding: CurrencyCardBinding) :
  ViewHolder(binding.root) {
  val card: MaterialCardView
    get() = binding.cv
  val bankName: CharSequence
    get() = binding.bankName.text
  val currencyName: CharSequence
    get() = binding.currencyName.text
  val currencyValue: CharSequence
    get() = binding.currencyValue.text

  fun bind(item: BestCurrencyRate) {
    binding.currencyName.text = itemView.context.getString(item.currencyNameRes)
    binding.currencyValue.text = item.currencyValue
    binding.bankName.text = item.bank
  }
}
