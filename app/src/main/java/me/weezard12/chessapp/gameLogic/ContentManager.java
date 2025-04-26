package me.weezard12.chessapp.gameLogic;

import static me.weezard12.chessapp.gameLogic.MyDebug.log;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public final class ContentManager {

    private final Context context;
    private final Map<String, Bitmap> textureCache;

    // Constructor to initialize ContentManager with application context
    public ContentManager(Context context) {
        this.context = context;
        this.textureCache = new HashMap<>();

        log("Content Manager created");
        String[] filePaths = getAllFilePaths();
        for (String path : filePaths) {
            log(path);
        }
    }

    // Load a texture from assets folder and cache it
    public Bitmap loadTexture(String path) {
        // First, check if the texture is already cached
        if (textureCache.containsKey(path)) {
            return textureCache.get(path);
        }

        // Load the texture from assets and cache it
        Bitmap texture = loadTextureFromAssets(path);
        if (texture != null) {
            textureCache.put(path, texture);
        }

        return texture;
    }

    // Helper method to load the texture from assets
    private Bitmap loadTextureFromAssets(String path) {
        Bitmap bitmap = null;
        InputStream inputStream = null;

        try {
            // Open the asset stream and decode it as a bitmap
            inputStream = context.getAssets().open(path);
            bitmap = BitmapFactory.decodeStream(inputStream);
        } catch (IOException e) {
            Log.e("ContentManager", "Failed to load texture: " + path, e);
        } finally {
            // Close the input stream to prevent memory leaks
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    Log.e("ContentManager", "Error closing input stream", e);
                }
            }
        }

        return bitmap;
    }


    // Load a texture from drawable resources by resource name
    public Bitmap loadTextureFromDrawable(int resId) {
        // Check if the texture is already cached
        String resIdKey = String.valueOf(resId); // Use resource ID as the key
        if (textureCache.containsKey(resIdKey)) {
            return textureCache.get(resIdKey);
        }

        // Load the bitmap from resources
        Bitmap texture = BitmapFactory.decodeResource(context.getResources(), resId);
        if (texture != null) {
            textureCache.put(resIdKey, texture);
        }
        return texture;
    }

    // Unload (remove) a texture from cache
    public void unloadTexture(String path) {
        Bitmap bitmap = textureCache.remove(path);
        if (bitmap != null) {
            bitmap.recycle();  // Frees up the memory used by the bitmap
        }
    }

    // Unload all textures and clear the cache
    public void unloadAllTextures() {
        for (String key : textureCache.keySet()) {
            Bitmap bitmap = textureCache.get(key);
            if (bitmap != null) {
                bitmap.recycle();
            }
        }
        textureCache.clear();
    }

    // Get a texture from the cache without loading it
    public Bitmap getTexture(String path) {
        return textureCache.get(path);
    }

    // Check if a texture is already loaded
    public boolean isTextureLoaded(String path) {
        return textureCache.containsKey(path);
    }

    // Get all file paths in the assets folder
    public String[] getAllFilePaths() {
        try {
            // Retrieve all files from the assets root folder or a specific folder
            return context.getAssets().list("");
        } catch (IOException e) {
            Log.e("ContentManager", "Failed to get file paths from assets", e);
            return new String[0]; // Return an empty array if there's an error
        }
    }

    /**
     * Loads a sprite sheet from resources and splits it into individual frame bitmaps
     *
     * @param context Android application context
     * @param resourceId Resource ID of the sprite sheet (R.drawable.xxx)
     * @param rows Number of rows in the sprite sheet
     * @param columns Number of columns in the sprite sheet
     * @param frameCount Total number of frames to extract (can be less than rows*columns if sheet isn't full)
     * @param frameWidth Optional specific width for each frame (null to auto-calculate based on columns)
     * @param frameHeight Optional specific height for each frame (null to auto-calculate based on rows)
     * @param startRow Starting row (0-indexed)
     * @param startColumn Starting column (0-indexed)
     * @param readOrder Whether to read frames in row-major (left-to-right, top-to-bottom) or column-major order
     * @return Array of bitmap frames extracted from the sprite sheet
     */
    public static Bitmap[] loadSpriteSheetFrames(
            Context context,
            int resourceId,
            int rows,
            int columns,
            int frameCount,
            Integer frameWidth,
            Integer frameHeight,
            int startRow,
            int startColumn,
            ReadOrder readOrder) {

        // Define read order enum if not already declared elsewhere
        if (readOrder == null) {
            readOrder = ReadOrder.ROW_MAJOR;
        }

        // Load the sprite sheet from resources
        Bitmap spriteSheet = BitmapFactory.decodeResource(context.getResources(), resourceId);
        if (spriteSheet == null) {
            return new Bitmap[0];
        }

        // Calculate frame dimensions if not specified
        int actualFrameWidth = (frameWidth != null) ? frameWidth : spriteSheet.getWidth() / columns;
        int actualFrameHeight = (frameHeight != null) ? frameHeight : spriteSheet.getHeight() / rows;

        // Validate parameters
        if (startRow < 0 || startColumn < 0 ||
                startRow >= rows || startColumn >= columns ||
                frameCount <= 0 ||
                actualFrameWidth <= 0 || actualFrameHeight <= 0) {
            return new Bitmap[0];
        }

        // Make sure we don't try to read more frames than available in the sheet
        int maxFramesAvailable = (rows * columns) - (startRow * columns + startColumn);
        frameCount = Math.min(frameCount, maxFramesAvailable);

        // Create array for resulting frames
        Bitmap[] frames = new Bitmap[frameCount];

        int frameIndex = 0;
        int currentRow = startRow;
        int currentColumn = startColumn;

        // Extract each frame
        while (frameIndex < frameCount) {
            // Calculate the x and y position on the sprite sheet
            int x = currentColumn * actualFrameWidth;
            int y = currentRow * actualFrameHeight;

            // Make sure we don't read outside the sprite sheet dimensions
            if (x + actualFrameWidth <= spriteSheet.getWidth() &&
                    y + actualFrameHeight <= spriteSheet.getHeight()) {

                // Create a new bitmap for this frame
                frames[frameIndex] = Bitmap.createBitmap(
                        spriteSheet,
                        x, y,
                        actualFrameWidth, actualFrameHeight
                );

                frameIndex++;

                // Move to next position based on read order
                if (readOrder == ReadOrder.ROW_MAJOR) {
                    // Move right, if at end of row, move to beginning of next row
                    currentColumn++;
                    if (currentColumn >= columns) {
                        currentColumn = 0;
                        currentRow++;
                    }
                } else { // COLUMN_MAJOR
                    // Move down, if at bottom of column, move to top of next column
                    currentRow++;
                    if (currentRow >= rows) {
                        currentRow = 0;
                        currentColumn++;
                    }
                }
            } else {
                // We've reached the end of the sprite sheet
                break;
            }
        }

        return frames;
    }

    /**
     * Defines the order in which frames are read from a sprite sheet
     */
    public enum ReadOrder {
        ROW_MAJOR,   // Left to right, then top to bottom
        COLUMN_MAJOR  // Top to bottom, then left to right
    }

    /**
     * Simplified version that assumes default row-major order and starts from top-left
     */
    public static Bitmap[] loadSpriteSheetFrames(
            Context context,
            int resourceId,
            int rows,
            int columns,
            int frameCount) {

        return loadSpriteSheetFrames(
                context, resourceId, rows, columns, frameCount,
                null, null, 0, 0, ReadOrder.ROW_MAJOR);
    }

    /**
     * Loads all frames from a sprite sheet with equal-sized cells
     */
    public static Bitmap[] loadSpriteSheet(
            Context context,
            int resourceId,
            int rows,
            int columns) {

        return loadSpriteSheetFrames(
                context, resourceId, rows, columns, rows * columns,
                null, null, 0, 0, ReadOrder.ROW_MAJOR);
    }
}
