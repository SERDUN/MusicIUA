package dmitriiserdun.gmail.com.musickiua.screens.login;

import android.os.Bundle;
import android.view.View;

import dmitriiserdun.gmail.com.musickiua.R;
import dmitriiserdun.gmail.com.musickiua.base.BaseActivity;


public class LoginActivity extends BaseActivity {
    private LoginContract.WelcomeView view;
    private LoginContract.WelcomePresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


         View root = findViewById(android.R.id.content);
        view = new LoginView(root, this);
        presenter = new LoginPresenter(this, view);


    }




}
