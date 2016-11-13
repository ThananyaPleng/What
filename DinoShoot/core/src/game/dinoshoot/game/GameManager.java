package game.dinoshoot.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import game.dinoshoot.utility.MathHelper;
import game.dinoshoot.utility.PositionHelper;

import java.util.ArrayList;
import java.util.Stack;

@SuppressWarnings("Duplicates")
public class GameManager {

    public enum State {
        SETUP,
        PLAYING,
        PAUSE,
        GAMEOVER
    }

    public enum Level {
        HARD,
        MEDIUM,
        EASY
    }

    // Game State
    private State state = State.SETUP;
    private Level level = null;
    private Egg eggWaitToShoot;
    private Egg eggShooting;

    private ArrayList<Egg> matchColorEgg = new ArrayList<Egg>();
    private Stack<Egg> eggsWaitForCheck = new Stack<Egg>();
    private ArrayList<Egg> notLinkedEgg = new ArrayList<Egg>();

    private int missingCount = 0;

    // Game Objects
    private ArrayList<Egg> eggEntities = new ArrayList<Egg>();

    // Game options
    private float baseEggVelocity = 1100f;
    private boolean shiftRowZero = false;
    private Vector2 shooterOriginPoint = new Vector2(212.5f, 50f);
    private int heightOffset = 3;

    public void fireEggToPoint(Vector2 directionPoint) {
        // Check is another egg is firing, if not shoot new egg
        if(eggShooting != null) {
            return;
        }

        // Check cursor click position
        if(isInGameArea(directionPoint)) {
            System.out.println("Out of game bound");
            return;
        }

        // Calculate angle from shooter point to direction point
        float angle = MathHelper.calculateDegreeBetween2Point(shooterOriginPoint, directionPoint);
        if(angle < 1 || angle > 179) {
            System.out.println("Can't shoot below shooter.");
            return;
        }

        // Start shoot egg
        eggShooting = eggWaitToShoot;
        eggWaitToShoot = null;

        // Set speed
        eggShooting.setSpeed(MathHelper.calculateVelocityToDirection(angle, baseEggVelocity));
    }

