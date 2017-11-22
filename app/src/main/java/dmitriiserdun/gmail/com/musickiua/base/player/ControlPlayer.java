package dmitriiserdun.gmail.com.musickiua.base.player;

/**
 * Created by dmitro on 21.11.17.
 */

public interface ControlPlayer {
    public void startOrPause();

    public void stop();

    public void resume();


    public void next();

    public void back();

    public void isRepeat(boolean repeat);


}
