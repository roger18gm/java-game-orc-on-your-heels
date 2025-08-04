package io.github.roger18gm.java2dgame;

//package items;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class HealthItem extends BaseItem {
    private int bonusHealth; // Additional health restored by the item

    public HealthItem(int hpRestore, int bonusHealth, String name, String description, float x, float y, float width, float height, Texture texture) {
        super(true, hpRestore, true, name, description, x, y, width, height, texture);
        this.bonusHealth = bonusHealth; // Specific to HealthItem
    }

    public int getBonusHealth() {
        return bonusHealth;
    }

    public void setBonusHealth(int bonusHealth) {
        this.bonusHealth = bonusHealth;
    }

    @Override
    public void applyEffect() {
        int totalHealthRestored = getHpRestore() + bonusHealth;
        System.out.println("Restoring " + totalHealthRestored + " HP using " + getName());
        // Add logic to update player health here
    }

    // Render the health item on the screen
    public void render(SpriteBatch batch) {
        if (canPickup()) {
            batch.draw(getTexture(), getPickupBox().x, getPickupBox().y, getPickupBox().width, getPickupBox().height);
        }
    }

    // Check collision with player
    public boolean isColliding(Rectangle playerBox) {
        System.out.println("Heart PickupBox: " + getPickupBox());
        System.out.println("Player box: " + playerBox);
        return getPickupBox().overlaps(playerBox);
    }

    // Dispose of resources
    public void dispose() {
        if (getTexture() != null) {
            getTexture().dispose();
        }
    }
}
