package me.weezard12.chessapp.gameLogic.myECS;

import static com.example.android2dtest.gameLogic.MyDebug.log;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import androidx.annotation.NonNull;

import com.example.android2dtest.gameLogic.ContentManager;
import com.example.android2dtest.gameLogic.GameLoop;
import com.example.android2dtest.gameLogic.myECS.components.touchable.TouchBase;
import com.example.android2dtest.gameLogic.myECS.entities.GameEntity;
import com.example.android2dtest.gameLogic.myPhysics.PhysicsSystem;

import java.util.ArrayList;
import java.util.List;

public class GameScene extends SurfaceView implements SurfaceHolder.Callback {
    private final List<GameEntity> entities;
    private final List<GameEntity> removeEntitiesAfterUpdate;
    private final List<TouchBase> touchables;
    private final List<TouchBase> removeTouchablesAfterUpdate;
    public ContentManager contentManager;
    private static final Point screenCenter = new Point();
    private static final Point screenEnd = new Point();
    public boolean debugRenderPhysics = false;

    public Bitmap getBackgroundImage() {
        return backgroundImage;
    }

    public void setBackgroundImage(Bitmap backgroundImage) {
        this.backgroundImage = backgroundImage;
    }

    private Bitmap backgroundImage;

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    private int backgroundColor;

    public void addTouchable(TouchBase touchable){
        touchables.add(touchable);
    }
    public void removeTouchable(TouchBase touchable){
        removeTouchablesAfterUpdate.add(touchable);
    }

    public static Point getScreenEnd() {
        return new Point(screenEnd.x,screenEnd.y);
    }

    public static Point getSurfaceCenter() {
        return new Point(screenCenter.x,screenCenter.y);
    }
    /**
    * WARNING when inheriting the class do not use the constructor scene init logic!
     * override the start method so you cant get null reference when creating entities at the scene start.
     */
    public GameScene(Context context) {
        super(context);
        entities = new ArrayList<>();
        touchables = new ArrayList<>();
        contentManager = new ContentManager(context);
        getHolder().addCallback(this);
        removeTouchablesAfterUpdate = new ArrayList<>();
        removeEntitiesAfterUpdate = new ArrayList<>();
    }
    public GameScene(Context context, AttributeSet attrs) {
        //super(context, attrs);
        this(context);
    }

    //this method will be called from the game loop.
    //override it for scene start logic
    public void start(){
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        //updates entities that needs a touch event
        for (TouchBase touchable : touchables)
            touchable.onTouchEvent(event);

        // components can be removed after all touchables are updated.
        for (int i = 0; i < removeTouchablesAfterUpdate.size(); i++) {
            touchables.remove(removeTouchablesAfterUpdate.get(i));
            removeTouchablesAfterUpdate.remove(0);
        }

        // Check if the event is a click
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            // Get the X and Y coordinates of the touch
            float x = event.getX();
            float y = event.getY();

            // Do something when the screen is clicked
            handleClick(x, y);

            // Return true to indicate the event was handled
            return true;
        }
        return false;
    }

    protected void handleClick(float x, float y) {

    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        //render background
        canvas.drawColor(backgroundColor);
        if(backgroundImage != null)
            canvas.drawBitmap(backgroundImage,null,canvas.getClipBounds(),new Paint());

        //render the entities
        for (int i = 0; i < entities.size(); i++)
            entities.get(i).render(GameLoop.deltaTime, canvas);

        //draw physics debug
        if(debugRenderPhysics)
            PhysicsSystem.debugRenderPhysics(canvas);

        debugRender(canvas);
    }

    public void update(float delta){

        for (GameEntity entityToRemove : removeEntitiesAfterUpdate) {
            entities.remove(entityToRemove);
        }

        //update all entities
        for (int i = 0; i < entities.size(); i++)
            entities.get(i).update(delta);

        //updates physics
        PhysicsSystem.update(delta);
    }

    public void addEntity(GameEntity entity){
        entity.attachToScene(this);
        entities.add(entity);
    }

    public void detachEntity(GameEntity entity){
        removeEntitiesAfterUpdate.add(entity);
    }
    public void detachEntity(String name){
        removeEntitiesAfterUpdate.add(getEntity(name));
    }

    public GameEntity getEntity(String name){
        for (GameEntity entity : entities)
            if(entity.getName().equals(name))
                return entity;

        return null;
    }

    public List<GameEntity> getEntitiesByPattern(String pattern){
        List<GameEntity> rEntities = new ArrayList<>();
        for (GameEntity entity : entities)
            if(entity.getName().contains(pattern))
                rEntities.add(entity);

        return rEntities;
    }

    private void debugRender(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setTextSize(80); // Adjust text size as needed
        canvas.drawText("FPS: " + GameLoop.fps, 50, 100, paint); // Draw FPS at top-left
        canvas.drawText("Entity Count: " + entities.size(), 50, 200, paint); // Draw FPS at top-left
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }
    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        log("Surface Created, Calling GameScene.start");
        screenCenter.set(holder.getSurfaceFrame().centerX(),holder.getSurfaceFrame().centerY());
        screenEnd.set(holder.getSurfaceFrame().right,holder.getSurfaceFrame().bottom);
        start();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {

    }

    public void setFullScreen(){
        setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_FULLSCREEN |
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        );
    }
}

