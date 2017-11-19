package dmitriiserdun.gmail.com.musickiua.screens.top_songs;

import android.content.Context;
import android.view.View;

import dmitriiserdun.gmail.com.musickiua.base.BaseFragment;

/**
 * Created by dmitro on 19.11.17.
 */

public class TopSongsView implements TopSongsContract.View {

    private View root;
    private BaseFragment baseFragment;

    public TopSongsView(View root, BaseFragment baseFragment) {
        this.root = root;
        this.baseFragment = baseFragment;
    }

    @Override
    public void setPresenter(TopSongsContract.Presenter presenter) {

    }

    @Override
    public void showMessage(int id) {

    }

    @Override
    public Context getContext() {
        return null;
    }
}
