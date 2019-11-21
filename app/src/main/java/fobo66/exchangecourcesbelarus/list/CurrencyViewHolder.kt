package fobo66.exchangecourcesbelarus.list

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.FirebaseAnalytics.Event
import com.google.firebase.analytics.FirebaseAnalytics.Param
import fobo66.exchangecourcesbelarus.R
import fobo66.exchangecourcesbelarus.databinding.CurrencyCardBinding
import fobo66.exchangecourcesbelarus.entities.BestCourse
import fobo66.exchangecourcesbelarus.util.ExceptionHandler

/**
 * (c) 2017 Andrey Mukamolov
 * Created 10/19/17.
 */
class CurrencyViewHolder(itemView: View) : ViewHolder(itemView), OnClickListener {
  private val binding: CurrencyCardBinding

  fun bind(item: BestCourse) {
    binding.currencyName.text = item.currencyName
    binding.currencyValue.text = item.currencyValue
    binding.bankName.text = item.bank
  }

  override fun onClick(view: View) {
    val req =
      Uri.parse("geo:0,0?q=" + binding.bankName.text.toString().replace(" ".toRegex(), "+"))
    val mapIntent = Intent(Intent.ACTION_VIEW, req)
    try {
      trackEvent()
      ActivityCompat.startActivity(view.context, mapIntent, null)
    } catch (e: ActivityNotFoundException) {
      ExceptionHandler.handleException(e)
      Snackbar.make(itemView, R.string.maps_app_required, Snackbar.LENGTH_LONG).show()
    }
  }

  private fun trackEvent() {
    val params = Bundle()
    params
      .putString(Param.CURRENCY, binding.currencyName.text.toString())
    FirebaseAnalytics.getInstance(itemView.context).logEvent(
      Event.VIEW_ITEM,
      params
    )
  }

  init {
    binding = CurrencyCardBinding.bind(itemView)
    binding.cv.setOnClickListener(this)
  }
}