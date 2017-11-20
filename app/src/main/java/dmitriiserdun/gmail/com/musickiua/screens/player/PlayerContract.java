package dmitriiserdun.gmail.com.musickiua.screens.player;

import android.content.Context;

import java.util.ArrayList;

import dmitriiserdun.gmail.com.musickiua.base.BasePresenter;
import dmitriiserdun.gmail.com.musickiua.base.BaseView;
import dmitriiserdun.gmail.com.musickiua.model.Sound;
import dmitriiserdun.gmail.com.musickiua.screens.top_songs.TopSongsContract;
import rx.Observable;

/**
 * Created by dmitro on 20.11.17.
 */

public class PlayerContract {
    interface Presenter extends BasePresenter {
        void initCallbacks();
    }

    interface View extends BaseView<Presenter> {


        public void showMessage(int id);

        public Context getContext();


    }
}
