package com.apps.gkakadiy.tripa.launcher;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.apps.gkakadiy.tripa.Injection;
import com.apps.gkakadiy.tripa.signin.LoginActivity;
import com.apps.gkakadiy.tripa.home.MainActivity;
import com.apps.gkakadiy.tripa.R;
import com.apps.gkakadiy.tripa.signup.EmailVerificationActivity;
import com.google.firebase.auth.FirebaseUser;

public class Launcher extends AppCompatActivity implements LauncherContract.LauncherBaseView {

    private LauncherPresenter mLauncherPresenter;
    private ProgressBar mProgressbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        mProgressbar = findViewById(R.id.launcher_progress_bar);
        mLauncherPresenter = new LauncherPresenter(Injection.provideUserRepository(),this);
        mLauncherPresenter.checkLogin();
    }

    @Override
    public void loginSuccess() {
        FirebaseUser user = mLauncherPresenter.getCurrentFirebaseUser();
        if(!mLauncherPresenter.getCurrentUser().getUser_login_type().equalsIgnoreCase("EMAIL") || user.isEmailVerified()) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }else{
            Intent intent = new Intent(this, EmailVerificationActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }

    @Override
    public void notLoggedIn() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public void loadCurrentUser() {
        mLauncherPresenter.loadCurrentUser();
    }

    @Override
    public void showProgressbar() {
        mProgressbar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressbar() {
        mProgressbar.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}
