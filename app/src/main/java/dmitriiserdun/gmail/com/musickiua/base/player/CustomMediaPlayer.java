package dmitriiserdun.gmail.com.musickiua.base.player;

import android.media.MediaPlayer;
import android.os.Handler;
import android.util.Log;

import com.danikula.videocache.HttpProxyCacheServer;

import java.util.ArrayList;
import java.util.List;

import dmitriiserdun.gmail.com.musickiua.base.App;
import dmitriiserdun.gmail.com.musickiua.model.Sound;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;

/**
 * Created by dmitro on 17.11.17.
 */

public class CustomMediaPlayer implements MediaPlayer.OnCompletionListener {
    private MediaPlayer mediaPlayer;
    private ArrayList<Sound> sounds;
    private static CustomMediaPlayer ourInstance = new CustomMediaPlayer();
    private boolean isPrepared = false;
    private int currentPosition;
    private int currentTime = 0;
    private PublishSubject<Integer> time = PublishSubject.create();

    private Action1<Integer> action1;
    private Handler handler = new Handler();
    private Runnable runnable;


    public static CustomMediaPlayer getInstance() {
        return ourInstance;
    }

    private CustomMediaPlayer() {

    }

    public void playSounds(List<Sound> sounds, int start) {

        currentPosition = start;
        this.sounds = (ArrayList<Sound>) sounds;
        dispose();

        startPlay();
    }

    private void startPlay() {
        if (currentPosition != sounds.size() - 1) {
            final MediaPlayer mediaPlayer = prepareSingleSound(sounds.get(currentPosition));
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    currentPosition++;
                    mediaPlayer.release();
                    startPlay();
                }
            });

        }

    }

    public MediaPlayer prepareSingleSound(Sound sound) {
        String url = "http://" + sound.getUrl();
        HttpProxyCacheServer proxy = App.getProxy(App.getInstance());
        String proxyUrl = proxy.getProxyUrl(url);
        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(proxyUrl);
            mediaPlayer.prepare();
            isPrepared = true;
            mediaPlayer.setOnCompletionListener(this);
            if (action1 != null) action1.call(mediaPlayer.getDuration());
            runnable = new TimePositionCursor();
            handler.postDelayed(runnable, 50);
            play();

        } catch (Exception e) {
            Log.d("ddd", "prepareSingleSound: ");
        }
        return mediaPlayer;
    }


    public void play() {
        synchronized (this) {
            mediaPlayer.start();

        }

    }

    public void stop() {
        mediaPlayer.stop();
        synchronized (this) {
            isPrepared = false;
        }
    }

    public void pause() {

    }

    public void setLooping(boolean looping) {
        mediaPlayer.setLooping(looping);
    }

    public void setVolume(float volume) {
        mediaPlayer.setVolume(volume, volume);
    }


    public boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }

    public boolean isStopped() {
        return !isPrepared;
    }

    public boolean isLooping() {
        return mediaPlayer.isLooping();
    }

    public void dispose() {
        if (mediaPlayer != null)
            if (mediaPlayer.isLooping()) mediaPlayer.stop();
        mediaPlayer.release();
    }


    @Override
    public void onCompletion(MediaPlayer mp) {
        synchronized (this) {

            isPrepared = false;
            play();
        }
    }

    public PublishSubject<Integer> getTime() {
        return time;
    }

    public void setAction1(Action1<Integer> action1) {
        this.action1 = action1;
    }

    private class TimePositionCursor implements Runnable {

        @Override
        public void run() {
            if (mediaPlayer != null)
                currentTime = mediaPlayer.getCurrentPosition();
            time.onNext(currentTime);
            handler.postDelayed(runnable, 50);
        }
    }
}
