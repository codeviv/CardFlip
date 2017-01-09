package com.codeviv.cardflip.fragment;

import android.app.Fragment;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.view.GestureDetectorCompat;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.codeviv.cardflip.CardFlipActivity;
import com.codeviv.cardflip.R;
import com.codeviv.cardflip.fragment.CardBackFragment;
import com.squareup.picasso.Picasso;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by vivek on 12/28/2016.
 */
public class CardFrontFragment extends Fragment implements GestureDetector.OnGestureListener {

    private String stringUrl;
    URL url = null;
    private static final String TAG = "Gestures";
    private GestureDetectorCompat mDetector;
    View view;
    ImageView mProfilePhoto;
    public CardFrontFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view =  inflater.inflate(R.layout.card_front, container,false);
        mProfilePhoto = (ImageView) view.findViewById(R.id.img_profile_photo);
        if(CardBackFragment.personPhoto != null) {
                Uri uri = CardBackFragment.personPhoto;
                stringUrl = uri.toString();

            Picasso.with(getActivity()).load(stringUrl).into(mProfilePhoto);
                //  url = uri.toURL();
                // Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),uri);
               // Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                //mProfilePhoto.setImageBitmap(bmp);

        }
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
                        (R.animator.card_flip_right_in,R.animator.card_flip_right_out,
                                R.animator.card_flip_left_in,R.animator.card_flip_left_out)
                .replace(R.id.container, new CardBackFragment())
                .addToBackStack(null)
                .commit();

    }
}