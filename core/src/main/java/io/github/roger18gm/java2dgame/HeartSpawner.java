package io.github.roger18gm.java2dgame;

//package items;

import com.badlogic.gdx.math.MathUtils;
//import items.HealthItem;
import com.badlogic.gdx.graphics.Texture;

public class HeartSpawner {
    public static HealthItem createRandomHeart(Texture heartTexture, float screenWidth, float screenHeight) {
        float x = MathUtils.random(0, screenWidth - 50); // Random x position, leaving space for heart width
        float y = MathUtils.random(0, screenHeight - 50); // Random y position, leaving space for heart height
//        return new HealthItem(20, 10, "Heart", "Restores 20 HP", x, y, 20, 20, heartTexture);

        HealthItem heart = new HealthItem(20, 10, "Heart", "Restores 20 HP", x, y, 20, 20, heartTexture);
        heart.setPickupBox(x, y, 20, 20);
        return heart;
    }
}
