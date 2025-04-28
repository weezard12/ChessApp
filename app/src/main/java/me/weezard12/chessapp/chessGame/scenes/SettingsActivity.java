package me.weezard12.chessapp.chessGame.scenes;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import me.weezard12.chessapp.R;
import me.weezard12.chessapp.database.DatabaseHelp;
import me.weezard12.chessapp.database.UserSessionManager;
import me.weezard12.chessapp.database.UserSettings;
import me.weezard12.chessapp.gameLogic.MusicManager;
import me.weezard12.chessapp.MyUtils;
import me.weezard12.chessapp.chessGame.board.BoardColors;
import me.weezard12.chessapp.chessGame.board.GameBoard;
import me.weezard12.chessapp.chessGame.scenes.views.ThemeSelectorView;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {
    
    // List of predefined themes
    private static final BoardColors[] THEMES = new BoardColors[7];

    LinearLayout themesLayout;
    Button backButton;
    SeekBar musicVolumeSeekBar;
    
    private DatabaseHelp databaseHelp;
    private UserSessionManager sessionManager;
    private UserSettings userSettings;
    private MusicManager musicManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_settings);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        
        // Initialize database helper, session manager, and music manager
        databaseHelp = new DatabaseHelp(this);
        sessionManager = UserSessionManager.getInstance(this);
        musicManager = MusicManager.getInstance(this);
        
        // Load user settings
        loadUserSettings();

        backButton = findViewById(R.id.btnBack);
        backButton.setOnClickListener(this);

        themesLayout = findViewById(R.id.themesLayout);
        updateThemes();
        
        // Setup music volume slider
        musicVolumeSeekBar = findViewById(R.id.seekBarMusic);
        setupMusicVolumeSlider();
    }
    // Initialize themes array with predefined themes
    static {
        // Theme 0: Default
        THEMES[0] = new BoardColors(Color.WHITE, Color.BLACK, Color.BLUE, Color.CYAN);
        
        // Theme 1: Chess.com
        THEMES[1] = new BoardColors(
                MyUtils.rgbToFloatRgb(235,236,208), 
                MyUtils.rgbToFloatRgb(115,149,82), 
                Color.valueOf(Color.BLUE), 
                Color.valueOf(Color.CYAN));
        
        // Theme 2: Brown
        THEMES[2] = new BoardColors(
                MyUtils.rgbToFloatRgb(237,214,176), 
                MyUtils.rgbToFloatRgb(184,135,98), 
                Color.valueOf(Color.BLUE), 
                Color.valueOf(Color.CYAN));
        
        // Theme 3: Sky
        THEMES[3] = new BoardColors(
                MyUtils.rgbToFloatRgb(240,241,240), 
                MyUtils.rgbToFloatRgb(196,216,228), 
                Color.valueOf(Color.BLUE), 
                Color.valueOf(Color.CYAN));
        
        // Theme 4: Clear
        THEMES[4] = new BoardColors(
                MyUtils.rgbToFloatRgb(139,138,136), 
                MyUtils.rgbToFloatRgb(105,104,102), 
                Color.valueOf(Color.BLUE), 
                Color.valueOf(Color.CYAN));
        
        // Theme 5: Light
        THEMES[5] = new BoardColors(
                MyUtils.rgbToFloatRgb(216,217,216), 
                MyUtils.rgbToFloatRgb(168,169,168), 
                Color.valueOf(Color.BLUE), 
                Color.valueOf(Color.CYAN));
        
        // Theme 6: Light Brown
        THEMES[6] = new BoardColors(
                MyUtils.rgbToFloatRgb(237,203,165), 
                MyUtils.rgbToFloatRgb(216,164,109), 
                Color.valueOf(Color.BLUE), 
                Color.valueOf(Color.CYAN));
    }
    
    // Get theme names array
    private static final String[] THEME_NAMES = {
        "Default", "Chess.com", "Brown", "Sky", "Clear", "Light", "Light Brown"
    };
    
    /**
     * Get a theme by its index
     */
    public static BoardColors getThemeByIndex(int index) {
        if (index >= 0 && index < THEMES.length) {
            return THEMES[index];
        }
        return THEMES[0]; // Default theme if index is out of bounds
    }
    
    /**
     * Get theme name by index
     */
    public static String getThemeNameByIndex(int index) {
        if (index >= 0 && index < THEME_NAMES.length) {
            return THEME_NAMES[index];
        }
        return THEME_NAMES[0]; // Default theme name if index is out of bounds
    }
    
    /**
     * Find theme index by comparing with a BoardColors object
     */
    public static int findThemeIndex(BoardColors colors) {
        for (int i = 0; i < THEMES.length; i++) {
            if (THEMES[i].equals(colors)) {
                return i;
            }
        }
        return 0; // Default theme if no match found
    }
    
    public void updateThemes(){
        themesLayout.removeAllViews();
        
        // Add all themes from the THEMES array
        for (int i = 0; i < THEMES.length; i++) {
            themesLayout.addView(new ThemeSelectorView(this, THEMES[i], THEME_NAMES[i], i));
        }
    }

    /**
     * Load user settings from database
     */
    private void loadUserSettings() {
        // Check if user is logged in
        if (sessionManager.isLoggedIn() && !sessionManager.isGuest()) {
            // Load user settings from database
            int userId = sessionManager.getUserId();
            userSettings = databaseHelp.getUserSettings(userId);
            
            // Apply user settings - use theme by index
            GameBoard.boardColors = getThemeByIndex(userSettings.getThemeIndex());
            
            // Apply music volume
            float musicVolume = userSettings.getVolume();
            musicManager.setMusicVolume(musicVolume);
        } else {
            // Guest user - use default settings
            userSettings = new UserSettings();
        }
    }
    
    /**
     * Save current theme to user settings
     */
    public void saveCurrentTheme(BoardColors colors, String themeName, int themeIndex) {
        // Check if user is logged in
        if (sessionManager.isLoggedIn() && !sessionManager.isGuest()) {
            // Update user settings with theme index
            userSettings.setFromBoardColors(colors, themeName, themeIndex);
            
            // Save to database
            boolean success = databaseHelp.saveUserSettings(userSettings);
            if (success) {
                Toast.makeText(this, "Settings saved", Toast.LENGTH_SHORT).show();
            }
        }
    }
    
    @Override
    public void onClick(View v) {
        if(v.equals(backButton)){
            Intent intent = new Intent(this, MenuActivity.class);
            startActivity(intent);
        }
    }

    
    /**
     * Setup the music volume slider
     */
    private void setupMusicVolumeSlider() {
        // Set initial progress based on current settings
        int progress = (int)(userSettings.getVolume() * 100);
        musicVolumeSeekBar.setProgress(progress);
        
        // Set listener for changes
        musicVolumeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    // Convert progress (0-100) to volume (0.0-1.0)
                    float volume = progress / 100.0f;
                    
                    // Apply volume change immediately
                    musicManager.setMusicVolume(volume);
                    
                    // Update user settings
                    userSettings.setVolume(volume);
                    
                    // Save settings if logged in
                    if (sessionManager.isLoggedIn() && !sessionManager.isGuest()) {
                        databaseHelp.saveUserSettings(userSettings);
                    }
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Not needed
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Not needed
            }
        });
    }
}