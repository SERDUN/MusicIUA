package dmitriiserdun.gmail.com.musickiua.services;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import java.util.ArrayList;

import dmitriiserdun.gmail.com.musickiua.R;
import dmitriiserdun.gmail.com.musickiua.base.player.SoundPlayer;
import dmitriiserdun.gmail.com.musickiua.model.Sound;
import dmitriiserdun.gmail.com.musickiua.screens.navigation.NavActivity;
import dmitriiserdun.gmail.com.musickiua.storage.provider.ContractClass;
import dmitriiserdun.gmail.com.musickiua.storage.provider.ConvertHelper;

public class MediaPlayService extends Service {
    SoundPlayer soundPlayer;

    public static class PlayController {
        public static String KEY = "play_controller";
        public static int PLAY = 1;
        public static int PAUSE = 2;
        public static int RESUME = 3;
        public static int STOP = 4;
        public static int NEXT = 5;
        public static int BACK = 6;
    }

    public static class DataSourceController {
        public static String KEY = "data_source_controller";
        public static String POSITION = "position";

        public static int LOAD = 1;


    }


    @Override
    public void onCreate() {
        Log.i("Test", "Service: onCreate");



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
        int keyPlaying = intent.getIntExtra(PlayController.KEY, 1);
        int keyDataSource = intent.getIntExtra(DataSourceController.KEY, 1);
        switch (keyDataSource) {
            case 1:
                soundPlayer = SoundPlayer.getInstance();
                soundPlayer.setSounds(getSound());
                break;
        }

        switch (keyPlaying) {
            case 1:
                int position = intent.getIntExtra(DataSourceController.POSITION, 0);
                soundPlayer.play(position);
                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
                break;
            case 5:
                break;
            case 6:
                break;

        }


        return START_STICKY;
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

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Log.d("dsdsdsds", "sraka servicu: ");
    }
}
