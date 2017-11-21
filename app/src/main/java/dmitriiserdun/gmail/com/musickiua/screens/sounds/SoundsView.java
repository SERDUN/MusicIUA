package dmitriiserdun.gmail.com.musickiua.screens.sounds;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;

import com.dd.morphingbutton.MorphingButton;
import com.jakewharton.rxbinding.view.RxView;

import java.util.ArrayList;

import dmitriiserdun.gmail.com.musickiua.R;
import dmitriiserdun.gmail.com.musickiua.base.BaseActivity;
import dmitriiserdun.gmail.com.musickiua.model.Sound;
import rx.Observable;
import rx.functions.Action2;

/**
 * Created by dmitro on 31.10.17.
 */

public class SoundsView implements SoundsContract.View {

    private RecyclerView recyclerView;
    private SoundsRecyclerAdapter soundsRecyclerAdapter;

    private View root;
    private BaseActivity activity;

    private Button button;

    private SeekBar seekBar;
    private MorphingButton btnMorphStartPlay;
    private MorphingButton btnMorphStartBack;
    private MorphingButton btnMorphStartNext;
    private MorphingButton.Params circlePlay = null;
    private MorphingButton.Params circlePause = null;
    private MorphingButton.Params circleBack = null;
    private MorphingButton.Params circleNext = null;

    private Handler handler = new Handler();

    public SoundsView(View root, BaseActivity activity) {
        this.root = root;
        this.activity = activity;
        initView();
    }

    private void initView() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(activity.getBaseContext());
        recyclerView = root.findViewById(R.id.recyclerViewSounds);
        recyclerView.setLayoutManager(layoutManager);
        soundsRecyclerAdapter = new SoundsRecyclerAdapter(new ArrayList<Sound>());
        recyclerView.setAdapter(soundsRecyclerAdapter);
//
//        btnMorphStartPlay = root.findViewById(R.id.btnMorphStartPlay);
//        btnMorphStartBack = root.findViewById(R.id.backPlay);
//        btnMorphStartNext = root.findViewById(R.id.nextPlay);

        seekBar = root.findViewById(R.id.seekBar);
//        btnMorphStartPlay.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.d("ds", "onClick: ");
//            }
//        });
        circlePause = MorphingButton.Params.create()
                .duration(500)
                .cornerRadius(100) // 56 dp
                .width(90) // 56 dp
                .height(90) // 56 dp
                .color(activity.getResources().getColor(R.color.colorPlayPast)) // normal state color
                .colorPressed(activity.getResources().getColor(R.color.colorPlay))
                .icon(R.drawable.ic_pause_black_24dp); // icon

        circlePlay = MorphingButton.Params.create()
                .duration(700)
                .cornerRadius(50) // 56 dp
                .width(90) // 56 dp
                .height(90) // 56 dp
                .color(activity.getResources().getColor(R.color.colorPausePast)) // normal state color
                .colorPressed(activity.getResources().getColor(R.color.colorPause))
                .icon(R.drawable.ic_play_circle_filled_black_24dp); // icon

        circleBack = MorphingButton.Params.create()
                .duration(500)
                .cornerRadius(10) // 56 dp
                .width(56) // 56 dp
                .height(56) // 56 dp
                .color(activity.getResources().getColor(R.color.colorPlayControl)) // normal state color
                .icon(R.drawable.ic_chevron_left_black_24dp);
        circleNext = MorphingButton.Params.create()
                .duration(500)
                .cornerRadius(10) // 56 dp
                .width(56) // 56 dp
                .height(56) // 56 dp
                .color(activity.getResources().getColor(R.color.colorPlayControl)) // normal state color
                .icon(R.drawable.ic_keyboard_arrow_right_black_24dp);

        btnMorphStartBack.morph(circleBack);
        btnMorphStartNext.morph(circleNext);

    }

    @Override
    public void setPresenter(SoundsContract.Presenter presenter) {


    }


    @Override
    public void addPlayListsInList(ArrayList<Sound> playlists) {
        soundsRecyclerAdapter.updateData(playlists);
    }

    @Override
    public void showMessage(int id) {
        Toast.makeText(activity, id, Toast.LENGTH_SHORT).show();
    }


    @Override
    public Context getContext() {
        return activity.getBaseContext();
    }

    @Override
    public void setOnItemListListener(Action2<Sound, Integer> action0) {
        soundsRecyclerAdapter.setOnClick(action0);
    }

    @Override
    public void onSeekHandler(Runnable runnable) {
        handler.postDelayed(runnable, 50);
    }

    @Override
    public void setProgress(int position) {
        seekBar.setProgress(position);
    }

    @Override
    public void setMaxProgress(int max) {
        seekBar.setMax(max);
    }

    @Override
    public Observable<Void> onClickPlay() {
        return RxView.clicks(btnMorphStartPlay);
    }

    @Override
    public Observable<Void> onClickBack() {
        return RxView.clicks(btnMorphStartBack);
    }

    @Override
    public Observable<Void> onClickNext() {
        return RxView.clicks(btnMorphStartNext);
    }

    @Override
    public void setColorItem(int hasCode) {
        soundsRecyclerAdapter.setSelectedItem(hasCode);
    }

    @Override
    public void morphPlay() {
        btnMorphStartPlay.morph(circlePlay);

    }

    @Override
    public void morphPause() {
        btnMorphStartPlay.morph(circlePause);

    }


}
