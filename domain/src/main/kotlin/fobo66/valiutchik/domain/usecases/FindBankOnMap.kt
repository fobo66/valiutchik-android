package fobo66.valiutchik.domain.usecases

import android.content.Intent

interface FindBankOnMap {
  fun execute(bankName: CharSequence): Intent?
}
