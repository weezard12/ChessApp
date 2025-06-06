package me.weezard12.chessapp.gameLogic;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.util.HashMap;
import java.util.List;

public class MusicManager {
    private static MusicManager instance;

    private MediaPlayer mediaPlayer; // Background music player
    private SoundPool soundPool; // Sound effects player
    private HashMap<Integer, Integer> soundMap; // Store sound effect IDs
    private float musicVolume = 1.0f; // Volume for music
    private float effectsVolume = 1.0f; // Volume for effects

    private boolean isMusicPlaying = false;
    private boolean stopMusicOnMinimize = true; // Flag for stopping music when minimized
    private boolean isAppInBackground = false; // Flag to track app state
    private Handler appStateHandler; // Handler for app state checking thread
    private Runnable appStateChecker; // Runnable for app state checking
    private static final int APP_STATE_CHECK_INTERVAL = 500; // Check interval in ms
    
    // Volume fade constants
    private static final int VOLUME_FADE_DURATION = 500; // Duration of fade in ms
    private static final int VOLUME_FADE_STEPS = 250; // Number of steps in fade
    private float savedMusicVolume = 1.0f; // Store original volume for fading
    private Handler fadeHandler = new Handler(Looper.getMainLooper());
    private Runnable fadeRunnable;

    private MusicManager(Context context) {
        instance = this;
        // Initialize SoundPool for effects
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .setUsage(AudioAttributes.USAGE_GAME)
                .build();

        soundPool = new SoundPool.Builder()
                .setMaxStreams(5) // Allows 5 sounds to play simultaneously
                .setAudioAttributes(audioAttributes)
                .build();

        soundMap = new HashMap<>();
        
        // Initialize app state checking
        appStateHandler = new Handler(Looper.getMainLooper());
        initAppStateChecker(context);
        startAppStateChecker();
    }

    // Singleton instance
    public static synchronized MusicManager getInstance(Context context) {
        if (instance == null) {
            instance = new MusicManager(context);
        }
        return instance;
    }

    /** 🎵 Load a background music track */
    public void loadMusic(Context context, int resId) {
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
        mediaPlayer = MediaPlayer.create(context, resId);
        mediaPlayer.setLooping(true); // Loop background music
        mediaPlayer.setVolume(musicVolume, musicVolume);
    }

    /** 🔊 Load a sound effect */
    public void loadSoundEffect(Context context, int resId) {
        int soundId = soundPool.load(context, resId, 1);
        soundMap.put(resId, soundId);
    }

    /** ▶️ Play background music */
    public void playMusic() {
        // Only play music if app is in foreground
        if (mediaPlayer != null && !mediaPlayer.isPlaying() && !isAppInBackground) {
            mediaPlayer.start();
            isMusicPlaying = true;
        }
    }

