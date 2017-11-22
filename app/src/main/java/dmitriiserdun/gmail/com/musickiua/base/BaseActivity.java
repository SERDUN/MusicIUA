package dmitriiserdun.gmail.com.musickiua.base;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.squareup.otto.Bus;

import dmitriiserdun.gmail.com.musickiua.R;
import dmitriiserdun.gmail.com.musickiua.base.callbacks.MenuCreatedCallback;

/**
 * Created by dmitro on 31.10.17.
 */

public class BaseActivity extends AppCompatActivity {
    protected final String TAG = getClass().getSimpleName();
    private Bus bus;
    private static final int TIME_INTERVAL = 1600;
    private boolean doubleBackToExitPressedOnce = false;



    //if menu !=1 then will be used native menu res/menu/name
    private MenuCreatedCallback menuCreatedCallback;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bus = new Bus();

    }



    @Override
    protected void onResume() {
        super.onResume();
        App.getInstance().setCurrentActivity(this);
    }


    @Override
    protected void onPause() {
        super.onPause();
        clearReferences();
    }

    @Override
    protected void onDestroy() {
        clearReferences();
        super.onDestroy();
    }

    public Bus getBus() {
        return bus;
    }

    public void hideKeyboard() {
        try {
            IBinder windowToken = getWindow().getDecorView().getRootView().getWindowToken();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(windowToken, 0);
        } catch (Exception e) {
        }
    }

    @Override
    public void onBackPressed() {

        navigateBack();
    }

    private void clearReferences() {
        BaseActivity currActivity = App.getInstance().getCurrentActivity();
        if (this.equals(currActivity))
            App.getInstance().setCurrentActivity(null);
    }

    private void navigateBack() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.getBackStackEntryCount() > 0) {

            FragmentManager.BackStackEntry backEntry = fragmentManager.getBackStackEntryAt(fragmentManager.getBackStackEntryCount() - 1);
            String fragmentName = backEntry.getName();
            fragmentManager.popBackStackImmediate(fragmentName, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        } else {
            tryExitActivity();
        }
    }

    private void tryExitActivity() {
        hideKeyboard();

        if (isTaskRoot()) {
            if (doubleBackToExitPressedOnce) {
                finish();
                return;
            }

            doubleBackToExitPressedOnce = true;
            //    UiPopupHelper.showCustomToast(this, this.getString(R.string.message_exit_from_app), Gravity.BOTTOM);
            Toast.makeText(this, this.getString(R.string.message_exit_from_app), Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }

            }, TIME_INTERVAL);
        } else {
            finish();
        }
    }

    public void addFragment(Fragment fragment, String fragmentKey) {
        if (getSupportFragmentManager().findFragmentByTag(fragmentKey) == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, fragment, fragmentKey)
                    .commit();
        }
    }
    public Toolbar getActivityToolbar() {
        return (Toolbar) findViewById(R.id.toolbar);
    }

}
