package game.dinoshoot.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import game.dinoshoot.utility.PositionHelper;

import java.util.ArrayList;
import java.util.Stack;

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
    private Vector2 shooterPoint = new Vector2(187.5f, 50f);
    private boolean shiftBaseRow = false;
    private int heightOffset = 3;

    public void fireEggToPoint(Vector2 directionPoint) {
        // Check is another egg is firing, if not shoot new egg
        if(isShooting) {
            return;
        }
        isShooting = true;

        // Calculate angle from shooter point to direction point
        float angle = (float) Math.toDegrees(Math.atan2(directionPoint.y - (shooterPoint.y), directionPoint.x - (shooterPoint.x)));
        if(angle < 0) angle += 360;
        if(angle < 1 || angle > 179) {
            System.out.println("Can't shoot below shooter.");
            return;
        }

        // Create egg
        Egg egg = new Egg(0, 0, Egg.Color.random(), shiftBaseRow, heightOffset);
        egg.setSpritePosition(shooterPoint);
        eggEntities.add(egg);
        
        // Set speed
        float vX = (float)(baseEggVelocity * Math.cos(Math.toRadians(angle))),
                vY = (float)(baseEggVelocity * Math.sin(Math.toRadians(angle)));
        egg.setSpeed(new Vector2(vX, vY));
    }

    public void createEggByWorldPosition(Vector2 position) {
        // Check cursor click position
        if(position.x > 425 || position.x < 0 || position.y < heightOffset * 50 || position.y > 700) {
            System.out.println("Out of game bound");
            return;
        }

        // Create Egg for calculation
        Egg egg = new Egg(0, 0, Egg.Color.random(), shiftBaseRow, heightOffset);
        egg.setSpritePosition(position);
        egg.updateGridPositionBySpritePosition(shiftBaseRow, heightOffset);
        egg.lockOnGrid(shiftBaseRow, heightOffset);

        // Check grid position
        float newCol = egg.getGridPosition().x, newRow = egg.getGridPosition().y;
        if(newCol > 7 || newCol < 0 || newRow > 10 || newRow < 0) {
            System.out.println("Out of game bound");
            return;
        }

        System.out.println("Set egg at: (" + newCol + ", " + newRow + ")");

        // Check if any egg has surround exists egg (to lock each others)
        boolean creatable = false;
        for(Egg existsEgg : eggEntities) {
            if(existsEgg.getGridPosition().x == newCol && existsEgg.getGridPosition().y == newRow) {
                System.out.println("Already have egg at that position");
                return;
            }

            boolean pos1, pos2, pos3, pos4, pos5, pos6;
            if(PositionHelper.calcShiftPosition(newRow, shiftBaseRow)) {
                pos1 = existsEgg.getGridPosition().x == newCol - 1 && existsEgg.getGridPosition().y == newRow + 1;
                pos2 = existsEgg.getGridPosition().x == newCol && existsEgg.getGridPosition().y == newRow + 1;
                pos3 = existsEgg.getGridPosition().x == newCol - 1 && existsEgg.getGridPosition().y == newRow;
                pos4 = existsEgg.getGridPosition().x == newCol + 1 && existsEgg.getGridPosition().y == newRow;
                pos5 = existsEgg.getGridPosition().x == newCol - 1 && existsEgg.getGridPosition().y == newRow - 1;
                pos6 = existsEgg.getGridPosition().x == newCol && existsEgg.getGridPosition().y == newRow - 1;
            } else {
                pos1 = existsEgg.getGridPosition().x == newCol && existsEgg.getGridPosition().y == newRow + 1;
                pos2 = existsEgg.getGridPosition().x == newCol + 1 && existsEgg.getGridPosition().y == newRow + 1;
                pos3 = existsEgg.getGridPosition().x == newCol - 1 && existsEgg.getGridPosition().y == newRow;
                pos4 = existsEgg.getGridPosition().x == newCol + 1 && existsEgg.getGridPosition().y == newRow;
                pos5 = existsEgg.getGridPosition().x == newCol && existsEgg.getGridPosition().y == newRow - 1;
                pos6 = existsEgg.getGridPosition().x == newCol + 1 && existsEgg.getGridPosition().y == newRow - 1;
            }

            if(pos1 || pos2 || pos3 || pos4 || pos5 || pos6) {
                creatable = true;
            }
        }

        if(creatable) {
            eggEntities.add(egg);
            System.out.println("New egg at: (" + newCol + ", " + newRow + ")");
        } else {
            System.out.println("Can't create egg");
        }
    }

    public void removeEggByWorldPosition(Vector2 position) {
        // Check cursor click position
        if(position.x > 425 || position.x < 0 || position.y < heightOffset * 50 || position.y > 700) {
            System.out.println("Out of game bound");
            return;
        }

        // Create Mock Egg for calculation
        Egg mockEgg = new Egg(0, 0, Egg.Color.random(), shiftBaseRow, heightOffset);
        mockEgg.setSpritePosition(position);
        mockEgg.updateGridPositionBySpritePosition(shiftBaseRow, heightOffset);
        mockEgg.lockOnGrid(shiftBaseRow, heightOffset);

        // Check grid position
        float newCol = mockEgg.getGridPosition().x, newRow = mockEgg.getGridPosition().y;
        if(newCol > 7 || newCol < 0 || newRow > 10 || newRow < 0) {
            System.out.println("Out of game bound");
            return;
        }

        // Find egg at that grid position
        Egg temp = null;
        for(Egg egg : eggEntities) {
            if(egg.getGridPosition().x == newCol && egg.getGridPosition().y == newRow) {
                temp = egg;
                break;
            }
        }

        if(temp != null) {
            eggEntities.remove(temp);
            System.out.println("Remove egg at: (" + newCol + ", " + newRow + ")");
        } else {
            System.out.println("No egg found.");
        }
    }

    public void setup() {
        for(int row = 5; row < 12; row++) {
            for(int col = 0; col < 8; col++) {
                eggEntities.add(new Egg(col, row, Egg.Color.random(), shiftBaseRow, heightOffset));
            }
        }
    }

    public void update(float dt) {
        if(!isPause()) {
            // For all eggs in game
            for (Egg egg : getAllEggs()) {
                // 1. update egg
                egg.update(dt);
                // END 1.

                // If egg is moving
                if(egg.getSpeed().x != 0 && egg.getSpeed().y != 0) {
                    // 2. detect collide with wall and reverse speed
                    Vector2 originEggPosition = egg.getSpritePosition();

                    if (originEggPosition.x > 400 || originEggPosition.x < 25) {
                        Vector2 eggSpeed = egg.getSpeed();
                        egg.setSpeed(new Vector2(-eggSpeed.x, eggSpeed.y));
                    }
                    // END 2.

                    // 3. detect collide with egg or roof, lock egg into grid
                    boolean collide = false;

                    // Check overlap with another eggs
                    for(Egg otherEgg : getAllEggs()) {
                        // if self, skip
                        if(egg == otherEgg) {
                            continue;
                        }

                        // Calculate distance of another egg
                        if(originEggPosition.dst(otherEgg.getSpritePosition()) <= 45f) {
                            collide = true;
                            break;
                        }
                    }

                    // Check with roof
                    if (originEggPosition.y >= 650) {
                        collide = true;
                    }

                    // If collide, lock egg into grid and set speed to zero
                    if(collide) {
                        egg.setSpeed(new Vector2(0, 0));
                        egg.updateGridPositionBySpritePosition(shiftBaseRow, heightOffset);
                        egg.lockOnGrid(shiftBaseRow, heightOffset);
                        isShooting = false;

                        // TODO 4. check match color and destroy it
                        final ArrayList<Egg> matchLinkedEgg = new ArrayList<Egg>();
                        Stack<Egg> eggsWaitForCheck;
                        Stack<Egg> tempStack = new Stack<Egg>();

                        matchLinkedEgg.add(egg);
                        tempStack.push(egg);

                        boolean swapStack = true;

                        while (swapStack) {
                            swapStack = false;
                            eggsWaitForCheck = tempStack;

                            for(Egg currentEgg : eggsWaitForCheck) {
                                tempStack = new Stack<Egg>();

                                // Check surround current egg if match color
                                float eggCol = currentEgg.getGridPosition().x, eggRow = currentEgg.getGridPosition().y;
                                if(PositionHelper.calcShiftPosition(eggRow, shiftBaseRow)) {
                                    Egg collideEgg;

                                    if((collideEgg = getEggAtGrid(eggCol - 1, eggRow + 1)) != null) {
                                        if(!matchLinkedEgg.contains(collideEgg) && collideEgg.getEggColor() == egg.getEggColor()) {
                                            matchLinkedEgg.add(collideEgg);
                                            tempStack.push(collideEgg);
                                            swapStack = true;
                                        }
                                    }

                                    if((collideEgg = getEggAtGrid(eggCol, eggRow + 1)) != null) {
                                        if(!matchLinkedEgg.contains(collideEgg) && collideEgg.getEggColor() == egg.getEggColor()) {
                                            matchLinkedEgg.add(collideEgg);
                                            tempStack.push(collideEgg);
                                            swapStack = true;
                                        }
                                    }

                                    if((collideEgg = getEggAtGrid(eggCol - 1, eggRow)) != null) {
                                        if(!matchLinkedEgg.contains(collideEgg) && collideEgg.getEggColor() == egg.getEggColor()) {
                                            matchLinkedEgg.add(collideEgg);
                                            tempStack.push(collideEgg);
                                            swapStack = true;
                                        }
                                    }

                                    if((collideEgg = getEggAtGrid(eggCol + 1, eggRow)) != null) {
                                        if(!matchLinkedEgg.contains(collideEgg) && collideEgg.getEggColor() == egg.getEggColor()) {
                                            matchLinkedEgg.add(collideEgg);
                                            tempStack.push(collideEgg);
                                            swapStack = true;
                                        }
                                    }

                                    if((collideEgg = getEggAtGrid(eggCol - 1, eggRow - 1)) != null) {
                                        if(!matchLinkedEgg.contains(collideEgg) && collideEgg.getEggColor() == egg.getEggColor()) {
                                            matchLinkedEgg.add(collideEgg);
                                            tempStack.push(collideEgg);
                                            swapStack = true;
                                        }
                                    }

                                    if((collideEgg = getEggAtGrid(eggCol, eggRow - 1)) != null) {
                                        if(!matchLinkedEgg.contains(collideEgg) && collideEgg.getEggColor() == egg.getEggColor()) {
                                            matchLinkedEgg.add(collideEgg);
                                            tempStack.push(collideEgg);
                                            swapStack = true;
                                        }
                                    }
                                } else {
                                    Egg collideEgg;

                                    if((collideEgg = getEggAtGrid(eggCol, eggRow + 1)) != null) {
                                        if(!matchLinkedEgg.contains(collideEgg) && collideEgg.getEggColor() == egg.getEggColor()) {
                                            matchLinkedEgg.add(collideEgg);
                                            tempStack.push(collideEgg);
                                            swapStack = true;
                                        }
                                    }

                                    if((collideEgg = getEggAtGrid(eggCol + 1, eggRow + 1)) != null) {
                                        if(!matchLinkedEgg.contains(collideEgg) && collideEgg.getEggColor() == egg.getEggColor()) {
                                            matchLinkedEgg.add(collideEgg);
                                            tempStack.push(collideEgg);
                                            swapStack = true;
                                        }
                                    }

                                    if((collideEgg = getEggAtGrid(eggCol - 1, eggRow)) != null) {
                                        if(!matchLinkedEgg.contains(collideEgg) && collideEgg.getEggColor() == egg.getEggColor()) {
                                            matchLinkedEgg.add(collideEgg);
                                            tempStack.push(collideEgg);
                                            swapStack = true;
                                        }
                                    }

                                    if((collideEgg = getEggAtGrid(eggCol + 1, eggRow)) != null) {
                                        if(!matchLinkedEgg.contains(collideEgg) && collideEgg.getEggColor() == egg.getEggColor()) {
                                            matchLinkedEgg.add(collideEgg);
                                            tempStack.push(collideEgg);
                                            swapStack = true;
                                        }
                                    }

                                    if((collideEgg = getEggAtGrid(eggCol, eggRow - 1)) != null) {
                                        if(!matchLinkedEgg.contains(collideEgg) && collideEgg.getEggColor() == egg.getEggColor()) {
                                            matchLinkedEgg.add(collideEgg);
                                            tempStack.push(collideEgg);
                                            swapStack = true;
                                        }
                                    }

                                    if((collideEgg = getEggAtGrid(eggCol + 1, eggRow - 1)) != null) {
                                        if(!matchLinkedEgg.contains(collideEgg) && collideEgg.getEggColor() == egg.getEggColor()) {
                                            matchLinkedEgg.add(collideEgg);
                                            tempStack.push(collideEgg);
                                            swapStack = true;
                                        }
                                    }
                                }
                            }
                        }

                        // if match egg more than 2, destroy all of it
                        if(matchLinkedEgg.size() > 2) {
                            Gdx.app.postRunnable(new Runnable() {
                                @Override
                                public void run() {
                                    for(Egg matchEgg : matchLinkedEgg) {
                                        eggEntities.remove(matchEgg);
                                    }

                                    matchLinkedEgg.clear();
                                }
                            });
                        }
                        // END 4.

                        // TODO 5. check if any egg is dependent (not link with another egg that link to roof)
                    }
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

    public boolean isShiftBaseRow() {
        return shiftBaseRow;
    }

    public Egg getEggAtGrid(float col, float row) {
        for(Egg egg : eggEntities) {
            if(egg.getGridPosition().x == col && egg.getGridPosition().y == row) {
                return egg;
            }
        }
        
        return null;
    }

}
