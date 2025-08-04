package io.github.roger18gm.java2dgame;

//package items;
import com.badlogic.gdx.graphics.Texture;

public class RandomItemDrop extends BaseItem {
    public RandomItemDrop(Texture texture) {
        // Generate random values for item attributes
        super(
            Math.random() < 0.5, // Randomly determine if consumable
            (int) (Math.random() * 100), // Random HP restore value between 0 and 100
            true, // Random items are always pickupable
            generateRandomName(), // Randomly generated name
            generateRandomDescription(), // Randomly generated description
            (float) (Math.random() * 500), // Random X position
            (float) (Math.random() * 500), // Random Y position
            50, // Fixed width
            50, // Fixed height
            texture // Texture passed to constructor
        );
    }

    private static String generateRandomName() {
        String[] names = {"Heart", "Potion", "Gem", "Shield", "Sword"};
        return names[(int) (Math.random() * names.length)];
    }

    private static String generateRandomDescription() {
        String[] descriptions = {
            "A magical artifact.",
            "Restores your strength.",
            "A rare collectible.",
            "Provides protection.",
            "Enhances your abilities."
        };
        return descriptions[(int) (Math.random() * descriptions.length)];
    }
}
