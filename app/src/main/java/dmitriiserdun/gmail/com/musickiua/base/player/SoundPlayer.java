package dmitriiserdun.gmail.com.musickiua.base.player;

import android.media.MediaPlayer;

import com.danikula.videocache.HttpProxyCacheServer;

import dmitriiserdun.gmail.com.musickiua.base.App;
import dmitriiserdun.gmail.com.musickiua.model.Sound;

/**
 * Created by dmitro on 18.11.17.
 */

public class SoundPlayer {
    private MediaPlayer mediaPlayer;
    private boolean isPrepared = false;


    public SoundPlayer() {
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
        mediaPlayer.pause();
    }


}
