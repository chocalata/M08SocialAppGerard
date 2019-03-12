package com.example.gerard.socialapp.view;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.example.gerard.socialapp.R;

public class ProfileViewHolder extends RecyclerView.ViewHolder {
    public ImageView profileImage;
    public ImageView profileImageBackground;

    public ProfileViewHolder(@NonNull View itemView) {
        super(itemView);

        profileImage = itemView.findViewById(R.id.profileImage);
        profileImageBackground = itemView.findViewById(R.id.profileImageBackground);
    }
}
