package com.itgosolutions.tutorial;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.itgosolutions.tutorial.google_login.ActivityGoogleLogin;
import com.itgosolutions.tutorial.image_upload.FragmentImageUpload;
import com.itgosolutions.tutorial.map.FragmentMap;
import com.itgosolutions.tutorial.rxjava.FragmentRxJava;
import com.itgosolutions.tutorial.webview_html.FragmentWebView;

public class MainActivity extends AppCompatActivity {

    private ActionBar mActionBar;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initToolbar();
        initNavigationMenu();
    }

    private void initToolbar(){
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mActionBar = getSupportActionBar();
        if(mActionBar != null){
            mActionBar.setTitle("Demo Tutorials");
            mActionBar.setDisplayHomeAsUpEnabled(true);
            mActionBar.setHomeButtonEnabled(true);
        }
    }

    private void initNavigationMenu(){
        NavigationView navigationView = findViewById(R.id.navigation_view);
        navigationView.setItemTextAppearance(R.style.AppTextMediumLight);

        final DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close){
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                mActionBar.setTitle(item.getTitle());
                drawerLayout.closeDrawers();

                Fragment fragment;
                switch (item.getItemId()){
                    case R.id.nav_google_login:
                        startActivityGoogleLogin();
                        break;
                    case R.id.nav_invoice_view:
                        fragment = FragmentWebView.newInstance();
                        addFragment(R.id.activity_main_content_frame, fragment);
                        break;
                    case R.id.nav_map:
                        fragment = FragmentMap.newInstance();
                        addFragment(R.id.activity_main_content_frame, fragment);
                        break;
                    case R.id.nav_rx_java:
                        fragment = FragmentRxJava.newInstance();
                        addFragment(R.id.activity_main_content_frame, fragment);
                        break;
                    case R.id.nav_image_upload:
                        fragment = FragmentImageUpload.newInstance();
                        addFragment(R.id.activity_main_content_frame, fragment);
                        break;
//                    default:
//                        fragment = FragmentWebView.newInstance();
//                        addFragment(R.id.activity_main_content_frame, fragment);
//                        break;
                }
                return true;
            }
        });

        drawerLayout.openDrawer(GravityCompat.START);
    }

    private void startActivityGoogleLogin() {
        startActivity(new Intent(this, ActivityGoogleLogin.class));
    }

    private void addFragment(int containerId, Fragment fragment){
        getSupportFragmentManager()
                .beginTransaction()
                .replace(containerId, fragment)
                //.disallowAddToBackStack()
                .commit();
    }
}
