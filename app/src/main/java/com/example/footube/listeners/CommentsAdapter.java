package com.example.footube.listeners;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.footube.BasicClasses.Comment;
import com.example.footube.R;
import com.example.footube.activities.MoviesList;
import com.example.footube.activities.VideoPlayerActivity;
import com.example.footube.managers.UserManager;

import java.util.List;
import java.util.Objects;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.CommentViewHolder> {

    private List<Comment> commentList;
    private OnDeleteCommentListener deleteCommentListener;
    private OnEditCommentListener editCommentListener;
    private Context context;
    private String LoggedInUser;

    public interface OnDeleteCommentListener {
        void onDeleteComment(int position);
    }

    public interface OnEditCommentListener {
        void onEditComment(int position, String newComment);
    }

    public CommentsAdapter(String LoggedInUser, Context context, List<Comment> commentList, OnDeleteCommentListener deleteCommentListener, OnEditCommentListener editCommentListener) {
        this.context = context;
        this.commentList = commentList;
        this.deleteCommentListener = deleteCommentListener;
        this.editCommentListener = editCommentListener;
        this.LoggedInUser = LoggedInUser;
    }

    @Override
    public CommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_comment, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CommentViewHolder holder, int position) {
        Comment comment = commentList.get(position);
        holder.usernameTextView.setText(comment.getUsername());
        holder.commentTextView.setText(comment.getComment());

        String image;
//        if (UserManager.getInstance().getUser(comment.getUsername()).getImage() == null || Objects.equals(UserManager.getInstance().getUser(comment.getUsername()).getImage(), "")){
//            holder.CommentUser.setImageResource(R.drawable.signin_man);
//        }else {
        image = comment.getUserImage();
        holder.CommentUser.setImageBitmap(MoviesList.base64ToBitmap(image));
//        }

        //display and gone the edit and delete button
//        Log.d("LoggedInUser12", this.LoggedInUser + "," + comment.getUsername()+ ","+Objects.equals(comment.getUsername(), this.LoggedInUser));
        if (!Objects.equals(comment.getUsername(), this.LoggedInUser)){
            holder.editCommentTextView.setVisibility(View.GONE);
            holder.deleteCommentTextView.setVisibility(View.GONE);
        }else {
            holder.editCommentTextView.setVisibility(View.VISIBLE);
            holder.deleteCommentTextView.setVisibility(View.VISIBLE);
        }
//        holder.bindData(position);

        holder.editCommentTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.commentTextView.setVisibility(View.GONE);
                holder.editCommentEditText.setVisibility(View.VISIBLE);
                holder.editCommentEditText.setText(comment.getComment());
                holder.editCommentButton.setVisibility(View.VISIBLE);
                if (context instanceof VideoPlayerActivity) {
                    ((VideoPlayerActivity) context).hideineditcomment();
                }

                //not work yet
                if (context instanceof VideoPlayerActivity) {
                    ((VideoPlayerActivity) context).openKeyboard(v);
                }
                holder.editCommentEditText.requestFocus();
            }
        });

        holder.editCommentEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (editCommentListener != null) {
                int position1 = holder.getAdapterPosition();
                if (position1 != RecyclerView.NO_POSITION) {
                    String newComment = holder.editCommentEditText.getText().toString();
                    editCommentListener.onEditComment(position1, newComment);
                    holder.commentTextView.setText(newComment);
                    holder.commentTextView.setVisibility(View.VISIBLE);
                    holder.editCommentEditText.setVisibility(View.GONE);
                    holder.editCommentButton.setVisibility(View.VISIBLE);
                }
            }
            return true;
        });

        holder.editCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editCommentListener != null && !holder.editCommentEditText.getText().toString().isEmpty()) {
                    int position1 = holder.getAdapterPosition();
                    if (position1 != RecyclerView.NO_POSITION) {
                        String newComment = holder.editCommentEditText.getText().toString();
                        editCommentListener.onEditComment(position1, newComment);
                        holder.commentTextView.setText(newComment);
                        holder.commentTextView.setVisibility(View.VISIBLE);
                        holder.editCommentEditText.setVisibility(View.GONE);
                        holder.editCommentButton.setVisibility(View.GONE);
                        if (context instanceof VideoPlayerActivity) {
                            ((VideoPlayerActivity) context).visibleineditcomment();
                        }
//                        holder.editmovie.setVisibility(View.GONE);
                    }
                }else {
                    int position1 = holder.getAdapterPosition();
                    if (position1 != RecyclerView.NO_POSITION) {
                        String newComment = comment.getComment();
                        editCommentListener.onEditComment(position1, newComment);
                        holder.commentTextView.setText(newComment);
                        holder.commentTextView.setVisibility(View.VISIBLE);
                        holder.editCommentEditText.setVisibility(View.GONE);
                        holder.editCommentButton.setVisibility(View.GONE);
                    }
                }
            }
        });

        holder.deleteCommentTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (deleteCommentListener != null) {
                    int position = holder.getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        deleteCommentListener.onDeleteComment(position);
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }


    class CommentViewHolder extends RecyclerView.ViewHolder {
        TextView usernameTextView;
        TextView commentTextView;
        EditText editCommentEditText;
        Button editCommentButton;
        TextView editCommentTextView;
        TextView deleteCommentTextView;
        ImageButton CommentUser;
//        Button editmovie;

        CommentViewHolder(View itemView) {
            super(itemView);
            usernameTextView = itemView.findViewById(R.id.usernameTextView);
            commentTextView = itemView.findViewById(R.id.commentTextView);
            editCommentEditText = itemView.findViewById(R.id.editCommentEditText);
            editCommentTextView = itemView.findViewById(R.id.editCommentTextView);
            deleteCommentTextView = itemView.findViewById(R.id.deleteCommentTextView);
            editCommentButton = itemView.findViewById(R.id.buttonEditComment);
            CommentUser = itemView.findViewById(R.id.CommentUser);

//            if (!iscreator){
//                editCommentTextView.setVisibility(View.GONE);
//                deleteCommentTextView.setVisibility(View.GONE);
//            }
        }

//        public void bindData(int position) {
//            if (!iscreator[position]) {
//                editCommentTextView.setVisibility(View.GONE);
//                deleteCommentTextView.setVisibility(View.GONE);
//            } else {
//                editCommentTextView.setVisibility(View.VISIBLE);
//                deleteCommentTextView.setVisibility(View.VISIBLE);
//            }
//        }
    }
}
