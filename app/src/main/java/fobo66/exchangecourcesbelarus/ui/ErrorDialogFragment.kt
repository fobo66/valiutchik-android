package fobo66.exchangecourcesbelarus.ui

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.google.android.gms.common.GoogleApiAvailability
import java.util.Objects

/**
 * (c) 2017 Andrey Mukamolov aka fobo66
 * Created by fobo66 on 12.02.2017.
 *
 * A fragment to display an error dialog
 */
class ErrorDialogFragment : DialogFragment() {
  override fun onCreateDialog(savedInstanceState: Bundle?): Dialog { // Get the error code and retrieve the appropriate dialog
    val errorCode = arguments?.getInt(DIALOG_ERROR) ?: REQUEST_RESOLVE_ERROR
    return GoogleApiAvailability.getInstance()
      .getErrorDialog(
        this.activity,
        errorCode,
        REQUEST_RESOLVE_ERROR
      )
  }

  override fun onDismiss(dialog: DialogInterface) {
    (Objects.requireNonNull(activity) as MainActivity).onDialogDismissed()
  }

  companion object {
    internal const val DIALOG_ERROR = "dialog_error"
    internal const val REQUEST_RESOLVE_ERROR = 1001
  }
}