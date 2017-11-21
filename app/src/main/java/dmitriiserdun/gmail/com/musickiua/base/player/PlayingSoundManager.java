package dmitriiserdun.gmail.com.musickiua.base.player;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Handler;

import java.util.ArrayList;

import dmitriiserdun.gmail.com.musickiua.model.Sound;
import rx.functions.Action1;
import rx.functions.Action2;

/**
 * Created by dmitro on 18.11.17.
 */

public class PlayingSoundManager implements OnCompletionListener, MediaPlayer.OnPreparedListener {
    private static final PlayingSoundManager ourInstance = new PlayingSoundManager();
    private SoundPlayer soundPlayer;
    private int currentPosition = 0;
    private ArrayList<Sound> sounds;
    private Handler handler = new Handler();
    private TimePositionCursor timePositionCursor;
    private Action1<Integer> currentTimePosition;
    private Action2<Integer, String> dataSound;


    private PlayingSoundManager() {
        soundPlayer = new SoundPlayer();
        timePositionCursor = new TimePositionCursor();
        sounds = new ArrayList<>();
    }

    public static PlayingSoundManager getInstance() {
        return ourInstance;
    }


    public void play(Sound sound) {
        preparePlayer();
        soundPlayer.play(sound);

    }

    public void play() {
        play(0);
    }

    public void play(int position) {
        if (sounds != null) perform(sounds, position);
    }

    public void play(ArrayList<Sound> sounds) {
        play(sounds, 0);
    }

    public void play(ArrayList<Sound> sounds, int position) {
        preparePlayer();
        this.sounds = sounds;
        this.currentPosition = position;
        perform(this.sounds, currentPosition);
    }

    public void perform(ArrayList<Sound> sounds, int position) {
        MediaPlayer mediaPlayer = soundPlayer.play(sounds.get(position));
        mediaPlayer.setOnCompletionListener(this);
        mediaPlayer.setOnPreparedListener(this);
        handler.removeCallbacks(timePositionCursor);
        handler.postDelayed(timePositionCursor, 50);

    }


    private void preparePlayer() {
        if (soundPlayer.isPlaying()) {
            soundPlayer.stop();
            soundPlayer.dispose();
        }
    }


    private void playNextSound() {
        currentPosition++;
        if (currentPosition < sounds.size()) {
            soundPlayer.dispose();
            perform(sounds, currentPosition);
        }

    }

    public void setCurrentTimePosition(Action1<Integer> currentTimePosition) {
        this.currentTimePosition = currentTimePosition;
    }

    public void setDataSound(Action2<Integer, String> dataSound) {
        this.dataSound = dataSound;
    }


    @Override
    public void onCompletion(MediaPlayer mp) {
        playNextSound();
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        if ((dataSound != null))
            dataSound.call(mp.getDuration(), sounds.get(currentPosition).getName());
        soundPlayer.play();

    }

    private class TimePositionCursor implements Runnable {

        @Override
        public void run() {
            if (!soundPlayer.isStopped()) {
                if (currentTimePosition != null)
                    currentTimePosition.call(soundPlayer.getCurrentTimePosition());
                handler.postDelayed(timePositionCursor, 50);
            }
        }
    }


    public boolean isPlayingSound() {
        return soundPlayer.isPlaying();
    }

    public void pause() {
        soundPlayer.pause();
    }

    public void resume() {
        soundPlayer.play();
    }

    public int getCurrentPosition() {
        return currentPosition;
    }

    public boolean isPaused() {
        return soundPlayer.isPaused();
    }

    public void nextSound() {
        currentPosition++;
        if (currentPosition < sounds.size()) {
            preparePlayer();
            perform(sounds, currentPosition);
        }
    }

    public void backSound() {

        currentPosition--;
        if (currentPosition >= 0) {
            preparePlayer();
            perform(sounds, currentPosition);
        }
    }

    public ArrayList<Sound> getSounds() {
        return sounds;
    }

    public void setSounds(ArrayList<Sound> sounds) {
        this.sounds = sounds;
    }

    public void putSounds(ArrayList<Sound> sounds) {
        this.sounds.addAll(sounds);
    }

    public void clear() {
        sounds.clear();
    }


}
