package dmitriiserdun.gmail.com.musickiua.screens.sounds;

import android.util.Log;

import com.orhanobut.hawk.Hawk;

import java.util.ArrayList;
import java.util.List;

import dmitriiserdun.gmail.com.musickiua.base.BaseActivity;
import dmitriiserdun.gmail.com.musickiua.base.Const;
import dmitriiserdun.gmail.com.musickiua.base.player.ManagerSoundPlayer;
import dmitriiserdun.gmail.com.musickiua.model.Sound;
import dmitriiserdun.gmail.com.musickiua.repository.SoundManagerRepository;
import dmitriiserdun.gmail.com.musickiua.repository.remote.RemoteSoundRepository;
import rx.functions.Action1;
import rx.functions.Action2;

/**
 * Created by dmitro on 31.10.17.
 */

public class SoundsPresenter implements SoundsContract.Presenter {
    private SoundsContract.View view;
    private BaseActivity baseActivity;
    private SoundManagerRepository soundManagerRepository;
    private String album_id;
    private Integer userId;
    private ManagerSoundPlayer managerSoundPlayer;


    public SoundsPresenter(BaseActivity baseActivity, final SoundsContract.View view, String stringExtra) {
        userId = Hawk.get(Const.USER_ID);
        initSoundManagerListener();
        this.view = view;
        this.baseActivity = baseActivity;
        soundManagerRepository = SoundManagerRepository.getInstance(RemoteSoundRepository.getInstance());
        album_id = stringExtra;
        managerSoundPlayer = ManagerSoundPlayer.getInstance();
        initPlayer();

        loadSounds();
        initAction();
    }

    private void initPlayer() {
        view.showPlayer(false);
        managerSoundPlayer = ManagerSoundPlayer.getInstance();
        view.initControllerWithPlayer(managerSoundPlayer.getController());
        managerSoundPlayer.updateViewPlayer();
        //   view.initControllerWithPlayer(managerSoundPlayer.getController());

    }

    private void initAction() {


        view.setOnItemListListener(new Action2<Sound, Integer>() {
            @Override
            public void call(Sound sound, Integer position) {
                managerSoundPlayer.selectAndPlaySound(view.getContext(), position);
                view.showPlayer(true);

            }
        });

    }

    public void initSoundManagerListener() {


    }


    private void loadSounds() {
        soundManagerRepository.getSounds(userId, album_id).subscribe(new Action1<List<Sound>>() {
            @Override
            public void call(List<Sound> sounds) {
                view.addPlayListsInList((ArrayList<Sound>) sounds);
                managerSoundPlayer.initSounds(view.getContext(), (ArrayList<Sound>) sounds);


            }
        });


    }


    @Override
    public void start() {
        Log.d("", "start: ");
    }

    @Override
    public void initCallbacks() {

    }


    private String getPackageName() {
        return "sound";
    }
}

