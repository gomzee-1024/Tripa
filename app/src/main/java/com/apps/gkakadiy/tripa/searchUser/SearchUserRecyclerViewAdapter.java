package com.apps.gkakadiy.tripa.searchUser;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.apps.gkakadiy.tripa.R;
import com.apps.gkakadiy.tripa.data.UserPublic;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class SearchUserRecyclerViewAdapter extends RecyclerView.Adapter<SearchUserRecyclerViewAdapter.SearchUserViewHolder> {

    private ArrayList<UserPublic> mSearchUserList;
    private Context mContext;

    public SearchUserRecyclerViewAdapter(Context context , ArrayList<UserPublic> searchUserList){
        mSearchUserList = searchUserList;
        mContext = context;
    }

    @NonNull
    @Override
    public SearchUserRecyclerViewAdapter.SearchUserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_search_users,parent,false);
        SearchUserViewHolder viewHolder = new SearchUserViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SearchUserRecyclerViewAdapter.SearchUserViewHolder holder, int position) {
        UserPublic user = mSearchUserList.get(position);
        if(user.getUser_profile_pic_url()!=null){
            Glide.with(mContext).load(user.getUser_profile_pic_url()).into(holder.mProfileImageView);
        }else{
            holder.mProfileImageView.setImageResource(R.drawable.avtaar_icon);
        }
        if(user.getUser_name()!=null ){
            holder.mUserDisplayName.setText(user.getUser_name());
        }else{
            holder.mUserDisplayName.setText("User Name");
        }
        if(user.getUser_about()!=null){
            holder.mUserHandle.setText(user.getUser_about());
        }else{
            holder.mUserHandle.setText("userHandle");
        }
    }

    @Override
    public int getItemCount() {
        return mSearchUserList.size();
    }

    public void addUser(UserPublic user){
        mSearchUserList.add(user);
        notifyItemInserted(mSearchUserList.size()-1);
        //notifyDataSetChanged();
    }

    public void clearUserList(){
        mSearchUserList.clear();
        notifyDataSetChanged();
    }

    public void cleanup(){
        mContext = null;
    }

    public class SearchUserViewHolder extends RecyclerView.ViewHolder {

        private CircleImageView mProfileImageView;
        private TextView mUserDisplayName;
        private TextView mUserHandle;

        public SearchUserViewHolder(View itemView) {
            super(itemView);
            mProfileImageView = itemView.findViewById(R.id.search_user_profile_image);
            mUserDisplayName = itemView.findViewById(R.id.search_user_display_name);
            mUserHandle =itemView.findViewById(R.id.search_user_handle);
        }
    }
}
