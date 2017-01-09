package com.codeviv.cardflip;

/**
 * Created by vivek on 12/28/2016.
 */

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.codeviv.cardflip.R;
import com.codeviv.cardflip.fragment.CardFrontFragment;
import com.facebook.CallbackManager;
import com.facebook.login.LoginManager;
import com.facebook.login.widget.LoginButton;

/**
 * Created by vivek on 12/28/2016.
 */

public class CardFlipActivity extends Activity {

    Button btnFlip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_flip);

        if(savedInstanceState == null) {

            getFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, new CardFrontFragment())
                    .commit();

        }

    }

}

