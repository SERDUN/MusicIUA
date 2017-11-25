package dmitriiserdun.gmail.com.musickiua.services;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import java.util.ArrayList;

import dmitriiserdun.gmail.com.musickiua.R;
import dmitriiserdun.gmail.com.musickiua.base.Const;
import dmitriiserdun.gmail.com.musickiua.base.player.SoundPlayer;
import dmitriiserdun.gmail.com.musickiua.model.Sound;
import dmitriiserdun.gmail.com.musickiua.screens.navigation.NavActivity;
import dmitriiserdun.gmail.com.musickiua.storage.base.DatabaseContract;
import dmitriiserdun.gmail.com.musickiua.storage.provider.ConvertHelper;
import rx.functions.Action1;

import static android.content.ContentValues.TAG;

public class MediaPlayService extends Service {

    private SoundPlayer soundPlayer;
    private TimePositionCursor timePositionCursor;
    private Handler handler = new Handler();
    private boolean statePlay = false;
    private Sound currentSound;
    private Notification status;


    public final static String STATUS = "status";
    public final static String SOUND_LOADED_STATUS = "sound_loaded_status";
    private final String LOG_TAG = "NotificationService";


    public static class PlayController {
        public static String KEY = "play_controller";
        public static String LOAD = "load_sound_with_database";
        public static String POSITION = "position";
        public static String IS_LIST = "is_list";

        public static int PLAY = 1;
        public static int NEXT = 5;
        public static int BACK = 6;
        public static int UPDATE_VIEW_PLAYER = 3;
    }


    @Override
    public void onCreate() {

        soundPlayer = SoundPlayer.getInstance();
        initSubscribe();
        super.onCreate();
    }

