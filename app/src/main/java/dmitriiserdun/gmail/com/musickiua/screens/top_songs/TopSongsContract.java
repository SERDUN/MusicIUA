package dmitriiserdun.gmail.com.musickiua.screens.top_songs;

import android.content.Context;

import java.util.ArrayList;

import dmitriiserdun.gmail.com.musickiua.base.BasePresenter;
import dmitriiserdun.gmail.com.musickiua.base.BaseView;
import dmitriiserdun.gmail.com.musickiua.model.Playlist;
import dmitriiserdun.gmail.com.musickiua.screens.playList.PlayListContract;
import rx.functions.Action1;

/**
 * Created by dmitro on 19.11.17.
 */

public class TopSongsContract {

    interface Presenter extends BasePresenter {
        void initCallbacks();
    }

    interface View extends BaseView<Presenter> {


        public void showMessage(int id);

        public Context getContext();



    }
}
