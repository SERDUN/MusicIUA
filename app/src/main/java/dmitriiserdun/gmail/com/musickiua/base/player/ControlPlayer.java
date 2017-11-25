package dmitriiserdun.gmail.com.musickiua.base.player;

/**
 * Created by dmitro on 21.11.17.
 */

public interface ControlPlayer {
     void startOrPause();

     void stop();

     void resume();


     void next();

     void back();

     void isRepeat(boolean repeat);


}
