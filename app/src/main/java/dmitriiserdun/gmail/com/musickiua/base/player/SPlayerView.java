package dmitriiserdun.gmail.com.musickiua.base.player;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import dmitriiserdun.gmail.com.musickiua.R;
import dmitriiserdun.gmail.com.musickiua.model.Sound;
import rx.functions.Action1;
import rx.functions.Action2;

/**
 * Created by dmitro on 21.11.17.
 */

public class SPlayerView extends LinearLayout {
    private View rootView;
    private TextView soundName;
    private TextView currentPositio;
    private Button playControllerButton;
    private Button nextSountButtom;
    private Button backSoundButtom;
    private SeekBar progressTime;
    private PlayingSoundManager manager;
    private String currentMaxTime;


    public SPlayerView(Context context) {
        super(context);
        init(context);
    }

    public SPlayerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        manager = PlayingSoundManager.getInstance();
        rootView = inflate(context, R.layout.fragment_player, this);
        this.soundName = rootView.findViewById(R.id.currentSoundNameTV);
        this.currentPositio = rootView.findViewById(R.id.currentPositionTv);
        this.playControllerButton = rootView.findViewById(R.id.playControllerButton);
        this.nextSountButtom = rootView.findViewById(R.id.nextPlay);
        this.backSoundButtom = rootView.findViewById(R.id.backPlay);
        this.progressTime = rootView.findViewById(R.id.seekBar);
        initListener();
    }

    private void initListener() {
        playControllerButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (manager.isPlayingSound()) {
                    manager.pause();
                } else if (!manager.isPlayingSound() && manager.isPaused()) {
                    manager.resume();
                } else if (!manager.isPlayingSound() && !manager.isPaused()) {
                    manager.play();
                }
            }
        });

        nextSountButtom.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                manager.nextSound();
            }
        });

        backSoundButtom.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                manager.backSound();
            }
        });

        manager.setDataSound(new Action2<Integer, String>() {
            @Override
            public void call(Integer integer, String name) {
                soundName.setText(name);
                progressTime.setMax(integer);

                currentMaxTime = String.format("%d, %d", TimeUnit.MILLISECONDS.toMinutes(integer), TimeUnit.MILLISECONDS.toSeconds(integer) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(integer)));
            }
        });

        manager.setCurrentTimePosition(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                String mils = String.format("%d, %d", TimeUnit.MILLISECONDS.toMinutes(integer), TimeUnit.MILLISECONDS.toSeconds(integer) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(integer)));

                progressTime.setProgress(integer);
                currentPositio.setText(mils + "/" + currentMaxTime);

            }
        });
    }

    public void compelSoundInPosition(int position) {
        manager.play(position);

    }

    public void setSounds(ArrayList<Sound> sounds) {
        manager.setSounds(sounds);
    }

    public void putSounds(ArrayList<Sound> sounds) {
        manager.putSounds(sounds);
    }

    public void clear() {
        manager.clear();
    }

    public PlayingSoundManager getManager() {
        return manager;
    }
}
