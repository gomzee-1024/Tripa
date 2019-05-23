package com.apps.gkakadiy.tripa.signup;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.apps.gkakadiy.tripa.Constants;
import com.apps.gkakadiy.tripa.Injection;
import com.apps.gkakadiy.tripa.data.LoginType;
import com.apps.gkakadiy.tripa.data.ProgressDialogType;
import com.apps.gkakadiy.tripa.R;
import com.apps.gkakadiy.tripa.data.User;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;

public class SignupActivity extends AppCompatActivity implements SignupContract.SignupView {

    private SignUpPresenter mSignUpPresenter;
    private Button mSignUpButton;
    private TextInputLayout mUserNameInput,mEmailInput,mPasswordInput,mConfirmPasswordInput;
    private ProgressDialog mProgressDialog;
    private View.OnFocusChangeListener mUserNameOnFocusChangeListener,mEmailOnFocusChangeListener,mPasswordOnFocusChangeListener,mConfirmPasswordOnFocusChangeListener;
    private ProgressBar mProgressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        mSignUpButton = findViewById(R.id.signupactivity_button);
        mProgressBar = findViewById(R.id.signup_progress_bar);
        mUserNameInput = findViewById(R.id.signup_user_name);
        mUserNameOnFocusChangeListener = new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    mUserNameInput.setError(null);
                }else{
                    validateName();
                }
            }
        };
        mUserNameInput.getEditText().setOnFocusChangeListener(mUserNameOnFocusChangeListener);
        mEmailInput = findViewById(R.id.signup_email);
        mEmailOnFocusChangeListener = new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    mEmailInput.setError(null);
                }else{
                    validateEmail();
                }
            }
        };
        mEmailInput.getEditText().setOnFocusChangeListener(mEmailOnFocusChangeListener);
        mPasswordInput = findViewById(R.id.signup_password);
        mPasswordOnFocusChangeListener = new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    mPasswordInput.setError(null);
                }else{
                    validatePassword();
                }
            }
        };
        mPasswordInput.getEditText().setOnFocusChangeListener(mPasswordOnFocusChangeListener);
        mConfirmPasswordInput = findViewById(R.id.signup_confirm_password);
        mConfirmPasswordOnFocusChangeListener = new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    mConfirmPasswordInput.setError(null);
                }else{
                    validateConfirmPassword();
                }
            }
        };
        mConfirmPasswordInput.getEditText().setOnFocusChangeListener(mConfirmPasswordOnFocusChangeListener);
        mProgressDialog = new ProgressDialog(this);
        mSignUpPresenter  = SignUpPresenter.getSignUpPresenterInstance(Injection.provideUserRepository(),this,null);
        mSignUpButton.setOnClickListener(v -> {
            if(validateName() && validateEmail() && validatePassword() && validateConfirmPassword() ) {
                if (TextUtils.equals(mPasswordInput.getEditText().getText(), mConfirmPasswordInput.getEditText().getText())) {
                    User user = new User();
                    user.setUser_name(mUserNameInput.getEditText().getText().toString());
                    user.setUser_name_lower(mUserNameInput.getEditText().getText().toString().toLowerCase());
                    user.setUser_email(mEmailInput.getEditText().getText().toString());
                    user.setUser_login_type(LoginType.EMAIL.toString());
                    LocalDate date = new LocalDate(DateTimeZone.UTC);
                    user.setUser_created_at(date.toString());
                    user.setUser_profile_pic_url(Constants.DEFAULT_AVTAARICON_URL);
                    mSignUpPresenter.signUp(user, mPasswordInput.getEditText().getText().toString());
                } else {
                    mPasswordInput.setError("Passwords do not match");
                    mConfirmPasswordInput.setError("Passwords do not match");
                }
            }
        });
    }

    private boolean validateName(){
        if(TextUtils.isEmpty(mUserNameInput.getEditText().getText())){
            mUserNameInput.setError("Name Cannot be empty");
            return false;
        }
        return true;
    }


    private boolean validateEmail() {
        if(TextUtils.isEmpty(mEmailInput.getEditText().getText())){
            mEmailInput.setError("Email Cannot be empty");
            return false;
        }
        return true;
    }

    private boolean validatePassword(){
        if(TextUtils.isEmpty(mPasswordInput.getEditText().getText())){
            mPasswordInput.setError("Password Cannot be empty");
            return false;
        }
        return true;
    }

    private boolean validateConfirmPassword(){
        if(TextUtils.isEmpty(mConfirmPasswordInput.getEditText().getText())){
            mConfirmPasswordInput.setError("Confirm password Cannot be empty");
            return false;
        }
        return true;
    }

    @Override
    public void showLoadingPopup() {

    }

    @Override
    public void showSnackBar(String message, int period) {
        Snackbar snackbar = Snackbar.make(findViewById(R.id.signup_activity_container),message,period);
        snackbar.setActionTextColor(Color.WHITE);
        snackbar.getView().setBackgroundColor(Color.BLACK);
        snackbar.show();
    }

    @Override
    public void signUpSuccess() {
        mSignUpPresenter.sendEmailVerification();
        Intent intent = new Intent(this,EmailVerificationActivity.class);
        startActivity(intent);
    }

    @Override
    public void showWeakPassword() {
        mPasswordInput.setError("Password should be at least 6 characters");
    }

    @Override
    public void showInvalidEmail() {
        mEmailInput.setError("Email Id not incorrectly formatted");
    }

    @Override
    public void showIncorrectEmail() {
        mEmailInput.setError("Account doesn't exists");
    }

    @Override
    public void showUserAlreadyExist() {
        //hideProgressDialog();
        mEmailInput.setError("Account already exists");
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
    public void hideProgessBar() {
        mProgressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void resendVerificationEmail() {

    }

    @Override
    public void verifyEmailSuccess() {

    }

    @Override
    public void showEmailResent() {

    }

    @Override
    public void showEmailResendFailed() {

    }

    @Override
    public void currentUserReloadSuccess() {

    }

    @Override
    public void disableButtons() {
        disableSignUpButton();
    }

    @Override
    public void enableButtons() {
        enableSignUpButton();
    }

    private void disableSignUpButton(){
        mSignUpButton.setEnabled(false);
    }

    private void enableSignUpButton() {
        mSignUpButton.setEnabled(true);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void cleanUp(){
        mUserNameOnFocusChangeListener = null;
        mEmailOnFocusChangeListener = null;
        mPasswordOnFocusChangeListener = null;
        mConfirmPasswordOnFocusChangeListener = null;
        mSignUpButton.setOnClickListener(null);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        mSignUpPresenter.unsubscribeSignUp();
        cleanUp();
        super.onDestroy();
    }
}
