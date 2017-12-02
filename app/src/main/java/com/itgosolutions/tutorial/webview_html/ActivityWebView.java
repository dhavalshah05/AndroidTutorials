package com.itgosolutions.tutorial.webview_html;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.Button;

import com.itgosolutions.tutorial.R;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;

public class ActivityWebView extends AppCompatActivity {

    private WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        mWebView = findViewById(R.id.activity_web_view_web_view);
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

    private void displayWebView(String htmlString){
        mWebView.loadDataWithBaseURL(null, htmlString, "text/html", "utf-8", null);
    }

    private class GetInvoiceAsyncTask extends AsyncTask<Void, Void, String>{

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

            if(htmlString != null){
                displayWebView(htmlString);
            }

        }
    }
}
