package me.weezard12.chessapp.chessGame.scenes;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import me.weezard12.chessapp.R;
import me.weezard12.chessapp.gameLogic.MusicManager;

public class LoadingScreenActivity extends AppCompatActivity {

    private static final int LOADING_TIME = 3000; // 3 seconds (in milliseconds)
    private AnimatorSet animatorSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_screen);

        ImageView spinner = findViewById(R.id.spinner);
        startLoadingAnimation(spinner);

        // Schedule method invocation after LOADING_TIME milliseconds
        new Handler().postDelayed(this::onLoadingComplete, LOADING_TIME);
    }

    private void startLoadingAnimation(ImageView spinner) {
        // Rotation animation
        ObjectAnimator rotate = ObjectAnimator.ofFloat(spinner, "rotation", 0f, 360f);
        rotate.setDuration(2000);
        rotate.setRepeatCount(ValueAnimator.INFINITE);
        rotate.setInterpolator(new AccelerateDecelerateInterpolator());

        // Scale animation
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(spinner, "scaleX", 1f, 1.5f, 1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(spinner, "scaleY", 1f, 1.5f, 1f);
        scaleX.setDuration(3000);
        scaleY.setDuration(3000);
        scaleX.setRepeatCount(ValueAnimator.INFINITE);
        scaleY.setRepeatCount(ValueAnimator.INFINITE);
        scaleX.setInterpolator(new AccelerateDecelerateInterpolator());
        scaleY.setInterpolator(new AccelerateDecelerateInterpolator());

        // Play animations together
        animatorSet = new AnimatorSet();
        animatorSet.playTogether(rotate, scaleX, scaleY);
        animatorSet.start();
    }

    private void onLoadingComplete() {
        // Stop animation
        if (animatorSet != null && animatorSet.isRunning()) {
            animatorSet.cancel();
        }

        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);

        MusicManager musicManager = MusicManager.getInstance(this);
        musicManager.loadMusic(this, R.raw.background_music);
        musicManager.playMusic();
    }
}
