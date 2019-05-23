package com.apps.gkakadiy.tripa.userprofile;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.apps.gkakadiy.tripa.Constants;
import com.apps.gkakadiy.tripa.Injection;
import com.apps.gkakadiy.tripa.R;
import com.apps.gkakadiy.tripa.data.FriendStatusType;
import com.bumptech.glide.Glide;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserProfileActivity extends AppCompatActivity implements UserProfileContract.UserProfileBaseView{

    private ImageView mFriendRequestButton;
    private TextView mFriendsCount,mUserDisplayName,mFriendStatusText;
    private View.OnClickListener mFriendRequestIconClickListener;
    private FriendStatusType mFriendStatus;
    private CircleImageView mProfileImage;
    private UserProfilePresenter mProfilePresenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        Log.d("UserProfileActivity","onCreate");
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        Intent intent = getIntent();
        String userId = intent.getStringExtra(Constants.DATA_USER_ID);
        mFriendRequestButton = findViewById(R.id.user_profile_request);
        mFriendsCount = findViewById(R.id.user_profile_friends_count);
        mProfileImage = findViewById(R.id.user_profile_image);
        mUserDisplayName = findViewById(R.id.user_profile_display_name);
        mFriendStatusText = findViewById(R.id.user_profile_friend_status_text);
        mFriendRequestIconClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (mFriendStatus){
                    case NOT_FRIENDS:
                        mProfilePresenter.sendFriendRequest();
                        break;
                    case FRIENDS:
                        break;
                    case REQUESTED:
                        break;
                }
            }
        };
        mFriendRequestButton.setOnClickListener(mFriendRequestIconClickListener);
        mProfilePresenter = new UserProfilePresenter(Injection.provideUserRepository(),Injection.provideUserFriendsRepository(),Injection.provideUserNotificationsRepository(),this);
        mProfilePresenter.loadUser(userId);
    }

    @Override
    public void setCoverImage(String url) {

    }

    @Override
    public void setProfileImage(String url) {
        Glide.with(this).load(url).into(mProfileImage);
    }

    @Override
    public void setFriendRequestIcon(int resId) {
        mFriendRequestButton.setImageResource(resId);
    }

    @Override
    public void setFriendRequestText(String text) {
        mFriendStatusText.setText(text);
    }

    @Override
    public void setAboutText(String about) {

    }

    @Override
    public void showAboutText() {

    }

    @Override
    public void hideAboutText() {

    }

    @Override
    public void setDisplayName(String display_name) {
        mUserDisplayName.setText(display_name);
    }

    @Override
    public void setTripCount(String count) {

    }

    @Override
    public void setFriendCount(String count) {
        mFriendsCount.setText(count);
    }

    @Override
    public void setFriendStatus(FriendStatusType status) {
        mFriendStatus = status;
    }

    @Override
    public FriendStatusType getFriendStatusType() {
        return mFriendStatus;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("UserProfileActivity:","onDestroy");
        mProfilePresenter.unsubscribe();
        mFriendRequestIconClickListener = null;
    }
}
