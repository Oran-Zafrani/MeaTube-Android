package com.example.footube;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.example.footube.R;

public class CustomMediaController extends MediaController {
    private VideoView videoView;
    private ImageButton playPauseButton;
    private ImageButton prevButton;
    private ImageButton nextButton;
    private SeekBar seekBar;
    private TextView timeTextView;

    public CustomMediaController(Context context) {
        super(context);
        init(context);
    }

    public CustomMediaController(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        // Inflate the custom layout
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View customView = inflater.inflate(R.layout.custom_media_controller, this, true);

        // Initialize your custom views
        prevButton = customView.findViewById(R.id.prev);
        playPauseButton = customView.findViewById(R.id.play_pause);
        nextButton = customView.findViewById(R.id.next);
        seekBar = customView.findViewById(R.id.progress);
        timeTextView = customView.findViewById(R.id.time);

        // Setup listeners for custom buttons
        playPauseButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (videoView != null) {
                    if (videoView.isPlaying()) {
                        videoView.pause();
                        playPauseButton.setImageResource(R.drawable.beef); // Change to play icon
                    } else {
                        videoView.start();
                        playPauseButton.setImageResource(R.drawable.vegan); // Change to pause icon
                    }
                }
            }
        });

        // Setup listeners for prev and next buttons if needed
        prevButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // Implement previous functionality
            }
        });

        nextButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // Implement next functionality
            }
        });

        // You can also add listeners for the seekBar and update timeTextView as needed
    }

    public void setVideoView(VideoView videoView) {
        this.videoView = videoView;

        // Setup additional videoView related functionality if needed
    }

    @Override
    public void setAnchorView(View view) {
        super.setAnchorView(view);
        // Additional customization for setting anchor view if needed
    }
}
