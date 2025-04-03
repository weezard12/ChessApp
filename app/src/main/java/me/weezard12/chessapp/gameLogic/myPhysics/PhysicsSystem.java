package me.weezard12.chessapp.gameLogic.myPhysics;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

import me.weezard12.chessapp.gameLogic.myPhysics.shapes.Box;
import me.weezard12.chessapp.gameLogic.myPhysics.shapes.Circle;
import me.weezard12.chessapp.gameLogic.myPhysics.shapes.Shape;

import java.util.ArrayList;
import java.util.List;

// PhysicsSystem.java
public class PhysicsSystem {
    public static final List<Collider> colliders = new ArrayList<>();

    public static final Paint OUTLINE_PAINT = new Paint();
    public static final Paint COLLIDER_POS = new Paint();
    public static final Paint COLLIDER_CENTER = new Paint();

    // This will be called at the beginning of the game to initialize any colliders
    public static void init() {
        OUTLINE_PAINT.setColor(Color.RED);
        OUTLINE_PAINT.setStrokeWidth(5);
        OUTLINE_PAINT.setStyle(Paint.Style.STROKE);

        COLLIDER_POS.setColor(Color.BLUE);
        COLLIDER_POS.setStyle(Paint.Style.FILL);

        COLLIDER_CENTER.setColor(Color.GREEN);
        COLLIDER_CENTER.setStyle(Paint.Style.FILL);
    }

    // This is the main update loop that checks for collisions
    public static void update(float delta) {
        for (int i = 0; i < colliders.size(); i++) {
            Collider colliderA = colliders.get(i);
            List<Collider> collidedWith = new ArrayList<>();

            for (int j = i + 1; j < colliders.size(); j++) {
                Collider colliderB = colliders.get(j);

                if (colliderA.checkCollision(colliderB)) {
                    collidedWith.add(colliderB);
                }
            }

            // If there are collisions, notify the collider
            if (!collidedWith.isEmpty()) {
                colliderA.onCollide(collidedWith);
            }
        }
    }
    public static void debugRenderPhysics(Canvas canvas){

        for (Collider collider : PhysicsSystem.colliders) {
            Shape shape = collider.getCollisionShape();
            float sX = shape.center.x + shape.offset.x;
            float sY = shape.center.y + shape.offset.y;

            if(shape instanceof Circle){
                Circle circle = (Circle)shape;
                canvas.drawCircle(sX,sY,circle.radius,OUTLINE_PAINT);
            }
            else if(shape instanceof Box){
                Box box = (Box) shape;
                canvas.drawRect(new RectF(sX - (box.width / 2),sY+(box.height / 2),sX + (box.width / 2),sY-(box.height/2)),OUTLINE_PAINT);
            }

            //draws shape center position
            canvas.drawRect(shape.center.x-5,shape.center.y+5,shape.center.x+5,shape.center.y-5,COLLIDER_CENTER);
            //draws shape position (center + offset)
            canvas.drawRect(sX-10,sY+10,sX+10,sY-10,COLLIDER_POS);

        }
    }

    // Adds a collider to the system
    public static void addCollider(Collider collider) {
        colliders.add(collider);
    }

    // Removes a collider from the system
    public static void removeCollider(Collider collider) {
        colliders.remove(collider);
    }
}

