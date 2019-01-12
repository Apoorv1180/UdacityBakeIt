package com.example.apoorvdubey.bakeit.view.fragment;


import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.apoorvdubey.bakeit.R;
import com.example.apoorvdubey.bakeit.service.model.Step;
import com.example.apoorvdubey.bakeit.view.activity.StepsDetailActivity;
import com.example.apoorvdubey.bakeit.view.callbacks.ListenFromActivity;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static android.view.View.GONE;

/**
 * A simple {@link Fragment} subclass.
 */
public class StepListFragment extends Fragment implements ListenFromActivity {
    public static final String STEP_KEY = "step_k";
    private static final String POSITION_KEY = "pos_k";
    private static final String PLAY_WHEN_READY_KEY = "play_when_ready_k";

    @BindView(R.id.instructions_container)
    NestedScrollView mInstructionsContainer;


    @BindView(R.id.exo_player_view)
    SimpleExoPlayerView mExoPlayerView;
    @BindView(R.id.step_thumbnail_image)
    ImageView mIvThumbnail;
    @BindView(R.id.instruction_text)
    TextView mTvInstructions;

    public SimpleExoPlayer mExoPlayer;
    private Step mStep;
    private Unbinder unbinder;

    private long mCurrentPosition = 0;
    private boolean mPlayWhenReady = true;

    public StepListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null && getArguments().containsKey(STEP_KEY)) {
            mStep = getArguments().getParcelable(STEP_KEY);
        }
        ((StepsDetailActivity) getActivity()).setActivityListener(StepListFragment.this);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_step_list, container, false);

        if (savedInstanceState != null && savedInstanceState.containsKey(POSITION_KEY)) {
            mCurrentPosition = savedInstanceState.getLong(POSITION_KEY);
            mPlayWhenReady = savedInstanceState.getBoolean(PLAY_WHEN_READY_KEY);
        }

        unbinder = ButterKnife.bind(this, rootView);

        mTvInstructions.setText(mStep.getDescription());
        setupImageView();

        return rootView;
    }

    private void setupImageView() {
        mIvThumbnail.setVisibility(View.VISIBLE);
        if(mStep.getThumbnailURL().isEmpty() || mStep.getThumbnailURL().equals("") || mStep.getThumbnailURL() == null){
            mInstructionsContainer.setVisibility(View.VISIBLE);
            Picasso.with(getActivity())
                    .load(R.drawable.chef)
                    .placeholder(R.drawable.chef)
                    .error(R.drawable.chef)
                    .into(mIvThumbnail);
        }

        // Show thumbnail if url exists
        else {
            Picasso.with(getActivity())
                    .load(mStep.getThumbnailURL())
                    .placeholder(R.drawable.chef)
                    .error(R.drawable.chef)
                    .into(mIvThumbnail);

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!TextUtils.isEmpty(mStep.getVideoURL()))
            initializePlayer(Uri.parse(mStep.getVideoURL()));
        else {
            // Un- hide InstructionsContainer because in case of phone landscape is hidden
            mInstructionsContainer.setVisibility(View.VISIBLE);
        }

        String orientation = getScreenOrientation();
        if(orientation.equals("ORIENTATION_LANDSCAPE")){
            if(mExoPlayer!=null){
            mInstructionsContainer.setVisibility(GONE);
            mExoPlayerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FILL);
            ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) mExoPlayerView.getLayoutParams();
            params.width=params.MATCH_PARENT;
            params.height=params.MATCH_PARENT;
            mExoPlayerView.setLayoutParams(params);
        }}


    }

    @Override
    public void onPause() {
        super.onPause();
        releasePlayer();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();

    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putLong(POSITION_KEY, mCurrentPosition);
        outState.putBoolean(PLAY_WHEN_READY_KEY, mPlayWhenReady);
    }

    private void initializePlayer(Uri mediaUri) {
        if (mExoPlayer == null) {
            // Create a default TrackSelector
            DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
            TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);

            // Create the player
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector);

            // Bind the player to the view.
            mExoPlayerView.setPlayer(mExoPlayer);
            // Measures bandwidth during playback. Can be null if not required.
            // Produces DataSource instances through which media data is loaded.
            DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(getContext(), Util.getUserAgent(getContext(), getString(R.string.app_name)), bandwidthMeter);
            // This is the MediaSource representing the media to be played.
            MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(mediaUri);
            // Prepare the player with the source.
            mExoPlayer.prepare(videoSource);

            // onRestore
            if (mCurrentPosition != 0)
                mExoPlayer.seekTo(mCurrentPosition);

            mExoPlayer.setPlayWhenReady(mPlayWhenReady);
            mExoPlayerView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Release ExoPlayer.
     */
    private void releasePlayer() {
        if (mExoPlayer != null) {
            mPlayWhenReady = mExoPlayer.getPlayWhenReady();
            mCurrentPosition = mExoPlayer.getCurrentPosition();

            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }


    @Override
    public void onStop() {
        super.onStop();
        if (mExoPlayer != null) {
            mPlayWhenReady = mExoPlayer.getPlayWhenReady();
            mCurrentPosition = mExoPlayer.getCurrentPosition();

            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
        //Toast.makeText(getActivity(),"on stop",Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Toast.makeText(getActivity(),"yoyo",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void doSomethingInFragment(int position) {
        if(!isVisible()) {
            if (mExoPlayer != null) {
                mPlayWhenReady = mExoPlayer.getPlayWhenReady();
                mCurrentPosition = mExoPlayer.getCurrentPosition();

                mExoPlayer.stop();
                mExoPlayer.release();
                mExoPlayer = null;
            }
        }
    }

    private String getScreenOrientation() {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
            return "ORIENTATION_PORTRAIT";
        else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
            return "ORIENTATION_LANDSCAPE";
        else
            return "";
    }
}
