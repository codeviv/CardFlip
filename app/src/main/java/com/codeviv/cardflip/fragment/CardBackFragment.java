package com.codeviv.cardflip.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.GestureDetectorCompat;
import android.telecom.Call;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.codeviv.cardflip.CardFlipActivity;
import com.codeviv.cardflip.R;
import com.digits.sdk.android.AuthCallback;
import com.digits.sdk.android.Digits;
import com.digits.sdk.android.DigitsAuthButton;
import com.digits.sdk.android.DigitsException;
import com.digits.sdk.android.DigitsSession;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;

import io.fabric.sdk.android.Fabric;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by vivek on 12/28/2016.
 */
public class CardBackFragment extends Fragment implements GestureDetector.OnGestureListener, GoogleApiClient.OnConnectionFailedListener{
    Profile profile;
    public static Uri personPhoto;
    private static final String TAG = "Gestures Back";
    private GestureDetectorCompat mDetector;
    View view;
    String mUsername;
    String mPassword;
    Button mSubmit;
    GoogleApiClient mGoogleApiClient;
    LoginButton loginButton;
    private int RC_SIGN_IN = 1000;
    CallbackManager callbackManager;
    //Fabric variables

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "mupn7j6MmFCoywSSPaqEEyC7S";
    private static final String TWITTER_SECRET = "CMmV6CY6NoWT3tJAXLUFlpQFXVlkHxWFjVDqV8PC8AI4Oi5SSh";


    public CardBackFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.card_back, container, false);
        mDetector = new GestureDetectorCompat(getActivity(), this);
        mUsername =  view.findViewById(R.id.et_username).toString();
        mPassword =  view.findViewById(R.id.et_password).toString();
        mSubmit = (Button) view.findViewById(R.id.btn_submit);
        signin();
        //Fabric build for otp
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(getActivity(), new TwitterCore(authConfig), new Digits.Builder().build());

        otpVerification();

        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flipcard();
            }
        });
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return mDetector.onTouchEvent(motionEvent);
            }

        });
        return view;
    }

    private void otpVerification() {
        DigitsAuthButton digitsButton = (DigitsAuthButton) view.findViewById(R.id.auth_button);
        digitsButton.setCallback(new AuthCallback() {
            @Override
            public void success(DigitsSession session, String phoneNumber) {
                // TODO: associate the session userID with your user model
                Toast.makeText(getApplicationContext(), "Authentication successful for "
                        + phoneNumber, Toast.LENGTH_LONG).show();
            }

            @Override
            public void failure(DigitsException exception) {
                Log.d("Digits", "Sign in with Digits failure", exception);
            }
        });

    }
    @Override
    public void onStart() {
        super.onStart();
        if(mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    public void onStop() {
        if(mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
        super.onStop();
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handlesignInResult" + result.isSuccess());
        if(result.isSuccess()) {
            GoogleSignInAccount acct = result.getSignInAccount();
            mSubmit.setText(acct.getDisplayName());
            personPhoto = acct.getPhotoUrl();
            Toast.makeText(getActivity(), "Login Success", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), "Login Failed", Toast.LENGTH_SHORT).show();

        }
    }
    private void signin() {

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        view.findViewById(R.id.sign_in_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.sign_in_button:
                        Intent SignInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                        startActivityForResult(SignInIntent, RC_SIGN_IN);
                        break;
                }
            }
        });

        if (mGoogleApiClient == null) {
            // Facebook connection
            loginButton = (LoginButton) view.findViewById(R.id.fb_login_butoon);
            loginButton.setReadPermissions("email");
            loginButton.setFragment(this);
            callbackManager = CallbackManager.Factory.create();
            profile = Profile.getCurrentProfile();

            loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                    personPhoto = Uri.parse("https://graph.facebook.com/" + profile.getId() + "/picture?return_ssl_resources=1");
                    Toast.makeText(getActivity(), "Login Success" + profile.getId(), Toast.LENGTH_LONG).show();
                }

                @Override
                public void onCancel() {

                    Toast.makeText(getActivity(), "Login Failed", Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onError(FacebookException error) {

                    Toast.makeText(getActivity(), "Login Failed", Toast.LENGTH_SHORT).show();

                }
            });

        }
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

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
