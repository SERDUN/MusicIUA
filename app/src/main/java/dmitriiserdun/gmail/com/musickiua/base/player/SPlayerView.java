package dmitriiserdun.gmail.com.musickiua.base.player;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

import dmitriiserdun.gmail.com.musickiua.R;
import dmitriiserdun.gmail.com.musickiua.base.CustomProgressBar;
import dmitriiserdun.gmail.com.musickiua.model.Sound;
import dmitriiserdun.gmail.com.musickiua.services.MediaPlayService;

import static dmitriiserdun.gmail.com.musickiua.base.Const.BROADCAST_ACTION;

/**
 * Created by dmitro on 21.11.17.
 */

public class SPlayerView extends LinearLayout {
    private View rootView;
    private TextView soundName;
    private TextView currentPosition;
    private Button playControllerButton;
    private Button nextCountButton;
    private Button backSoundButton;
    private SeekBar progressTime;
    private String maxSeekPosition;

    private ViewGroup seekContent;
    private CustomProgressBar customProgressBar;

    private ControlPlayer controlPlayer;

    public void initController(ControlPlayer controlPlayer) {
        this.controlPlayer = controlPlayer;
    }


    public SPlayerView(Context context) {
        super(context);
        init(context);
    }

    public SPlayerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        rootView = inflate(context, R.layout.view_player, this);
        this.soundName = rootView.findViewById(R.id.currentSoundNameTV);
        this.currentPosition = rootView.findViewById(R.id.currentPositionTv);
        this.playControllerButton = rootView.findViewById(R.id.playControllerButton);
        this.nextCountButton = rootView.findViewById(R.id.nextPlay);
        this.backSoundButton = rootView.findViewById(R.id.backPlay);
        this.progressTime = rootView.findViewById(R.id.seekBar);
        this.seekContent=rootView.findViewById(R.id.mediaPlayerSeekContent);
        this.customProgressBar=rootView.findViewById(R.id.mediaPlayerProgressBar);
        initListener();
        initButton();
    }

    private void initButton() {
        playControllerButton.setText("=>");
        nextCountButton.setText("->");
        backSoundButton.setText("<-");
    }

    private void initListener() {
        playControllerButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                controlPlayer.startOrPause();

            }
        });

        nextCountButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                controlPlayer.next();
            }
        });

        backSoundButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // manager.backSound();
                controlPlayer.back();
            }
        });

        BroadcastReceiver br = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String status = intent.getStringExtra("status");
                if (status.equals("buttonState")) {
                    boolean playState = intent.getBooleanExtra("statePlay", false);
                    handleStatePlayController(playState);

                }
                if (status.equals("sound_data")) {
                    boolean soundLoaded = intent.getBooleanExtra(MediaPlayService.SOUND_LOADED_STATUS, false);
                    handleViewData((Sound) intent.getSerializableExtra("sound"), soundLoaded);
                }

                if (status.equals("seek_data")) {
                    handleSeekBar(intent.getIntExtra("currentSeekTime", 0));
                }
            }
        };

        IntentFilter intFilt = new IntentFilter(BROADCAST_ACTION);
        getContext().registerReceiver(br, intFilt);

    }

    private void handleViewData(Sound sound, boolean soundLoaded) {
        if (soundLoaded) {
            customProgressBar.setVisibility(GONE);
            seekContent.setVisibility(VISIBLE);
            maxSeekPosition = sound.getTime();
            soundName.setText(sound.getName());
            progressTime.setMax(sound.getTimeMilis());
        } else {
            customProgressBar.setVisibility(VISIBLE);
            seekContent.setVisibility(GONE);
            soundName.setText(sound.getName());

        }
    }

    private void handleSeekBar(int position) {

        progressTime.setProgress(position);
        String mils = String.format("%d:%d", TimeUnit.MILLISECONDS.toMinutes(position), TimeUnit.MILLISECONDS.toSeconds(position) -
                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(position)));
        currentPosition.setText(mils + " / " + maxSeekPosition);
    }

    public void handleStatePlayController(boolean state) {
        if (state) {
            playControllerButton.setText("||");
        } else {
            playControllerButton.setText("->");

        }
    }

}
