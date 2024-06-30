package com.example.footube;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.CommentViewHolder> {

    private List<Comment> commentList;
    private OnDeleteCommentListener deleteCommentListener;
    private OnEditCommentListener editCommentListener;

    public interface OnDeleteCommentListener {
        void onDeleteComment(int position);
    }

    public interface OnEditCommentListener {
        void onEditComment(int position, String newComment);
    }

    public CommentsAdapter(List<Comment> commentList, OnDeleteCommentListener deleteCommentListener, OnEditCommentListener editCommentListener) {
        this.commentList = commentList;
        this.deleteCommentListener = deleteCommentListener;
        this.editCommentListener = editCommentListener;
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

        holder.editCommentTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.commentTextView.setVisibility(View.GONE);
                holder.editCommentEditText.setVisibility(View.VISIBLE);
                holder.editCommentEditText.setText(comment.getComment());
                holder.editCommentButton.setVisibility(View.VISIBLE);
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
                    holder.editCommentButton.setVisibility(View.GONE);
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

        CommentViewHolder(View itemView) {
            super(itemView);
            usernameTextView = itemView.findViewById(R.id.usernameTextView);
            commentTextView = itemView.findViewById(R.id.commentTextView);
            editCommentEditText = itemView.findViewById(R.id.editCommentEditText);
            editCommentTextView = itemView.findViewById(R.id.editCommentTextView);
            deleteCommentTextView = itemView.findViewById(R.id.deleteCommentTextView);
            editCommentButton = itemView.findViewById(R.id.buttonEditComment);
        }
    }
}
