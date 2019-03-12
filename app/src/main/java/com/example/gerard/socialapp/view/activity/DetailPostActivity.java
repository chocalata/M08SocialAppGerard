package com.example.gerard.socialapp.view.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.gerard.socialapp.GlideApp;
import com.example.gerard.socialapp.R;
import com.example.gerard.socialapp.converter.ConverterPostToJSon;
import com.example.gerard.socialapp.model.Comment;
import com.example.gerard.socialapp.model.Post;
import com.example.gerard.socialapp.view.CommentViewHolder;
import com.example.gerard.socialapp.view.fragment.PostsFragment;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DetailPostActivity extends AppCompatActivity {

    ImageView detailPostProfileImage;
    TextView detailPostProfileUserName;
    TextView detailPostText;
    ImageView detailPostAIV;

    LinearLayout detailLikeLayout;
    ImageView detailLike;
    TextView detailNumLikes;

    EditText detailComment;

    public FirebaseUser mUser;
    public DatabaseReference dbr;
//aaaa

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_post);

        final String postKey = getIntent().getStringExtra("postKey");
        final Post post = ConverterPostToJSon.stringToSomeObject(getIntent().getStringExtra("post"));

        dbr = FirebaseDatabase.getInstance().getReference();
        mUser = FirebaseAuth.getInstance().getCurrentUser();

        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<Comment>()
                .setQuery(dbr.child("posts/comments/"+postKey), Comment.class)
                .setLifecycleOwner(this)
                .build();

        detailPostProfileImage = findViewById(R.id.detailPostProfileImage);
        detailPostProfileUserName = findViewById(R.id.detailPostProfileUserName);
        detailPostText = findViewById(R.id.detailPostText);
        detailPostAIV = findViewById(R.id.detailPostAIV);

        detailLikeLayout = findViewById(R.id.detailLikeLayout);
        detailLike = findViewById(R.id.detailLike);
        detailNumLikes = findViewById(R.id.detailNumLikes);

        detailComment = findViewById(R.id.detailComment);

        GlideApp.with(this).load(post.authorPhotoUrl).circleCrop().into(detailPostProfileImage);

        detailPostProfileUserName.setText(post.author);

        detailPostText.setText(post.content);

        detailLikeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (post.likes.containsKey(mUser.getUid())) {
                    dbr.child("posts/data").child(postKey).child("likes").child(mUser.getUid()).setValue(null);
                    dbr.child("posts/user-likes").child(mUser.getUid()).child(postKey).setValue(null);
                } else {
                    dbr.child("posts/data").child(postKey).child("likes").child(mUser.getUid()).setValue(true);
                    dbr.child("posts/user-likes").child(mUser.getUid()).child(postKey).setValue(true);
                }
            }
        });

        if (post.likes.containsKey(mUser.getUid())) {
            detailLike.setImageResource(R.drawable.heart_on);
            detailNumLikes.setTextColor(getResources().getColor(R.color.red));
        } else {
            detailLike.setImageResource(R.drawable.heart_off);
            detailNumLikes.setTextColor(getResources().getColor(R.color.grey));
        }

        detailNumLikes.setText(String.valueOf(post.likes.size()));

        if (post.mediaUrl != null) {
            detailPostAIV.setVisibility(View.VISIBLE);
            if ("audio".equals(post.mediaType)) {
                detailPostAIV.setImageResource(R.drawable.audio);
            } else {
                GlideApp.with(this).load(post.mediaUrl).centerCrop().into(detailPostAIV);

            }
            detailPostAIV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), MediaActivity.class);
                    intent.putExtra("mediaUrl", post.mediaUrl);
                    intent.putExtra("mediaType", post.mediaType);
                    startActivity(intent);
                }
            });
        } else {
            detailPostAIV.setVisibility(View.GONE);
        }

        findViewById(R.id.detailSendCommnet).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Comment comment = new Comment(mUser.getDisplayName(),detailComment.getText().toString());
                String commentkey = FirebaseDatabase.getInstance().getReference().push().getKey();
                dbr.child("/posts/comments/"+postKey+"/"+commentkey).setValue(comment);
            }
        });

        RecyclerView recycler = findViewById(R.id.rvComments);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.setAdapter(new FirebaseRecyclerAdapter<Comment, CommentViewHolder>(options) {
            @Override
            public CommentViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
                LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
                return new CommentViewHolder(inflater.inflate(R.layout.item_comment, viewGroup, false));
            }

            @Override
            protected void onBindViewHolder(final CommentViewHolder viewHolder, int position, final Comment comment) {
                viewHolder.userCommentName.setText(comment.name + ": ");
                viewHolder.userCommentData.setText(comment.comment);
            }
        });



    }
}
