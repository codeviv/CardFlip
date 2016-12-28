package com.codeviv.cardflip.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.GestureDetectorCompat;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.codeviv.cardflip.R;

/**
 * Created by vivek on 12/28/2016.
 */
public class CardBackFragment extends Fragment implements GestureDetector.OnGestureListener{

    private static final String TAG = "Gestures Back";
    private GestureDetectorCompat mDetector;
    View view;

    public CardBackFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.card_back, container, false);
        mDetector = new GestureDetectorCompat(getActivity(), this);
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return mDetector.onTouchEvent(motionEvent);
            }
        });
        return view;
    }

    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        flipcard();
        return true;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return true;
    }

    private void flipcard() {
        boolean mShowingBack = false;

        if(mShowingBack) {
            getFragmentManager().popBackStack();
            return;
        }

        mShowingBack = true;

        getFragmentManager().beginTransaction()
                .setCustomAnimations
                        (R.animator.card_flip_left_in,R.animator.card_flip_left_out,
                                R.animator.card_flip_right_in,R.animator.card_flip_right_out)
                .replace(R.id.container, new CardFrontFragment())
                .addToBackStack(null)
                .commit();

    }
}
