package me.weezard12.chessapp.gameLogic;

import static com.example.android2dtest.gameLogic.MyDebug.log;

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
}
