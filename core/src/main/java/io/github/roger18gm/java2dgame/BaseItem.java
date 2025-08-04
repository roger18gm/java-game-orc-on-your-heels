package io.github.roger18gm.java2dgame;

//package items;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

public class BaseItem {
    private boolean consumable;
    private int hpRestore;
    private boolean canPickup;
    private String name;
    private String description;
    private Rectangle pickupBox;
    private  Texture texture;

    // Full constructor with validation
    public BaseItem(boolean consume, int hpRestore, boolean canPickup, String name, String description, float x, float y, float width, float height, Texture texture) {
        this.consumable = consume;
        this.hpRestore = Math.max(hpRestore, 0); // Prevent negative HP restore
        this.canPickup = canPickup;
        this.name = name;
        this.description = description;
        this.pickupBox = new Rectangle(x, y, width, height);
        this.texture = texture;
//        Texture heart = new Texture("C:\\Users\\austi\\IdeaProjects\\java-game\\core\\src\\main\\java\\items\\heart pixel art 254x254.png");
    }

    // Default constructor for placeholders
    public BaseItem() {
        this.consumable = false;
        this.hpRestore = 0;
        this.canPickup = true;
        this.name = "Default Item";
        this.description = "Default Description";
    }

    public Rectangle getPickupBox() {
        return pickupBox;
    }

    public void setPickupBox(float x, float y, float width, float height) {
        this.pickupBox.set(x, y, width, height);
    }

    public Texture getTexture() {
        return texture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    public boolean isConsumable() {
        return consumable;
    }

    public void setConsumable(boolean consume) {
        this.consumable = consume;
    }

    public int getHpRestore() {
        return hpRestore;
    }

    public void setHpRestore(int hpRestore) {
        if (hpRestore >= 0) {
            this.hpRestore = hpRestore;
        }
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public boolean canPickup() {
        return canPickup;
    }

    public void setCanPickup(boolean canPickup) {
        this.canPickup = canPickup;
    }

    public void applyEffect() {
        // Example logic: Restore HP or trigger special behavior
        if (consumable) {
            System.out.println("+" + hpRestore + " HP.");
        } else {
            System.out.println("This item cannot be consumed.");
        }
    }

    @Override
    public String toString() {
        return "BaseItem{name='" + name + "', description='" + description +
            "', consumable=" + consumable + ", hpRestore=" + hpRestore +
            ", canPickup=" + canPickup + "}";
    }
}
