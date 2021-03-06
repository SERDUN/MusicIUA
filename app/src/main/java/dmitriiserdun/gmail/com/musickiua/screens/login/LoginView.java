package dmitriiserdun.gmail.com.musickiua.screens.login;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.jakewharton.rxbinding.view.RxView;

import dmitriiserdun.gmail.com.musickiua.R;
import dmitriiserdun.gmail.com.musickiua.base.BaseActivity;

/**
 * Created by dmitro on 31.10.17.
 */

public class LoginView implements LoginContract.WelcomeView {

    private View root;
    private BaseActivity activity;
    private EditText editTextLogin;
    private EditText editTextPassword;
    private Button login;
    private ProgressBar progressBar;
    private ViewGroup viewGroup;


    public LoginView(View root, BaseActivity activity) {
        this.root = root;
        this.activity = activity;
        initView();
    }

    private void initView() {
        editTextLogin = root.findViewById(R.id.editTextLogin);
        editTextPassword = root.findViewById(R.id.editTextPassword);
        login = root.findViewById(R.id.buttonLogin);
        progressBar = root.findViewById(R.id.progressBar);
        viewGroup = root.findViewById(R.id.content);

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

    @Override
    public void showMainLoader(boolean show) {
        if (show) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void showUI(boolean show) {
        if (show) {
            viewGroup.setVisibility(View.VISIBLE);
        } else {
            viewGroup.setVisibility(View.GONE);
        }
    }
}
