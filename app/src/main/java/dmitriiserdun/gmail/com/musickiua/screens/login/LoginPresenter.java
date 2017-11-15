package dmitriiserdun.gmail.com.musickiua.screens.login;

import android.content.Intent;
import android.util.Log;


import com.orhanobut.hawk.Hawk;

import java.io.IOException;

import java.util.HashMap;


import dmitriiserdun.gmail.com.musickiua.R;
import dmitriiserdun.gmail.com.musickiua.base.BaseActivity;
import dmitriiserdun.gmail.com.musickiua.api.RetrofitFactory;
import dmitriiserdun.gmail.com.musickiua.base.Const;
import dmitriiserdun.gmail.com.musickiua.model.User;
import dmitriiserdun.gmail.com.musickiua.repository.SoundManagerRepository;
import dmitriiserdun.gmail.com.musickiua.repository.remote.RemoteSoundRepository;
import dmitriiserdun.gmail.com.musickiua.screens.playList.PlayListActivity;
import okhttp3.ResponseBody;
import retrofit2.Response;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by dmitro on 31.10.17.
 */

public class LoginPresenter implements LoginContract.WelcomePresenter {
    private LoginContract.WelcomeView view;
    private BaseActivity baseActivity;
    private SoundManagerRepository soundManagerRepository;

    public LoginPresenter(BaseActivity baseActivity, LoginContract.WelcomeView view) {
        this.view = view;
        this.baseActivity = baseActivity;
        soundManagerRepository = SoundManagerRepository.getInstance(RemoteSoundRepository.getInstance());
        initListener();
    }

    private void initListener() {
        view.onLogin().subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                try {
                    login();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void login() throws IOException {
        soundManagerRepository.login("serdun", "2856413razs").subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer userId) {
                Hawk.put(Const.USER_ID, userId);

                baseActivity.startActivity(new Intent(baseActivity.getApplication(), PlayListActivity.class));

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

