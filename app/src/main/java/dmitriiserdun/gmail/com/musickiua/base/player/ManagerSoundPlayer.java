package dmitriiserdun.gmail.com.musickiua.base.player;

import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;

import dmitriiserdun.gmail.com.musickiua.base.App;
import dmitriiserdun.gmail.com.musickiua.model.Sound;
import dmitriiserdun.gmail.com.musickiua.services.MediaPlayService;
import dmitriiserdun.gmail.com.musickiua.storage.provider.ContractClass;
import dmitriiserdun.gmail.com.musickiua.storage.provider.ConvertHelper;
import rx.functions.Action1;

/**
 * Created by dmitro on 21.11.17.
 */

public class ManagerSoundPlayer implements ControlPlayer {

    private static final ManagerSoundPlayer ourInstance = new ManagerSoundPlayer();

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
        for (Sound sound : sounds)
            App.getInstance().getContentResolver().insert(ContractClass.Sounds.CONTENT_URI, ConvertHelper.createContentValues(sound));

        Intent intent = new Intent(context, MediaPlayService.class);
        intent.putExtra(MediaPlayService.DataSourceController.KEY, MediaPlayService.DataSourceController.LOAD);
        context.startService(intent);

    }


    @Override
    public void startOrPause() {
        // soundPlayer.play();
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
        App.getInstance().startService(intent);    }

    @Override
    public void isRepeat(boolean repeat) {

    }


}
