package io.github.roger18gm.java2dgame;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Timer;

import java.awt.event.KeyEvent;


public class MovePerson  implements InputProcessor {

    private Character character;  // Now accepts a character via constructor
    private int x, y;
    protected boolean movingLeft = false;
    protected boolean movingRight = false;
    protected boolean movingUp = false;
    protected boolean movingDown = false;
    public boolean attack = false;

    protected final int SPEED = 60;
    private static final String WALKING_PATH = "characters\\Characters(100x100)\\Soldier\\Soldier\\Soldier-Walk.png";
    private static final String IDLE_PATH = "characters\\Characters(100x100)\\Soldier\\Soldier\\Soldier-Idle.png";
    private static final String ATTACK_PATH = "characters\\Characters(100x100)\\Soldier\\Soldier\\Soldier-Attack02.png";
    private static final String HURT_PATH = "characters\\Characters(100x100)\\Soldier\\Soldier\\Soldier-Hurt.png";
    private static final String DEATH_PATH = "characters\\Characters(100x100)\\Soldier\\Soldier\\Soldier-Death.png";

    public boolean damage = false; // Damage
    public Enemy enemy;

    public boolean faceLeft = false;

    protected int lifePoints;

    public MovePerson(Character character) {
        this.character = character;
//        this.x = character.GetX();
//        this.y = character.GetY();
    }

    public float GetX() {
        return character.getBody().getPosition().x;
    }
    public float GetY() {
        return character.getBody().getPosition().y;
    }

    public int GetLife() {
        return lifePoints;
    }

    public void setLifePoints() {
        lifePoints = lifePoints - 1;
    }


    // Called when a key is pressed
    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.A) {
            movingLeft = true; // Start moving left
        } else if (keycode == Input.Keys.D) {
            movingRight = true; // Start moving right
        } else if (keycode == Input.Keys.W) {
            movingUp = true; // Start moving up
        } else if (keycode == Input.Keys.S) {
            movingDown = true; // Start moving down
        } else if (keycode == Input.Keys.SPACE){
            attack = true;
        }
        return true;
    }
    boolean isAttacking(){
        return attack;
    }

    // Called when a key is released
    @Override
    public boolean keyUp(int keycode) {
        if (keycode == Input.Keys.A) {
            movingLeft = false; // Stop moving left
        } else if (keycode == Input.Keys.D) {
            movingRight = false; // Stop moving right
        } else if (keycode == Input.Keys.W) {
            movingUp = false; // Stop moving up
        } else if (keycode == Input.Keys.S) {
            movingDown = false; // Stop moving down
        }
        else if (keycode == Input.Keys.SPACE){
            attack = true;
            Timer.schedule(new Timer.Task(){
                @Override
                public void run(){
                    attack = false;
                }
            }, 0.5f);
        }
        return true;
    }

    // Update class that moves the character in Render in Main
    public void update(float deltaTime) {
        // new addition
        Vector2 velocity = new Vector2(0,0);
        if (movingLeft) {
//            character.SetX(character.GetX() - 1); // Move Left
            velocity.x = -SPEED;
            character.setFacingLeft(true);           // Set character to face left
        }
        if (movingRight) {
//            character.SetX(character.GetX() + 1); // Move Right
            velocity.x = SPEED;
            character.setFacingLeft(false);          // Set character to face right
        }
        if (movingUp) {
//            character.SetY(character.GetY() + 1); // Move Up
            velocity.y = SPEED;
        }
        if (movingDown) {
//            character.SetY(character.GetY() - 1); // Move Down
            velocity.y = -SPEED;
        }

        character.getBody().setLinearVelocity(velocity);

        // If the character is taking damage, the hurt animation will play
        if (damage && character.getLifePoints() != 0) {
            // Set hurt animation immediately
            character.SetFilePath(HURT_PATH, 4);
            setLifePoints();
        } else {
            // Update the character's movement and animations as normal
            if (movingDown || movingUp || movingLeft || movingRight) {
                character.SetFilePath(WALKING_PATH, 8); // Walking animation
            } else if (attack) {
                character.SetFilePath(ATTACK_PATH, 6); // Attack animation
            } else {
                character.SetFilePath(IDLE_PATH, 6); // Idle animation
            }
        }

        if (character.getLifePoints() == 0) {
            character.SetFilePath(DEATH_PATH, 4);
        }

        character.update(deltaTime);
    }



    // Ignore these. They are here to avoid errors.
    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }

}

