package dmitriiserdun.gmail.com.musickiua.screens.top_songs;

import dmitriiserdun.gmail.com.musickiua.base.BaseFragment;
import dmitriiserdun.gmail.com.musickiua.screens.playList.PlayListContract;

/**
 * Created by dmitro on 19.11.17.
 */

public class TopSongsPresenter implements TopSongsContract.Presenter {

    private TopSongsContract.View view;
    private BaseFragment baseFragment;

    public TopSongsPresenter(TopSongsContract.View view, BaseFragment baseFragment) {
        this.view = view;
        this.baseFragment = baseFragment;
    }

    @Override
    public void start() {

    }

    @Override
    public void initCallbacks() {

    }
}
