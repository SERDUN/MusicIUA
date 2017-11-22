package dmitriiserdun.gmail.com.musickiua.services;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import java.util.ArrayList;

import dmitriiserdun.gmail.com.musickiua.R;
import dmitriiserdun.gmail.com.musickiua.base.Const;
import dmitriiserdun.gmail.com.musickiua.base.player.SoundPlayer;
import dmitriiserdun.gmail.com.musickiua.model.Sound;
import dmitriiserdun.gmail.com.musickiua.storage.provider.ContractClass;
import dmitriiserdun.gmail.com.musickiua.storage.provider.ConvertHelper;
import rx.functions.Action1;

import static android.content.ContentValues.TAG;

public class MediaPlayService extends Service {

    private SoundPlayer soundPlayer;
    private TimePositionCursor timePositionCursor;
    private Handler handler = new Handler();
    private boolean statePlay = false;
    private Sound currentSound;


    public static class PlayController {
        public static String KEY = "play_controller";
        public static int PLAY = 1;
        public static int NEXT = 5;
        public static int BACK = 6;
    }

    public static class DataSourceController {
        public static String KEY = "data_source_controller";
        public static String POSITION = "position";
        public static int LOAD = 1;
        public static int CLEAR_LIST_IN_DATABASE = 2;
        public static String IS_LIST = "is_list";
        public static int UPDATE_VIEW_PLAYER = 3;
//        public static String CLEAR_LIST_IN_DATABASE = "clear_tmp_list";


    }


    @Override
    public void onCreate() {
        Log.i("Test", "Service: onCreate");
        timePositionCursor = new TimePositionCursor();

        Notification.Builder builder = new Notification.Builder(this)
//                .addAction(R.drawable.ic_pause_black_24dp, "Call", resultPendingIntent)
                .setSmallIcon(R.drawable.ic_play_circle_filled_black_24dp);
        Notification notification;
        if (Build.VERSION.SDK_INT < 16)
            notification = builder.getNotification();
        else
            notification = builder.build();
        startForeground(777, notification);
        soundPlayer = SoundPlayer.getInstance();
        initSubscribe();
        handleTmpSounds();
        super.onCreate();
    }

    private void handleTmpSounds() {
        ContentObserver ob = new ContentObserver(new Handler(Looper.getMainLooper())) {
            @Override
            public void onChange(boolean selfChangem, Uri uri) {
                Cursor record = getBaseContext().getContentResolver().query(uri,
                        ContractClass.Sounds.DEFAULT_PROJECTION,
                        null, null,
                        null);
                ArrayList<Sound> sounds = ConvertHelper.createSounds(record);
                soundPlayer.putSounds(sounds);
            }
        };
        getBaseContext().getContentResolver().registerContentObserver(ContractClass.Sounds.CONTENT_URI, true, ob);

    }

    public MediaPlayService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int keyPlaying = intent.getIntExtra(PlayController.KEY, -1);
        int keyDataSource = intent.getIntExtra(DataSourceController.KEY, -1);
        Log.d(TAG, "onStartCommand:------- KEY PLAYING:    " + keyPlaying);
        Log.d(TAG, "onStartCommand:------- KEY DATASOURCE: " + keyDataSource);


        switch (keyDataSource) {
            case 1:
                Log.d(TAG, "onStartCommand: LOAD DATA");
                soundPlayer = SoundPlayer.getInstance();
                soundPlayer.setSounds(getSound());
                break;
            case 2:
                clear();
                break;
            case 3:
                updateViewPlayer(currentSound);
                break;
        }

        switch (keyPlaying) {
            case 1:
                Log.d(TAG, "onStartCommand: PLAY");
                handleClickPlay(intent);
                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
                break;
            case 5:
                Log.d(TAG, "onStartCommand: NEXT");
                soundPlayer.nextSound();
                break;
            case 6:
                Log.d(TAG, "onStartCommand: AGO");
                soundPlayer.backSound();
                break;

        }


        return START_STICKY;
    }

    private void clear() {
        Log.d(TAG, "onStartCommand: CLEAR");

        getBaseContext().getContentResolver().delete(
                ContractClass.Sounds.CONTENT_URI,
                null,
                null);
        // soundPlayer.clear();
    }

    private void handleClickPlay(Intent intent) {
        int position = intent.getIntExtra(DataSourceController.POSITION, 0);
        Log.d(TAG, "onStartCommand: CURRENT_POSITION: " + position);
        int ff = soundPlayer.getCurrentSoundPosition();
        boolean isList = intent.getBooleanExtra(DataSourceController.IS_LIST, false);

        if (soundPlayer.isPlayingSound() && !isList) {
            soundPlayer.pause();
            statePlay = false;
        } else if (soundPlayer.isPlayingSound() && isList) {
            soundPlayer.play(position);
            statePlay = true;
        } else if (!soundPlayer.isPlayingSound() && !soundPlayer.isPaused()) {
            soundPlayer.play(position);
            statePlay = true;
        } else if (!soundPlayer.isPlayingSound() && soundPlayer.isPaused()) {
            soundPlayer.resume();
            statePlay = true;

        }


        updateStateViewController();


    }


    public void updateStateViewController() {
        Intent intent = new Intent(Const.BROADCAST_ACTION);
        intent.putExtra("statePlay", statePlay);
        intent.putExtra("status", "buttonState");
        getBaseContext().sendBroadcast(intent);

    }

    private void initSubscribe() {

        handler.removeCallbacks(timePositionCursor);
        handler.postDelayed(timePositionCursor, 50);
        soundPlayer.soundPublishSubject.subscribe(new Action1<Sound>() {
            @Override
            public void call(Sound sound) {
                MediaPlayService.this.currentSound = sound;
                updateViewPlayer(sound);

            }
        });

    }

    private void updateViewPlayer(Sound sound) {
        if (currentSound != null) {
            Log.d(TAG, "onStartCommand: SUBSCRIBE------------------: " + sound.getName());
            Intent intent = new Intent(Const.BROADCAST_ACTION);
            intent.putExtra("status", "sound_data");
            intent.putExtra("sound", sound);
            getBaseContext().sendBroadcast(intent);
        }
    }

    private ArrayList<Sound> getSound() {
        Cursor c = getBaseContext().getContentResolver().query(
                ContractClass.Sounds.CONTENT_URI,
                ContractClass.Sounds.DEFAULT_PROJECTION,
                null, null,
                null);
        ArrayList<Sound> sounds = ConvertHelper.createSounds(c);

        return sounds;
    }


    private class TimePositionCursor implements Runnable {

        @Override
        public void run() {
            //if (!soundPlayer.getProxyMediaPlayer().isStopped()) {
            // if (currentTimePosition != null)
            //currentTimePosition.call(proxyMediaPlayer.getCurrentTimePosition());

            Intent intent = new Intent(Const.BROADCAST_ACTION);
            intent.putExtra("currentSeekTime", soundPlayer.getCurrentTimePosition());
            intent.putExtra("status", "seek_data");

            getBaseContext().sendBroadcast(intent);
            handler.postDelayed(timePositionCursor, 50);
        }
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Log.d("dsdsdsds", "sraka servicu: ");
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
