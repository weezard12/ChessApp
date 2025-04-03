package me.weezard12.chessapp.chessGame.board;

import android.graphics.Color;

import java.util.Objects;

public class BoardColors {
    public Color white;
    public Color black;
    public Color selectedTile;
    public Color movesHighlightColor;

    public BoardColors(Color white, Color black, Color selectedTile, Color movesHighlightColor) {
        this.white = white;
        this.black = black;
        this.selectedTile = selectedTile;
        this.movesHighlightColor = movesHighlightColor;
    }

    public BoardColors(int white, int black, int selectedTile, int movesHighlightColor) {
        this.white = Color.valueOf(white);
        this.black = Color.valueOf(black);
        this.selectedTile = Color.valueOf(selectedTile);
        this.movesHighlightColor = Color.valueOf(movesHighlightColor);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true; // Same instance
        if (obj == null || getClass() != obj.getClass()) return false; // Null or different class

        BoardColors that = (BoardColors) obj;

        // Compare colors using their RGBA components
        return colorsEqual(this.white, that.white) &&
                colorsEqual(this.black, that.black) &&
                colorsEqual(this.selectedTile, that.selectedTile) &&
                colorsEqual(this.movesHighlightColor, that.movesHighlightColor);
    }

    private boolean colorsEqual(Color c1, Color c2) {
        if (c1 == null || c2 == null) return c1 == c2; // Handle nulls
        return c1.red() == c2.red() &&
                c1.green() == c2.green() &&
                c1.blue() == c2.blue() &&
                c1.alpha() == c2.alpha();
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                white != null ? white.toArgb() : 0,
                black != null ? black.toArgb() : 0,
                selectedTile != null ? selectedTile.toArgb() : 0,
                movesHighlightColor != null ? movesHighlightColor.toArgb() : 0
        );
    }

    @Override
    public String toString() {
        return "BoardColors{" +
                "white=" + colorToHex(white) +
                ", black=" + colorToHex(black) +
                ", selectedTile=" + colorToHex(selectedTile) +
                ", movesHighlightColor=" + colorToHex(movesHighlightColor) +
                '}';
    }

    private String colorToHex(Color color) {
        if (color == null) return "null";
        int r = (int) (color.red() * 255);
        int g = (int) (color.green() * 255);
        int b = (int) (color.blue() * 255);
        int a = (int) (color.alpha() * 255);
        return String.format("#%02X%02X%02X%02X", a, r, g, b);
    }
}
