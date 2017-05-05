package fobo66.exchangecourcesbelarus.ui;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import com.google.android.gms.common.GoogleApiAvailability;

import fobo66.exchangecourcesbelarus.MainActivity;

/**
 * (c) 2017 Andrey Mukamolow aka fobo66 <fobo66@protonmail.com>
 * Created by fobo66 on 12.02.2017.
 *
 * A fragment to display an error dialog
 */
public class ErrorDialogFragment extends DialogFragment {
    public static final String DIALOG_ERROR = "dialog_error";
    public static final int REQUEST_RESOLVE_ERROR = 1001;

    public ErrorDialogFragment() {
    }

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Get the error code and retrieve the appropriate dialog
        int errorCode = this.getArguments().getInt(DIALOG_ERROR);
        return GoogleApiAvailability.getInstance().getErrorDialog(
                this.getActivity(), errorCode,REQUEST_RESOLVE_ERROR);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        ((MainActivity) getActivity()).onDialogDismissed();
    }
}
