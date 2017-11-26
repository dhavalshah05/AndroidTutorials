package com.itgosolutions.tutorial.google_login;

import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


class GoogleLogInAsyncTask extends AsyncTask<Void, Void, String> {

    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static final String mURL = "https://google-signin.herokuapp.com/tokensignin";
    //private static final String mURL = "http://192.168.0.103:7777/tokensignin";

    private final OnGoogleLoginAsyncTaskListener mListener;
    private final String mIdToken;
    private Gson gson;

    GoogleLogInAsyncTask(String idToken, OnGoogleLoginAsyncTaskListener listener) {
        mIdToken = idToken;
        mListener = listener;
        gson = new GsonBuilder().create();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if(mListener != null)
            mListener.showProgress();
    }

    @Override
    protected String doInBackground(Void... voids) {

        OkHttpClient client = new OkHttpClient();

        Map<String, String> map = new HashMap<>();
        map.put("name", "Dhaval");

        RequestBody formBody = RequestBody.create(JSON, gson.toJson(map));
        Request request = new Request.Builder()
                .header("idToken", mIdToken)
                .url(mURL)
                .post(formBody)
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
    protected void onPostExecute(String response) {
        super.onPostExecute(response);

        if(response != null && !response.isEmpty()){

            JsonObject obj = gson.fromJson(response, JsonObject.class);
            // parse json data
            boolean success = obj.get("success").getAsBoolean();

            if(success){
                User user = new User();
                user.setEmail(obj.get("email").getAsString());
                user.setName(obj.get("name").getAsString());
                user.setGoogleUserId(obj.get("googleUserId").getAsString());
                user.setPictureURL(obj.get("picture").getAsString());

                if(mListener != null)
                    mListener.onGoogleLoginAsyncTaskComplete(user);
            }else {
                if(mListener != null)
                    mListener.onGoogleLoginAsyncTaskFailed("Server authentication failed!");
            }

        }else{
            if(mListener != null)
                mListener.onGoogleLoginAsyncTaskFailed("Error while authentication");
        }
        if(mListener != null)
            mListener.hideProgress();
    }

    interface OnGoogleLoginAsyncTaskListener{
        void showProgress();
        void hideProgress();
        void onGoogleLoginAsyncTaskComplete(User user);
        void onGoogleLoginAsyncTaskFailed(String errorMessage);
    }
}
