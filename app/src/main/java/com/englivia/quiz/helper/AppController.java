package com.englivia.quiz.helper;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.MediaPlayer;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.multidex.MultiDex;
import androidx.appcompat.app.AppCompatDelegate;

import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

import com.englivia.quiz.Constant;
import com.englivia.quiz.R;

import java.security.Key;
import java.util.Date;

import javax.crypto.spec.SecretKeySpec;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;


public class AppController extends Application {

    public static final String TAG = AppController.class
            .getSimpleName();
    private static final int YOUR_PERMISSION_REQUEST_CODE = 123;

    private static Context mContext;

    public static MediaPlayer player;
    public static Activity currentActivity;

    private RequestQueue mRequestQueue;

    private static AppController mInstance;
    private com.android.volley.toolbox.ImageLoader mImageLoader;
    AppOpenManager appOpenManager;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        setContext(getApplicationContext());
        if (currentActivity != null) {
            // Check for READ_PHONE_STATE permission
            if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                // Request the permission.
                ActivityCompat.requestPermissions(currentActivity, new String[]{Manifest.permission.READ_PHONE_STATE}, YOUR_PERMISSION_REQUEST_CODE);
            } else {
                // Permission has already been granted. Set up the telephone listener.
                setTelephoneListener();
            }
        }

        player = new MediaPlayer();
        mediaPlayerInitializer();

        if (Session.getUserAdsData(Session.INAPP_MODE, mContext).equals("1")) {
            if (Session.getUserAdsData(Session.ADDTYPE, mContext).equals("1")) {
                appOpenManager = new AppOpenManager(AppController.this);
                AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
            }
        }

    }

    public static void mediaPlayerInitializer() {
        try {
            int resourceId = R.raw.snd_bg;
            player = MediaPlayer.create(mContext, resourceId);
            player.setOnCompletionListener(mp -> {

                player.reset();
                player.release();
            });
            player = MediaPlayer.create(getAppContext(), R.raw.snd_bg);
            player.setAudioStreamType(AudioManager.STREAM_MUSIC);
            player.setLooping(true);
            player.setVolume(1f, 1f);
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    private static void setContext(Context context) {
        mContext = context;
    }

    public static Context getAppContext() {
        return mContext;
    }

    public static void playSound() {
        try {
            if (Session.getMusicEnableDisable(mContext) && !player.isPlaying()) {
                player.start();
            }

        } catch (IllegalStateException e) {
            e.printStackTrace();
            mediaPlayerInitializer();
            player.start();
        }
    }

    public static void StopSound() {
        if (player.isPlaying()) {
            player.pause();
        }

    }

    public static String createJWT(String issuer, String subject) {
        try {
            SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
            long nowMillis = System.currentTimeMillis();
            Date now = new Date(nowMillis);
            byte[] apiKeySecretBytes = Constant.JWT_KEY.getBytes();
            Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());
            JwtBuilder builder = Jwts.builder()
                    .setIssuedAt(now)
                    .setSubject(subject)
                    .setIssuer(issuer)
                    .signWith(signatureAlgorithm, signingKey);
            return builder.compact();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setTelephoneListener() {
        PhoneStateListener phoneStateListener = new PhoneStateListener() {
            @Override
            public void onCallStateChanged(int state, String incomingNumber) {
                if (state == TelephonyManager.CALL_STATE_RINGING || state == TelephonyManager.CALL_STATE_OFFHOOK)
                    StopSound();
                super.onCallStateChanged(state, incomingNumber);
            }
        };

        TelephonyManager telephony = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
        telephony.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
    }


    public static synchronized AppController getInstance() {
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public ImageLoader getImageLoader() {
        getRequestQueue();
        if (mImageLoader == null) {
            mImageLoader = new ImageLoader(this.mRequestQueue,
                    new LruBitmapCache());
        }
        return this.mImageLoader;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }
}
