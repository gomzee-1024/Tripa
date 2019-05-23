package com.apps.gkakadiy.tripa.notifications;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.apps.gkakadiy.tripa.R;
import com.apps.gkakadiy.tripa.data.Notification;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class NotificationsRecyclerAdpater extends RecyclerView.Adapter<NotificationsRecyclerAdpater.NotificationsViewHolder> implements NotficationsContract.NotificationsBaseView {

    private ArrayList<Notification> mNotificationsList;
    private Context mContext;
    private NotificationsPresenter mNotificationsPresenter;

    public NotificationsRecyclerAdpater(Context context,ArrayList<Notification> notificationsList){
        mContext = context;
        mNotificationsList = notificationsList;
    }

    public void setmNotificationsPresenter(NotificationsPresenter notificationsPresenter){
        mNotificationsPresenter = notificationsPresenter;
    }

    @NonNull
    @Override
    public NotificationsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d("NotificationsAdapter:","on CreateViewHolder");
        View v = LayoutInflater.from(mContext).inflate(R.layout.notification_item,parent,false);
        return new NotificationsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationsViewHolder holder, int position) {
        Log.d("NotificationsAdapter:","on Bind");
        if(holder.mHolderStateDirty==false) {
            Log.d("NotificationsAdapter:","holder dirty");
            Notification notification = mNotificationsList.get(position);
            holder.mImageView.setVisibility(View.GONE);
            holder.mAcceptButton.setVisibility(View.GONE);
            holder.mRejectButton.setVisibility(View.GONE);
            Log.d("NotifyRecyclerAdapter:",notification.toString());
            switch (notification.getNotification_context()) {
                case "FRIEND_REQUEST_STATUS" :
                    Log.d("NotificationsAdapter:","Friend request status");
                    loadFriendRequesStatustNotification(holder, position);
                    break;
                case "FRIEND_REQUEST":
                    Log.d("NotificationsAdapter:","Friend request");
                    loadFriendRequestNotification(holder, position);
                    break;
                case "TRIP_REQUEST" :
                    break;
                case "LIKE":
                    break;
                case "COMMENT":
                    break;

            }
        }
    }

    private void loadFriendRequesStatustNotification(NotificationsViewHolder holder, int position){
        Notification notification = mNotificationsList.get(position);
        Glide.with(mContext).load(notification.getText1()).into(holder.mProfileImageView);
        holder.hideAcceptButton();
        holder.hideRejectButton();
        String notificationText = notification.getText2();
        holder.mNotificationText.setText(notificationText);
    }

    private void loadFriendRequestNotification(NotificationsViewHolder holder, int position) {
        Notification notification = mNotificationsList.get(position);
        Glide.with(mContext).load(notification.getText1()).into(holder.mProfileImageView);
        holder.showAcceptButton();
        holder.showRejectButton();
        String notificationText = notification.getText2();
        holder.mNotificationText.setText(Html.fromHtml(notificationText));
    }

    @Override
    public void onViewRecycled(@NonNull NotificationsViewHolder holder) {
        super.onViewRecycled(holder);
        holder.cleanUp();
    }

    @Override
    public int getItemCount() {
        return mNotificationsList.size();
    }

    @Override
    public void addNotification(Notification notification) {
        Log.d("NotificationsAdapter:","Notification Inserted");
        mNotificationsList.add(notification);
        //notifyDataSetChanged();
        notifyItemInserted(mNotificationsList.size()-1);
    }

    @Override
    public void clearNotifications() {
        mNotificationsList.clear();
        notifyDataSetChanged();
    }

    public class NotificationsViewHolder extends RecyclerView.ViewHolder{
        private CircleImageView mProfileImageView,mProfileImageView1,mProfileImageView2;
        private TextView mNotificationText;
        private Button mAcceptButton,mRejectButton;
        private ImageView mImageView;
        private View.OnClickListener mItemClickListener,mAcceptClickistner,mRejectClickListener;
        private boolean mHolderStateDirty;
        public NotificationsViewHolder(View itemView) {
            super(itemView);
            Log.d("NotificationsHolder:","Holder created");
            mProfileImageView = itemView.findViewById(R.id.notifications_profile_image_large);
            mProfileImageView1 = itemView.findViewById(R.id.notifications_profile_image_small_1);
            mProfileImageView2 =  itemView.findViewById(R.id.notifications_profile_image_small_2);
            mNotificationText = itemView.findViewById(R.id.notifications_textview);
            mImageView = itemView.findViewById(R.id.notifications_image_view);
            mAcceptButton = itemView.findViewById(R.id.notifications_accept_button);
            mRejectButton = itemView.findViewById(R.id.notifications_reject_button);
            mItemClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            };
            setmHolderStateDirty(false);
            mAcceptClickistner = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    Notification notification = mNotificationsList.get(position);
                    mNotificationsPresenter.acceptFriendRequest(notification,position);
                    //mNotificationsPresenter.deleteNotification(notification.getNotification_id());
                    hideRejectButton();
                    setAcceptButtonText("Accepted");
                    disableAcceptButton();
                    setmHolderStateDirty(true);
                }
            };
            mRejectClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    Notification notification = mNotificationsList.get(position);
                    mNotificationsPresenter.rejectFriendRequest(notification,position);
                    //mNotificationsPresenter.deleteNotification(notification.getNotification_id());
                    hideAcceptButton();
                    setRejectButtonText("Rejected");
                    disableRejectButton();
                    setmHolderStateDirty(true);
                }
            };
            itemView.setOnClickListener(mItemClickListener);
            mAcceptButton.setOnClickListener(mAcceptClickistner);
            mRejectButton.setOnClickListener(mRejectClickListener);
        }

        private void setmHolderStateDirty(boolean dirty){
            mHolderStateDirty = dirty;
        }

        private void hideRejectButton(){
            mRejectButton.setVisibility(View.GONE);
        }

        private void hideAcceptButton(){
            mAcceptButton.setVisibility(View.GONE);
        }

        private void showRejectButton(){
            mRejectButton.setVisibility(View.VISIBLE);
        }

        private void showAcceptButton(){
            mAcceptButton.setVisibility(View.VISIBLE);
        }

        private void setAcceptButtonText(String text) {
            mAcceptButton.setText(text);
        }

        private void setRejectButtonText(String text) {
            mRejectButton.setText(text);
        }

        private void disableRejectButton(){
            mRejectButton.setEnabled(false);
        }

        private void disableAcceptButton(){
            mAcceptButton.setEnabled(false);
        }

        public void cleanUp(){
            mItemClickListener = null;
            mAcceptClickistner = null;
            mRejectClickListener = null;
        }
    }

    public void loadAllNotififcations(){
        mNotificationsPresenter.loadAllNotifications();
    }

    public void cleanUp(){
        mContext = null;
        mNotificationsPresenter.unsubscribe();
    }

}
