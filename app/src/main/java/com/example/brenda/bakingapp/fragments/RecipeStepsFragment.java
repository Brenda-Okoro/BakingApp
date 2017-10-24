package com.example.brenda.bakingapp.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.brenda.bakingapp.R;
import com.example.brenda.bakingapp.models.Steps;
import com.example.brenda.bakingapp.utils.ImageUtils;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

/**
 * Created by brenda on 10/23/17.
 */

public class RecipeStepsFragment extends Fragment {

    public static final String KEY_STEP = "step";
    Steps mStep;
    TextView mFullDescription;
    SimpleExoPlayerView mPlayerView;
    SimpleExoPlayer player;
    ImageView mNoVideoImageView;

    boolean playWhenReady = false;
    long playbackPosition = 0;
    int currentWindow = 0;

    private static final String KEY_PLAY_WHEN_READY = "play_when_ready";
    private static final String KEY_PLAYBACK_POSITION = "play_back_position";
    private static final String KEY_CURRENT_WINDOW = "current_window";

    public RecipeStepsFragment() {
        // Required empty public constructor
    }



    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            initializePlayer();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if ((Util.SDK_INT <= 23 || player == null)) {
            initializePlayer();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }


    private void releasePlayer() {
        if (player != null) {
            playbackPosition = player.getCurrentPosition();
            currentWindow = player.getCurrentWindowIndex();
            playWhenReady = player.getPlayWhenReady();
            player.release();
            player = null;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_CURRENT_WINDOW, currentWindow);
        outState.putLong(KEY_PLAYBACK_POSITION, playbackPosition);
        outState.putBoolean(KEY_PLAY_WHEN_READY, playWhenReady);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_recipe_steps, container, false);
        mFullDescription = (TextView) rootView.findViewById(R.id.tv_full_description);
        mPlayerView = (SimpleExoPlayerView) rootView.findViewById(R.id.video_view);
        mNoVideoImageView = (ImageView) rootView.findViewById(R.id.iv_no_video);
        bindView();
        return  rootView;
    }

    private void initializePlayer() {
        player = ExoPlayerFactory.newSimpleInstance(
                new DefaultRenderersFactory(getActivity()),
                new DefaultTrackSelector(), new DefaultLoadControl());

        mPlayerView.setPlayer(player);
        player.setPlayWhenReady(playWhenReady);
        player.seekTo(currentWindow, playbackPosition);

        //set the video URL
        if (mStep != null) {
            if (mStep.hasVideo()) {
                mNoVideoImageView.setVisibility(View.GONE);
                mPlayerView.setVisibility(View.VISIBLE);
                Uri uri = Uri.parse(mStep.getVideoURL());
                MediaSource mediaSource = buildMediaSource(uri);
                player.prepare(mediaSource, true, false);

            } else if(mStep.hasThumbnail()){
                mPlayerView.setVisibility(View.GONE);
                mNoVideoImageView.setVisibility(View.VISIBLE);
                ImageUtils.loadImageFromRemoteServerIntoImageView(getActivity().getApplicationContext(),
                        mStep.getThumbnailURL(), mNoVideoImageView);
            }
            else {
                mPlayerView.setVisibility(View.GONE);
                mNoVideoImageView.setVisibility(View.VISIBLE);
                ImageUtils.loadImageFromResourcesToImageView(getActivity().getApplicationContext(),
                        R.drawable.no_video
                        , mNoVideoImageView);

            }
        }
    }

    private MediaSource buildMediaSource(Uri uri) {
        return new ExtractorMediaSource(uri,
                new DefaultHttpDataSourceFactory("ua"),
                new DefaultExtractorsFactory(), null, null);
    }



    private void bindView() {
        if (mStep != null && mFullDescription != null) {
            mFullDescription.setText(mStep.getDescription());
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        if(arguments.containsKey(KEY_STEP)){
            mStep = arguments.getParcelable(KEY_STEP);
        }

        if (savedInstanceState != null && savedInstanceState.containsKey(KEY_PLAY_WHEN_READY)) {
            playbackPosition = savedInstanceState.getLong(KEY_PLAYBACK_POSITION);
            playWhenReady = savedInstanceState.getBoolean(KEY_PLAY_WHEN_READY);
            currentWindow = savedInstanceState.getInt(KEY_CURRENT_WINDOW);
        }


    }

    public static RecipeStepsFragment getNewInstance(Steps step){
        Bundle argument = new Bundle();
        argument.putParcelable(KEY_STEP, step);

        RecipeStepsFragment fragment = new RecipeStepsFragment();
        fragment.setArguments(argument);

        return  fragment;
    }


}

