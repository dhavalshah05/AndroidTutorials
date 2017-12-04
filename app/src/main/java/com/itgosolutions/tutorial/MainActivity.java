package com.itgosolutions.tutorial;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.itgosolutions.tutorial.map.FragmentMap;
import com.itgosolutions.tutorial.webview_html.FragmentWebView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //addFragmentWebView();
        addFragmentMap();
    }

    private void addFragmentWebView(){
        FragmentWebView fragmentWebView = FragmentWebView.newInstance();
        addFragment(R.id.activity_main_content_frame, fragmentWebView, FragmentWebView.class.getName());
    }

    private void addFragmentMap(){
        FragmentMap fragmentMap = FragmentMap.newInstance();
        addFragment(R.id.activity_main_content_frame, fragmentMap, FragmentMap.class.getName());
    }

    private void addFragment(int containerId, Fragment fragment, String TAG){
        getSupportFragmentManager()
                .beginTransaction()
                .replace(containerId, fragment, TAG)
                .disallowAddToBackStack()
                .commit();
    }
}
