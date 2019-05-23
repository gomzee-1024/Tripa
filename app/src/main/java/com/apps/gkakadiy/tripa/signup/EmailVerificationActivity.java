package com.apps.gkakadiy.tripa.signup;

import android.app.ProgressDialog;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.apps.gkakadiy.tripa.Constants;
import com.apps.gkakadiy.tripa.Injection;
import com.apps.gkakadiy.tripa.R;
import com.apps.gkakadiy.tripa.data.ProgressDialogType;
import com.apps.gkakadiy.tripa.home.MainActivity;
import com.google.android.material.textfield.TextInputLayout;

public class EmailVerificationActivity extends AppCompatActivity implements SignupContract.SignupView ,View.OnClickListener{

    private TextView mEmailVericationMessageText;
    private Button mResendEmailButton,mSignInButton;
    private TextInputLayout mUpdateEmailTextView;
    private SignUpPresenter mSignUpPresenter;
    private ProgressDialog mProgressDialog;
    private ProgressBar mProgressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_verification);
        mEmailVericationMessageText = findViewById(R.id.emailverify_message_text);
        mResendEmailButton = findViewById(R.id.emailverify_resend_button);
        mSignInButton = findViewById(R.id.emailverify_signin_button);
        mUpdateEmailTextView = findViewById(R.id.emailverify_email);
        mProgressBar = findViewById(R.id.emailverify_progress_bar);
        mSignUpPresenter = SignUpPresenter.getSignUpPresenterInstance(Injection.provideUserRepository(),null,this);
        mSignUpPresenter.setmEmailVerifyView(this);
        mResendEmailButton.setOnClickListener(this);
        mSignInButton.setOnClickListener(this);
        mProgressDialog = new ProgressDialog(this);
    }

    private boolean validateEmail(){
        if(TextUtils.isEmpty(mUpdateEmailTextView.getEditText().getText())){
            mUpdateEmailTextView.setError("Email cannot be Empty!");
            return false;
        }else{
            return true;
        }
    }

    @Override
    public void showLoadingPopup() {

    }

    @Override
    public void showSnackBar(String message, int period) {

    }

    @Override
    public void signUpSuccess() {

    }

    @Override
    public void showWeakPassword() {

    }

    @Override
    public void showInvalidEmail() {

    }

    @Override
    public void showIncorrectEmail() {

    }

    @Override
    public void showUserAlreadyExist() {

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
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void resendVerificationEmail() {

    }

    @Override
    public void verifyEmailSuccess() {

    }

    @Override
    public void showEmailResent() {
        setVerifyEmailMessage(Constants.VERIFY_EMAIL_RESENT);
    }

    @Override
    public void showEmailResendFailed() {
        setVerifyEmailMessage(Constants.VERIFY_EMAIL_FAILED);
        if(mUpdateEmailTextView.getVisibility()==View.GONE){
            mUpdateEmailTextView.setVisibility(View.VISIBLE);
            mUpdateEmailTextView.getEditText().setText(mSignUpPresenter.getCurrentFirebaseUser().getEmail());
            mEmailVericationMessageText.setText(Constants.VERIFY_EMAIL_FAILED);
        }else{
            mEmailVericationMessageText.setText(Constants.VERIFY_EMAIL_FAILED);
        }
    }

    @Override
    public void currentUserReloadSuccess() {
        if(mSignUpPresenter.getCurrentFirebaseUser().isEmailVerified()) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }else{
            mEmailVericationMessageText.setText(Constants.VERIFY_EMAIL);
        }
    }

    @Override
    public void disableButtons() {
        disableResendButton();
        disableSignInButton();
    }

    @Override
    public void enableButtons() {
        enableResendButton();
        enableSignInButton();
    }

    private void disableResendButton(){
        mResendEmailButton.setEnabled(false);
    }

    private void enableResendButton(){
        mResendEmailButton.setEnabled(true);
    }

    private void disableSignInButton(){
        mSignInButton.setEnabled(false);
    }

    private void enableSignInButton(){
        mSignInButton.setEnabled(true);
    }

    private void setVerifyEmailMessage(String message){
        mEmailVericationMessageText.setText(message);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.emailverify_resend_button : resendEmail();
                break;
            case R.id.emailverify_signin_button : signIn();
                break;
        }
    }

    private void signIn() {
        mSignUpPresenter.currentUserReload();
    }

    private void resendEmail() {
        if(mUpdateEmailTextView.getVisibility()==View.GONE){
            mSignUpPresenter.sendEmailVerification();
        }else{
            if(validateEmail()){
                mSignUpPresenter.sendUpdateEmailVerification(mUpdateEmailTextView.getEditText().getText().toString());
            }
        }
    }

    @Override
    protected void onDestroy() {
        mSignUpPresenter.unsubscribe();
        super.onDestroy();
    }
}
