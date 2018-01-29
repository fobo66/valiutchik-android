package fobo66.exchangecourcesbelarus;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import fobo66.exchangecourcesbelarus.list.BestCurrencyAdapter;
import fobo66.exchangecourcesbelarus.models.BestCourse;
import fobo66.exchangecourcesbelarus.ui.AboutActivity;
import fobo66.exchangecourcesbelarus.ui.SettingsActivity;
import fobo66.exchangecourcesbelarus.util.Constants;
import fobo66.exchangecourcesbelarus.util.ExceptionHandler;
import fobo66.exchangecourcesbelarus.util.Util;

public class MainActivity extends BaseActivity {

  public static final String TIMESTAMP = "timestamp";
  private static final String TAG = MainActivity.class.getSimpleName();
  public static final String FIREBASE_REGISTERING_KEY = "firebase_registering";

  private SwipeRefreshLayout mySwipeRefreshLayout;
  private RecyclerView rv;
  private TextView buysell_indicator;

  public static boolean buyOrSell;

  public BestCurrencyAdapter adapter;

  private static List<BestCourse> previousBest = new ArrayList<>();
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

    setupCurrenciesList();

    setupSwipeRefreshLayout();

    setBuySellIndicator();
  }

  @Override protected void onStart() {
    super.onStart();
    buyOrSell = prefs.getBoolean(getString(R.string.pref_buysell), false);
    mGoogleApiClient.connect();
    LocalBroadcastManager.getInstance(this)
        .registerReceiver(receiver, new IntentFilter(Constants.BROADCAST_ACTION));
  }

  @Override protected void onStop() {
    super.onStop();

    SharedPreferences.Editor editor = prefs.edit();
    editor.putBoolean(getString(R.string.pref_buysell), buyOrSell);
    editor.apply();

    if (mGoogleApiClient.isConnected()) {
      mGoogleApiClient.disconnect();
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
        mySwipeRefreshLayout.setRefreshing(true);
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
      buysell_indicator.setText(R.string.sell);
    } else {
      buysell_indicator.setText(R.string.buy);
    }

    control.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
      @Override public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        mySwipeRefreshLayout.setRefreshing(true);
        buyOrSell = compoundButton.isChecked();

        if (mGoogleApiClient.isConnected() && userCity == null) {
          resolveUserCity();
        }
        try {
          fetchCourses(false);
        } catch (Exception e) {
          ExceptionHandler.handleException(e);
          Toast.makeText(MainActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
        }

        setBuySellIndicator();
      }
    });

    return true;
  }

  @Override public void onSaveInstanceState(Bundle savedInstanceState) {
    savedInstanceState.putBoolean(ADDRESS_REQUESTED_KEY, mAddressRequested);
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
        mySwipeRefreshLayout.post(new Runnable() {
          @Override public void run() {
            mySwipeRefreshLayout.setRefreshing(false);
          }
        });
      }
    } else if (requestCode == Constants.INTERNET_PERMISSIONS_REQUEST) {
      if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
        try {
          fetchCourses(true);
        } catch (Exception e) {
          ExceptionHandler.handleException(e);
          Toast.makeText(MainActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
        }
      } else {
        mySwipeRefreshLayout.post(new Runnable() {
          @Override public void run() {
            mySwipeRefreshLayout.setRefreshing(false);
          }
        });
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
        CurrencyRateService.fetchCourses(this, userCity);
      } else {
        Toast.makeText(this, R.string.connection_unavailable_info, Toast.LENGTH_LONG).show();
        onDataError();
      }

      runOnUiThread(new Runnable() {
        @Override public void run() {
          mySwipeRefreshLayout.setRefreshing(false);
        }
      });
    }
  }

  private void setBuySellIndicator() {
    if (buyOrSell) {
      buysell_indicator.setText(R.string.sell);
    } else {
      buysell_indicator.setText(R.string.buy);
    }
  }

  private void setupSwipeRefreshLayout() {
    mySwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
      @Override public void onRefresh() {
        if (userCity == null) {
          resolveUserCity();
        } else {
          try {
            fetchCourses(false);
          } catch (Exception e) {
            ExceptionHandler.handleException(e);
            Toast.makeText(MainActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();

          }
        }
      }
    });
    mySwipeRefreshLayout.setColorSchemeResources(R.color.primary_material_light_1);
    mySwipeRefreshLayout.post(new Runnable() {
      @Override public void run() {
        mySwipeRefreshLayout.setRefreshing(true);
      }
    });
  }

  private void setupCurrenciesList() {
    LinearLayoutManager llm = new LinearLayoutManager(this);
    adapter = new BestCurrencyAdapter(previousBest);
    rv.setLayoutManager(llm);
    rv.setItemAnimator(new DefaultItemAnimator());
    rv.setAdapter(adapter);
    rv.setHasFixedSize(true);
  }

  private void setupLayout() {
    Toolbar toolbar = findViewById(R.id.toolbar);
    mySwipeRefreshLayout = findViewById(R.id.swipe_refresh);
    rv = findViewById(R.id.rv);
    buysell_indicator = findViewById(R.id.buysell_indicator);

    setSupportActionBar(toolbar);
  }

  private void setupFirebaseReference() {
    bestCourseRef = getBestCoursesReference();
    bestCourseRef.keepSynced(false);
    bestCourseRef.addValueEventListener(new ValueEventListener() {
      @Override public void onDataChange(DataSnapshot dataSnapshot) {
        GenericTypeIndicator<List<BestCourse>> t = new GenericTypeIndicator<List<BestCourse>>() {
        };
        previousBest = dataSnapshot.getValue(t);
      }

      @Override public void onCancelled(DatabaseError databaseError) {
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
    runOnUiThread(new Runnable() {
      @Override public void run() {
        MainActivity.this.adapter.onDataUpdate(previousBest);
        mySwipeRefreshLayout.setRefreshing(false);
      }
    });
  }

  private void updateValuesFromBundle(Bundle savedInstanceState) {
    if (savedInstanceState != null) {
      if (savedInstanceState.keySet().contains(ADDRESS_REQUESTED_KEY)) {
        mAddressRequested = savedInstanceState.getBoolean(ADDRESS_REQUESTED_KEY);
      }

      if (savedInstanceState.keySet().contains(LOCATION_ADDRESS_KEY)) {
        userCity = savedInstanceState.getString(LOCATION_ADDRESS_KEY);
      }

      if (savedInstanceState.keySet().contains(getString(R.string.pref_buysell))) {
        buyOrSell = savedInstanceState.getBoolean(getString(R.string.pref_buysell));
      }

      if (savedInstanceState.keySet().contains(FIREBASE_REGISTERING_KEY)) {
        firebaseRegistering = savedInstanceState.getBoolean(FIREBASE_REGISTERING_KEY);
      }
    }
  }

  private void constructBroadcastReceiver() {
    receiver = new BroadcastReceiver() {
      @Override public void onReceive(Context context, Intent intent) {
        ArrayList<BestCourse> extra =
            intent.getParcelableArrayListExtra(Constants.EXTRA_BESTCOURSES);
        MainActivity.this.adapter.onDataUpdate(extra);
        MainActivity.this.getBestCoursesReference().setValue(extra);
      }
    };
  }
}
