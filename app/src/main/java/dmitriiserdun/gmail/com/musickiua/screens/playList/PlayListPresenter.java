package dmitriiserdun.gmail.com.musickiua.screens.playList;

import android.util.Log;

import com.orhanobut.hawk.Hawk;

import java.util.ArrayList;
import java.util.List;

import dmitriiserdun.gmail.com.musickiua.base.BaseActivity;
import dmitriiserdun.gmail.com.musickiua.base.Const;
import dmitriiserdun.gmail.com.musickiua.model.Playlist;
import dmitriiserdun.gmail.com.musickiua.model.Sound;
import dmitriiserdun.gmail.com.musickiua.repository.SoundManagerRepository;
import dmitriiserdun.gmail.com.musickiua.repository.remote.RemoteSoundRepository;
import rx.functions.Action0;
import rx.functions.Action1;

/**
 * Created by dmitro on 31.10.17.
 */

public class PlayListPresenter implements PlayListContract.Presenter {
    private PlayListContract.View view;
    private BaseActivity baseActivity;
    private SoundManagerRepository soundManagerRepository;

    public PlayListPresenter(BaseActivity baseActivity, final PlayListContract.View view) {
        this.view = view;
        this.baseActivity = baseActivity;
        soundManagerRepository = SoundManagerRepository.getInstance(RemoteSoundRepository.getInstance());
        final Integer userId = Hawk.get(Const.USER_ID);
        soundManagerRepository.getPlaylists(userId).subscribe(new Action1<List<Playlist>>() {
            @Override
            public void call(List<Playlist> playlists) {
                soundManagerRepository.getSounds(userId,playlists.get(0).getId()).subscribe(new Action1<List<Sound>>() {
                    @Override
                    public void call(List<Sound> sounds) {
                        Log.d("dfdf", "call: ");
                    }
                });

//                view.addPlayListsInList((ArrayList<Playlist>) playlists);
            }
        });

        view.onClickListener(new Action0() {
            @Override
            public void call() {
               // view.showMessage(R.string.app_name);
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
}

