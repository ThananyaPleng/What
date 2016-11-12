package game.dinoshoot.utility;

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

}
