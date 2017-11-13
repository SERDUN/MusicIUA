package dmitriiserdun.gmail.com.musickiua.screens.login;

import android.util.Log;


import java.io.IOException;

import java.util.HashMap;


import dmitriiserdun.gmail.com.musickiua.R;
import dmitriiserdun.gmail.com.musickiua.base.BaseActivity;
import dmitriiserdun.gmail.com.musickiua.base.Const;
import dmitriiserdun.gmail.com.musickiua.services.RetrofitFactory;
import okhttp3.ResponseBody;
import retrofit2.Callback;
import retrofit2.Response;
import rx.functions.Action1;

/**
 * Created by dmitro on 31.10.17.
 */

public class LoginPresenter implements LoginContract.WelcomePresenter {
    private LoginContract.WelcomeView view;
    private BaseActivity baseActivity;

    public LoginPresenter(BaseActivity baseActivity, LoginContract.WelcomeView view) {
        this.view = view;
        this.baseActivity = baseActivity;
        initListener();
    }

    private void initListener() {
        view.onLogin().subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                try {
                    login();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void login() throws IOException {
        HashMap<String, String> form = new HashMap<>();
        form.put("_subm", "lform");
        form.put("_url", "http://mbox.i.ua/?_rand=1510489421&phcode=895931e653895e42d258ed3d1d2ad951");
        form.put("_rand", "0.36141481371766515");
        form.put("login", "serdun");
        form.put("pass", "2856413razs");
        form.put("domn", "i.ua");
        form.put("cpass", "");
        form.put("scode", "692044ae19cca5b96fcae2c6ef96e5b9");




//        HashMap<String,RequestBody> form=new HashMap<>();
//        form.put("_subm", RequestBody.create(MediaType.parse("text/plain"),"lform"));
//        form.put("_url",RequestBody.create(MediaType.parse("text/plain"), "http://mbox.i.ua/?_rand=1"));
//        form.put("login",RequestBody.create(MediaType.parse("text/plain"), "serdun"));
//        form.put("pass",RequestBody.create(MediaType.parse("text/plain"), "2856413razs"));
//        form.put("domn",RequestBody.create(MediaType.parse("text/plain"), "i.ua"));
//
        RetrofitFactory.getService().login(view.getContext().getString(R.string.login_url), form
        ).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Response<ResponseBody> response) {
                try {
                    String html = response.body().string();
                    int startPos = html.indexOf(Const.user_id_prefix + Const.user_id_prefix.length());
                    int endPos = html.indexOf(Const.user_id_postfix);
                    Log.d("sddsd", "onResponse: ");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });


//        OkHttpClient client = new OkHttpClient();
//
//        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
//        RequestBody body = RequestBody.create(mediaType, "_subm=lform&login=serdun&pass=2856413razs&domn=i.ua");
//        Request request = new Request.Builder()
//                .url("https://passport.i.ua/login")
//                .post(body)
//                .addHeader("content-type", "application/x-www-form-urlencoded")
//                .addHeader("cache-control", "no-cache")
//                .addHeader("postman-token", "5de3aa93-12ee-a298-c260-fc791d048cc4")
//                .build();
//        client.newCall(request).enqueue(new okhttp3.Callback() {
//            @Override
//            public void onFailure(okhttp3.Call call, IOException e) {
//
//            }
//
//            @Override
//            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
//                try {
//                    String html = response.body().string();
//                    int startPos = html.indexOf(Const.user_id_prefix + Const.user_id_prefix.length());
//                    int endPos = html.indexOf(Const.user_id_postfix);
//
//                    //      String user_id=html.substring(startPos,endPos);
//                    Log.d("sddsd", "onResponse: ");
//
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        });

        //   Response response = client.newCall(request).execute();
    }


    @Override
    public void start() {
        Log.d("", "start: ");
    }

    @Override
    public void initCallbacks() {

    }
}

