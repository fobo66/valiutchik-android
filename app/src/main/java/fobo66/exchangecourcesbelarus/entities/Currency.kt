package fobo66.exchangecourcesbelarus.entities

/**
 * Currency model from XML
 *
 * Created by fobo66 on 16.08.2015.
 */
data class Currency(
  val bankname: String = "",
  val usdBuy: String = "",
  val usdSell: String = "",
  val eurBuy: String = "",
  val eurSell: String = "",
  val rurBuy: String = "",
  val rurSell: String = ""
)
