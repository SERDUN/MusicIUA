package dmitriiserdun.gmail.com.musickiua.screens.login;


import android.content.Context;

import dmitriiserdun.gmail.com.musickiua.base.BasePresenter;
import dmitriiserdun.gmail.com.musickiua.base.BaseView;

/**
 * Created by dmitro on 31.10.17.
 */

public class LoginContract {

    interface WelcomePresenter extends BasePresenter {
        void initCallbacks();
    }

    interface WelcomeView extends BaseView<WelcomePresenter> {
        public void showMainLoader(boolean isShow);

        public void showUI(boolean isShow);

        String getLogin();

        String getPassword();


        void showMessage(int id);

        rx.Observable<Void> onLogin();

        Context getContext();


    }

}
