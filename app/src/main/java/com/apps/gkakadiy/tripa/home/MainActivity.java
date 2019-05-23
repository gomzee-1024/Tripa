package com.apps.gkakadiy.tripa.home;

import android.content.Intent;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.apps.gkakadiy.tripa.addtrip.AddTripActivity;
import com.apps.gkakadiy.tripa.Injection;
import com.apps.gkakadiy.tripa.R;
import com.apps.gkakadiy.tripa.notifications.NotificationsFragment;
import com.apps.gkakadiy.tripa.searchUser.SearchUserFragment;
import com.apps.gkakadiy.tripa.signin.LoginActivity;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

public class MainActivity extends AppCompatActivity implements HomeContract.HomeBaseView, SearchUserFragment.OnFragmentInteractionListener {

    private BottomNavigationViewEx mBottomNavigationViewEx;
    private HomePresenter mHomePresenter;
    private androidx.appcompat.widget.Toolbar mToolbar;
    private SearchUserFragment mSearchUserFragment;
    private NotificationsFragment mNotificationsFragment;
    private AppBarLayout mAppBar;
    FragmentManager mFragmentManager ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mToolbar = findViewById(R.id.main_activity_toolbar);
        setSupportActionBar(mToolbar);
        mAppBar = findViewById(R.id.main_activity_appbar);
        mBottomNavigationViewEx = findViewById(R.id.bnve);
        mBottomNavigationViewEx.enableShiftingMode(false);
        mBottomNavigationViewEx.enableItemShiftingMode(false);
        mBottomNavigationViewEx.setTextVisibility(false);
        mBottomNavigationViewEx.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.new_trip:
                        Intent intent = new Intent(MainActivity.this, AddTripActivity.class);
                        startActivity(intent);
                        return true;

                    case R.id.bottom_menu_search:
                        Log.d("MainActivity: ", "Search Clicked");
                        if(!mSearchUserFragment.isAdded()) {
                            FragmentTransaction transaction = mFragmentManager.beginTransaction();
                            transaction.replace(R.id.main_activity_fragment_container, mSearchUserFragment);
                            mAppBar.setVisibility(View.GONE);
                            transaction.commit();
                        }
                        return true;
                    case R.id.bottom_menu_notifications:
                        Log.d("MainActivity: ", "Notifications Clicked");
                        if(!mNotificationsFragment.isAdded()){
                            FragmentTransaction transaction = mFragmentManager.beginTransaction();
                            transaction.replace(R.id.main_activity_fragment_container, mNotificationsFragment);
                            mAppBar.setVisibility(View.GONE);
                            transaction.commit();
                        }
                        return true;

                    default:
                        return true;
                }
            }
        });
        mHomePresenter = new HomePresenter(Injection.provideUserRepository(), this);
        mHomePresenter.subscribe();
        mHomePresenter.setOnDisconnect();
        mHomePresenter.setOnline();
        mHomePresenter.setFCMToken();
        mFragmentManager = getSupportFragmentManager();
        mSearchUserFragment = SearchUserFragment.newInstance(null, null);
        mNotificationsFragment = NotificationsFragment.newInstance(null,null);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mHomePresenter.subscribe();
    }

    @Override
    public void signOutSuccess() {
        Intent signInActivityIntent = new Intent(this, LoginActivity.class);
        signInActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(signInActivityIntent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mHomePresenter.unsubscribe();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_logout:
                Log.d("MainActivity:", "logout");
                mHomePresenter.signOut();
                return true;
            default:
                return false;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        mBottomNavigationViewEx.setOnNavigationItemSelectedListener(null);
        super.onDestroy();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
