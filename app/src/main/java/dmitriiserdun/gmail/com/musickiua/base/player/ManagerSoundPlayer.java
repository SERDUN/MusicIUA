package dmitriiserdun.gmail.com.musickiua.base.player;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;

import java.util.ArrayList;

import dmitriiserdun.gmail.com.musickiua.base.App;
import dmitriiserdun.gmail.com.musickiua.base.Const;
import dmitriiserdun.gmail.com.musickiua.model.Sound;
import dmitriiserdun.gmail.com.musickiua.services.MediaPlayService;
import dmitriiserdun.gmail.com.musickiua.storage.base.DatabaseContract;
import dmitriiserdun.gmail.com.musickiua.storage.provider.ConvertHelper;

/**
 * Created by dmitro on 21.11.17.
 */

public class ManagerSoundPlayer implements ControlPlayer {

    public boolean showedNotification = false;
    private static final ManagerSoundPlayer ourInstance = new ManagerSoundPlayer();

    private int lastClicedPosition = 0;

    public static ManagerSoundPlayer getInstance() {
        return ourInstance;
    }

    public ContentObserver observableSounds;

    private ManagerSoundPlayer() {
    }


    public ControlPlayer getController() {
        return this;
    }


    public void selectAndPlaySound(Context context, int position) {

        Intent intent = new Intent(context, MediaPlayService.class);
        intent.putExtra(MediaPlayService.DataSourceController.IS_LIST, true);
        intent.putExtra(MediaPlayService.DataSourceController.KEY, MediaPlayService.DataSourceController.LOAD);
        intent.putExtra(MediaPlayService.PlayController.KEY, MediaPlayService.PlayController.PLAY);
        intent.putExtra(MediaPlayService.DataSourceController.POSITION, position);
        if (!showedNotification) {
            showedNotification = true;
            intent.setAction(Const.ACTION.STARTFOREGROUND_ACTION);
        }
        context.startService(intent);
    }

    public void selectAndPlaySound(Context context) {
        Intent intent = new Intent(context, MediaPlayService.class);
        //intent.putExtra(MediaPlayService.DataSourceController.KEY, MediaPlayService.DataSourceController.LOAD);
        intent.putExtra(MediaPlayService.PlayController.KEY, MediaPlayService.PlayController.PLAY);
        context.startService(intent);
    }


    public void initSounds(final Context context, ArrayList<Sound> sounds) {
        ContentValues[] contentValues = ConvertHelper.createContentValues(sounds);
        App.getInstance().getContentResolver().bulkInsert(DatabaseContract.Sounds.CONTENT_URI, contentValues);
        registerSoundObservable(context);

    }

    private void registerSoundObservable(final Context context) {
        if (observableSounds == null) {
            observableSounds = new ContentObserver(new Handler(Looper.getMainLooper())) {
                @Override
                public void onChange(boolean selfChange, Uri uri) {
                    Intent intent = new Intent(context, MediaPlayService.class);
                    intent.putExtra(MediaPlayService.DataSourceController.KEY, MediaPlayService.DataSourceController.LOAD);
                    context.startService(intent);
                    context.getContentResolver().unregisterContentObserver(this);

                }
            };
            context.getContentResolver().registerContentObserver(DatabaseContract.Sounds.CONTENT_URI, true, observableSounds);
        }
    }


    @Override
    public void startOrPause() {
        Intent intent = new Intent(App.getInstance(), MediaPlayService.class);
        intent.putExtra(MediaPlayService.DataSourceController.IS_LIST, false);
        intent.putExtra(MediaPlayService.PlayController.KEY, MediaPlayService.PlayController.PLAY);
        intent.putExtra(MediaPlayService.DataSourceController.POSITION, lastClicedPosition);

        App.getInstance().startService(intent);
    }

    @Override
    public void stop() {

    }

    @Override
    public void resume() {

    }


    @Override
    public void next() {
        Intent intent = new Intent(App.getInstance(), MediaPlayService.class);
        intent.putExtra(MediaPlayService.PlayController.KEY, MediaPlayService.PlayController.NEXT);
        App.getInstance().startService(intent);
    }

    @Override
    public void back() {
        Intent intent = new Intent(App.getInstance(), MediaPlayService.class);
        intent.putExtra(MediaPlayService.PlayController.KEY, MediaPlayService.PlayController.BACK);
        App.getInstance().startService(intent);
    }

    @Override
    public void isRepeat(boolean repeat) {

    }

    public void deleteTemporarySound(Context context) {
//        Intent intent = new Intent(App.getInstance(), MediaPlayService.class);
//        intent.putExtra(MediaPlayService.DataSourceController.KEY, MediaPlayService.DataSourceController.CLEAR_LIST_IN_DATABASE);
//        App.getInstance().startService(intent);
        context.getContentResolver().delete(
                DatabaseContract.Sounds.CONTENT_URI,
                null,
                null);
    }

    public void updateViewPlayer() {
        Intent intent = new Intent(App.getInstance(), MediaPlayService.class);
        intent.putExtra(MediaPlayService.DataSourceController.KEY, MediaPlayService.DataSourceController.UPDATE_VIEW_PLAYER);
        App.getInstance().startService(intent);


    }


}
