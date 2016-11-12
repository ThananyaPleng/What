package game.dinoshoot.game;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import game.dinoshoot.DinoShoot;
import game.dinoshoot.utility.PositionHelper;

import java.util.Random;

public class Egg {

    public enum Color {
        RED,
        YELLOW,
        ORANGE,
        GREEN,
        PURPLE;

        public static Color random() {
            int rand = new Random().nextInt(5);
            switch (rand) {
                case 0: return RED;
                case 1: return YELLOW;
                case 2: return ORANGE;
                case 3: return GREEN;
                case 4: return PURPLE;
                default: return null;
            }
        }
    }

    // Egg State
    private Color color;
    private Vector2 gridPosition;
    private Vector2 speed = new Vector2(0, 0);

    // Texture & Sprite
    private Sprite sprite;
    private Vector2 originPoint = new Vector2(25, 25);

    public Egg(float col, float row, Color color, boolean shiftBaseRow, float heightOffset) {
        this.gridPosition = new Vector2(col, row);

        // Set color
        setEggColor(color);

        // Lock on Grid
        lockOnGrid(shiftBaseRow, heightOffset);
    }

    public void updateGridPositionBySpritePosition(boolean shiftBaseRow, float heightOffset) {
        int row = (int)((getSpritePosition().y - (heightOffset * 50)) / 50);
        int col = (int)((getSpritePosition().x - (PositionHelper.calcShiftPosition(row, shiftBaseRow) ? 0 : 25)) / 50);

        setGridPosition(col, row);
    }

    public void lockOnGrid(boolean shiftBaseRow, float heightOffset) {
        if(PositionHelper.calcShiftPosition(this.gridPosition.y, shiftBaseRow)) {
            sprite.setX(gridPosition.x * sprite.getWidth());
        } else {
            sprite.setX(gridPosition.x * sprite.getWidth() + 25);
        }
        sprite.setY((gridPosition.y * sprite.getHeight()) + (50 * heightOffset));
    }

    public void setEggColor(Color color) {
        this.color = color;

        AssetManager assetManager = DinoShoot.instance.getAssetManager();
        switch (color) {
            case RED:
                sprite = new Sprite(assetManager.get("img/red.png", Texture.class));
                break;
            case YELLOW:
                sprite = new Sprite(assetManager.get("img/yellow.png", Texture.class));
                break;
            case ORANGE:
                sprite = new Sprite(assetManager.get("img/orange.png", Texture.class));
                break;
            case GREEN:
                sprite = new Sprite(assetManager.get("img/green.png", Texture.class));
                break;
            case PURPLE:
                sprite = new Sprite(assetManager.get("img/purple.png", Texture.class));
                break;
        }

        sprite.setPosition(sprite.getX() - originPoint.x, sprite.getY() - originPoint.y);
    }

    /**
     * Draw a texture.
     * Need to be called every frame.
     * @param batch
     */
    public void draw(Batch batch) {
        boolean isBatchDrawing = batch.isDrawing();

        if(!isBatchDrawing) batch.begin();
        sprite.draw(batch);
        if(!isBatchDrawing) batch.end();
    }

    /**
     * Update sprite gridPosition with speed.
     * Need to be called every frame.
     * @param dt Delta time of frames.
     */
    public void update(float dt) {
        if(getSpeed().x != 0 || getSpeed().y != 0) {
            Vector2 originPosition = getSpritePosition();

            setSpritePosition(new Vector2(originPosition.x + (speed.x * dt), originPosition.y + (speed.y * dt)));
        }
    }

    public Color getEggColor() {
        return color;
    }

    public Vector2 getGridPosition() {
        return gridPosition;
    }

    public void setGridPosition(float col, float row) {
        this.gridPosition = new Vector2(col, row);
    }

    public Vector2 getSpeed() {
        return speed;
    }

    public void setSpeed(Vector2 speed) {
        this.speed = speed;
    }

    public Vector2 getSpritePosition() {
        return new Vector2(sprite.getX() + originPoint.x, sprite.getY() + originPoint.y);
    }

    public void setSpritePosition(Vector2 position) {
        sprite.setPosition(position.x - originPoint.x, position.y - originPoint.y);
    }

}
