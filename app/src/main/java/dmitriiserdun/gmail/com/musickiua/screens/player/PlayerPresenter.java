package dmitriiserdun.gmail.com.musickiua.screens.player;

import dmitriiserdun.gmail.com.musickiua.base.BaseFragment;

/**
 * Created by dmitro on 20.11.17.
 */

public class PlayerPresenter implements PlayerContract.Presenter {
    private BaseFragment baseFragment;
    private PlayerContract.View view;

    public PlayerPresenter(BaseFragment baseFragment, PlayerContract.View view) {
        this.baseFragment = baseFragment;
        this.view = view;
    }

    @Override
    public void start() {

    }

    @Override
    public void initCallbacks() {

    }
}
