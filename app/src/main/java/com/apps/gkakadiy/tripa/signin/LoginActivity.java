package com.apps.gkakadiy.tripa.signin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.apps.gkakadiy.tripa.Constants;
import com.apps.gkakadiy.tripa.Injection;
import com.apps.gkakadiy.tripa.data.LoginType;
import com.apps.gkakadiy.tripa.data.ProgressDialogType;
import com.apps.gkakadiy.tripa.data.User;
import com.apps.gkakadiy.tripa.home.MainActivity;
import com.apps.gkakadiy.tripa.R;
import com.apps.gkakadiy.tripa.signup.SignupActivity;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.GoogleAuthProvider;

import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;

import java.util.Arrays;

public class LoginActivity extends AppCompatActivity implements SigninContract.SigninView , View.OnClickListener {

    private Button mSignUpButton;
    private Button mSignInButton;
    private Intent mSignUpActivityIntent;
    private Intent mSignInActivityIntent;
    private SignInPresenter mSignInPresenter;
    private TextInputLayout mEmailIdInput,mPasswordInput;
    private ProgressDialog mProgressDialog;
    private GoogleSignInClient mGoogleSignInClient;
    private CallbackManager mFBCallbackManager;
    private ImageView mGoogleSignInButton,mFbFakeSignInButton;
    private View.OnFocusChangeListener mEmailOnFocusChangelistener,mPasswordOnFocusChangeListener;
    private ProgressBar mProgressBar;
    //private LoginButton mFbSignInButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mSignInPresenter = new SignInPresenter(Injection.provideUserRepository(),this);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        mSignUpButton = findViewById(R.id.signup_button);
        mSignUpButton.setOnClickListener(this);
        mSignInButton = findViewById(R.id.signin_button);
        mSignInButton.setOnClickListener(this);
        mEmailIdInput = findViewById(R.id.signin_email);
        mProgressBar = findViewById(R.id.signin_progress_bar);
        mEmailOnFocusChangelistener = new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    Log.d("LoginActivity:","signin_email focused");
                    mEmailIdInput.setError(null);
                }else{
                    Log.d("LoginActivity:","signin_email unfocused");
                    validateEmail();
                }
            }
        };
        mEmailIdInput.getEditText().setOnFocusChangeListener(mEmailOnFocusChangelistener);
        mPasswordInput = findViewById(R.id.signin_password);
        mPasswordOnFocusChangeListener = new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    Log.d("LoginActivity:","signin_password focused");
                    mPasswordInput.setError(null);
                }else{
                    Log.d("LoginActivity:","signin_password unfocused");
                    validatePassword();
                }
            }
        };
        mPasswordInput.getEditText().setOnFocusChangeListener(mPasswordOnFocusChangeListener);
        mGoogleSignInButton = findViewById(R.id.signin_google);
        mGoogleSignInButton.setOnClickListener(this);
        //mFbSignInButton = findViewById(R.id.signin_fb);
        mFbFakeSignInButton = findViewById(R.id.signin_fb_custom);
        mFbFakeSignInButton.setOnClickListener(this);
        mProgressDialog = new ProgressDialog(this);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(Constants.GOOGLE_CLIENT_ID)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mFBCallbackManager = CallbackManager.Factory.create();
        //mFbSignInButton.setReadPermissions(Arrays.asList("email","public_profile"));
        LoginManager.getInstance().registerCallback(mFBCallbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        // App code
                        Log.d("LoginActivity", "facebook:onSuccess:" + loginResult);
                        facebookLoginPostTask(loginResult);
                    }

                    @Override
                    public void onCancel() {
                        Log.d("LoginActivity", "facebook:onCancel:");
                        // App code
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Log.d("LoginActivity", "facebook:onException:"+ exception.getMessage());
                        // App code
                    }
                });
    }

    private void facebookLoginPostTask(LoginResult loginResult) {
        Log.d("LoginActivity", "facebookLoginPostTask:" + loginResult.getAccessToken().getUserId());
        AccessToken accessToken = loginResult.getAccessToken();
        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        User user = new User();
        user.setUser_email(Profile.getCurrentProfile().getId());
        user.setUser_name(Profile.getCurrentProfile().getFirstName() + " " + Profile.getCurrentProfile().getLastName());
        user.setUser_name_lower(user.getUser_name().toLowerCase());
        user.setUser_login_type(LoginType.FACEBOOK.toString());
        LocalDate date = new LocalDate(DateTimeZone.UTC);
        user.setUser_created_at(date.toString());
        user.setUser_profile_pic_url(Profile.getCurrentProfile().getProfilePictureUri(256,256).toString());
        mSignInPresenter.signIn(credential,user);
    }

    @Override
    public void showLoadingPopup() {

    }

    @Override
    public void showSnackBar(String message, int period) {
        Snackbar snackbar = Snackbar.make(findViewById(R.id.login_activity_container),message,period);
        snackbar.setActionTextColor(Color.WHITE);
        snackbar.getView().setBackgroundColor(Color.BLACK);
        snackbar.show();
    }

    @Override
    public void signInSuccess() {
        mSignInActivityIntent = new Intent(this,MainActivity.class);
        mSignInActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mSignInActivityIntent);
    }

    @Override
    public void showIncorrectPassword() {
        mPasswordInput.setError("Incorrect Password");
    }

    @Override
    public void showIncorrectUsername() {
        mEmailIdInput.setError("User account does not exist");
    }

    @Override
    public void showInvalidEmail() {
        mEmailIdInput.setError("Email Id not correctly formatted");
    }

    @Override
    public void checkUser(User user) {
        mSignInPresenter.checkUser(user);
    }

    @Override
    public void showAccountDisabled() {
        mEmailIdInput.setError("Account Disabled");
    }

    @Override
    public void showProgressDialog(String title, String message, ProgressDialogType progressDialogType) {
        if(title!=null) {
            mProgressDialog.setTitle(title);
        }
        if(message!=null) {
            mProgressDialog.setMessage(message);
        }
        if(progressDialogType == ProgressDialogType.BOTH_BUTTON){

        }else if(progressDialogType== ProgressDialogType.NEGATIVE_BUTTON){

        }else if(progressDialogType == ProgressDialogType.POSITIVE_BUTTON){

        }
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
    }

    @Override
    public void hideProgressDialog() {
        mProgressDialog.cancel();
    }

    @Override
    public void showProgressBar() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        mProgressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void disableButtons() {
        disableSignInButton();
        disableSignUpButton();
    }

    @Override
    public void enableButtons() {
        enableSignInButton();
        enableSignUpButton();
    }

    private void disableSignInButton(){
        mSignInButton.setEnabled(false);
    }

    private void disableSignUpButton(){
        mSignUpButton.setEnabled(false);
    }

    private void enableSignInButton(){
        mSignInButton.setEnabled(true);
    }

    private void enableSignUpButton(){
        mSignUpButton.setEnabled(true);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    boolean validatePassword(){
        if (TextUtils.isEmpty(mPasswordInput.getEditText().getText().toString())){
            mPasswordInput.setError("Please Enter Password");
            return false;
        }else{
            mPasswordInput.setError(null);
            return true;
        }
    }

    boolean validateEmail(){
        if(TextUtils.isEmpty(mEmailIdInput.getEditText().getText().toString())){
            mEmailIdInput.setError("Email Id required");
            return false;
        }else{
            mEmailIdInput.setError(null);
            return true;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.signin_google : Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, Constants.RC_SIGN_IN);
                break;

            case R.id.signin_fb_custom : LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("email","public_profile"));
                //mFbSignInButton.performClick();
                break;
            case R.id.signup_button :
                mSignUpActivityIntent = new Intent(LoginActivity.this,SignupActivity.class);
                startActivity(mSignUpActivityIntent);
                break;
            case R.id.signin_button :
                if( validateEmail() && validatePassword() ){
                    mSignInPresenter.signIn(mEmailIdInput.getEditText().getText().toString(),mPasswordInput.getEditText().getText().toString());
                }
                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mFBCallbackManager.onActivityResult(requestCode, resultCode, data);
        Log.d("SignInActivity","onActivityResult "+resultCode);
        if (requestCode == Constants.RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
                Log.d("SignInActivity", "Google sign in success ");
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.d("SignInActivity", "Google sign in failed "+e.getMessage(),e);
                // ...
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        Log.d("SignInActivity", "firebaseAuthWithGoogle:" + account.getId());
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        User user = new User();
        user.setUser_email(account.getEmail());
        user.setUser_name(account.getDisplayName());
        user.setUser_name_lower(user.getUser_name().toLowerCase());
        user.setUser_login_type(LoginType.GOOGLE.toString());
        LocalDate date = new LocalDate(DateTimeZone.UTC);
        user.setUser_created_at(date.toString());
        user.setUser_profile_pic_url(account.getPhotoUrl().toString());
        mSignInPresenter.signIn(credential,user);
    }

    private void cleanUp(){
        mEmailOnFocusChangelistener = null;
        mPasswordOnFocusChangeListener = null;
        mSignInButton.setOnClickListener(null);
        mSignUpButton.setOnClickListener(null);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        mSignInPresenter.unsubscribe();
        cleanUp();
        super.onDestroy();
    }
}
