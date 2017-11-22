package dmitriiserdun.gmail.com.musickiua.screens.top_songs;

import android.content.Context;

import java.util.ArrayList;

import dmitriiserdun.gmail.com.musickiua.base.BasePresenter;
import dmitriiserdun.gmail.com.musickiua.base.BaseView;
import dmitriiserdun.gmail.com.musickiua.base.player.ControlPlayer;
import dmitriiserdun.gmail.com.musickiua.base.player.ManagerSoundPlayer;
import dmitriiserdun.gmail.com.musickiua.model.Playlist;
import dmitriiserdun.gmail.com.musickiua.model.Sound;
import dmitriiserdun.gmail.com.musickiua.screens.playList.PlayListContract;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Action2;

/**
 * Created by dmitro on 19.11.17.
 */

public class TopSongsContract {

    interface Presenter extends BasePresenter {
        void initCallbacks();
    }

    interface View extends BaseView<Presenter> {


        public void addSoundsList(ArrayList<Sound> sounds);

        public void showMessage(int id);

        public Context getContext();

        public Observable<CharSequence> getSearchText();

        public void initControllerWithPlayer(ControlPlayer controlPlayer);

        rx.Observable<Void> onClickFind();

        public void showButtonFind();

        public void hideButtonFind();

        public void hideSearchView();

        public void showMainLoader(boolean isShow);

        public void showUI(boolean isShow);

        public void showPlayer(boolean isShow);

        public void setOnItemListListener(Action2<Sound, Integer> action0);


    }
}
