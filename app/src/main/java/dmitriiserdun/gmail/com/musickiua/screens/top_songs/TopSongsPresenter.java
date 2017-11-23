package dmitriiserdun.gmail.com.musickiua.screens.top_songs;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import dmitriiserdun.gmail.com.musickiua.base.BaseFragment;
import dmitriiserdun.gmail.com.musickiua.base.player.ManagerSoundPlayer;
import dmitriiserdun.gmail.com.musickiua.model.FoundSounds;
import dmitriiserdun.gmail.com.musickiua.model.Sound;
import dmitriiserdun.gmail.com.musickiua.repository.SoundManagerRepository;
import dmitriiserdun.gmail.com.musickiua.repository.remote.RemoteSoundRepository;
import rx.functions.Action1;
import rx.functions.Action2;

/**
 * Created by dmitro on 19.11.17.
 */

public class TopSongsPresenter implements TopSongsContract.Presenter {
    private SoundManagerRepository soundManagerRepository;
    private ManagerSoundPlayer managerSoundPlayer;
    private TopSongsContract.View view;
    private BaseFragment baseFragment;
    private String searchingKey = "";


    public TopSongsPresenter(final TopSongsContract.View view, BaseFragment baseFragment) {
        this.view = view;
        this.baseFragment = baseFragment;
        init();
        initCallbacks();
        initPlayer();
        getLoadTopSounds();

    }

    private void init() {
        soundManagerRepository = SoundManagerRepository.getInstance(RemoteSoundRepository.getInstance());
        view.showPlayer(false);
    }


    public void getLoadTopSounds() {
        managerSoundPlayer.deleteTemporarySound();
        view.showMainLoader(true);
        view.showUI(false);
        soundManagerRepository.searchSounds("", 0).subscribe(new Action1<FoundSounds>() {
            @Override
            public void call(FoundSounds foundSounds) {
                view.addSoundsList(foundSounds.getSounds());
                view.showMainLoader(false);
                view.showUI(true);
                managerSoundPlayer.initSounds(view.getContext(), foundSounds.getSounds());
            }
        });
    }


    private void initPlayer() {
        managerSoundPlayer = ManagerSoundPlayer.getInstance();
        view.initControllerWithPlayer(managerSoundPlayer.getController());
        managerSoundPlayer.updateViewPlayer();
    }

    @Override
    public void start() {

    }

    @Override
    public void initCallbacks() {


        view.setOnItemListListener(new Action2<Sound, Integer>() {
            @Override
            public void call(Sound sound, Integer position) {
                view.showPlayer(true);
                managerSoundPlayer.selectAndPlaySound(view.getContext(), position);

            }
        });

        view.getSearchText().subscribe(new Action1<CharSequence>() {
            @Override
            public void call(CharSequence charSequence) {
                if (charSequence.length() > 1) {
                    view.showButtonFind();
                    searchingKey = charSequence.toString();
                } else {
                    view.hideButtonFind();
                }
            }

        });
        view.onClickFind().subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                view.hideButtonFind();
                view.hideSearchView();
                view.showMainLoader(true);
                view.showUI(false);
                findSound();
            }
        });
    }

    public void findSound() {
        managerSoundPlayer.deleteTemporarySound();
        try {
            soundManagerRepository.searchSounds(URLEncoder.encode(searchingKey, "windows-1251"), 0).subscribe(new Action1<FoundSounds>() {
                @Override
                public void call(FoundSounds foundSounds) {
                    view.addSoundsList(foundSounds.getSounds());
                    view.showMainLoader(false);
                    view.showUI(true);
                    managerSoundPlayer.initSounds(view.getContext(), foundSounds.getSounds());
                }
            });
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
