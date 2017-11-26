package com.itgosolutions.tutorial.google_login;


import android.content.Context;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

final class GoogleLoginHelper {

    static final int RC_SIGN_IN = 100;
    private static final String SERVER_CLIENT_ID = "505181810181-fkmb259gd8oqolnt181bulo831tgqgh0.apps.googleusercontent.com";

    private static GoogleSignInClient mGoogleSignInClient;

    static GoogleSignInClient getGoogleSignInClient(Context context){
        if(mGoogleSignInClient == null)
            configureGoogleSignInClient(context);
        return mGoogleSignInClient;
    }

    static void configureGoogleSignInClient(Context context){
        mGoogleSignInClient = GoogleSignIn.getClient(context, configureGoogleSignInOptions());
    }

    private static GoogleSignInOptions configureGoogleSignInOptions(){
        return new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestIdToken(SERVER_CLIENT_ID)
                .build();
    }

}
