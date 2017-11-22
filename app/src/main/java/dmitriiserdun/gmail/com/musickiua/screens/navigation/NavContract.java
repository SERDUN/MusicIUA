package dmitriiserdun.gmail.com.musickiua.screens.navigation;

import android.content.Context;

import dmitriiserdun.gmail.com.musickiua.base.BasePresenter;
import dmitriiserdun.gmail.com.musickiua.base.BaseView;

/**
 * Created by dmitro on 19.11.17.
 */

public class NavContract {
    interface Presenter extends BasePresenter {
        void initCallbacks();
    }

    interface View extends BaseView<Presenter> {


        public void showMessage(int id);

        public Context getContext();


    }
}
