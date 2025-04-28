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
    private int themeIndex; // Added theme index to store instead of full colors
    private int whiteColor;
    private int blackColor;
    private int selectedTileColor;
    private int movesHighlightColor;
    private float volume;

    public UserSettings() {
        // Default settings
        this.themeName = "Default";
        this.themeIndex = 0; // Default theme index
        this.whiteColor = Color.WHITE;
        this.blackColor = Color.BLACK;
        this.selectedTileColor = Color.BLUE;
        this.movesHighlightColor = Color.CYAN;
        this.volume = 1.0f;
    }

    public UserSettings(int userId, String themeName, int themeIndex, int whiteColor, int blackColor, 
                        int selectedTileColor, int movesHighlightColor, float volume) {
        this.userId = userId;
        this.themeName = themeName;
        this.themeIndex = themeIndex;
        this.whiteColor = whiteColor;
        this.blackColor = blackColor;
        this.selectedTileColor = selectedTileColor;
        this.movesHighlightColor = movesHighlightColor;
        this.volume = volume;
    }

    // Convert to BoardColors object
    public BoardColors toBoardColors() {
        return new BoardColors(
                whiteColor,
                blackColor,
                selectedTileColor,
                movesHighlightColor
        );
    }

    // Set from BoardColors object and theme index
    public void setFromBoardColors(BoardColors colors, String themeName, int themeIndex) {
        this.themeName = themeName;
        this.themeIndex = themeIndex;
        // We still store the colors for backward compatibility
        this.whiteColor = colors.white.toArgb();
        this.blackColor = colors.black.toArgb();
        this.selectedTileColor = colors.selectedTile.toArgb();
        this.movesHighlightColor = colors.movesHighlightColor.toArgb();
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

    public int getWhiteColor() {
        return whiteColor;
    }

    public void setWhiteColor(int whiteColor) {
        this.whiteColor = whiteColor;
    }

    public int getBlackColor() {
        return blackColor;
    }

    public void setBlackColor(int blackColor) {
        this.blackColor = blackColor;
    }

    public int getSelectedTileColor() {
        return selectedTileColor;
    }

    public void setSelectedTileColor(int selectedTileColor) {
        this.selectedTileColor = selectedTileColor;
    }

    public int getMovesHighlightColor() {
        return movesHighlightColor;
    }

    public void setMovesHighlightColor(int movesHighlightColor) {
        this.movesHighlightColor = movesHighlightColor;
    }





    public float getVolume() {
        return volume;
    }

    public void setVolume(float volume) {
        this.volume = volume;
    }
}
