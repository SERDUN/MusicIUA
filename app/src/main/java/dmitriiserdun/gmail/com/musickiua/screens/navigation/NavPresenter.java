package dmitriiserdun.gmail.com.musickiua.screens.navigation;

import dmitriiserdun.gmail.com.musickiua.base.BaseActivity;

/**
 * Created by dmitro on 19.11.17.
 */

public class NavPresenter implements NavContract.Presenter {
    private NavContract.View view;
    private BaseActivity baseActivity;

    public NavPresenter(NavContract.View view, BaseActivity baseActivity) {
        this.view = view;
        this.baseActivity = baseActivity;
    }

    @Override
    public void start() {

    }

    @Override
    public void initCallbacks() {

    }
}
