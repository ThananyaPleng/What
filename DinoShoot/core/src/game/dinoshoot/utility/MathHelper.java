package game.dinoshoot.utility;

import com.badlogic.gdx.math.Vector2;

/**
 * Stateless class : MathHelper
 */
public class MathHelper {

    private MathHelper() { }

    public static float calculateDegreeBetween2Point(Vector2 basePoint, Vector2 toPoint) {
        float angle = (float) Math.toDegrees(Math.atan2(toPoint.y - (basePoint.y), toPoint.x - (basePoint.x)));
        if(angle < 0) angle += 360;

        return angle;
    }

    public static Vector2 calculateVelocityToDirection(float degree, float velocity) {
        return new Vector2((float)(velocity * Math.cos(Math.toRadians(degree))), (float)(velocity * Math.sin(Math.toRadians(degree))));
    }

}
