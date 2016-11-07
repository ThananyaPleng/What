package game.dinoshoot.ui;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector3;

public class Button {

    private Sprite sprite;
    private String name;
    private Runnable clickCallback;

    public Button(Sprite sprite, String name) {
        this.sprite = sprite;
        this.name = name;

        this.clickCallback = new Runnable() {
            @Override
            public void run() {
                System.out.println(Button.this.name);
            }
        };
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Sprite getSprite() {
        return sprite;
    }

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
    }

    public void executeOnClick() {
        clickCallback.run();
    }

    public void setOnClickListener(Runnable runnable) {
        this.clickCallback = runnable;
    }

    public boolean isInside(Vector3 worldCoords) {
        return worldCoords.x >= sprite.getX() && worldCoords.x <= sprite.getX() + sprite.getWidth() &&
                worldCoords.y >= sprite.getY() && worldCoords.y <= sprite.getY() + sprite.getHeight();
    }

}
