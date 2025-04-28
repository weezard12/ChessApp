package me.weezard12.chessapp.database;

import android.graphics.Color;

import me.weezard12.chessapp.chessGame.board.BoardColors;
import me.weezard12.chessapp.chessGame.scenes.SettingsActivity;

/**
 * Class to store user settings that can be saved to the database
 */
public class UserSettings {
    private int userId;
    private String themeName;
    private int themeIndex; // Store only the theme index, not the full colors
    private float volume;

    public UserSettings() {
        // Default settings
        this.themeName = "Default";
        this.themeIndex = 0; // Default theme index
        this.volume = 1.0f;
    }

    public UserSettings(int userId, String themeName, int themeIndex, float volume) {
        this.userId = userId;
        this.themeName = themeName;
        this.themeIndex = themeIndex;
        this.volume = volume;
    }

    // Convert to BoardColors object based on theme index
    public BoardColors toBoardColors() {
        return getThemeByIndex();
    }

    // Set theme name and index only
    public void setFromBoardColors(BoardColors colors, String themeName, int themeIndex) {
        this.themeName = themeName;
        this.themeIndex = themeIndex;
        // No longer storing individual colors
    }
    
    // Get theme by index
    public BoardColors getThemeByIndex() {
        return SettingsActivity.getThemeByIndex(themeIndex);
    }

    // Getters and Setters
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getThemeName() {
        return themeName;
    }

    public void setThemeName(String themeName) {
        this.themeName = themeName;
    }
    
    public int getThemeIndex() {
        return themeIndex;
    }
    
    public void setThemeIndex(int themeIndex) {
        this.themeIndex = themeIndex;
    }

    // Color getters and setters removed as we're now using theme index only





    public float getVolume() {
        return volume;
    }

    public void setVolume(float volume) {
        this.volume = volume;
    }
}
