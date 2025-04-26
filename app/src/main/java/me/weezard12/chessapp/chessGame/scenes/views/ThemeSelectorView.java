package me.weezard12.chessapp.chessGame.scenes.views;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import me.weezard12.chessapp.R;
import me.weezard12.chessapp.chessGame.board.BoardColors;
import me.weezard12.chessapp.chessGame.board.GameBoard;
import me.weezard12.chessapp.chessGame.scenes.SettingsActivity;

public class ThemeSelectorView extends LinearLayout {

    BoardColors colors;
    String themeName;

    public ThemeSelectorView(Context context, BoardColors colors, String themeName) {
        super(context);
        this.colors = colors;
        this.themeName = themeName;
        init(context);
    }


    private void init(Context context) {
        // Inflate custom layout
        LayoutInflater.from(context).inflate(R.layout.sample_theme_selector_view, this, true);

        // Find the inner layout that wraps the buttons
        LinearLayout contentLayout = findViewById(R.id.contentLayout);

        // Create a GradientDrawable with a limited size
        GradientDrawable gradientDrawable = new GradientDrawable(
                GradientDrawable.Orientation.LEFT_RIGHT, // Gradient direction
                new int[]{colors.black.toArgb(), colors.white.toArgb()} // Blue to Purple
        );
        gradientDrawable.setCornerRadius(16); // Optional: Rounded corners

        // Set the drawable as background only for the content layout
        contentLayout.setBackground(gradientDrawable);

        // Find views (colored squares)
        View color1 = findViewById(R.id.themeColor1);
        View color2 = findViewById(R.id.themeColor2);
        View color3 = findViewById(R.id.themeColor3);

        // Handle click events for each theme option
        color1.setOnClickListener(v -> selectTheme(context));
        color1.setBackgroundColor(colors.white.toArgb());

        color2.setOnClickListener(v -> selectTheme(context));
        color2.setBackgroundColor(colors.black.toArgb());

        color3.setOnClickListener(v -> selectTheme(context));
        color3.setBackgroundColor(colors.movesHighlightColor.toArgb());

        // if this is the current selected theme
        if (colors.equals(GameBoard.boardColors)) {

            // Define the scale factor
            float scaleFactor = 1.3f;



            // Scale padding
            int scaledLeftPadding = (int) (getPaddingLeft() * scaleFactor * 2);
            int scaledTopPadding = (int) (getPaddingTop() * scaleFactor * 2);
            int scaledRightPadding = (int) (getPaddingRight() * scaleFactor * 2);
            int scaledBottomPadding = (int) (getPaddingBottom() * scaleFactor * 2);

            setPadding(scaledLeftPadding, scaledTopPadding, scaledRightPadding, 64);

            // Set pivot point to the center to keep scaling centered
            setPivotX(getWidth() / 2f);
            setPivotY(getHeight() / 2f);

            // Apply scaling
            setScaleX(scaleFactor);
            setScaleY(scaleFactor);
        }
    }

    private void selectTheme(Context context) {
        Toast.makeText(context, "Theme Selected: " + themeName, Toast.LENGTH_SHORT).show();
        GameBoard.boardColors = colors;
        
        // Save theme to user settings
        if (context instanceof SettingsActivity) {
            SettingsActivity settingsActivity = (SettingsActivity) context;
            settingsActivity.saveCurrentTheme(colors, themeName);
            settingsActivity.updateThemes();
        }
    }
}