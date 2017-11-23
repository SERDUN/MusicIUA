package dmitriiserdun.gmail.com.musickiua.base.player;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.util.Log;

import com.danikula.videocache.HttpProxyCacheServer;

import java.util.ArrayList;

import dmitriiserdun.gmail.com.musickiua.base.App;
import dmitriiserdun.gmail.com.musickiua.model.Sound;
import rx.subjects.PublishSubject;

import static android.content.ContentValues.TAG;

/**
 * Created by dmitro on 18.11.17.
 */

public class SoundPlayer implements OnCompletionListener, MediaPlayer.OnPreparedListener {
    private static final SoundPlayer ourInstance = new SoundPlayer();
    private ProxyMediaPlayer proxyMediaPlayer;
    private int currentSoundPosition = 0;
    private ArrayList<Sound> sounds;
    public PublishSubject<Sound> soundPublishSubject;

    private SoundPlayer() {
        proxyMediaPlayer = new ProxyMediaPlayer();
        sounds = new ArrayList<>();
        soundPublishSubject = PublishSubject.create();
    }

    public static SoundPlayer getInstance() {
        return ourInstance;
    }

    public void play() {
        play(0);
    }

    public void play(int position) {
        Log.d("position", "play: " + position);
        if (sounds != null) {
            currentSoundPosition = position;
            preparePlayer();
            perform(sounds, position);
        }
    }

    public void play(ArrayList<Sound> sounds) {
        play(sounds, 0);
    }

    public void play(ArrayList<Sound> sounds, int position) {
        preparePlayer();
        this.sounds = sounds;
        this.currentSoundPosition = position;
        perform(this.sounds, currentSoundPosition);
    }

    public void perform(ArrayList<Sound> sounds, int position) {
        Sound current = sounds.get(position);
        Log.d("position", "play: " + current.getName());


        MediaPlayer mediaPlayer = proxyMediaPlayer.play(sounds.get(position));
        mediaPlayer.setOnCompletionListener(this);
        mediaPlayer.setOnPreparedListener(this);


    }


    public void preparePlayer() {
        if (proxyMediaPlayer.isPlaying()) {
            proxyMediaPlayer.stop();
            proxyMediaPlayer.dispose();
        }
    }


    private void playNextSound() {
        currentSoundPosition++;
        if (currentSoundPosition < sounds.size()) {
            proxyMediaPlayer.dispose();
            perform(sounds, currentSoundPosition);
        }

    }


    @Override
    public void onCompletion(MediaPlayer mp) {
        playNextSound();
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        Log.d(TAG, "onPrepared: ttt");
        Sound current = sounds.get(currentSoundPosition);
        current.setTimeMilis(mp.getDuration());
        soundPublishSubject.onNext(sounds.get(currentSoundPosition));
        soundPublishSubject.publish();
        proxyMediaPlayer.play();

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

    public int getCurrentSoundPosition() {
        return currentSoundPosition;
    }

    public int getCurrentTimePosition() {
        return proxyMediaPlayer.getCurrentTimePosition();
    }

    public boolean isPaused() {
        return proxyMediaPlayer.isPaused();
    }

    public void nextSound() {
        currentSoundPosition++;
        if (currentSoundPosition < sounds.size()) {
            preparePlayer();
            perform(sounds, currentSoundPosition);
        }
    }

    public void backSound() {

        currentSoundPosition--;
        if (currentSoundPosition >= 0) {
            preparePlayer();
            perform(sounds, currentSoundPosition);
        }
    }

    public ArrayList<Sound> getSounds() {
        return sounds;
    }

    public void setSounds(ArrayList<Sound> sounds) {
        Log.d("position", "play arr: " + sounds.toString());
        this.sounds.clear();
        this.sounds.addAll(sounds);
        currentSoundPosition=0;
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
            String url = "http://" + sound.getUrl();
            HttpProxyCacheServer proxy = App.getProxy(App.getInstance(), sound.getName());
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
                    isPaused = false;
                    mediaPlayer.start();
                }
            } catch (IllegalStateException e) {
                e.printStackTrace();
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

    public ProxyMediaPlayer getProxyMediaPlayer() {
        return proxyMediaPlayer;
    }
}
