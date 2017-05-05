package fobo66.exchangecourcesbelarus;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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

import com.appodeal.ads.Appodeal;
import com.appodeal.ads.BannerCallbacks;
import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import fobo66.exchangecourcesbelarus.models.BestCourse;
import fobo66.exchangecourcesbelarus.ui.AboutActivity;
import fobo66.exchangecourcesbelarus.ui.SettingsActivity;
import fobo66.exchangecourcesbelarus.util.Constants;
import fobo66.exchangecourcesbelarus.util.ExceptionHandler;

public class MainActivity extends BaseActivity {

    public static final String TIMESTAMP = "timestamp";
    private static final String TAG = "MainActivity";

    private SwipeRefreshLayout mySwipeRefreshLayout;
    private RecyclerView rv;
    private TextView buysell_indicator;

    public static boolean buyOrSell;

    public BestCurrencyAdapter adapter;

    private static List<BestCourse> previousBest;
    private static FirebaseDatabase mDatabase;
    private DatabaseReference bestCourseRef;
    private boolean firebaseRegistering = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prefs = getPreferences(Context.MODE_PRIVATE);

        updateValuesFromBundle(savedInstanceState);

        mDatabase = getFirebase();
        constructBroadcastReceiver();

        bestCourseRef = getReference();
        bestCourseRef.keepSynced(false);
        bestCourseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                GenericTypeIndicator<List<BestCourse>> t = new GenericTypeIndicator<List<BestCourse>>() {
                };
                previousBest = dataSnapshot.getValue(t);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "onCancelled: Firebase failed: " + databaseError.getDetails());
            }


        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        mySwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        rv = (RecyclerView) findViewById(R.id.rv);
        buysell_indicator = (TextView) findViewById(R.id.buysell_indicator);

        setSupportActionBar(toolbar);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        adapter = new BestCurrencyAdapter(previousBest);
        rv.setLayoutManager(llm);
        rv.setItemAnimator(new DefaultItemAnimator());
        rv.setAdapter(adapter);
        rv.setHasFixedSize(true);

        mySwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        if (userCity == null) {
                            resolveUserCity();
                        } else {
                            // This method performs the actual previousBest-refresh operation.
                            // The method calls setRefreshing(false) when it's finished.
                            try {
                                UpdateOperation(false);
                            } catch (Exception e) {
                                FirebaseCrash.report(e);
                                e.printStackTrace();
                            }
                        }
                    }
                }
        );
        mySwipeRefreshLayout.setColorSchemeResources(R.color.primary_material_light_1);
        mySwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mySwipeRefreshLayout.setRefreshing(true);
            }
        });

        if (buyOrSell) {
            buysell_indicator.setText(R.string.sell);
        } else {
            buysell_indicator.setText(R.string.buy);
        }

        Appodeal.setAutoCache(Appodeal.BANNER, false);
        Appodeal.disableNetwork(this, "cheetah");
        Appodeal.initialize(this, Constants.APPODEAL_APP_KEY, Appodeal.BANNER);
        Appodeal.cache(this, Appodeal.BANNER);
        Appodeal.setBannerCallbacks(new BannerCallbacks() {
            @Override
            public void onBannerLoaded(int i, boolean b) {
                Appodeal.show(MainActivity.this, Appodeal.BANNER);
            }

            public void onBannerFailedToLoad() {
                FirebaseCrash.log("Ads failed to load");
            }

            public void onBannerShown() {
            }

            public void onBannerClicked() {
            }
        });
    }

    private DatabaseReference getReference() {
        if (buyOrSell) {
            return mDatabase.getReference("bestcourse_buy");
        } else {
            return mDatabase.getReference("bestcourse_sell");
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        buyOrSell = prefs.getBoolean(getString(R.string.pref_buysell), false);
        mGoogleApiClient.connect();

    }

    @Override
    protected void onStop() {
        super.onStop();

        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(getString(R.string.pref_buysell), buyOrSell);
        editor.apply();

        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }

        bestCourseRef.onDisconnect();

        firebaseRegistering = false;
    }

    @Override
    public void onResume() {
        super.onResume();
        Appodeal.onResume(this, Appodeal.BANNER);
    }


    public void UpdateOperation(boolean force) throws Exception {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.INTERNET},
                    Constants.INTERNET_PERMISSIONS_REQUEST);
        } else {

            ConnectivityManager connectManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = connectManager.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()) {
                CurrencyRateService.fetchCourses(this, userCity);
            } else {
                Toast.makeText(this, R.string.connection_unavailable_info,
                        Toast.LENGTH_LONG).show();
                onDataError();

                throw new Exception("Cannot fetch data from network ");
            }

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mySwipeRefreshLayout.setRefreshing(false);
                }
            });
        }
    }

    private void onDataError() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.courses = previousBest;
                adapter.notifyDataSetChanged();
                mySwipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void updateValuesFromBundle(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            // Check savedInstanceState to see if the address was previously requested.
            if (savedInstanceState.keySet().contains(ADDRESS_REQUESTED_KEY)) {
                mAddressRequested = savedInstanceState.getBoolean(ADDRESS_REQUESTED_KEY);
            }
            // Check savedInstanceState to see if the location address string was previously found
            // and stored in the Bundle. If it was found, display the address string in the UI.
            if (savedInstanceState.keySet().contains(LOCATION_ADDRESS_KEY)) {
                userCity = savedInstanceState.getString(LOCATION_ADDRESS_KEY);
            }

            if (savedInstanceState.keySet().contains(getString(R.string.pref_buysell))) {
                buyOrSell = savedInstanceState.getBoolean(getString(R.string.pref_buysell));
            }

            if (savedInstanceState.keySet().contains("firebase_registering")) {
                firebaseRegistering = savedInstanceState.getBoolean("firebase_registering");
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem item = menu.findItem(R.id.action_buysell);
        item.setActionView(R.layout.switch_actionbar);
        item.setChecked(buyOrSell);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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
                    UpdateOperation(true);
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

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuItem item = menu.findItem(R.id.action_buysell);
        RelativeLayout rootView = (RelativeLayout) item.getActionView();

        SwitchCompat control = (SwitchCompat) rootView.findViewById(R.id.switchForActionBar);

        control.setChecked(buyOrSell);

        if (control.isChecked()) {
            buysell_indicator.setText(R.string.sell);
        } else {
            buysell_indicator.setText(R.string.buy);
        }

        control.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mySwipeRefreshLayout.setRefreshing(true);
                buyOrSell = compoundButton.isChecked();

                if (mGoogleApiClient.isConnected() && userCity == null) {
                    resolveUserCity();
                }
                try {
                    UpdateOperation(false);
                } catch (Exception e) {
                    ExceptionHandler.handleException(e);
                }

                if (buyOrSell) {
                    buysell_indicator.setText(R.string.sell);
                } else {
                    buysell_indicator.setText(R.string.buy);
                }

                mySwipeRefreshLayout.setRefreshing(false);
            }
        });


        return true;
    }


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putBoolean(ADDRESS_REQUESTED_KEY, mAddressRequested);
        savedInstanceState.putString(LOCATION_ADDRESS_KEY, userCity);

        savedInstanceState.putBoolean(getString(R.string.pref_buysell), buyOrSell);

        savedInstanceState.putBoolean("firebase_registering", firebaseRegistering);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == Constants.LOCATION_PERMISSION_REQUEST) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                resolveUserCity();
            } else {
                mySwipeRefreshLayout.post(new Runnable() {
                    @Override
                    public void run() {
                        mySwipeRefreshLayout.setRefreshing(false);
                    }
                });
            }
        } else if (requestCode == Constants.INTERNET_PERMISSIONS_REQUEST) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                try {
                    UpdateOperation(true);
                } catch (Exception e) {
                    ExceptionHandler.handleException(e);
                }
            } else {
                mySwipeRefreshLayout.post(new Runnable() {
                    @Override
                    public void run() {
                        mySwipeRefreshLayout.setRefreshing(false);
                    }
                });
            }
        }
    }

    private static FirebaseDatabase getFirebase() {
        if (mDatabase == null) {
            mDatabase = FirebaseDatabase.getInstance();
            mDatabase.setPersistenceEnabled(true);
        }
        return mDatabase;
    }

    private void constructBroadcastReceiver() {
        IntentFilter intentFilter = new IntentFilter(
                Constants.BROADCAST_ACTION);
        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                ArrayList<BestCourse> extra = intent.getParcelableArrayListExtra(Constants.EXTRA_BESTCOURSES);
                MainActivity.this.adapter.courses = extra;
                MainActivity.this.getReference().setValue(extra);
                MainActivity.this.adapter.notifyDataSetChanged();
            }
        };
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, intentFilter);
    }
}
