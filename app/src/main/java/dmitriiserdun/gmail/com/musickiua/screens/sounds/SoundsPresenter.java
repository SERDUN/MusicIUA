package dmitriiserdun.gmail.com.musickiua.screens.sounds;

import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;

import com.orhanobut.hawk.Hawk;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import dmitriiserdun.gmail.com.musickiua.base.BaseActivity;
import dmitriiserdun.gmail.com.musickiua.base.Const;
import dmitriiserdun.gmail.com.musickiua.base.MusicApp;
import dmitriiserdun.gmail.com.musickiua.model.Sound;
import dmitriiserdun.gmail.com.musickiua.repository.SoundManagerRepository;
import dmitriiserdun.gmail.com.musickiua.repository.remote.RemoteSoundRepository;
import okhttp3.ResponseBody;
import rx.functions.Action1;

import static android.content.ContentValues.TAG;

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

                soundManagerRepository.getSounds(url).subscribe(new Action1<ResponseBody>() {
                    @Override
                    public void call(ResponseBody streamSound) {

                        saveToDisk(streamSound);


                    }
                });

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

    public void saveToDisk(ResponseBody body) {
        try {
            File file = new File("storage/emulated/0", "audio.mp3");

            String path = file.getAbsolutePath();
            InputStream is = null;
            OutputStream os = null;

            try {
                Log.d(TAG, "File Size=" + body.contentLength());


                is = body.byteStream();
                os = new FileOutputStream(file);


                byte data[] = new byte[1048576];
                int count;
                int progress = 0;
                while ((count = is.read(data)) != -1) {
                    os.write(data, 0, count);
                    progress += count;
                    Log.d(TAG, "Progress: " + progress + "/" + body.contentLength() + " >>>> " + (float) progress / body.contentLength());
                }

                os.flush();

                Log.d(TAG, "File saved successfully!");
                return;
            } catch (IOException e) {
                e.printStackTrace();
                Log.d(TAG, "Failed to save the file!");
                return;
            } finally {
                if (is != null) is.close();
                if (os != null) os.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG, "Failed to save the file!");
            return;
        }
    }

    private String getPackageName() {
        return "sound";
    }
}

