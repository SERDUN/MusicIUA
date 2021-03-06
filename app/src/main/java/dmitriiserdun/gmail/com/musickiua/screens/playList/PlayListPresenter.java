package dmitriiserdun.gmail.com.musickiua.screens.playList;

import android.content.Intent;
import android.util.Log;

import com.orhanobut.hawk.Hawk;

import java.util.ArrayList;
import java.util.List;

import dmitriiserdun.gmail.com.musickiua.base.BaseFragment;
import dmitriiserdun.gmail.com.musickiua.base.Const;
import dmitriiserdun.gmail.com.musickiua.model.Playlist;
import dmitriiserdun.gmail.com.musickiua.repository.SoundManagerRepository;
import dmitriiserdun.gmail.com.musickiua.repository.remote.RemoteSoundRepository;
import dmitriiserdun.gmail.com.musickiua.screens.sounds.SoundsActivity;
import rx.functions.Action1;

/**
 * Created by dmitro on 31.10.17.
 */

public class PlayListPresenter implements PlayListContract.Presenter {
    private PlayListContract.View view;
    private BaseFragment baseFragment;
    private SoundManagerRepository soundManagerRepository;

    public PlayListPresenter(final BaseFragment baseFragment, final PlayListContract.View view) {
        this.view = view;
        this.baseFragment = baseFragment;

        soundManagerRepository = SoundManagerRepository.getInstance(RemoteSoundRepository.getInstance());

        final Integer userId = Hawk.get(Const.USER_ID);

        soundManagerRepository.getPlaylists(userId).subscribe(new Action1<List<Playlist>>() {
            @Override
            public void call(List<Playlist> playlists) {
                view.addPlayListsInList((ArrayList<Playlist>) playlists);
            }
        });

        view.onClickListener(new Action1<String>() {
            @Override
            public void call(String s) {
                Intent intent = new Intent(new Intent(baseFragment.getContext(), SoundsActivity.class));
                intent.putExtra(Const.CURRENT_ALBUM_ID, s);
                baseFragment.startActivity(intent);
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

