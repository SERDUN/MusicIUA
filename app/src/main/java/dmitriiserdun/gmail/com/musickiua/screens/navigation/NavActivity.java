package dmitriiserdun.gmail.com.musickiua.screens.navigation;

import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import dmitriiserdun.gmail.com.musickiua.R;
import dmitriiserdun.gmail.com.musickiua.base.BaseActivity;

public class NavActivity extends BaseActivity {
    private NavContract.View view;
    private NavContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);


        View root = findViewById(android.R.id.content);
        view = new NavView(root, this);
        presenter = new NavPresenter(view, this);

    }



}
