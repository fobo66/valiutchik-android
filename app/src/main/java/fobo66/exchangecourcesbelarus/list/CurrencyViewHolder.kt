package fobo66.exchangecourcesbelarus.list

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.view.View
import android.view.View.OnClickListener
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.google.android.material.snackbar.Snackbar
import fobo66.exchangecourcesbelarus.R
import fobo66.exchangecourcesbelarus.entities.BestCourse
import fobo66.exchangecourcesbelarus.util.ExceptionHandler

/**
 * (c) 2017 Andrey Mukamolov
 * Created 10/19/17.
 */
class CurrencyViewHolder(itemView: View) : ViewHolder(itemView), OnClickListener {
  private val currName: TextView
  private val currValue: TextView
  private val bankName: TextView

  fun bind(item: BestCourse) {
    currName.text = item.currencyName
    currValue.text = item.currencyValue
    bankName.text = item.bank
  }

  override fun onClick(view: View) {
    val req =
      Uri.parse("geo:0,0?q=" + bankName.text.toString().replace(" ".toRegex(), "+"))
    val mapIntent = Intent(Intent.ACTION_VIEW, req)
    try {
      ActivityCompat.startActivity(view.context, mapIntent, null)
    } catch (e: ActivityNotFoundException) {
      ExceptionHandler.handleException(e)
      Snackbar.make(itemView, R.string.maps_app_required, Snackbar.LENGTH_LONG).show()
    }
  }

  init {
    currName = itemView.findViewById(R.id.currency_name)
    currValue = itemView.findViewById(R.id.currency_value)
    bankName = itemView.findViewById(R.id.bank_name)
    val cv: CardView = itemView.findViewById(R.id.cv)
    cv.setOnClickListener(this)
  }
}