package dmitriiserdun.gmail.com.musickiua.screens.top_songs;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import dmitriiserdun.gmail.com.musickiua.R;
import dmitriiserdun.gmail.com.musickiua.base.BaseFragment;
import dmitriiserdun.gmail.com.musickiua.model.Sound;
import dmitriiserdun.gmail.com.musickiua.screens.playList.PlayListContract;
import dmitriiserdun.gmail.com.musickiua.screens.playList.PlayListPresenter;
import dmitriiserdun.gmail.com.musickiua.screens.playList.PlayListView;
import dmitriiserdun.gmail.com.musickiua.screens.player.PlayerFragment;
import dmitriiserdun.gmail.com.musickiua.storage.provider.ContractClass;
import dmitriiserdun.gmail.com.musickiua.storage.provider.ConvertHelper;

public class TopSongsFragment extends BaseFragment {
    public final static String KEY_FRAGMENT = TopSongsFragment.class.getName();
    private TopSongsContract.View view;
    private TopSongsContract.Presenter presenter;


    public TopSongsFragment() {
        // Required empty public constructor
    }


    public static TopSongsFragment newInstance() {
        TopSongsFragment fragment = new TopSongsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);
        View root = inflater.inflate(R.layout.fragment_top_songs, container, false);
        view = new TopSongsView(root, this);
        presenter = new TopSongsPresenter(view, this);


//        ContentObserver ob = new ContentObserver(new Handler(Looper.getMainLooper())) {
//            @Override
//            public void onChange(boolean selfChangem, Uri uri) {
//                Cursor record = getContext().getContentResolver().query(uri,
//                        ContractClass.Sounds.DEFAULT_PROJECTION,
//                        null, null,
//                        null);

//        Cursor c = getContext().getContentResolver().query(
//                ContractClass.Sounds.CONTENT_URI,
//                ContractClass.Sounds.DEFAULT_PROJECTION,
//                null, null,
//                null);
//        ArrayList<Sound> sounds = ConvertHelper.createSounds(c);
//
//        Log.d("sdsd", "onChange: ");
//
//        Cursor cr= getContext().getContentResolver().delete(
//                ContractClass.Sounds.CONTENT_URI,
//                null,
//                null);
//        ArrayList<Sound> sounds1 = ConvertHelper.createSounds(cr);

        Log.d("sdsd", "onChange: ");


//            }
//        };
//        getContext(). getContentResolver().registerContentObserver(ContractClass.Sounds.CONTENT_URI, true, ob);
        return root;
    }


}
