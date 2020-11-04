package fobo66.exchangecourcesbelarus.list

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.net.Uri
import android.view.View
import android.view.View.OnClickListener
import android.view.View.OnLongClickListener
import androidx.core.app.ActivityCompat
import androidx.core.content.getSystemService
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.analytics.FirebaseAnalytics.Event
import com.google.firebase.analytics.FirebaseAnalytics.Param
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.ktx.Firebase
import fobo66.exchangecourcesbelarus.R
import fobo66.exchangecourcesbelarus.databinding.CurrencyCardBinding
import fobo66.valiutchik.core.entities.BestCurrencyRate
import timber.log.Timber

/**
 * (c) 2017 Andrey Mukamolov
 * Created 10/19/17.
 */
class BestCurrencyRatesViewHolder(private val binding: CurrencyCardBinding) :
  ViewHolder(binding.root), OnClickListener, OnLongClickListener {

  init {
    binding.cv.setOnClickListener(this)
    binding.cv.setOnLongClickListener(this)
  }

  fun bind(item: BestCurrencyRate) {
    binding.currencyName.text = item.currencyName
    binding.currencyValue.text = item.currencyValue
    binding.bankName.text = item.bank
  }

  override fun onClick(view: View) {
    val req =
      Uri.parse("geo:0,0?q=" + binding.bankName.text.toString().replace(' ', '+'))
    val mapIntent = Intent(Intent.ACTION_VIEW, req)

    if (mapIntent.resolveActivity(itemView.context.packageManager) != null) {
      trackEvent()
      ActivityCompat.startActivity(view.context, mapIntent, null)
    } else {
      Timber.e("Failed to show banks on map: maps app not found")
      Snackbar.make(itemView, R.string.maps_app_required, Snackbar.LENGTH_LONG).show()
    }
  }

  override fun onLongClick(view: View): Boolean {
    val clipData = ClipData.newPlainText(binding.currencyName.text, binding.currencyValue.text)
    val clipboardManager: ClipboardManager? = itemView.context.getSystemService()
    clipboardManager?.setPrimaryClip(clipData)
    Snackbar.make(itemView, R.string.currency_value_copied, Snackbar.LENGTH_SHORT).show()
    return true
  }

  private fun trackEvent() {
    Firebase.analytics.logEvent(
      Event.VIEW_ITEM
    ) {
      param(Param.CURRENCY, binding.currencyName.text.toString())
    }
  }
}