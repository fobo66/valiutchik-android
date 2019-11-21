package fobo66.exchangecourcesbelarus.ui

import android.Manifest.permission
import android.content.IntentSender.SendIntentException
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.preference.PreferenceManager
import com.crashlytics.android.Crashlytics
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.GoogleApiClient.Builder
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener
import com.google.android.gms.location.LocationServices
import com.mapbox.api.geocoding.v5.GeocodingCriteria
import com.mapbox.api.geocoding.v5.MapboxGeocoding
import com.mapbox.api.geocoding.v5.models.GeocodingResponse
import com.mapbox.geojson.Point
import fobo66.exchangecourcesbelarus.R.string
import fobo66.exchangecourcesbelarus.util.ExceptionHandler.handleException
import fobo66.exchangecourcesbelarus.util.GEOCODER_ACCESS_TOKEN
import fobo66.exchangecourcesbelarus.util.LOCATION_PERMISSION_REQUEST
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * (c) 2017 Andrey Mukamolov aka fobo66
 * Created by fobo66 on 19.03.2017.
 */
abstract class BaseActivity : AppCompatActivity(), ConnectionCallbacks,
  OnConnectionFailedListener {
  var userCity: String? = null
  var googleApiClient: GoogleApiClient? = null
  private var resolvingError = false
  protected lateinit var prefs: SharedPreferences
  private var geocodingRequest: MapboxGeocoding? = null

  abstract fun fetchCourses(force: Boolean)

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    prefs = PreferenceManager.getDefaultSharedPreferences(this)
    if (googleApiClient == null) {
      googleApiClient = Builder(this).addConnectionCallbacks(this)
        .addOnConnectionFailedListener(this)
        .addApi(LocationServices.API)
        .build()
    }
    loadDefaultCity()
  }

  override fun onDestroy() {
    super.onDestroy()
    geocodingRequest?.cancelCall()
  }

  override fun onConnected(bundle: Bundle?) {
    resolveUserCity()
  }

  override fun onConnectionSuspended(i: Int) {
    googleApiClient?.connect()
  }

  override fun onConnectionFailed(connectionResult: ConnectionResult) {
    if (!resolvingError && connectionResult.hasResolution()) {
      try {
        resolvingError = true
        connectionResult.startResolutionForResult(this, ErrorDialogFragment.REQUEST_RESOLVE_ERROR)
      } catch (e: SendIntentException) {
        googleApiClient?.connect()
      }
    } else {
      showErrorDialog(connectionResult.errorCode)
      resolvingError = true
    }
  }

  private fun showErrorDialog(errorCode: Int) {
    val dialogFragment = ErrorDialogFragment()
    val args = Bundle().apply {
      putInt(ErrorDialogFragment.DIALOG_ERROR, errorCode)
    }
    dialogFragment.arguments = args
    dialogFragment.show(supportFragmentManager, "ErrorDialog")
  }

  fun onDialogDismissed() {
    resolvingError = false
  }

  fun resolveUserCity() {
    if (ActivityCompat.checkSelfPermission(
        this,
        permission.ACCESS_COARSE_LOCATION
      )
      != PackageManager.PERMISSION_GRANTED) {
      ActivityCompat.requestPermissions(
        this, arrayOf(permission.ACCESS_COARSE_LOCATION),
        LOCATION_PERMISSION_REQUEST
      )
    } else {
      LocationServices.getFusedLocationProviderClient(this)
        .lastLocation
        .addOnSuccessListener { lastLocation: Location? ->
          if (lastLocation != null) {
            geocodingRequest = MapboxGeocoding.builder()
              .accessToken(GEOCODER_ACCESS_TOKEN)
              .geocodingTypes(GeocodingCriteria.TYPE_PLACE)
              .query(Point.fromLngLat(lastLocation.longitude, lastLocation.latitude))
              .languages("ru-RU")
              .country("by").build()

            geocodingRequest?.enqueueCall(object : Callback<GeocodingResponse?> {
              override fun onResponse(
                call: Call<GeocodingResponse?>,
                response: Response<GeocodingResponse?>
              ) {
                if (response.body() != null) {
                  val features = response.body()!!.features()
                  if (!features.isEmpty()) {
                    userCity = features[0].text()
                  } else {
                    loadDefaultCity()
                  }
                } else {
                  loadDefaultCity()
                }
                fetchCourses(true)
              }

              override fun onFailure(call: Call<GeocodingResponse?>, t: Throwable) {
                Crashlytics.log(
                  0,
                  TAG,
                  "Getting city using Mapbox Geocoding API unsuccessful"
                )
                handleException(t)
                loadDefaultCity()
                fetchCourses(true)
              }
            })
          } else {
            Crashlytics.log(
              0,
              TAG,
              "Last location unavailable, setting default city..."
            )
            loadDefaultCity()
            fetchCourses(true)
          }
        }
        .addOnFailureListener { e: Exception? ->
          handleException(e)
          Toast.makeText(this@BaseActivity, string.location_error_title, Toast.LENGTH_SHORT)
            .show()
        }
    }
  }

  private fun loadDefaultCity() {
    userCity = prefs.getString("default_city", "Минск")
  }

  companion object {
    private const val TAG = "BaseActivity"
    const val LOCATION_ADDRESS_KEY = "location-address"
  }
}