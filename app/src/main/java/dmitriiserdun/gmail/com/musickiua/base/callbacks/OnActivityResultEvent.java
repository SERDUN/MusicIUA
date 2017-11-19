package dmitriiserdun.gmail.com.musickiua.base.callbacks;

import android.content.Intent;

/**
 * Created by dmitro on 31.10.17.
 */

public class OnActivityResultEvent {
    public int requestCode,  resultCode;
    public Intent data;

    public OnActivityResultEvent(int requestCode, int resultCode, Intent data) {
        this.requestCode = requestCode;
        this.resultCode = resultCode;
        this.data = data;
    }

}
