package io.github.roger18gm.java2dgame.movement;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import io.github.roger18gm.java2dgame.entities.Entity;

public class PlayerMovement implements Movement {
    public void move(Entity entity) {
        int dx = getInputX(); // Assume a method that reads input
        int dy = getInputY();
        entity.setX(entity.getX() + dx * entity.getSpeed());
        entity.setY(entity.getY() + dy * entity.getSpeed());
    }

    private int getInputX() {
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) return -1;
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) return 1;
        return 0;
    }

    private int getInputY() {
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) return -1;
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) return 1;
        return 0;
    }

}
