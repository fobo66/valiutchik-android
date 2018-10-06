package fobo66.exchangecourcesbelarus;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.security.ProviderInstaller;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import fobo66.exchangecourcesbelarus.list.BestCoursesAdapter;
import fobo66.exchangecourcesbelarus.models.BestCourse;
import fobo66.exchangecourcesbelarus.ui.AboutActivity;
import fobo66.exchangecourcesbelarus.ui.SettingsActivity;
import fobo66.exchangecourcesbelarus.util.Constants;
import fobo66.exchangecourcesbelarus.util.ExceptionHandler;
import fobo66.exchangecourcesbelarus.util.Util;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {

  private static final String TAG = MainActivity.class.getSimpleName();

  private SwipeRefreshLayout swipeRefreshLayout;
  private RecyclerView coursesList;
  private TextView buysellIndicator;

  public boolean buyOrSell;

  private BestCoursesAdapter adapter;

  private List<BestCourse> previousBest = new ArrayList<>();
  private DatabaseReference bestCourseRef;
  private boolean firebaseRegistering = true;
  private BroadcastReceiver receiver;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    prefs = getPreferences(Context.MODE_PRIVATE);

    updateValuesFromBundle(savedInstanceState);

    constructBroadcastReceiver();
    setupFirebaseReference();
    setupLayout();

    setupPlayServices();

    setupCoursesList();

    setupSwipeRefreshLayout();

    setBuySellIndicator();
  }

  @Override protected void onStart() {
    super.onStart();
    buyOrSell = prefs.getBoolean(getString(R.string.pref_buysell), false);
    googleApiClient.connect();
    IntentFilter intentFilter = new IntentFilter(Constants.BROADCAST_ACTION_SUCCESS);
    intentFilter.addAction(Constants.BROADCAST_ACTION_ERROR);
    LocalBroadcastManager.getInstance(this).registerReceiver(receiver, intentFilter);
  }

  @Override protected void onStop() {
    super.onStop();

    SharedPreferences.Editor editor = prefs.edit();
    editor.putBoolean(getString(R.string.pref_buysell), buyOrSell);
    editor.apply();

    if (googleApiClient.isConnected()) {
      googleApiClient.disconnect();
    }

    LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);

    bestCourseRef.onDisconnect();

    firebaseRegistering = false;
  }

  @Override public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_main, menu);
    MenuItem item = menu.findItem(R.id.action_buysell);
    item.setActionView(R.layout.switch_actionbar);
    item.setChecked(buyOrSell);
    return true;
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    int id = item.getItemId();

    switch (id) {
      case R.id.action_settings:
        Intent settingsIntent = new Intent(this, SettingsActivity.class);
        settingsIntent.putExtra(SettingsActivity.EXTRA_NO_HEADERS, true);
        startActivity(settingsIntent);
        return true;
      case R.id.action_update:
        swipeRefreshLayout.setRefreshing(true);
        try {
          fetchCourses(true);
        } catch (Exception e) {
          ExceptionHandler.handleException(e);
          resolveUserCity();
        }
        return true;
      case R.id.action_about:
        startActivity(new Intent(this, AboutActivity.class));
        return true;
    }

    return super.onOptionsItemSelected(item);
  }

  @Override public boolean onPrepareOptionsMenu(Menu menu) {
    super.onPrepareOptionsMenu(menu);
    MenuItem item = menu.findItem(R.id.action_buysell);
    RelativeLayout rootView = (RelativeLayout) item.getActionView();

    SwitchCompat control = rootView.findViewById(R.id.switchForActionBar);

    control.setChecked(buyOrSell);

    if (control.isChecked()) {
      buysellIndicator.setText(R.string.sell);
    } else {
      buysellIndicator.setText(R.string.buy);
    }

    control.setOnCheckedChangeListener((compoundButton, b) -> {
      swipeRefreshLayout.setRefreshing(true);
      buyOrSell = compoundButton.isChecked();
      adapter.setBuyOrSell(buyOrSell);

      if (googleApiClient.isConnected() && userCity == null) {
        resolveUserCity();
      }
      try {
        fetchCourses(false);
      } catch (Exception e) {
        ExceptionHandler.handleException(e);
        Snackbar.make(swipeRefreshLayout, R.string.get_data_error, Snackbar.LENGTH_SHORT).show();
      }

      setBuySellIndicator();
    });

    return true;
  }

  @Override public void onSaveInstanceState(Bundle savedInstanceState) {
    savedInstanceState.putBoolean(ADDRESS_REQUESTED_KEY, addressRequested);
    savedInstanceState.putString(LOCATION_ADDRESS_KEY, userCity);

    savedInstanceState.putBoolean(getString(R.string.pref_buysell), buyOrSell);

    savedInstanceState.putBoolean("firebase_registering", firebaseRegistering);
    super.onSaveInstanceState(savedInstanceState);
  }

  @Override public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
      @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    if (requestCode == Constants.LOCATION_PERMISSION_REQUEST) {
      if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
        resolveUserCity();
      } else {
        swipeRefreshLayout.post(() -> swipeRefreshLayout.setRefreshing(false));
      }
    } else if (requestCode == Constants.INTERNET_PERMISSIONS_REQUEST) {
      if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
        try {
          fetchCourses(true);
        } catch (Exception e) {
          ExceptionHandler.handleException(e);
          Snackbar.make(swipeRefreshLayout, R.string.get_data_error, Snackbar.LENGTH_SHORT).show();
        }
      } else {
        swipeRefreshLayout.post(() -> swipeRefreshLayout.setRefreshing(false));
      }
    }
  }

  @Override public void fetchCourses(boolean forceReload) {

    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE)
        != PackageManager.PERMISSION_GRANTED
        && ActivityCompat.checkSelfPermission(this, Manifest.permission.INTERNET)
        != PackageManager.PERMISSION_GRANTED) {

      ActivityCompat.requestPermissions(this,
          new String[] { Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.INTERNET },
          Constants.INTERNET_PERMISSIONS_REQUEST);
    } else {

      if (new Util().isNetworkAvailable(this)) {
        CurrencyRateService.fetchCourses(this, userCity, buyOrSell);
      } else {
        onDataError();
      }

      swipeRefreshLayout.post(() -> swipeRefreshLayout.setRefreshing(false));
    }
  }

  private void setBuySellIndicator() {
    if (buyOrSell) {
      buysellIndicator.setText(R.string.sell);
    } else {
      buysellIndicator.setText(R.string.buy);
    }
  }

  private void setupSwipeRefreshLayout() {
    swipeRefreshLayout.setOnRefreshListener(() -> {
      if (userCity == null) {
        resolveUserCity();
      } else {
        try {
          fetchCourses(false);
        } catch (Exception e) {
          ExceptionHandler.handleException(e);
          Snackbar.make(swipeRefreshLayout, R.string.get_data_error, Snackbar.LENGTH_SHORT).show();
        }
      }
    });
    swipeRefreshLayout.setColorSchemeResources(R.color.primary_color);
    swipeRefreshLayout.post(() -> swipeRefreshLayout.setRefreshing(true));
  }

  private void setupPlayServices() {
    try {
      ProviderInstaller.installIfNeeded(this);
    } catch (GooglePlayServicesRepairableException e) {
      GoogleApiAvailability.getInstance().showErrorNotification(this, e.getConnectionStatusCode());
    } catch (GooglePlayServicesNotAvailableException e) {
      GoogleApiAvailability.getInstance().showErrorDialogFragment(this, e.errorCode, 0);
    }
  }

  private void setupCoursesList() {
    LinearLayoutManager llm = new LinearLayoutManager(this);
    adapter = new BestCoursesAdapter(previousBest);
    coursesList.setLayoutManager(llm);
    coursesList.setItemAnimator(new DefaultItemAnimator());
    coursesList.setAdapter(adapter);
    coursesList.setHasFixedSize(true);
  }

  private void setupLayout() {
    Toolbar toolbar = findViewById(R.id.toolbar);
    swipeRefreshLayout = findViewById(R.id.swipe_refresh);
    coursesList = findViewById(R.id.rv);
    buysellIndicator = findViewById(R.id.buysell_indicator);

    setSupportActionBar(toolbar);
  }

  private void setupFirebaseReference() {
    bestCourseRef = getBestCoursesReference();
    bestCourseRef.keepSynced(false);
    bestCourseRef.addValueEventListener(new ValueEventListener() {
      @Override public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        GenericTypeIndicator<List<BestCourse>> t = new GenericTypeIndicator<List<BestCourse>>() {
        };
        previousBest.clear();
        previousBest.addAll(dataSnapshot.getValue(t));
      }

      @Override public void onCancelled(@NonNull DatabaseError databaseError) {
        Log.e(TAG, "onCancelled: Firebase failed: " + databaseError.getDetails());
      }
    });
  }

  private DatabaseReference getBestCoursesReference() {
    if (buyOrSell) {
      return FirebaseDatabase.getInstance().getReference("bestcourse_buy");
    } else {
      return FirebaseDatabase.getInstance().getReference("bestcourse_sell");
    }
  }

  private void onDataError() {
    Snackbar.make(swipeRefreshLayout, R.string.courses_unavailable_info, Snackbar.LENGTH_LONG)
        .show();

    runOnUiThread(() -> {
      MainActivity.this.adapter.onDataUpdate(previousBest);
      swipeRefreshLayout.setRefreshing(false);
    });
  }

  private void updateValuesFromBundle(Bundle savedInstanceState) {
    if (savedInstanceState != null) {
      if (savedInstanceState.keySet().contains(ADDRESS_REQUESTED_KEY)) {
        addressRequested = savedInstanceState.getBoolean(ADDRESS_REQUESTED_KEY);
      }

      if (savedInstanceState.keySet().contains(LOCATION_ADDRESS_KEY)) {
        userCity = savedInstanceState.getString(LOCATION_ADDRESS_KEY);
      }

      if (savedInstanceState.keySet().contains(getString(R.string.pref_buysell))) {
        buyOrSell = savedInstanceState.getBoolean(getString(R.string.pref_buysell));
      }

      if (savedInstanceState.keySet().contains(Constants.FIREBASE_REGISTERING_KEY)) {
        firebaseRegistering = savedInstanceState.getBoolean(Constants.FIREBASE_REGISTERING_KEY);
      }
    }
  }

  private void constructBroadcastReceiver() {
    receiver = new BroadcastReceiver() {
      @Override public void onReceive(Context context, Intent intent) {
        String intentAction = intent.getAction();
        if (intentAction != null) {
          if (intentAction.equals(Constants.BROADCAST_ACTION_SUCCESS)) {
            ArrayList<BestCourse> extra =
                intent.getParcelableArrayListExtra(Constants.EXTRA_BESTCOURSES);
            MainActivity.this.adapter.onDataUpdate(extra);
            MainActivity.this.getBestCoursesReference().setValue(extra);
          } else if (intentAction.equals(Constants.BROADCAST_ACTION_ERROR)) {
            onDataError();
          }
        } else {
          onDataError();
        }
      }
    };
  }
}
