package fobo66.valiutchik.core.usecases

import android.content.Intent

interface FindBankOnMap {
  fun execute(bankName: CharSequence): Intent?
}
