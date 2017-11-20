package dmitriiserdun.gmail.com.musickiua.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;

import com.squareup.otto.Bus;

import dmitriiserdun.gmail.com.musickiua.R;

/**
 * Created by dmitro on 31.10.17.
 */

public class BaseFragment extends Fragment {
    protected Bus bus;
    public boolean stopped;
    public boolean paused;
    public boolean destroyed;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        bus = new Bus();
    }

    @Override
    public void onStart() {
        stopped = false;
        super.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();
        stopped = true;
    }


    @Override
    public void onDestroy() {
        destroyed = true;

        super.onDestroy();
    }

    @Override
    public void onResume() {
        paused = false;
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        paused = true;
    }

    public Bus getBus() {
        return bus;
    }


}
