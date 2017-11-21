package dmitriiserdun.gmail.com.musickiua.base.player;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Handler;

import com.danikula.videocache.HttpProxyCacheServer;

import java.util.ArrayList;

import dmitriiserdun.gmail.com.musickiua.base.App;
import dmitriiserdun.gmail.com.musickiua.model.Sound;
import rx.functions.Action1;
import rx.functions.Action2;

/**
 * Created by dmitro on 18.11.17.
 */

public class SoundPlayer implements OnCompletionListener, MediaPlayer.OnPreparedListener {
    private static final SoundPlayer ourInstance = new SoundPlayer();
    private ProxyMediaPlayer proxyMediaPlayer;
    private int currentPosition = 0;
    private ArrayList<Sound> sounds;
    private Handler handler = new Handler();
    private TimePositionCursor timePositionCursor;
    private Action1<Integer> currentTimePosition;
    private Action2<Integer, String> dataSound;


    private SoundPlayer() {
        proxyMediaPlayer = new ProxyMediaPlayer();
        timePositionCursor = new TimePositionCursor();
        sounds = new ArrayList<>();
    }

    public static SoundPlayer getInstance() {
        return ourInstance;
    }


    public void play(Sound sound) {
        preparePlayer();
        proxyMediaPlayer.play(sound);

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
        MediaPlayer mediaPlayer = proxyMediaPlayer.play(sounds.get(position));
        mediaPlayer.setOnCompletionListener(this);
        mediaPlayer.setOnPreparedListener(this);
        handler.removeCallbacks(timePositionCursor);
        handler.postDelayed(timePositionCursor, 50);

    }


    private void preparePlayer() {
        if (proxyMediaPlayer.isPlaying()) {
            proxyMediaPlayer.stop();
            proxyMediaPlayer.dispose();
        }
    }


    private void playNextSound() {
        currentPosition++;
        if (currentPosition < sounds.size()) {
            proxyMediaPlayer.dispose();
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
        //if ((dataSound != null))
//            dataSound.call(mp.getDuration(), sounds.get(currentPosition).getName());
        proxyMediaPlayer.play();

    }

    private class TimePositionCursor implements Runnable {

        @Override
        public void run() {
            if (!proxyMediaPlayer.isStopped()) {
                // if (currentTimePosition != null)
                // currentTimePosition.call(proxyMediaPlayer.getCurrentTimePosition());
                handler.postDelayed(timePositionCursor, 50);
            }
        }
    }


    public boolean isPlayingSound() {
        return proxyMediaPlayer.isPlaying();
    }

    public void pause() {
        proxyMediaPlayer.pause();
    }

    public void resume() {
        proxyMediaPlayer.play();
    }

    public int getCurrentPosition() {
        return currentPosition;
    }

    public boolean isPaused() {
        return proxyMediaPlayer.isPaused();
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


    public class ProxyMediaPlayer {
        private MediaPlayer mediaPlayer;
        private boolean isPrepared = false;
        private boolean isPaused = false;


        public ProxyMediaPlayer() {
            mediaPlayer = new MediaPlayer();
        }

        public MediaPlayer play(Sound sound) {
            mediaPlayer = new MediaPlayer();
            String url = "http://" + sound.getUrl() + "?name=" + sound.getName();
            HttpProxyCacheServer proxy = App.getProxy(App.getInstance());
            String proxyUrl = proxy.getProxyUrl(url);

            try {
                mediaPlayer.setDataSource(proxyUrl);
                mediaPlayer.prepareAsync();
                isPrepared = true;
                // mediaPlayer.setOnCompletionListener(this);
            } catch (Exception e) {
                throw new RuntimeException("Couldn't load music");
            }
            return mediaPlayer;
        }


        public void dispose() {
            if (mediaPlayer.isPlaying())
                mediaPlayer.stop();
            mediaPlayer.release();
        }

        public boolean isLooping() {
            return mediaPlayer.isLooping();
        }

        public boolean isPlaying() {
            return mediaPlayer.isPlaying();
        }

        public boolean isStopped() {
            return !isPrepared;
        }

        public void play() {
            if (mediaPlayer.isPlaying())
                return;
            try {
                synchronized (this) {
                    // if (!isPrepared) mediaPlayer.prepare();
                    isPaused = false;
                    mediaPlayer.start();
                }
            } catch (IllegalStateException e) {
                e.printStackTrace();
                // } catch (IOException e) {
                //    e.printStackTrace();
            }
        }

        public void setLooping(boolean isLooping) {
            mediaPlayer.setLooping(isLooping);
        }

        public void setVolume(float volume) {
            mediaPlayer.setVolume(volume, volume);
        }

        public void stop() {
            mediaPlayer.stop();
            synchronized (this) {
                isPrepared = false;
            }
        }

        public synchronized int getCurrentTimePosition() {
            if (mediaPlayer != null) {
                if (mediaPlayer.isPlaying()) {
                    return mediaPlayer.getCurrentPosition();
                }
                return 0;
            } else
                return 0;
        }


        public void pause() {
            isPaused = true;
            mediaPlayer.pause();
        }

        public boolean isPaused() {
            return isPaused;
        }
    }

}
