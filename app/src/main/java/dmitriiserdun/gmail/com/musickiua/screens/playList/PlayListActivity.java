package dmitriiserdun.gmail.com.musickiua.screens.playList;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import dmitriiserdun.gmail.com.musickiua.R;
import dmitriiserdun.gmail.com.musickiua.base.BaseActivity;
import dmitriiserdun.gmail.com.musickiua.screens.login.LoginContract;
import dmitriiserdun.gmail.com.musickiua.screens.login.LoginPresenter;
import dmitriiserdun.gmail.com.musickiua.screens.login.LoginView;

public class PlayListActivity extends BaseActivity {
    private PlayListContract.View view;
    private PlayListContract.Presenter  presenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_list);

        View root = findViewById(android.R.id.content);
        view = new PlayListView(root, this);
        presenter = new PlayListPresenter(this, view);

    }
}
