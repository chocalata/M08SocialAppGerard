package com.example.gerard.socialapp.view;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.gerard.socialapp.R;

public class CommentViewHolder extends RecyclerView.ViewHolder {
    public TextView userCommentName;
    public TextView userCommentData;

    public CommentViewHolder(@NonNull View itemView) {
        super(itemView);

        userCommentName = itemView.findViewById(R.id.userCommentName);
        userCommentData = itemView.findViewById(R.id.userCommentData);
        
        
    }
}
