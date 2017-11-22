package dmitriiserdun.gmail.com.musickiua.screens.sounds;

import android.util.Log;

import com.orhanobut.hawk.Hawk;

import java.util.ArrayList;
import java.util.List;

import dmitriiserdun.gmail.com.musickiua.base.BaseActivity;
import dmitriiserdun.gmail.com.musickiua.base.Const;
import dmitriiserdun.gmail.com.musickiua.base.player.SoundPlayer;
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
    private boolean playerRunning = false;
    private boolean firstOpened = true;


   // private SoundPlayer soundPlayer;
    private SoundPlayer soundPlayer;

    public SoundsPresenter(BaseActivity baseActivity, final SoundsContract.View view, String stringExtra) {
        userId = Hawk.get(Const.USER_ID);
        soundPlayer = SoundPlayer.getInstance();
        initSoundManagerListener();
        this.view = view;
        this.baseActivity = baseActivity;
        soundManagerRepository = SoundManagerRepository.getInstance(RemoteSoundRepository.getInstance());
        album_id = stringExtra;
        loadSounds();
        initAction();
    }


    private void initAction() {
        view.onClickPlay().subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if (!soundPlayer.isPlayingSound() && firstOpened) {
                    firstOpened = false;
                    soundPlayer.play(soundsList);
                    view.morphPause();
                }
                if (soundPlayer.isPlayingSound()) {
                    soundPlayer.pause();
                    view.morphPlay();
                } else if (soundPlayer.isPaused()) {
                    soundPlayer.resume();
                    view.morphPause();
                }
            }
        });

        view.setOnItemListListener(new Action2<Sound, Integer>() {
            @Override
            public void call(Sound sound, Integer integer) {
                view.setColorItem(sound.hashCode());
                if (soundPlayer.isPlayingSound() && soundPlayer.getCurrentTimePosition() == integer) {
                    soundPlayer.pause();
                    view.setColorItem(sound.hashCode());
                    view.morphPlay();
                } else if (soundPlayer.isPaused() && soundPlayer.getCurrentTimePosition() == integer) {
                    soundPlayer.resume();
                    view.morphPause();
                } else {
                    soundPlayer.play(soundsList, integer);
                    view.setColorItem(sound.hashCode());

                    view.morphPause();
                }

            }
        });

        view.onClickNext().subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                view.setColorItem(soundsList.get(soundPlayer.getCurrentTimePosition() + 1).hashCode());
                soundPlayer.nextSound();
            }
        });

        view.onClickBack().subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                view.setColorItem(soundsList.get(soundPlayer.getCurrentTimePosition() - 1).hashCode());
                soundPlayer.backSound();

            }
        });
    }

    public void initSoundManagerListener() {
//        soundPlayer.setDataSound(new Action1<Integer>() {
//            @Override
//            public void call(Integer maxPosition) {
//                view.setMaxProgress(maxPosition);
//            }
//        });


    }


    private void loadSounds() {
        soundManagerRepository.getSounds(userId, album_id).subscribe(new Action1<List<Sound>>() {
            @Override
            public void call(List<Sound> sounds) {
                soundsList = (ArrayList<Sound>) sounds;
                view.addPlayListsInList((ArrayList<Sound>) sounds);
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

