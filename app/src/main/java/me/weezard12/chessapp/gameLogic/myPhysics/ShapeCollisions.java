package me.weezard12.chessapp.gameLogic.myPhysics;

import me.weezard12.chessapp.gameLogic.myPhysics.shapes.Box;
import me.weezard12.chessapp.gameLogic.myPhysics.shapes.Circle;

public final class ShapeCollisions {

    // Circle-to-Circle Collision
    public static boolean circleToCircle(Circle c1, Circle c2) {
        float dx = (c1.center.x + c1.offset.x) - (c2.center.x + c2.offset.x);
        float dy = (c1.center.y + c1.offset.y) - (c2.center.y + c2.offset.y);
        float distance = (float) Math.sqrt(dx * dx + dy * dy);
        return distance < (c1.radius + c2.radius);
    }

    // Circle-to-Box Collision
    public static boolean circleToBox(Circle circle, Box box) {
        // Circle center position
        float circleX = circle.center.x + circle.offset.x;
        float circleY = circle.center.y + circle.offset.y;

        // Box bounds
        float boxLeft = box.center.x - box.width / 2 + box.offset.x;
        float boxRight = box.center.x + box.width / 2 + box.offset.x;
        float boxTop = box.center.y - box.height / 2 + box.offset.y;
        float boxBottom = box.center.y + box.height / 2 + box.offset.y;

        // Clamp circle center to box bounds to find closest point on the box
        float closestX = Math.max(boxLeft, Math.min(circleX, boxRight));
        float closestY = Math.max(boxTop, Math.min(circleY, boxBottom));

        // Calculate the distance from the circle center to the closest point on the box
        float dx = closestX - circleX;
        float dy = closestY - circleY;
        float distance = (float) Math.sqrt(dx * dx + dy * dy);

        // Collision happens if the distance is less than the circle's radius
        return distance < circle.radius;
    }

    // Box-to-Box Collision (Axis-Aligned Bounding Boxes)
    public static boolean boxToBox(Box b1, Box b2) {
        // Get box boundaries
        float b1Left = b1.center.x - b1.width / 2 + b1.offset.x;
        float b1Right = b1.center.x + b1.width / 2 + b1.offset.x;
        float b1Top = b1.center.y - b1.height / 2 + b1.offset.y;
        float b1Bottom = b1.center.y + b1.height / 2 + b1.offset.y;

        float b2Left = b2.center.x - b2.width / 2 + b2.offset.x;
        float b2Right = b2.center.x + b2.width / 2 + b2.offset.x;
        float b2Top = b2.center.y - b2.height / 2 + b2.offset.y;
        float b2Bottom = b2.center.y + b2.height / 2 + b2.offset.y;

        // Check for overlap
        return !(b1Right < b2Left || b1Left > b2Right || b1Bottom < b2Top || b1Top > b2Bottom);
    }
}
