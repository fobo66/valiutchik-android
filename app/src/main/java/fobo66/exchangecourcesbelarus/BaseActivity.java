package fobo66.exchangecourcesbelarus;

import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.crash.FirebaseCrash;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.GeocodingApiRequest;
import com.google.maps.PendingResult;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;

import fobo66.exchangecourcesbelarus.ui.ErrorDialogFragment;
import fobo66.exchangecourcesbelarus.util.Constants;
import fobo66.exchangecourcesbelarus.util.ExceptionHandler;

/**
 * (c) 2017 Andrey Mukamolow aka fobo66 <fobo66@protonmail.com>
 * Created by fobo66 on 19.03.2017.
 */

public abstract class BaseActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "BaseActivity";
    protected static final String ADDRESS_REQUESTED_KEY = "address-request-pending";
    protected static final String LOCATION_ADDRESS_KEY = "location-address";
    public static String userCity;
    public GoogleApiClient mGoogleApiClient;
    protected Location mLastLocation;
    private boolean mResolvingError = false;
    protected boolean mAddressRequested;

    protected SharedPreferences prefs;

    public abstract void UpdateOperation(boolean force) throws Exception;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.i(TAG, "Connection established");
        resolveUserCity();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Connection suspended");
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

        if (mResolvingError) {
            return;
        } else if (connectionResult.hasResolution()) {
            try {
                mResolvingError = true;
                connectionResult.startResolutionForResult(this, ErrorDialogFragment.REQUEST_RESOLVE_ERROR);
            } catch (IntentSender.SendIntentException e) {
                mGoogleApiClient.connect();
            }
        } else {
            showErrorDialog(connectionResult.getErrorCode());
            mResolvingError = true;
        }
    }

    private void showErrorDialog(int errorCode) {
        ErrorDialogFragment dialogFragment = new ErrorDialogFragment();
        Bundle args = new Bundle();
        args.putInt(ErrorDialogFragment.DIALOG_ERROR, errorCode);
        dialogFragment.setArguments(args);
        dialogFragment.show(getSupportFragmentManager(), "ErrorDialog");
    }

    public void onDialogDismissed() {
        mResolvingError = false;
    }

    public void resolveUserCity() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION},
                    Constants.LOCATION_PERMISSION_REQUEST);
        } else {
            mAddressRequested = true;
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if (mLastLocation != null) {
                GeoApiContext geocontext = new GeoApiContext().setApiKey(Constants.GEOCODER_API_KEY);
                GeocodingApiRequest req = GeocodingApi.newRequest(geocontext).language("ru-RU").latlng(
                        new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude()));

                req.setCallback(new PendingResult.Callback<GeocodingResult[]>() {
                    @Override
                    public void onResult(GeocodingResult[] result) {
                        userCity = result[2].addressComponents[0].longName;

                        try {
                            UpdateOperation(true);
                        } catch (Exception e) {
                            ExceptionHandler.handleException(e);
                        }
                    }

                    @Override
                    public void onFailure(Throwable e) {
                        Log.d(TAG, "onFailure: Getting city using GMaps Geocoding API unsuccessful, setting default city...", e);
                        userCity = prefs.getString("default_city", "Минск");
                        FirebaseCrash.report(e);

                        try {
                            UpdateOperation(true);
                        } catch (Exception ex) {
                            ExceptionHandler.handleException(ex);
                        }
                    }
                });

                mAddressRequested = false;
            } else {
                Log.i(TAG, "Last location unavailable, setting default city...");
                userCity = prefs.getString("default_city", "Минск");
                try {
                    UpdateOperation(true);
                } catch (Exception e) {
                    ExceptionHandler.handleException(e);
                }

            }
        }
    }
}
