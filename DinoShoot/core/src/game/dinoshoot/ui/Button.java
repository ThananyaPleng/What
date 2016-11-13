package game.dinoshoot.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class Button {

    private Sprite sprite;
    private Runnable clickCallback;

    public Button(Texture texture, Vector2 size, Vector2 position) {
        this.sprite = new Sprite(texture);

        this.clickCallback = new Runnable() {
            @Override
            public void run() {

            }
        };

        setPosition(position);
        setSize(size);
    }

    public Button(Texture texture, Vector2 position, Runnable callback) {
        this.sprite = new Sprite(texture);
        this.clickCallback = callback;

        setPosition(position);
    }

    public Button(Texture texture, Vector2 size, Vector2 position, Runnable callback) {
        this.sprite = new Sprite(texture);
        this.clickCallback = callback;

        setPosition(position);
        setSize(size);
    }

    public void executeOnClick() {
        clickCallback.run();
    }

    public void setOnClickListener(Runnable runnable) {
        this.clickCallback = runnable;
    }

    public void setPosition(Vector2 position) {
        sprite.setPosition(position.x, position.y);
    }

    public Vector2 getPosition() {
        return new Vector2(sprite.getX(), sprite.getY());
    }

    public void setSize(Vector2 size) {
        sprite.setSize(size.x, size.y);
    }

    public Vector2 getSize() {
        return new Vector2(sprite.getWidth(), sprite.getHeight());
    }

    public boolean isInside(Vector2 point) {
        return point.x >= sprite.getX() && point.x <= sprite.getX() + sprite.getWidth() &&
                point.y >= sprite.getY() && point.y <= sprite.getY() + sprite.getHeight();
    }

    public void draw(Batch batch) {
        boolean isBatchDrawing = batch.isDrawing();

        if(!isBatchDrawing) batch.begin();
        sprite.draw(batch);
        if(!isBatchDrawing) batch.end();
    }

}
