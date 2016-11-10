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
    private boolean isShooting = false;
    private State state = State.NOT_PLAYING;

    // Game Objects
    private ArrayList<Egg> eggEntities = new ArrayList<Egg>();

    // Game options
    private float baseEggVelocity = 1100f;
    private Vector2 originPoint = new Vector2(187.5f, 50f);
    private boolean shiftBasePosition = false;
    private int heightOffset = 3;

    protected boolean isCurrentRowShiftPosition(float row) {
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

        return shiftPosition;
    }

    public void fireEggAtPosition(Vector2 target) {
        if(isShooting) {
            return;
        }
        isShooting = true;

        float angle = (float) Math.toDegrees(Math.atan2(target.y - (originPoint.y + 25), target.x - (originPoint.x + 25)));

        if(angle < 0){
            angle += 360;
        }

        float vX = (float)(baseEggVelocity * Math.cos(Math.toRadians(angle))),
                vY = (float)(baseEggVelocity * Math.sin(Math.toRadians(angle)));

        if(angle < 1 || angle > 179) {
            System.out.println("Can't shoot below shooter.");
            return;
        }

        Egg egg = createEgg(0, 0, Egg.Color.random());
        egg.setSpritePosition(originPoint);
        egg.setSpeed(new Vector2(vX, vY));
    }

    public void createEggByWorldPosition(Vector2 position) {
        if(position.x > 425 || position.x < 0 || position.y < heightOffset * 50 || position.y > 700) {
            System.out.println("Out of game bound");
            return;
        }

        int row = (int)((position.y - (heightOffset * 50)) / 50);
        boolean shiftPosition = isCurrentRowShiftPosition(row);
        int col = (int)((position.x - (shiftPosition ? 25 : 0)) / 50);

        if(col > 7 || col < 0 || row > 10 || row < 0) {
            System.out.println("Out of game bound");
            return;
        }

        System.out.println(col + ", " + row);

        boolean creatable = false;
        for(Egg existsEgg : eggEntities) {
            boolean curPos = existsEgg.getPosition().x == col && existsEgg.getPosition().y == row;

            if(curPos) {
                System.out.println("Already have egg at that position");
                return;
            }

            boolean pos1, pos2, pos3, pos4, pos5, pos6;
            if(shiftPosition) {
                pos1 = existsEgg.getPosition().x == col && existsEgg.getPosition().y == row + 1;
                pos2 = existsEgg.getPosition().x == col + 1 && existsEgg.getPosition().y == row + 1;
                pos3 = existsEgg.getPosition().x == col - 1 && existsEgg.getPosition().y == row;
                pos4 = existsEgg.getPosition().x == col + 1 && existsEgg.getPosition().y == row;
                pos5 = existsEgg.getPosition().x == col && existsEgg.getPosition().y == row - 1;
                pos6 = existsEgg.getPosition().x == col + 1 && existsEgg.getPosition().y == row - 1;
            } else {
                pos1 = existsEgg.getPosition().x == col - 1 && existsEgg.getPosition().y == row + 1;
                pos2 = existsEgg.getPosition().x == col && existsEgg.getPosition().y == row + 1;
                pos3 = existsEgg.getPosition().x == col - 1 && existsEgg.getPosition().y == row;
                pos4 = existsEgg.getPosition().x == col + 1 && existsEgg.getPosition().y == row;
                pos5 = existsEgg.getPosition().x == col - 1 && existsEgg.getPosition().y == row - 1;
                pos6 = existsEgg.getPosition().x == col && existsEgg.getPosition().y == row - 1;
            }

            if(pos1 || pos2 || pos3 || pos4 || pos5 || pos6) {
                creatable = true;
            }
        }

        if(creatable) {
            createEgg(col, row, Egg.Color.random());
        } else {
            System.out.println("Can't create egg");
        }

        return;
    }

    public Egg createEgg(float col, float row, Egg.Color color) {
        boolean shiftPosition = isCurrentRowShiftPosition(row);

        Egg egg = new Egg(col, row, color, shiftPosition, heightOffset);

        eggEntities.add(egg);

        return egg;
    }

    public void removeEggByWorldPosition(Vector2 position) {
        if(position.x > 425 || position.x < 0 || position.y < heightOffset * 50 || position.y > 700) {
            System.out.println("Out of game bound");
            return;
        }

        int row = (int)((position.y - (heightOffset * 50)) / 50);
        boolean shiftPosition = isCurrentRowShiftPosition(row);
        int col = (int)((position.x - (shiftPosition ? 25 : 0)) / 50);

        if(col > 7 || col < 0 || row > 10 || row < 0) {
            System.out.println("Out of game bound");
            return;
        }

        System.out.println(col + ", " + row);

        removeEgg(col, row);
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
                createEgg(col, row, Egg.Color.random());
            }
        }
    }

    public void update(float dt) {
        if(!isPause()) {
            // 1. update egg position
            for (Egg egg : getAllEggs()) {
                egg.update(dt);
            }

            // 2. detect collide with wall and reverse speed
            for (Egg egg : getAllEggs()) {
                Vector2 originEggPosition = egg.getSpritePosition();

                if (originEggPosition.x > 375 || originEggPosition.x < 0) {
                    Vector2 eggSpeed = egg.getSpeed();
                    egg.setSpeed(new Vector2(-eggSpeed.x, eggSpeed.y));
                }
            }

            // 3. detect collide with egg or top, lock egg
            for (Egg egg : getAllEggs()) {
                if(egg.getSpeed().x == 0 && egg.getSpeed().y == 0) {
                    continue;
                }

                Vector2 originEggPosition = egg.getSpritePosition();

                boolean collide = false;
                egg.calcPositionBySpritePosition(shiftBasePosition, heightOffset);

                // Egg?
                for(Egg otherEgg : getAllEggs()) {
                    if(egg == otherEgg) {
                        break;
                    }

                    boolean pos1, pos2, pos3, pos4, pos5, pos6;
                    if(isCurrentRowShiftPosition(egg.getPosition().y)) {
                        pos1 = otherEgg.getPosition().x == egg.getPosition().x && otherEgg.getPosition().y == egg.getPosition().y + 1;
                        pos2 = otherEgg.getPosition().x == egg.getPosition().x + 1 && otherEgg.getPosition().y == egg.getPosition().y + 1;
                        pos3 = otherEgg.getPosition().x == egg.getPosition().x - 1 && otherEgg.getPosition().y == egg.getPosition().y;
                        pos4 = otherEgg.getPosition().x == egg.getPosition().x + 1 && otherEgg.getPosition().y == egg.getPosition().y;
                        pos5 = otherEgg.getPosition().x == egg.getPosition().x && otherEgg.getPosition().y == egg.getPosition().y - 1;
                        pos6 = otherEgg.getPosition().x == egg.getPosition().x + 1 && otherEgg.getPosition().y == egg.getPosition().y - 1;
                    } else {
                        pos1 = otherEgg.getPosition().x == egg.getPosition().x - 1 && otherEgg.getPosition().y == egg.getPosition().y + 1;
                        pos2 = otherEgg.getPosition().x == egg.getPosition().x && otherEgg.getPosition().y == egg.getPosition().y + 1;
                        pos3 = otherEgg.getPosition().x == egg.getPosition().x - 1 && otherEgg.getPosition().y == egg.getPosition().y;
                        pos4 = otherEgg.getPosition().x == egg.getPosition().x + 1 && otherEgg.getPosition().y == egg.getPosition().y;
                        pos5 = otherEgg.getPosition().x == egg.getPosition().x - 1 && otherEgg.getPosition().y == egg.getPosition().y - 1;
                        pos6 = otherEgg.getPosition().x == egg.getPosition().x && otherEgg.getPosition().y == egg.getPosition().y - 1;
                    }

                    if(pos1 || pos2 || pos3 || pos4 || pos5 || pos6) {
                        collide = true;
                        break;
                    }
                }
                
                // Top
                if (originEggPosition.y >= 650) {
                    collide = true;
                }

                if(collide) {
                    egg.setSpeed(new Vector2(0, 0));
                    egg.maintainOnGridPosition(isCurrentRowShiftPosition(egg.getPosition().y), heightOffset);
                    isShooting = false;
                }
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
