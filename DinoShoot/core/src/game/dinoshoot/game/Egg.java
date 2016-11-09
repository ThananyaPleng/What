package game.dinoshoot.game;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import game.dinoshoot.DinoShoot;

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

    private Vector2 position;
    private Color color;

    private Sprite sprite;

    public Egg(float col, float row, Color color, boolean shiftPosition, float heightOffset) {
        this.position = new Vector2(col, row);
        this.color = color;

        // Pick sprite by color
        setSpriteColor();
        // Calculate sprite position
        calcSpritePosition(shiftPosition, heightOffset);
    }

    private void setSpriteColor() {
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
    }

    private void calcSpritePosition(boolean shiftPosition, float heightOffset) {
        if(shiftPosition) {
            sprite.setX(position.x * sprite.getWidth() + 25);
        } else {
            sprite.setX(position.x * sprite.getWidth());
        }
        sprite.setY((position.y * sprite.getHeight()) + (50 * heightOffset));
    }

    public Vector2 getPosition() {
        return position;
    }

    public void setPosition(float col, float row, boolean shiftPosition, float heightOffset) {
        this.position = new Vector2(col, row);
        calcSpritePosition(shiftPosition, heightOffset);
    }

    public void draw(Batch batch) {
        sprite.draw(batch);
    }
}
