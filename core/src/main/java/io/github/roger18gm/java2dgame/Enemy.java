package io.github.roger18gm.java2dgame;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Timer;

public class Enemy implements InputProcessor{
    private Character character;  // Now accepts a character via constructor
    private float x, y;
    private boolean movingLeft = false;
    private boolean movingRight = false;
    private boolean movingUp = false;
    private boolean movingDown = false;
    public boolean attack = false;
    private final int SPEED = 60;
    private static final String WALKING_PATH = "characters\\Characters(100x100)\\Orc\\Orc\\Orc-Walk.png";
    private static final String IDLE_PATH = "characters\\Characters(100x100)\\Orc\\Orc\\Orc-Idle.png";
    private static final String ATTACK_PATH = "characters\\Characters(100x100)\\Orc\\Orc\\Orc-Attack02.png";
    private static final String HURT_PATH = "characters\\Characters(100x100)\\Orc\\Orc\\Orc-Hurt.png";
    public boolean damage = false; // Damage
    public MovePerson movePerson;
    public boolean faceLeft = false;

    public Enemy(Character character) {
        this.character = character;
        this.x = character.GetX();
        this.y = character.GetY();
//        Gdx.input.setInputProcessor(this);
    }

    public float GetX() {
        return character.getBody().getPosition().x;
    }
    public float GetY() {
        return character.getBody().getPosition().y;
    }


    // Called when a key is pressed
    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.LEFT) {
            movingLeft = true; // Start moving left
        } else if (keycode == Input.Keys.RIGHT) {
            movingRight = true; // Start moving right
        } else if (keycode == Input.Keys.UP) {
            movingUp = true; // Start moving up
        } else if (keycode == Input.Keys.DOWN) {
            movingDown = true; // Start moving down
        } else if (keycode == Input.Keys.M){
            attack = true;
        }
        return false;
    }

    // Called when a key is released
    @Override
    public boolean keyUp(int keycode) {
        if (keycode == Input.Keys.LEFT) {
            movingLeft = false; // Stop moving left
        } else if (keycode == Input.Keys.RIGHT) {
            movingRight = false; // Stop moving right
        } else if (keycode == Input.Keys.UP) {
            movingUp = false; // Stop moving up
        } else if (keycode == Input.Keys.DOWN) {
            movingDown = false; // Stop moving down
        } else if (keycode == Input.Keys.M){
            attack = true;
            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    attack = false;
                }
            }, 0.5f);
        }
        return false;
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
        if (damage) {
            // Set hurt animation immediately
            character.SetFilePath(HURT_PATH, 4);
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
