package me.sankalpchauhan.bakingtime.view.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.sankalpchauhan.bakingtime.R;
import me.sankalpchauhan.bakingtime.service.model.Step;
import me.sankalpchauhan.bakingtime.utils.Constants;
import me.sankalpchauhan.bakingtime.utils.Utility;
import timber.log.Timber;

import static me.sankalpchauhan.bakingtime.utils.Constants.PLAY_BACK_POSITION;
import static me.sankalpchauhan.bakingtime.utils.Constants.PLAY_WHEN_READY;

public class StepDetailFragment extends Fragment implements Player.EventListener {
    @BindView(R.id.step_description)
    TextView mStepDescription;
    @BindView(R.id.step_thumbnail)
    ImageView mStepThumbnail;
    @BindView(R.id.simpleExoPlayerView)
    PlayerView mExoPlayerView;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    private boolean playWhenReady = true;
    private Step mStep;
    private SimpleExoPlayer mExoPlayer;
    private long playBackPosition = 0L;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle catchBundle = this.getArguments();
        if (catchBundle != null) {
            mStep = (Step) catchBundle.getSerializable(Constants.STEP_DATA);
        }
        final View rootView = inflater.inflate(R.layout.step_detail_fragment, container, false);
        ButterKnife.bind(this, rootView);
        if (savedInstanceState != null) {
            playBackPosition = savedInstanceState.getLong(PLAY_BACK_POSITION, 0L);
            playWhenReady = savedInstanceState.getBoolean(PLAY_WHEN_READY);
        }
        mStepDescription.setText(mStep.getDescription());
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
//        progressBar.setVisibility(View.VISIBLE);
//        mExoPlayerView.setVisibility(View.GONE);
//        if(!mStep.getVideoURL().isEmpty()) {
//            initializePlayer();
//        } else {
//            progressBar.setVisibility(View.GONE);
//        }
    }

    @Override
    public void onResume() {
        super.onResume();
        progressBar.setVisibility(View.VISIBLE);
        mExoPlayerView.setVisibility(View.GONE);
        if (!mStep.getVideoURL().isEmpty()) {
            initializePlayer();
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void onPause() {

        releaseExoplayer();
        super.onPause();
    }

    private void initializePlayer() {
        mExoPlayerView.setVisibility(View.VISIBLE);
        if (mExoPlayer == null) {
            mExoPlayerView.setVisibility(View.VISIBLE);
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), new DefaultRenderersFactory(getContext()), new DefaultTrackSelector(), new DefaultLoadControl());
            prepareExoPlayer();
            mExoPlayerView.setPlayer(mExoPlayer);
            mExoPlayer.seekTo(playBackPosition);
            mExoPlayer.setPlayWhenReady(true);
            mExoPlayer.addListener(this);
        }
    }

    private void prepareExoPlayer() {
        Uri uri = Uri.parse(mStep.getVideoURL());
        MediaSource mediaSource = new ProgressiveMediaSource.Factory(new DefaultDataSourceFactory(getContext(), Utility.getUserAgent(getContext(), "BakingTime"))).createMediaSource(uri);
        mExoPlayer.prepare(mediaSource);
    }

    private void releaseExoplayer() {
        if (mExoPlayer != null) {
            playBackPosition = mExoPlayer.getCurrentPosition();
            playWhenReady = mExoPlayer.getPlayWhenReady();
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {
        Timber.d("Loading Listener: " + isLoading);

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        progressBar.setVisibility(View.INVISIBLE);
        if (playbackState == Player.STATE_BUFFERING) {
            progressBar.setVisibility(View.VISIBLE);
        } else if (playbackState == Player.STATE_READY) {
            progressBar.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    public void onRepeatModeChanged(int repeatMode) {

    }

    @Override
    public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {
        mExoPlayerView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        mStepThumbnail.setVisibility(View.VISIBLE);
        if (!mStep.getThumbnailURL().isEmpty()) {
            Picasso.get().load(mStep.getThumbnailURL()).into(mStepThumbnail, new Callback() {
                @Override
                public void onSuccess() {
                    progressBar.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onError(Exception e) {
                    progressBar.setVisibility(View.INVISIBLE);
                    Timber.d("Both video & thumbnail failed");
                    mStepThumbnail.setVisibility(View.GONE);
                    e.printStackTrace();
                }
            });
        } else {
            progressBar.setVisibility(View.INVISIBLE);
            Timber.d("Both video & thumbnail failed");
            mStepThumbnail.setVisibility(View.GONE);
        }
    }

    @Override
    public void onPositionDiscontinuity(int reason) {

    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

    }

    @Override
    public void onSeekProcessed() {

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(PLAY_WHEN_READY, playWhenReady);
        outState.putLong(PLAY_BACK_POSITION, playBackPosition);
    }


}
