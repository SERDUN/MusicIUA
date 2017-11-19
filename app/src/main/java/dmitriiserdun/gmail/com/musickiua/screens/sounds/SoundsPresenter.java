package dmitriiserdun.gmail.com.musickiua.screens.sounds;

import android.util.Log;

import com.orhanobut.hawk.Hawk;

import java.util.ArrayList;
import java.util.List;

import dmitriiserdun.gmail.com.musickiua.base.BaseActivity;
import dmitriiserdun.gmail.com.musickiua.base.Const;
import dmitriiserdun.gmail.com.musickiua.base.player.PlayingSoundManager;
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


    private SoundPlayer soundPlayer;
    private PlayingSoundManager playingSoundManager;

    public SoundsPresenter(BaseActivity baseActivity, final SoundsContract.View view, String stringExtra) {
        userId = Hawk.get(Const.USER_ID);
        playingSoundManager = PlayingSoundManager.getInstance();
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
                if (!playingSoundManager.isPlayingSound() && firstOpened) {
                    firstOpened = false;
                    playingSoundManager.play(soundsList);
                    view.morphPause();
                }
                if (playingSoundManager.isPlayingSound()) {
                    playingSoundManager.pause();
                    view.morphPlay();
                } else if (playingSoundManager.isPaused()) {
                    playingSoundManager.resume();
                    view.morphPause();
                }
            }
        });

        view.setOnItemListListener(new Action2<Sound, Integer>() {
            @Override
            public void call(Sound sound, Integer integer) {
                view.setColorItem(sound.hashCode());
                if (playingSoundManager.isPlayingSound() && playingSoundManager.getCurrentPosition() == integer) {
                    playingSoundManager.pause();
                    view.setColorItem(sound.hashCode());
                    view.morphPlay();
                } else if (playingSoundManager.isPaused() && playingSoundManager.getCurrentPosition() == integer) {
                    playingSoundManager.resume();
                    view.morphPause();
                } else {
                    playingSoundManager.play(soundsList, integer);
                    view.setColorItem(sound.hashCode());

                    view.morphPause();
                }

            }
        });

        view.onClickNext().subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                view.setColorItem(soundsList.get(playingSoundManager.getCurrentPosition() + 1).hashCode());
                playingSoundManager.nextSound();
            }
        });

        view.onClickBack().subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                view.setColorItem(soundsList.get(playingSoundManager.getCurrentPosition() - 1).hashCode());
                playingSoundManager.backSound();

            }
        });
    }

    public void initSoundManagerListener() {
        playingSoundManager.setMaxTimePosition(new Action1<Integer>() {
            @Override
            public void call(Integer maxPosition) {
                view.setMaxProgress(maxPosition);
            }
        });

        playingSoundManager.setCurrentTimePosition(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                view.setProgress(integer);
            }
        });
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

