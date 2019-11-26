package fobo66.exchangecourcesbelarus.entities

import android.os.Parcelable
import com.google.firebase.database.IgnoreExtraProperties
import fobo66.exchangecourcesbelarus.util.CurrencyName
import fobo66.exchangecourcesbelarus.util.USD
import kotlinx.android.parcel.Parcelize

/**
 * Model for best available exchange courses to put into cardview
 * Created by fobo66 on 23.08.2015.
 */
@IgnoreExtraProperties
@Parcelize
data class BestCourse(
  val bank: String = "",
  val currencyValue: String = "",
  @CurrencyName val currencyName: String = USD,
  val isBuy: Boolean = false
) : Parcelable