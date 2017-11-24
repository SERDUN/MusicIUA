package dmitriiserdun.gmail.com.musickiua.base;

import android.content.Context;
import android.graphics.drawable.ClipDrawable;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import dmitriiserdun.gmail.com.musickiua.R;

/**
 * Created by dmitro on 23.11.17.
 */

public class CustomProgressBar extends FrameLayout {

    private View rootView;

    private ClipDrawable inLeft;
    private ClipDrawable inRight;


    private int mLevel = 0;
    private int fromLevel = 0;
    private int toLevel = 0;

    public static final int MAX_LEVEL = 10000;
    public static final int LEVEL_DIFF = 100;
    public static final int DELAY = 60;


    private Handler mRightHandler = new Handler();
    private Handler mLeftHandler = new Handler();


    private ViewGroup viewGroup;

    ImageView leftImg;
    ImageView rightImg;

    public CustomProgressBar(@NonNull Context context) {
        super(context);
        init(context);
    }

    public CustomProgressBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        toLevel = (100 * MAX_LEVEL) / 100;
        rootView = inflate(context, R.layout.custom_progress_bar, this);
        viewGroup = rootView.findViewById(R.id.container);
        leftImg = rootView.findViewById(R.id.imageView1);
        inLeft = (ClipDrawable) leftImg.getDrawable();
        inLeft.setLevel(0);


        rightImg = rootView.findViewById(R.id.imageView3);
        inRight = (ClipDrawable) rightImg.getDrawable();
        inLeft.setLevel(0);

        mLeftHandler.removeCallbacks(animateLeftImage);
        mRightHandler.post(animateRightImage);
    }


    private void doTheRightAnimation(int fromLevel, int toLevel) {
        rightImg.setVisibility(View.VISIBLE);
        leftImg.setVisibility(View.GONE);

        mLevel += LEVEL_DIFF;
        inRight.setLevel(mLevel);
        if (mLevel <= toLevel) {
            mRightHandler.postDelayed(animateRightImage, DELAY);
        } else {

            mRightHandler.removeCallbacks(animateRightImage);
            mRightHandler.removeCallbacks(animateLeftImage);
            this.mLevel = this.mLevel = this.fromLevel = 0;
            mRightHandler.post(animateLeftImage);

        }
    }

    private void doTheLeftAnimation(int fromLevel, int toLevel) {
        rightImg.setVisibility(View.GONE);
        leftImg.setVisibility(View.VISIBLE);
        mLevel += LEVEL_DIFF;
        inLeft.setLevel(mLevel);
        if (mLevel <= toLevel) {
            mLeftHandler.postDelayed(animateLeftImage, DELAY);
        } else {
            mRightHandler.removeCallbacks(animateLeftImage);
            mRightHandler.removeCallbacks(animateRightImage);
            this.mLevel = this.mLevel = this.fromLevel = 0;
            mRightHandler.post(animateRightImage);


        }
    }


    private Runnable animateRightImage = new Runnable() {

        @Override
        public void run() {
            doTheRightAnimation(fromLevel, toLevel);
        }
    };

    private Runnable animateLeftImage = new Runnable() {

        @Override
        public void run() {
            doTheLeftAnimation(fromLevel, toLevel);
        }
    };

    public void setVisibility(int status) {
        super.setVisibility(status);
    }
}
