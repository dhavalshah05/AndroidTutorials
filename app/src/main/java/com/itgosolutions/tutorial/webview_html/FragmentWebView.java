package com.itgosolutions.tutorial.webview_html;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.itgosolutions.tutorial.R;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

public class FragmentWebView extends Fragment {

    private WebView mWebView;

    public static FragmentWebView newInstance() {
        
        Bundle args = new Bundle();
        
        FragmentWebView fragment = new FragmentWebView();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_web_view, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        mWebView = view.findViewById(R.id.fragment_web_view_web_view);

        // to support zooming
        mWebView.getSettings().setBuiltInZoomControls(true);

        // to use wide viewport
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.getSettings().setUseWideViewPort(true);

        /* To display HTML String to web view */
        //String htmlAsString = getString(R.string.html);
        //displayWebView(htmlAsString);

        /* To display HTML String loaded from server */
        //GetInvoiceAsyncTask task = new GetInvoiceAsyncTask();
        //task.execute();

        /* TO display html file located in assets folder */
        mWebView.loadUrl("file:///android_asset/invoice.html");
    }

    private void displayWebView(String htmlString) {
        mWebView.loadDataWithBaseURL(null, htmlString, "text/html", "utf-8", null);
    }

    private class GetInvoiceAsyncTask extends AsyncTask<Void, Void, String> {

        private static final String mURL = "http://192.168.0.103:7777/api/html";

        @Override
        protected String doInBackground(Void... voids) {

            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(mURL)
                    .build();

            try {
                Response response = client.newCall(request).execute();
                return response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String htmlString) {

            if (htmlString != null) {
                displayWebView(htmlString);
            }

        }
    }
}