    public MediaPlayService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        handleViewPlayerEvents(intent);
        handleNotificationsEvent(intent);
        return START_STICKY;
    }

    private void handleNotificationsEvent(Intent intent) {
        String act = intent.getAction();
        if (act != null) {
            if (intent.getAction().equals(Const.ACTION.STARTFOREGROUND_ACTION)) {
                showNotification();
                Log.i(LOG_TAG, "notification  Started");

            } else if (intent.getAction().equals(Const.ACTION.PREV_ACTION)) {
                Log.i(LOG_TAG, "Clicked Previous");
                soundPlayer.backSound();
            } else if (intent.getAction().equals(Const.ACTION.PLAY_ACTION)) {
                Log.i(LOG_TAG, "Clicked Play");
                handleClickPlay(intent);

            } else if (intent.getAction().equals(Const.ACTION.NEXT_ACTION)) {
                Log.i(LOG_TAG, "Clicked Next");
                soundPlayer.nextSound();

            } else if (intent.getAction().equals(
                    Const.ACTION.STOPFOREGROUND_ACTION)) {
                Log.i(LOG_TAG, "Received Stop Foreground Intent");
                //stopForeground(true);
                //  stopSelf();
            }
        }
    }

    private void showNotification() {

        RemoteViews views = new RemoteViews(getPackageName(), R.layout.notification_status_bar);

        //   RemoteViews bigViews = new RemoteViews(getPackageName(), R.layout.notification_status_bar_expanded);


        views.setViewVisibility(R.id.status_bar_icon, View.VISIBLE);
        views.setViewVisibility(R.id.status_bar_album_art, View.GONE);

        // bigViews.setImageViewBitmap(R.id.status_bar_album_art, Const.getDefaultAlbumArt(this));


        Intent notificationIntent = new Intent(this, NavActivity.class);
        notificationIntent.setAction(Const.ACTION.MAIN_ACTION);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);


        Intent previousIntent = new Intent(this, MediaPlayService.class);
        previousIntent.setAction(Const.ACTION.PREV_ACTION);
        PendingIntent ppreviousIntent = PendingIntent.getService(this, 0,
                previousIntent, 0);


        Intent playIntent = new Intent(this, MediaPlayService.class);
        playIntent.setAction(Const.ACTION.PLAY_ACTION);
        PendingIntent pplayIntent = PendingIntent.getService(this, 0,
                playIntent, 0);


        Intent nextIntent = new Intent(this, MediaPlayService.class);
        nextIntent.setAction(Const.ACTION.NEXT_ACTION);
        PendingIntent pnextIntent = PendingIntent.getService(this, 0,
                nextIntent, 0);


        Intent closeIntent = new Intent(this, MediaPlayService.class);
        closeIntent.setAction(Const.ACTION.STOPFOREGROUND_ACTION);
        PendingIntent pcloseIntent = PendingIntent.getService(this, 0,
                closeIntent, 0);


        views.setOnClickPendingIntent(R.id.status_bar_play, pplayIntent);
        //bigViews.setOnClickPendingIntent(R.id.status_bar_play, pplayIntent);

        views.setOnClickPendingIntent(R.id.status_bar_next, pnextIntent);
        //bigViews.setOnClickPendingIntent(R.id.status_bar_next, pnextIntent);
        views.setOnClickPendingIntent(R.id.status_bar_prev, ppreviousIntent);
        // bigViews.setOnClickPendingIntent(R.id.status_bar_prev, ppreviousIntent);

        views.setOnClickPendingIntent(R.id.status_bar_collapse, pcloseIntent);
        // bigViews.setOnClickPendingIntent(R.id.status_bar_collapse, pcloseIntent);

        views.setImageViewResource(R.id.status_bar_play,
                R.drawable.ic_pause_black_24dp);

        //bigViews.setImageViewResource(R.id.status_bar_play,
        //  R.drawable.ic_pause_black_24dp);

        views.setTextViewText(R.id.status_bar_track_name, "Song Title");
        //bigViews.setTextViewText(R.id.status_bar_track_name, "Song Title");

        views.setTextViewText(R.id.status_bar_artist_name, "Artist Name");
        //bigViews.setTextViewText(R.id.status_bar_artist_name, "Artist Name");

        // bigViews.setTextViewText(R.id.status_bar_album_name, "Album Name");


        status = new Notification.Builder(this).build();
        status.contentView = views;
        // status.bigContentView = bigViews;
        status.flags = Notification.FLAG_ONGOING_EVENT;
        status.icon = R.drawable.ic_launcher_background;
        status.contentIntent = pendingIntent;
        startForeground(Const.NOTIFICATION_ID.FOREGROUND_SERVICE, status);

    }

    private void handleViewPlayerEvents(Intent intent) {
        int keyPlaying = intent.getIntExtra(PlayController.KEY, -1);

        boolean loadData = intent.getBooleanExtra(PlayController.LOAD, false);

        if (loadData) {
            soundPlayer = SoundPlayer.getInstance();
            soundPlayer.setSounds(getSound());
        }

        switch (keyPlaying) {
            case 1:
                Log.d(TAG, "onStartCommand: PLAY");
                handleClickPlay(intent);
                break;
            case 2:
                break;
            case 3:
                updateViewPlayer(currentSound, true);
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
    }


    private void handleClickPlay(Intent intent) {
        int position = intent.getIntExtra(PlayController.POSITION, 0);
        Log.d(TAG, "onStartCommand: CURRENT_POSITION: " + position);
        boolean isList = intent.getBooleanExtra(PlayController.IS_LIST, false);

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
        intent.putExtra(STATUS, "buttonState");
        getBaseContext().sendBroadcast(intent);

    }

    private void initSubscribe() {
        timePositionCursor = new TimePositionCursor();
        handler.removeCallbacks(timePositionCursor);

        handler.postDelayed(timePositionCursor, 50);

        soundPlayer.startPreparedAction.subscribe(new Action1<Sound>() {
            @Override
            public void call(Sound sound) {
                updateViewPlayer(sound, false);

            }
        });

        soundPlayer.finishedPreparedAction.subscribe(new Action1<Sound>() {
            @Override
            public void call(Sound sound) {
                MediaPlayService.this.currentSound = sound;
                updateViewPlayer(sound, true);

            }
        });
    }

    private void updateViewPlayer(Sound sound, boolean soundLoaded) {
        if (currentSound != null) {
            Intent intent = new Intent(Const.BROADCAST_ACTION);
            intent.putExtra(STATUS, "sound_data");
            intent.putExtra("sound", sound);
            intent.putExtra(SOUND_LOADED_STATUS, soundLoaded);
            getBaseContext().sendBroadcast(intent);
        }
    }

    private ArrayList<Sound> getSound() {
        Cursor c = getBaseContext().getContentResolver().query(
                DatabaseContract.Sounds.CONTENT_URI,
                DatabaseContract.Sounds.DEFAULT_PROJECTION,
                null, null,
                null);
        ArrayList<Sound> sounds = ConvertHelper.createSounds(c);
        Log.d("empty_arr", "getSound: " + sounds);


        return sounds;
    }

    private class TimePositionCursor implements Runnable {

        @Override
        public void run() {

            Intent intent = new Intent(Const.BROADCAST_ACTION);
            intent.putExtra("status", "seek_data");
            intent.putExtra("currentSeekTime", soundPlayer.getCurrentTimePosition());

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
