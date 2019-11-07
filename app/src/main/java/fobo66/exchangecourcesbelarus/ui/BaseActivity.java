package fobo66.exchangecourcesbelarus.ui;

import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.preference.PreferenceManager;
import com.crashlytics.android.Crashlytics;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.mapbox.api.geocoding.v5.GeocodingCriteria;
import com.mapbox.api.geocoding.v5.MapboxGeocoding;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.api.geocoding.v5.models.GeocodingResponse;
import com.mapbox.geojson.Point;
import fobo66.exchangecourcesbelarus.R;
import fobo66.exchangecourcesbelarus.util.Constants;
import fobo66.exchangecourcesbelarus.util.ExceptionHandler;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * (c) 2017 Andrey Mukamolov aka fobo66
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

  public abstract void fetchCourses(boolean force);

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    prefs = PreferenceManager.getDefaultSharedPreferences(this);

    if (googleApiClient == null) {
      googleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this)
          .addOnConnectionFailedListener(this)
          .addApi(LocationServices.API)
          .build();
    }

    userCity = prefs.getString("default_city", "Минск");
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

              geocodingRequest = MapboxGeocoding.builder()
                  .accessToken(Constants.GEOCODER_ACCESS_TOKEN)
                  .geocodingTypes(GeocodingCriteria.TYPE_PLACE)
                  .query(Point.fromLngLat(lastLocation.getLongitude(), lastLocation.getLatitude()))
                  .languages("ru-RU")
                  .country("by")
                      .build();

              geocodingRequest.enqueueCall(new Callback<GeocodingResponse>() {
                @Override public void onResponse(Call<GeocodingResponse> call,
                    Response<GeocodingResponse> response) {
                  List<CarmenFeature> features = response.body().features();

                  if (!features.isEmpty()) {
                    userCity = features.get(0).text();
                  } else {
                    userCity = prefs.getString("default_city", "Минск");
                  }

                  fetchCourses(true);

                  addressRequested = false;
                }

                @Override public void onFailure(Call<GeocodingResponse> call, Throwable t) {
                  Crashlytics.log(0, TAG, "Getting city using Mapbox Geocoding API unsuccessful");
                  userCity = prefs.getString("default_city", "Минск");
                  Crashlytics.logException(t);

                  fetchCourses(true);

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
