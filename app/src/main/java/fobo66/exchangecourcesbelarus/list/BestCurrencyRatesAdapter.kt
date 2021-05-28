package fobo66.exchangecourcesbelarus.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ListAdapter
import fobo66.exchangecourcesbelarus.databinding.CurrencyCardBinding
import fobo66.valiutchik.core.entities.BestCurrencyRate
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collect
import reactivecircus.flowbinding.android.view.clicks
import reactivecircus.flowbinding.android.view.longClicks

/**
 * (c) 2019 Andrey Mukamolov <fobo66@protonmail.com>
 * Created 11/26/19.
 */
class BestCurrencyRatesAdapter :
  ListAdapter<BestCurrencyRate, BestCurrencyRatesViewHolder>(
    BestCurrencyRateDiffUtilItemCallback()
  ) {

  val currencyRateClicked: SharedFlow<CharSequence>
    get() = _currencyRateClicked

  private val _currencyRateClicked = MutableSharedFlow<CharSequence>()

  val currencyRateLongClicked: SharedFlow<Pair<CharSequence, CharSequence>>
    get() = _currencyRateLongClicked

  private val _currencyRateLongClicked = MutableSharedFlow<Pair<CharSequence, CharSequence>>()

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BestCurrencyRatesViewHolder {
    val binding = CurrencyCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    return BestCurrencyRatesViewHolder(binding)
  }

  override fun onBindViewHolder(holder: BestCurrencyRatesViewHolder, position: Int) {
    holder.bind(getItem(position))
  }

  override fun onViewAttachedToWindow(holder: BestCurrencyRatesViewHolder) {
    holder.itemView.findViewTreeLifecycleOwner()?.lifecycleScope?.launchWhenResumed {
      holder.card.clicks()
        .collect {
          _currencyRateClicked.emit(holder.bankName)
        }
    }

    holder.itemView.findViewTreeLifecycleOwner()?.lifecycleScope?.launchWhenResumed {
      holder.itemView.longClicks()
        .collect {
          _currencyRateLongClicked.emit(holder.currencyName to holder.currencyValue)
        }
    }
  }
}
