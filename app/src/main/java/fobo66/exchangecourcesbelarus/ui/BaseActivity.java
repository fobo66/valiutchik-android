package fobo66.exchangecourcesbelarus.ui;

import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.mapbox.services.api.geocoding.v5.GeocodingCriteria;
import com.mapbox.services.api.geocoding.v5.MapboxGeocoding;
import com.mapbox.services.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.services.api.geocoding.v5.models.GeocodingResponse;
import com.mapbox.services.commons.models.Position;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import fobo66.exchangecourcesbelarus.R;
import fobo66.exchangecourcesbelarus.util.Constants;
import fobo66.exchangecourcesbelarus.util.ExceptionHandler;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * (c) 2017 Andrey Mukamolov aka fobo66 <fobo66@protonmail.com>
 * Created by fobo66 on 19.03.2017.
 */

public abstract class BaseActivity extends AppCompatActivity
    implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

  private static final String TAG = "BaseActivity";
  protected static final String ADDRESS_REQUESTED_KEY = "address-request-pending";
  protected static final String LOCATION_ADDRESS_KEY = "location-address";

  public String userCity;
  public GoogleApiClient googleApiClient;
  private boolean resolvingError = false;
  protected boolean addressRequested;

  protected SharedPreferences prefs;
  private MapboxGeocoding geocodingRequest;

  public abstract void fetchCourses(boolean force) throws Exception;

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    if (googleApiClient == null) {
      googleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this)
          .addOnConnectionFailedListener(this)
          .addApi(LocationServices.API)
          .build();
    }
  }

  @Override protected void onDestroy() {
    super.onDestroy();

    if (geocodingRequest != null) {
      geocodingRequest.cancelCall();
    }
  }

  @Override public void onConnected(Bundle bundle) {
    resolveUserCity();
  }

  @Override public void onConnectionSuspended(int i) {
    googleApiClient.connect();
  }

  @Override public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    if (!resolvingError && connectionResult.hasResolution()) {
      try {
        resolvingError = true;
        connectionResult.startResolutionForResult(this, ErrorDialogFragment.REQUEST_RESOLVE_ERROR);
      } catch (IntentSender.SendIntentException e) {
        googleApiClient.connect();
      }
    } else {
      showErrorDialog(connectionResult.getErrorCode());
      resolvingError = true;
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
    resolvingError = false;
  }

  public void resolveUserCity() {
    if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
        != PackageManager.PERMISSION_GRANTED) {

      ActivityCompat.requestPermissions(this,
          new String[] { android.Manifest.permission.ACCESS_COARSE_LOCATION },
          Constants.LOCATION_PERMISSION_REQUEST);
    } else {
      addressRequested = true;
      LocationServices.getFusedLocationProviderClient(this)
          .getLastLocation()
          .addOnSuccessListener(lastLocation -> {

            if (lastLocation != null) {

              geocodingRequest =
                  new MapboxGeocoding.Builder().setAccessToken(Constants.GEOCODER_ACCESS_TOKEN)
                      .setCoordinates(Position.fromCoordinates(lastLocation.getLongitude(),
                          lastLocation.getLatitude()))
                      .setGeocodingType(GeocodingCriteria.TYPE_PLACE)
                      .setLanguages("ru-RU")
                      .setCountry("by")
                      .build();

              geocodingRequest.enqueueCall(new Callback<GeocodingResponse>() {
                @Override public void onResponse(Call<GeocodingResponse> call,
                    Response<GeocodingResponse> response) {
                  List<CarmenFeature> features = response.body().getFeatures();

                  if (!features.isEmpty()) {
                    userCity = features.get(0).getText();

                    try {
                      fetchCourses(true);
                    } catch (Exception e) {
                      ExceptionHandler.handleException(e);
                    }
                  }
                  addressRequested = false;
                }

                @Override public void onFailure(Call<GeocodingResponse> call, Throwable t) {
                  Crashlytics.log(0, TAG,
                      "onFailure: Getting city using Mapbox Geocoding API unsuccessful, setting default city...");
                  userCity = prefs.getString("default_city", "Минск");
                  Crashlytics.logException(t);

                  try {
                    fetchCourses(true);
                  } catch (Exception ex) {
                    ExceptionHandler.handleException(ex);
                  }

                  addressRequested = false;
                }
              });
            } else {
              Crashlytics.log(0, TAG, "Last location unavailable, setting default city...");
              userCity = prefs.getString("default_city", "Минск");
              try {
                fetchCourses(true);
              } catch (Exception e) {
                ExceptionHandler.handleException(e);
              }

              addressRequested = false;
            }
          })
          .addOnFailureListener(e -> {
            ExceptionHandler.handleException(e);
            Toast.makeText(BaseActivity.this, R.string.location_error_title, Toast.LENGTH_SHORT)
                .show();
            addressRequested = false;
          });
    }
  }
}
