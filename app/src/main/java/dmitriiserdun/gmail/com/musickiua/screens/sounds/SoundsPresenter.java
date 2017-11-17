package dmitriiserdun.gmail.com.musickiua.screens.sounds;

import android.media.MediaPlayer;
import android.util.Log;

import com.danikula.videocache.HttpProxyCacheServer;
import com.orhanobut.hawk.Hawk;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import dmitriiserdun.gmail.com.musickiua.base.App;
import dmitriiserdun.gmail.com.musickiua.base.BaseActivity;
import dmitriiserdun.gmail.com.musickiua.base.Const;
import dmitriiserdun.gmail.com.musickiua.model.Sound;
import dmitriiserdun.gmail.com.musickiua.repository.SoundManagerRepository;
import dmitriiserdun.gmail.com.musickiua.repository.remote.RemoteSoundRepository;
import okhttp3.ResponseBody;
import rx.functions.Action1;

/**
 * Created by dmitro on 31.10.17.
 */

public class SoundsPresenter implements SoundsContract.Presenter {
    private SoundsContract.View view;
    private BaseActivity baseActivity;
    private SoundManagerRepository soundManagerRepository;
    private String album_id;

    public SoundsPresenter(BaseActivity baseActivity, final SoundsContract.View view, String stringExtra) {
        this.view = view;
        this.baseActivity = baseActivity;
        soundManagerRepository = SoundManagerRepository.getInstance(RemoteSoundRepository.getInstance());
        album_id = stringExtra;
        loadSounds();
    }

    private void loadSounds() {
        final Integer userId = Hawk.get(Const.USER_ID);

        soundManagerRepository.getSounds(userId, album_id).subscribe(new Action1<List<Sound>>() {
            @Override
            public void call(List<Sound> sounds) {
                view.addPlayListsInList((ArrayList<Sound>) sounds);
            }
        });
        view.onClickListener(new Action1<String>() {
            @Override
            public void call(String s) {
                String url = "http://" + s;

                File file = new File("storage/emulated/0");


                HttpProxyCacheServer proxy = App.getProxy(baseActivity.getBaseContext());
                String proxyUrl = proxy.getProxyUrl(url);
               // view.setVideoPath(proxyUrl);


                final MediaPlayer    mMediaPlayer = new MediaPlayer();
//
                    try {
                        mMediaPlayer.setDataSource(proxyUrl);
                        mMediaPlayer.prepare();
                        mMediaPlayer.start();
//                        mMediaPlayer.prepareAsync();
//


//                soundManagerRepository.getSounds(url).subscribe(new Action1<ResponseBody>() {
//                    @Override
//                    public void call(final ResponseBody streamSound) {
//
//                    }
//                });

            } catch (IOException e) {
                        e.printStackTrace();
                    }}
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

