package dmitriiserdun.gmail.com.musickiua.screens.login;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.jakewharton.rxbinding.view.RxView;
import com.squareup.picasso.Picasso;

import dmitriiserdun.gmail.com.musickiua.R;
import dmitriiserdun.gmail.com.musickiua.base.BaseActivity;
import io.reactivex.Observable;

/**
 * Created by dmitro on 31.10.17.
 */

public class LoginView implements LoginContract.WelcomeView {

    private View root;
    private BaseActivity activity;
    private EditText editTextLogin;
    private EditText editTextPassword;
    private Button login;


    public LoginView(View root, BaseActivity activity) {
        this.root = root;
        this.activity = activity;
        initView();
    }

    private void initView() {
        editTextLogin = root.findViewById(R.id.editTextLogin);
        editTextPassword = root.findViewById(R.id.editTextPassword);
        login = root.findViewById(R.id.buttonLogin);

    }

    @Override
    public void setPresenter(LoginContract.WelcomePresenter presenter) {


    }

    @Override
    public String getLogin() {
        return editTextLogin.getText().toString();
    }

    @Override
    public String getPassword() {
        return editTextPassword.getText().toString();
    }


    @Override
    public void showMessage(int id) {
        Toast.makeText(activity, id, Toast.LENGTH_SHORT).show();
    }

    @Override
    public rx.Observable<Void> onLogin() {
        return RxView.clicks(login);
    }

    @Override
    public Context getContext() {
        return activity.getBaseContext();
    }


}
