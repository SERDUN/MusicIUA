package dmitriiserdun.gmail.com.musickiua.screens.sounds;

import android.util.Log;

import com.orhanobut.hawk.Hawk;

import java.util.ArrayList;
import java.util.List;

import dmitriiserdun.gmail.com.musickiua.base.BaseActivity;
import dmitriiserdun.gmail.com.musickiua.base.Const;
import dmitriiserdun.gmail.com.musickiua.base.player.CustomMediaPlayer;
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
    private ArrayList<Sound> soundsList;
    CustomMediaPlayer customMediaPlayer;

    public SoundsPresenter(BaseActivity baseActivity, final SoundsContract.View view, String stringExtra) {
        userId = Hawk.get(Const.USER_ID);

        this.view = view;
        this.baseActivity = baseActivity;
        soundManagerRepository = SoundManagerRepository.getInstance(RemoteSoundRepository.getInstance());
        album_id = stringExtra;
        customMediaPlayer = CustomMediaPlayer.getInstance();
        loadSounds();
        initAction();

    }

    private void initAction() {
        view.onClickPlay().subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                view.morphPlayPause();
                startPlayAllSound(1);
            }
        });
    }

    private void startPlayAllSound(final int i) {
        // soundManagerRepository.getSounds(userId, album_id).subscribe(new Action1<List<Sound>>() {
//            @Override
//            public void call(List<Sound> sounds) {
//                //view.addPlayListsInList((ArrayList<Sound>) sounds);
        customMediaPlayer.playSounds(soundsList, i);

        //    }
        // });

    }

    private void loadSounds() {

        soundManagerRepository.getSounds(userId, album_id).subscribe(new Action1<List<Sound>>() {
            @Override
            public void call(List<Sound> sounds) {
                soundsList = (ArrayList<Sound>) sounds;
                view.addPlayListsInList((ArrayList<Sound>) sounds);
            }
        });


        view.onClickListener(new Action2<Sound, Integer>() {
            @Override
            public void call(Sound sound, Integer integer) {
                customMediaPlayer.setAction1(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        view.setMaxProgress(integer);

                    }
                });

                customMediaPlayer.getTime().subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        view.setProgress(integer);
                        Log.d("sds", "call: ");
                    }
                });


                customMediaPlayer.playSounds(soundsList, integer);


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

