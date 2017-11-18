package dmitriiserdun.gmail.com.musickiua.screens.sounds;

import android.os.Bundle;
import android.view.View;

import dmitriiserdun.gmail.com.musickiua.R;
import dmitriiserdun.gmail.com.musickiua.base.BaseActivity;
import dmitriiserdun.gmail.com.musickiua.base.Const;

public class SoundsActivity extends BaseActivity {
    private SoundsContract.View view;
    private SoundsContract.Presenter  presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sounds);

        View root = findViewById(android.R.id.content);
        view = new SoundsView(root, this);
        presenter = new SoundsPresenter(this, view,getIntent().getStringExtra(Const.CURRENT_ALBUM_ID));
    }
}
