package com.itgosolutions.tutorial.google_login;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.itgosolutions.tutorial.R;

public class ActivityGoogleLogin extends AppCompatActivity implements FragmentGoogleLogin.OnGoogleLogInCompleteListener,
FragmentGoogleUserDetails.OnGoogleLogoutCompleteListener, GoogleLogInAsyncTask.OnGoogleLoginAsyncTaskListener {

    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_login);

        FragmentGoogleLogin googleLoginFragment = FragmentGoogleLogin.newInstance();
        addFragment(R.id.activity_google_login_container, googleLoginFragment, FragmentGoogleLogin.class.getName());
    }


    private void addFragment(int containerId, Fragment fragment, String TAG){
        getSupportFragmentManager()
                .beginTransaction()
                .replace(containerId, fragment, TAG)
                .disallowAddToBackStack()
                .commit();
    }

    @Override
    public void onLoginSuccess(GoogleSignInAccount account) {
        GoogleLogInAsyncTask task = new GoogleLogInAsyncTask(account.getIdToken(), this);
        task.execute();
    }

    @Override
    public void onLoginFailed(String message) {
        Toast.makeText(this, "Login Failed", Toast.LENGTH_SHORT).show();
        System.out.println("Google Login Failed");
        System.out.println(message);
    }

    @Override
    public void onLogoutSuccess() {
        FragmentGoogleLogin googleLoginFragment = FragmentGoogleLogin.newInstance();
        addFragment(R.id.activity_google_login_container, googleLoginFragment, FragmentGoogleLogin.class.getName());
    }

    @Override
    public void onLogoutFailed() {
        Toast.makeText(this, "Logout Failed", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showProgress() {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setTitle("Server verification");
        mProgressDialog.setMessage("Please wait, verifying user...");
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
    }

    @Override
    public void hideProgress() {
        if(mProgressDialog !=  null && mProgressDialog.isShowing())
            mProgressDialog.dismiss();
    }

    @Override
    public void onGoogleLoginAsyncTaskComplete(User user) {
        FragmentGoogleUserDetails fragmentGoogleUserDetails = FragmentGoogleUserDetails.newInstance(user);
        addFragment(R.id.activity_google_login_container, fragmentGoogleUserDetails, fragmentGoogleUserDetails.getClass().getName());
    }

    @Override
    public void onGoogleLoginAsyncTaskFailed(String errorMessage) {
        GoogleLoginHelper.getGoogleSignInClient(this).signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                    }
                });
    }

}