    /** ⏸ Pause background music */
    public void pauseMusic() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            // We keep isMusicPlaying true to remember that music should be playing
            // This way we know to resume it when the app comes back to foreground
        }
    }
    
    /**
     * Fade out music volume and then pause
     */
    private void fadeOutMusic() {
        if (mediaPlayer == null || !mediaPlayer.isPlaying()) {
            return;
        }
        
        // Cancel any existing fade operations
        if (fadeRunnable != null) {
            fadeHandler.removeCallbacks(fadeRunnable);
        }
        
        // Save the current volume to restore later
        savedMusicVolume = musicVolume;
        
        // Calculate step size for smooth fade
        final float stepSize = savedMusicVolume / VOLUME_FADE_STEPS;
        final int stepDuration = VOLUME_FADE_DURATION / VOLUME_FADE_STEPS;
        
        // Create fade out runnable
        final float[] currentVolume = {savedMusicVolume};
        
        fadeRunnable = new Runnable() {
            @Override
            public void run() {
                // Reduce volume by step size
                currentVolume[0] -= stepSize;
                
                if (currentVolume[0] <= 0) {
                    // Reached minimum volume, pause the music
                    currentVolume[0] = 0;
                    if (mediaPlayer != null) {
                        mediaPlayer.setVolume(0, 0);
                        mediaPlayer.pause();
                    }
                } else {
                    // Set the new volume
                    if (mediaPlayer != null) {
                        mediaPlayer.setVolume(currentVolume[0], currentVolume[0]);
                        // Schedule next volume reduction
                        fadeHandler.postDelayed(this, stepDuration);
                    }
                }
            }
        };
        
        // Start the fade out process
        fadeHandler.post(fadeRunnable);
    }
    
    /**
     * Fade in music volume
     */
    private void fadeInMusic() {
        if (mediaPlayer == null) {
            return;
        }
        
        // Cancel any existing fade operations
        if (fadeRunnable != null) {
            fadeHandler.removeCallbacks(fadeRunnable);
        }
        
        // Start with volume at 0
        if (mediaPlayer != null) {
            mediaPlayer.setVolume(0, 0);
        }
        
        // Calculate step size for smooth fade
        final float stepSize = savedMusicVolume / VOLUME_FADE_STEPS;
        final int stepDuration = VOLUME_FADE_DURATION / VOLUME_FADE_STEPS;
        
        // Create fade in runnable
        final float[] currentVolume = {0};
        
        fadeRunnable = new Runnable() {
            @Override
            public void run() {
                // Increase volume by step size
                currentVolume[0] += stepSize;
                
                if (currentVolume[0] >= savedMusicVolume) {
                    // Reached target volume
                    currentVolume[0] = savedMusicVolume;
                    if (mediaPlayer != null) {
                        mediaPlayer.setVolume(savedMusicVolume, savedMusicVolume);
                    }
                } else {
                    // Set the new volume
                    if (mediaPlayer != null) {
                        mediaPlayer.setVolume(currentVolume[0], currentVolume[0]);
                        // Schedule next volume increase
                        fadeHandler.postDelayed(this, stepDuration);
                    }
                }
            }
        };
        
        // Start the fade in process
        fadeHandler.post(fadeRunnable);
    }

    /** ⏹ Stop background music */
    public void stopMusic() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.prepareAsync();
            isMusicPlaying = false;
        }
    }

    /** 🔊 Play a sound effect */
    public void playSoundEffect(int resId) {
        Integer soundId = soundMap.get(resId);
        if (soundId != null) {
            soundPool.play(soundId, effectsVolume, effectsVolume, 1, 0, 1.0f);
        }
    }

    /** 🔄 Resume music if it was playing */
    public void resumeMusic() {
        if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
            mediaPlayer.start();
            isMusicPlaying = true;
        }
    }

    /** 🎚 Set music volume */
    public void setMusicVolume(float volume) {
        this.musicVolume = volume;
        this.savedMusicVolume = volume; // Save the user-set volume
        if (mediaPlayer != null) {
            mediaPlayer.setVolume(volume, volume);
        }
    }

    /** 🎚 Set effects volume */
    public void setEffectsVolume(float volume) {
        this.effectsVolume = volume;
    }

    /** 🛑 Release resources */
    public void release() {
        // Stop the app state checker
        stopAppStateChecker();
        
        // Cancel any fade operations
        if (fadeRunnable != null) {
            fadeHandler.removeCallbacks(fadeRunnable);
            fadeRunnable = null;
        }
        
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        soundPool.release();
        soundPool = null;
        soundMap.clear();
    }

    /** 🔄 Handle app minimize behavior */
    public void onAppMinimized() {
        isAppInBackground = true;
        if (stopMusicOnMinimize && mediaPlayer != null && mediaPlayer.isPlaying()) {
            // Fade out instead of abrupt pause
            fadeOutMusic();
        }
    }

    /** 🔄 Handle app resume behavior */
    public void onAppResumed() {
        isAppInBackground = false;
        if (stopMusicOnMinimize && isMusicPlaying) {
            // If music was paused due to background, fade it back in
            if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
                mediaPlayer.start();
                fadeInMusic();
            }
        }
        // Log the state for debugging
        Log.d("MusicManager", "App resumed. isMusicPlaying: " + isMusicPlaying);
    }

    /** ✅ Enable or disable stop music on minimize */
    public void setStopMusicOnMinimize(boolean stopMusic) {
        this.stopMusicOnMinimize = stopMusic;
    }
    
    /**
     * Initialize the app state checker
     */
    private void initAppStateChecker(Context context) {
        final Context appContext = context.getApplicationContext();
        appStateChecker = new Runnable() {
            @Override
            public void run() {
                checkAppState(appContext);
                // Schedule next check
                appStateHandler.postDelayed(this, APP_STATE_CHECK_INTERVAL);
            }
        };
    }
    
    /**
     * Start the app state checker thread
     */
    private void startAppStateChecker() {
        appStateHandler.post(appStateChecker);
    }
    
    /**
     * Stop the app state checker thread
     */
    private void stopAppStateChecker() {
        appStateHandler.removeCallbacks(appStateChecker);
    }
    
    /**
     * Check if the app is in foreground or background
     */
    private void checkAppState(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        if (appProcesses == null) {
            return;
        }
        
        String packageName = context.getPackageName();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND 
                    && appProcess.processName.equals(packageName)) {
                // App is in foreground
                if (isAppInBackground) {
                    Log.d("MusicManager", "App state changed: Background -> Foreground");
                    isAppInBackground = false;
                    onAppResumed();
                }
                return;
            }
        }
        
        // If we get here, app is in background
        if (!isAppInBackground) {
            Log.d("MusicManager", "App state changed: Foreground -> Background");
            isAppInBackground = true;
            onAppMinimized();
        }
    }

    /** ℹ️ Check if stop music on minimize is enabled */
    public boolean isStopMusicOnMinimize() {
        return stopMusicOnMinimize;
    }
}
