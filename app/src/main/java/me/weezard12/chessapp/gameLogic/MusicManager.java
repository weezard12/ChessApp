package me.weezard12.chessapp.gameLogic;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.media.SoundPool;

import java.util.HashMap;

public class MusicManager {
    private static MusicManager instance;

    private MediaPlayer mediaPlayer; // Background music player
    private SoundPool soundPool; // Sound effects player
    private HashMap<Integer, Integer> soundMap; // Store sound effect IDs
    private float musicVolume = 1.0f; // Volume for music
    private float effectsVolume = 1.0f; // Volume for effects

    private boolean isMusicPlaying = false;
    private boolean stopMusicOnMinimize = true; // New flag for stopping music when minimized

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
        if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
            mediaPlayer.start();
            isMusicPlaying = true;
        }
    }

    /** ⏸ Pause background music */
    public void pauseMusic() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            isMusicPlaying = false;
        }
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
        if (!isMusicPlaying) {
            playMusic();
        }
    }

    /** 🎚 Set music volume */
    public void setMusicVolume(float volume) {
        this.musicVolume = volume;
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
        if (stopMusicOnMinimize) {
            pauseMusic();
        }
    }

    /** 🔄 Handle app resume behavior */
    public void onAppResumed() {
        if (stopMusicOnMinimize) {
            resumeMusic();
        }
    }

    /** ✅ Enable or disable stop music on minimize */
    public void setStopMusicOnMinimize(boolean stopMusic) {
        this.stopMusicOnMinimize = stopMusic;
    }

    /** ℹ️ Check if stop music on minimize is enabled */
    public boolean isStopMusicOnMinimize() {
        return stopMusicOnMinimize;
    }
}
