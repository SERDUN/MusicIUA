package dmitriiserdun.gmail.com.musickiua.screens.login;

import android.content.Intent;
import android.util.Log;

import com.orhanobut.hawk.Hawk;

import java.io.IOException;

import dmitriiserdun.gmail.com.musickiua.base.BaseActivity;
import dmitriiserdun.gmail.com.musickiua.base.Const;
import dmitriiserdun.gmail.com.musickiua.repository.SoundManagerRepository;
import dmitriiserdun.gmail.com.musickiua.repository.remote.RemoteSoundRepository;
import dmitriiserdun.gmail.com.musickiua.screens.navigation.NavActivity;
import rx.functions.Action1;

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
        trySignIn();
    }

    private void trySignIn() {
        Integer userId = Hawk.get(Const.USER_ID);
        if (userId!=null) {
            this.baseActivity.startActivity(new Intent(baseActivity.getApplication(), NavActivity.class));
        } else {
            soundManagerRepository = SoundManagerRepository.getInstance(RemoteSoundRepository.getInstance());
            initListener();
        }

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

                baseActivity.startActivity(new Intent(baseActivity.getApplication(), NavActivity.class));

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

