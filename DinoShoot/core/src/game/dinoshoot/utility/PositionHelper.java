package game.dinoshoot.utility;

import com.badlogic.gdx.math.Vector2;

public class PositionHelper {

    public static boolean calcShiftPosition(float row, boolean shiftBaseRow) {
        boolean shiftPosition = false;

        if(shiftBaseRow) {
            if(row % 2 == 0) {
                shiftPosition = true;
            }
        } else {
            if(row % 2 != 0) {
                shiftPosition = true;
            }
        }

        return shiftPosition;
    }

    public static Vector2[] calcSurroundPositions(float col, float row, boolean shiftRowZero) {
        if(calcShiftPosition(row, shiftRowZero)) {
            return new Vector2[] {
                    new Vector2(col - 1, row + 1),      // Top-Left
                    new Vector2(col, row + 1),          // Top-Right
                    new Vector2(col - 1, row),          // Left
                    new Vector2(col + 1, row),          // Right
                    new Vector2(col - 1, row - 1),      // Bottom-Left
                    new Vector2(col, row - 1),          // Bottom-Right
            };
        } else {
            return new Vector2[] {
                    new Vector2(col, row + 1),          // Top-Left
                    new Vector2(col + 1, row + 1),      // Top-Right
                    new Vector2(col - 1, row),          // Left
                    new Vector2(col + 1, row),          // Right
                    new Vector2(col, row - 1),          // Bottom-Left
                    new Vector2(col + 1, row - 1),      // Bottom-Right
            };
        }
    }

}
