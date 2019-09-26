package fobo66.exchangecourcesbelarus.ui;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import com.google.android.gms.common.GoogleApiAvailability;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

/**
 * (c) 2017 Andrey Mukamolov aka fobo66 <fobo66@protonmail.com>
 * Created by fobo66 on 12.02.2017.
 *
 * A fragment to display an error dialog
 */
public class ErrorDialogFragment extends DialogFragment {
  static final String DIALOG_ERROR = "dialog_error";
  static final int REQUEST_RESOLVE_ERROR = 1001;

  public ErrorDialogFragment() {
  }

  @Override @NonNull public Dialog onCreateDialog(Bundle savedInstanceState) {
    // Get the error code and retrieve the appropriate dialog
    int errorCode = Objects.requireNonNull(this.getArguments()).getInt(DIALOG_ERROR);
    return GoogleApiAvailability.getInstance()
        .getErrorDialog(this.getActivity(), errorCode, REQUEST_RESOLVE_ERROR);
  }

  @Override public void onDismiss(DialogInterface dialog) {
    ((MainActivity) Objects.requireNonNull(getActivity())).onDialogDismissed();
  }
}
