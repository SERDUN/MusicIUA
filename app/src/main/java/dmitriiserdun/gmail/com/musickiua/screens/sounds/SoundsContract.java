package dmitriiserdun.gmail.com.musickiua.screens.sounds;


import android.content.Context;

import java.util.ArrayList;

import dmitriiserdun.gmail.com.musickiua.base.BasePresenter;
import dmitriiserdun.gmail.com.musickiua.base.BaseView;
import dmitriiserdun.gmail.com.musickiua.model.Playlist;
import dmitriiserdun.gmail.com.musickiua.model.Sound;
import rx.functions.Action1;

/**
 * Created by dmitro on 31.10.17.
 */

public class SoundsContract {
    interface Presenter extends BasePresenter {
        void initCallbacks();
    }

    interface View extends BaseView<Presenter> {

        public void addPlayListsInList(ArrayList<Sound> sounds);

        public void showMessage(int id);

        public Context getContext();

        public void onClickListener(Action1<String> action0);




    }

}
