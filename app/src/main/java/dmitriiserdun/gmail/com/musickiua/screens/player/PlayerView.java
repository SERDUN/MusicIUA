package dmitriiserdun.gmail.com.musickiua.screens.player;

import android.content.Context;
import android.view.View;

import dmitriiserdun.gmail.com.musickiua.base.BaseFragment;
import dmitriiserdun.gmail.com.musickiua.screens.playList.PlayListContract;

/**
 * Created by dmitro on 20.11.17.
 */

public class PlayerView implements PlayerContract.View {
    private View root;
    private BaseFragment baseFragment;

    public PlayerView(View root, BaseFragment baseFragment) {
        this.root = root;
        this.baseFragment = baseFragment;
    }

    @Override
    public void setPresenter(PlayerContract.Presenter presenter) {

    }

    @Override
    public void showMessage(int id) {

    }

    @Override
    public Context getContext() {
        return null;
    }
}
