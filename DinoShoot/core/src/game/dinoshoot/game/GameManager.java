package game.dinoshoot.game;

import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

public class GameManager {

    public enum State {
        NOT_PLAYING,
        PLAYING,
        GAMEOVER
    }

    // Game State
    private boolean isPause = false;
    private State state = State.NOT_PLAYING;

    // Game Objects
    private ArrayList<Egg> eggEntities = new ArrayList<Egg>();

    // Game options
    private boolean shiftBasePosition = false;
    private int heightOffset = 3;

    public void createEggByWorldPosition(Vector2 position) {
        if(position.x > 425 || position.x < 0 || position.y < heightOffset * 50 || position.y > 700) {
            System.out.println("Out of game bound");
            return;
        }

        System.out.println(position.x + ", " + position.y);

        int row = (int)((position.y - (heightOffset * 50)) / 50);

        boolean shiftPosition = false;
        if(shiftBasePosition) {
            if(row % 2 == 0) {
                shiftPosition = true;
            }
        } else {
            if(row % 2 != 0) {
                shiftPosition = true;
            }
        }

        int col = (int)((position.x - (shiftPosition ? 25 : 0)) / 50);

        System.out.println(col + ", " + row);

        boolean creatable = false;
        for(Egg existsEgg : eggEntities) {
            boolean pos1 = existsEgg.getPosition().x == col && existsEgg.getPosition().y == row + 1,
                    pos2 = existsEgg.getPosition().x == col + 1 && existsEgg.getPosition().y == row + 1,
                    pos3 = existsEgg.getPosition().x == col - 1 && existsEgg.getPosition().y == row,
                    pos4 = existsEgg.getPosition().x == col + 1 && existsEgg.getPosition().y == row,
                    pos5 = existsEgg.getPosition().x == col && existsEgg.getPosition().y == row - 1,
                    pos6 = existsEgg.getPosition().x == col + 1 && existsEgg.getPosition().y == row - 1,
                    curPos = existsEgg.getPosition().x == col && existsEgg.getPosition().y == row;

            if(curPos) {
                System.out.println("Already have egg at that position");
                return;
            }

            if(!curPos && (pos1 || pos2 || pos3 || pos4 || pos5 || pos6)) {
                creatable = true;
                break;
            }
        }

        if(creatable) {
            createEgg(col, row, Egg.Color.random());
            return;
        } else {
            System.out.println("Can't create egg");
        }
    }

    public void createEgg(float col, float row, Egg.Color color) {
        boolean shiftPosition = false;
        if(shiftBasePosition) {
            if(row % 2 == 0) {
                shiftPosition = true;
            }
        } else {
            if(row % 2 != 0) {
                shiftPosition = true;
            }
        }

        Egg egg = new Egg(col, row, color, shiftPosition, heightOffset);

        eggEntities.add(egg);
    }

    public void removeEgg(float col, float row) {
        Egg temp = null;

        for(Egg egg : eggEntities) {
            if(egg.getPosition().x == col && egg.getPosition().y == row) {
                temp = egg;
                break;
            }
        }

        if(temp != null) {
            eggEntities.remove(temp);
        }
    }

    public void setup() {
        for(int row = 5; row < 12; row++) {
            for(int col = 0; col < 8; col++) {
                boolean shiftPosition = false;
                if(shiftBasePosition) {
                    if(row % 2 == 0) {
                        shiftPosition = true;
                    }
                } else {
                    if(row % 2 != 0) {
                        shiftPosition = true;
                    }
                }

                createEgg(col, row, Egg.Color.random());
            }
        }
    }

    public ArrayList<Egg> getAllEggs() {
        return eggEntities;
    }

    public boolean isPause() {
        return isPause;
    }

    public void setPause(boolean pause) {
        isPause = pause;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public int getHeightOffset() {
        return heightOffset;
    }

    public void setHeightOffset(int heightOffset) {
        this.heightOffset = heightOffset;
    }
}
