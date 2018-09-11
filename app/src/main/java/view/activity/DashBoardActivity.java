package view.activity;


import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.vsolv.bigflow.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import models.CustomExpandableListAdapter;
import models.ExpandableListDataSource;
import models.FragmentNavigationManager;
import models.UserDetails;
import network.ConnectivityReceiver;
import network.LocationService;
import presenter.NavigationManager;
import presenter.UserSessionManager;
import view.fragment.AboutFragment;
import view.fragment.DirctScheduleFragment;
import view.fragment.ProfileFragment;


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

    private ExpandableListView mExpandableListView;
    private List<String> mExpandableListTitle;
    private NavigationManager mNavigationManager;

    private Map<String, List<String>> mExpandableListData;
    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);

        session = new UserSessionManager(getApplicationContext());
        connectivityReceiver = new ConnectivityReceiver();

        mExpandableListView = findViewById(R.id.navList);
        mNavigationManager = FragmentNavigationManager.obtain(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fabButtonDetails();

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        setHeaderDetails();

        mExpandableListView.addHeaderView(navHeader);
        mExpandableListData = ExpandableListDataSource.getData(this);
        mExpandableListTitle = new ArrayList(mExpandableListData.keySet());

        addDrawerItems();
        connectivityReceiver.setConnectivityReceiver(new ConnectivityReceiver.ConnectivityReceiverListener() {
            @Override
            public void onNetworkConnectionChanged(boolean isConnected) {
                dataSynchronize();
            }
        });


        //location service

        checkLocationPermission();
        if (boolean_permission_location) {
            Intent intent = new Intent(getApplicationContext(), LocationService.class);
            startService(intent);
        }
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
                setFragment(fragment);
            }
        });

        nav_header_title.setText(UserDetails.getUser_name());
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
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            setFragment(new AboutFragment());
        } else if (id == R.id.action_about) {
            setFragment(new AboutFragment());
        } else if (id == R.id.action_logout) {
            session.logoutUser();
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private void addDrawerItems() {
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
                setFragment(selectFragment(selectedItem));
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
                    setFragment(selectFragment(selectedItem));
                }
                return false;
            }
        });


    }

    public void setFragment(Fragment fragment) {
        if (fragment != null) {

            android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.setCustomAnimations(android.R.anim.fade_in,
                    android.R.anim.fade_out);
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
            default:
                fragment = AboutFragment.newInstance("About", "");
                break;
        }
        return fragment;
    }


}