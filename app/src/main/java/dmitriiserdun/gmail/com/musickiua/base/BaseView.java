package dmitriiserdun.gmail.com.musickiua.base;

/**
 * Created by dmitro on 31.10.17.
 */

public interface BaseView<T extends BasePresenter> {
    void setPresenter(T presenter);

}
