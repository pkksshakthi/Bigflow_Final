package view.activity;


import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.vsolv.bigflow.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import models.Common;
import models.CustomExpandableListAdapter;
import models.ExpandableListDataSource;
import models.LocationSync;
import models.UserDetails;
import network.ConnectivityReceiver;
import network.LocationService;
import presenter.UserSessionManager;
import view.fragment.AboutFragment;
import view.fragment.AddScheduleFragment;
import view.fragment.DayScheduleFragment;
import view.fragment.DirctScheduleFragment;
import view.fragment.EmpAchievementFragment;
import view.fragment.ProfileFragment;
import view.fragment.SynchronizeFragment;


public class DashBoardActivity extends AppCompatActivity {
    UserSessionManager session;
    private static final int REQUEST_PERMISSIONS_LOCATION = 100;
    private boolean boolean_permission_location;
    View navHeader;
    TextView nav_header_title, nav_header_subtitle;
    ImageView nav_profile;
    FloatingActionButton fab;
    IntentFilter mIntentFilter;
    ConnectivityReceiver connectivityReceiver;
    private Boolean isOffline = false;

    private ExpandableListView mExpandableListView;
    private List<String> mExpandableListTitle;

    private Map<String, List<String>> mExpandableListData;
    private DrawerLayout drawer;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
        loadView();
        initializeView();
        loadData();


        fabButtonDetails();

        checkLocationPermission();
        connectivityReceiver.setConnectivityReceiver(new ConnectivityReceiver.ConnectivityReceiverListener() {
            @Override
            public void onNetworkConnectionChanged(boolean isConnected) {
                View view = findViewById(R.id.content_frame);
                if (isConnected) {
                    dataSynchronize();
                    if (isOffline) {
                        Common.showSnackbar_success(getApplicationContext(), view, "You are online");
                        isOffline = false;
                    }
                } else {
                    isOffline = true;
                    Common.showSnackbar_warning(getApplicationContext(), view, "You are offline");
                }
            }
        });


        if (boolean_permission_location) {
            Intent intent = new Intent(getApplicationContext(), LocationService.class);
            startService(intent);
        }
        if (getFragmentManager().findFragmentById(R.id.content_frame) == null) {
            setFragment(selectFragment(""), false);
        }
    }

    private void loadView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        mExpandableListView = findViewById(R.id.navList);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    }

    private void initializeView() {
        session = new UserSessionManager(getApplicationContext());
        connectivityReceiver = new ConnectivityReceiver();
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        setHeaderDetails();
        addDrawerItems();
    }

    private void loadData() {
    }

    private void checkLocationPermission() {
        if ((ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {

            if ((ActivityCompat.shouldShowRequestPermissionRationale(DashBoardActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION))) {

                ActivityCompat.requestPermissions(DashBoardActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_PERMISSIONS_LOCATION);
            } else {
                ActivityCompat.requestPermissions(DashBoardActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION

                        },
                        REQUEST_PERMISSIONS_LOCATION);
                ActivityCompat.requestPermissions(DashBoardActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_PERMISSIONS_LOCATION);
            }
        } else {
            boolean_permission_location = true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_PERMISSIONS_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    boolean_permission_location = true;

                } else {

                    Toast.makeText(getApplicationContext(), "Go to Setting and enable Location Permission", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    public void dataSynchronize() {
        String out_message = LocationSync.LatLongSet(DashBoardActivity.this);
    }

    private void checkLocationOn() {

        LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (!Common.isOnline(getApplicationContext())) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Internet Connection");
            builder.setMessage("Please enable Internet connection");
            builder.setCancelable(false);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent intent = new Intent(android.provider.Settings.ACTION_SETTINGS);
                    startActivityForResult(intent, 0);
                }
            });
            Dialog alertDialog = builder.create();
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.show();
        } else if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                !lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            // Build the alert dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Location Services Not Active");
            builder.setMessage("Please enable Location Services and GPS");
            builder.setCancelable(false);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                }
            });
            Dialog alertDialog = builder.create();
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.show();
        }
    }

    private void fabButtonDetails() {
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private void setHeaderDetails() {
        LayoutInflater inflater = getLayoutInflater();
        navHeader = inflater.inflate(R.layout.nav_header_dash_board, mExpandableListView, false);

        nav_header_title = navHeader.findViewById(R.id.nav_header_title);
        nav_header_subtitle = navHeader.findViewById(R.id.nav_header_subtitle);
        nav_profile = navHeader.findViewById(R.id.nav_ivprofile);

        nav_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new ProfileFragment();
                setFragment(fragment, true);
            }
        });

        nav_header_title.setText(UserDetails.getUser_name());
        mExpandableListView.addHeaderView(navHeader);
    }


    @Override
    protected void onResume() {
        super.onResume();
        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        mIntentFilter.addAction("restarting.services");
        mIntentFilter.addAction("android.intent.action.BOOT_COMPLETED");
        registerReceiver(connectivityReceiver, mIntentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(connectivityReceiver);
    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {

            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dash_board, menu);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkLocationOn();
        //checkInternetOn();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            setFragment(new SynchronizeFragment(), true);
        } else if (id == R.id.action_about) {
            setFragment(new AboutFragment(), true);
        } else if (id == R.id.action_logout) {
            session.logoutUser();
            startActivity(new Intent(getApplicationContext(), MapsActivity.class));
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private void addDrawerItems() {
        mExpandableListData = ExpandableListDataSource.getData(this);
        mExpandableListTitle = new ArrayList(mExpandableListData.keySet());
        ExpandableListAdapter mExpandableListAdapter = new CustomExpandableListAdapter(this, mExpandableListTitle, mExpandableListData);
        mExpandableListView.setAdapter(mExpandableListAdapter);
        mExpandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                int r = 1;
                //getSupportActionBar().setTitle(mExpandableListTitle.get(groupPosition).toString());
            }
        });

        mExpandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {
                //getSupportActionBar().setTitle(mExpandableListTitle.get(groupPosition).toString());
            }
        });

        mExpandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                String selectedItem = ((List) (mExpandableListData.get(mExpandableListTitle.get(groupPosition))))
                        .get(childPosition).toString();
                setFragment(selectFragment(selectedItem), true);
                return false;
            }
        });
        mExpandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                ExpandableListAdapter adapter = parent.getExpandableListAdapter();
                int childCount = adapter.getChildrenCount(groupPosition);
                if (childCount == 0) {
                    String selectedItem = mExpandableListTitle.get(childCount).toString();
                    setFragment(selectFragment(selectedItem), true);
                }
                return false;
            }
        });


    }

    public void setFragment(Fragment fragment, Boolean addHistory) {
        if (fragment != null) {

            android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.setCustomAnimations(android.R.anim.fade_in,
                    android.R.anim.fade_out);
            if (addHistory)
                transaction.addToBackStack("fragment");
            transaction.replace(R.id.content_frame, fragment, "example");
            transaction.commitAllowingStateLoss();
        }
        drawer.closeDrawer(GravityCompat.START);
    }

    public Fragment selectFragment(String selectedItem) {
        Fragment fragment = null;
        switch (selectedItem) {
            case "Direct":
                fragment = DirctScheduleFragment.newInstance("Today Schedule", "");
                fab.hide();
                break;
            case "FET":
                fragment = AddScheduleFragment.newInstance("Add Schedule", "");
                fab.hide();
                break;
            case "Approve":
                fragment = EmpAchievementFragment.newInstance("Add Schedule", "");
                fab.hide();
                break;
            default:
                fragment = DayScheduleFragment.newInstance("Customers", "");
                fab.hide();
                break;
        }
        return fragment;
    }


}