    public void createEggByWorldPosition(Vector2 position) {
        // Check cursor click position
        if(isInGameArea(position)) {
            System.out.println("Out of game bound");
            return;
        }

        // Create Egg for calculation
        Egg egg = new Egg(0, 0, Egg.Color.random(), shiftRowZero, heightOffset);
        egg.setSpritePosition(position);
        egg.updateGridPositionBySpritePosition(shiftRowZero, heightOffset);
        egg.lockOnGrid(shiftRowZero, heightOffset);

        // Check grid position
        float newCol = egg.getGridPosition().x, newRow = egg.getGridPosition().y;
        if(isValidGrid(newCol, newRow)) {
            System.out.println("Out of game bound");
            return;
        }

        System.out.println("Point egg at: (" + newCol + ", " + newRow + ")");

        // Check if any egg has surround exists egg (to lock each others)
        boolean creatable = false;
        for(Egg existsEgg : eggEntities) {
            // Check if that position have exists egg
            if(existsEgg.getGridPosition().x == newCol && existsEgg.getGridPosition().y == newRow) {
                System.out.println("Already have egg at that position");
                return;
            }

            boolean pos1, pos2, pos3, pos4, pos5, pos6;
            if(PositionHelper.calcShiftPosition(newRow, shiftRowZero)) {
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

        // Hit roof
        if(newRow == 10) {
            creatable = true;
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
        if(isInGameArea(position)) {
            System.out.println("Out of game bound");
            return;
        }

        // Create Mock Egg for calculation
        Egg mockEgg = new Egg(0, 0, Egg.Color.random(), shiftRowZero, heightOffset);
        mockEgg.setSpritePosition(position);
        mockEgg.updateGridPositionBySpritePosition(shiftRowZero, heightOffset);
        mockEgg.lockOnGrid(shiftRowZero, heightOffset);

        // Check grid position
        float newCol = mockEgg.getGridPosition().x, newRow = mockEgg.getGridPosition().y;
        if(isValidGrid(newCol, newRow)) {
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

    private void prepareEggToShoot() {
        if(eggWaitToShoot == null) {
            // Create egg
            eggWaitToShoot = new Egg(0, 0, Egg.Color.random(), shiftRowZero, heightOffset);
            eggWaitToShoot.setSpritePosition(shooterOriginPoint);
            eggEntities.add(eggWaitToShoot);
        }
    }

    private boolean isInGameArea(Vector2 point) {
        return point.x > 425 || point.x < 0 || point.y < 0 || point.y > 700;
    }

    private boolean isValidGrid(float col, float row) {
        return col > 7 || col < 0 || row > 10 || row < 0;
    }

    public void setup() {
        for(int row = 5; row <= 10; row++) {
            for(int col = 0; col < 8; col++) {
                eggEntities.add(new Egg(col, row, Egg.Color.random(), shiftRowZero, heightOffset));
            }
        }

        prepareEggToShoot();

        state = State.PLAYING;
    }

    public void update(float dt) {
        if(state == State.PLAYING) {
            // For all eggs in game
            for (Egg egg : getAllEggs()) {
                // Before 1. if have any egg at row 0, game over
                // Skip egg that is shooting or wait to shoot
                if(!(egg == eggWaitToShoot || egg == eggShooting) && egg.getGridPosition().y == 0) {
                    state = State.GAMEOVER;
                }

                // 1. update egg
                egg.update(dt);
                // END 1.
            }

            // If egg is shooting
            if(eggShooting != null) {
                // 2. detect collide with wall and reverse speed
                Vector2 originEggPosition = eggShooting.getSpritePosition();

                // Refection
                if (originEggPosition.x > 400 || originEggPosition.x < 25) {
                    Vector2 eggSpeed = eggShooting.getSpeed();
                    eggShooting.setSpeed(new Vector2(-eggSpeed.x, eggSpeed.y));
                }
                // END 2.

                // 3. detect collide with egg or roof, lock egg into grid
                boolean collide = false;

                // Check overlap with another eggs
                for(Egg otherEgg : getAllEggs()) {
                    // if self, skip
                    if(eggShooting == otherEgg) {
                        continue;
                    }

                    // Calculate distance of another egg
                    if(originEggPosition.dst(otherEgg.getSpritePosition()) <= 50f) {
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
                    eggShooting.setSpeed(new Vector2(0, 0));
                    eggShooting.updateGridPositionBySpritePosition(shiftRowZero, heightOffset);
                    eggShooting.lockOnGrid(shiftRowZero, heightOffset);

                    // Clear last collide state (after post runnable is run)
                    matchColorEgg.clear();
                    notLinkedEgg.clear();

                    // 4. check match color and destroy it
                    matchColorEgg.add(eggShooting);
                    eggsWaitForCheck.push(eggShooting);

                    // Iteration until no egg to check (no more linked egg)
                    while(!eggsWaitForCheck.isEmpty()) {
                        Egg currentEgg = eggsWaitForCheck.pop();

                        // Check surround current egg if match color
                        float eggCol = currentEgg.getGridPosition().x, eggRow = currentEgg.getGridPosition().y;
                        Egg collideEgg;

                        for(Vector2 cPosition : PositionHelper.calcSurroundPositions(eggCol, eggRow, shiftRowZero)) {
                            if ((collideEgg = getEggAtGrid(cPosition.x, cPosition.y)) != null) {
                                if (!matchColorEgg.contains(collideEgg) && collideEgg.getEggColor() == eggShooting.getEggColor()) {
                                    matchColorEgg.add(collideEgg);
                                    eggsWaitForCheck.push(collideEgg);
                                }
                            }
                        }
                    }
                    // END 4.

                    // 5. check if any egg is dependent (not link with another egg that link to roof)
                    if(matchColorEgg.size() > 2) {
                        for (Egg theEgg : eggEntities) {
                            // skip destroyed egg or self
                            if (matchColorEgg.contains(theEgg) || eggShooting == theEgg) {
                                continue;
                            }

                            ArrayList<Egg> checkedEggList = new ArrayList<Egg>();
                            Egg highestEgg = theEgg;
                            eggsWaitForCheck.push(theEgg);

                            while (!eggsWaitForCheck.isEmpty() && highestEgg.getGridPosition().y != 10) {
                                // Check surround current egg
                                Egg nextEgg = eggsWaitForCheck.pop();
                                float eggCol = nextEgg.getGridPosition().x, eggRow = nextEgg.getGridPosition().y;
                                Egg collideEgg;

                                for(Vector2 cPosition : PositionHelper.calcSurroundPositions(eggCol, eggRow, shiftRowZero)) {
                                    if ((collideEgg = getEggAtGrid(cPosition.x, cPosition.y)) != null) {
                                        if (!(matchColorEgg.contains(collideEgg) || notLinkedEgg.contains(collideEgg) || checkedEggList.contains(collideEgg))) {
                                            checkedEggList.add(collideEgg);
                                            eggsWaitForCheck.push(collideEgg);

                                            if(collideEgg.getGridPosition().y > highestEgg.getGridPosition().y) {
                                                highestEgg = collideEgg;
                                            }
                                        }
                                    }
                                }
                            }

                            eggsWaitForCheck.clear();

                            if (highestEgg.getGridPosition().y != 10) {
                                notLinkedEgg.add(theEgg);
                            }
                        }
                    }
                    // End 5.

                    // 6.
                    // Post runnable for prevent ConCurrentException
                    Gdx.app.postRunnable(new Runnable() {
                        @Override
                        public void run() {
                            // if match linked egg more than 2, destroy all of it
                            if(matchColorEgg.size() > 2) {
                                for (Egg matchEgg : matchColorEgg) {
                                    eggEntities.remove(matchEgg);
                                }

                                // Destroy all of not linked to roof, egg
                                // Post runnable for prevent ConCurrentException
                                for(Egg matchEgg : notLinkedEgg) {
                                    eggEntities.remove(matchEgg);
                                }
                            }

                            // 7. prepared new egg to shoot
                            prepareEggToShoot();
                        }
                    });

                    // 8. Update score

                    // 9. Reset shooting egg
                    eggShooting = null;

                    // TODO 10. if all egg is destroyed, move down 3 row
                } else {
                    // TODO 4. continue counting missing

                    // TODO 5. check if missing shot count is over threshold of current level, move down
                }
            }
        }
    }

    public ArrayList<Egg> getAllEggs() {
        return eggEntities;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public int getHeightOffset() {
        return heightOffset;
    }

    public void setHeightOffset(int heightOffset) {
        this.heightOffset = heightOffset;
    }

    public boolean isShiftRowZero() {
        return shiftRowZero;
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
