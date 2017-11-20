package dmitriiserdun.gmail.com.musickiua.screens.top_songs;

import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import dmitriiserdun.gmail.com.musickiua.base.BaseFragment;
import dmitriiserdun.gmail.com.musickiua.model.FoundSounds;
import dmitriiserdun.gmail.com.musickiua.repository.SoundManagerRepository;
import dmitriiserdun.gmail.com.musickiua.repository.remote.RemoteSoundRepository;
import dmitriiserdun.gmail.com.musickiua.screens.playList.PlayListContract;
import rx.functions.Action1;

import static android.content.ContentValues.TAG;

/**
 * Created by dmitro on 19.11.17.
 */

public class TopSongsPresenter implements TopSongsContract.Presenter {
    private SoundManagerRepository soundManagerRepository;

    private TopSongsContract.View view;
    private BaseFragment baseFragment;
    private String searchingKey = "";

    public TopSongsPresenter(final TopSongsContract.View view, BaseFragment baseFragment) {
        this.view = view;
        this.baseFragment = baseFragment;
        soundManagerRepository = SoundManagerRepository.getInstance(RemoteSoundRepository.getInstance());
        initCallbacks();

    }

    @Override
    public void start() {

    }

    @Override
    public void initCallbacks() {


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
        view.onClickFind().

                subscribe(new Action1<Void>() {
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
        Log.d(TAG, "findSound: ");
        try {

            soundManagerRepository.searchSounds(URLEncoder.encode(searchingKey, "windows-1251"), 0).subscribe(new Action1<FoundSounds>() {
                @Override
                public void call(FoundSounds foundSounds) {
                    view.addSoundsList(foundSounds.getSounds());
                    view.showMainLoader(false);
                    view.showUI(true);
                }
            });
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
