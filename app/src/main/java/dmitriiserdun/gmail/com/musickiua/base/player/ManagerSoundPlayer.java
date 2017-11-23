package dmitriiserdun.gmail.com.musickiua.base.player;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.ArrayList;

import dmitriiserdun.gmail.com.musickiua.base.App;
import dmitriiserdun.gmail.com.musickiua.model.Sound;
import dmitriiserdun.gmail.com.musickiua.services.MediaPlayService;
import dmitriiserdun.gmail.com.musickiua.storage.base.DatabaseContract;
import dmitriiserdun.gmail.com.musickiua.storage.provider.ConvertHelper;

/**
 * Created by dmitro on 21.11.17.
 */

public class ManagerSoundPlayer implements ControlPlayer {

    private static final ManagerSoundPlayer ourInstance = new ManagerSoundPlayer();

    private int lastClicedPosition = 0;

    public static ManagerSoundPlayer getInstance() {
        return ourInstance;
    }


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
        context.startService(intent);
    }

    public void selectAndPlaySound(Context context) {
        Intent intent = new Intent(context, MediaPlayService.class);
        //intent.putExtra(MediaPlayService.DataSourceController.KEY, MediaPlayService.DataSourceController.LOAD);
        intent.putExtra(MediaPlayService.PlayController.KEY, MediaPlayService.PlayController.PLAY);
        context.startService(intent);
    }


    public void initSounds(Context context, ArrayList<Sound> sounds) {
        Log.d("position", "manager: " + sounds);

//        for (Sound sound : sounds) {
//           //  Log.d("position", "forr: " + sound);
//                App.getInstance().getContentResolver().insert(DatabaseContract.Sounds.CONTENT_URI, ConvertHelper.createContentValue(sound));
//
//        }

        ContentValues[] contentValues = ConvertHelper.createContentValues(sounds);
        App.getInstance().getContentResolver().bulkInsert(DatabaseContract.Sounds.CONTENT_URI, contentValues);

        Intent intent = new Intent(context, MediaPlayService.class);
        intent.putExtra(MediaPlayService.DataSourceController.KEY, MediaPlayService.DataSourceController.LOAD);
        context.startService(intent);

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

    public void deleteTemporarySound() {
        Intent intent = new Intent(App.getInstance(), MediaPlayService.class);
        intent.putExtra(MediaPlayService.DataSourceController.KEY, MediaPlayService.DataSourceController.CLEAR_LIST_IN_DATABASE);
        App.getInstance().startService(intent);
    }

    public void updateViewPlayer() {
        Intent intent = new Intent(App.getInstance(), MediaPlayService.class);
        intent.putExtra(MediaPlayService.DataSourceController.KEY, MediaPlayService.DataSourceController.UPDATE_VIEW_PLAYER);
        App.getInstance().startService(intent);


    }


}
