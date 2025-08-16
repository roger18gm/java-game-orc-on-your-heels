package io.github.roger18gm.java2dgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.*;

public class Player {
    public enum State {
        IDLE, WALK, ATTACK
    }
    public static float PPM = 48f; // Pixels per meter for Box2D
    private State currentState = State.IDLE;
    private float stateTime; // For animation timing
    private Animation<TextureRegion> idleAnimation;
    private Animation<TextureRegion> walkAnimation;
    private Animation<TextureRegion> attackAnimation;
    private TextureRegion currentFrame;
    private float speed = 200f; // adjust as needed
    private boolean facingLeft = false; // For sprite flipping

    // Physics body if using Box2D
    private Body body;

    public Player(World world, float startX, float startY) {
        // Load separate sheets for each action
        idleAnimation = loadAnimation("characters\\\\Characters(100x100)\\\\Soldier\\\\Soldier\\\\Soldier-Idle.png", 6, 0.2f);
        walkAnimation = loadAnimation("characters\\\\Characters(100x100)\\\\Soldier\\\\Soldier\\\\Soldier-Walk.png", 8, 0.1f);
        attackAnimation = loadAnimation("characters\\\\Characters(100x100)\\\\Soldier\\\\Soldier\\\\Soldier-Attack02.png", 6, 0.07f);

        // Create physics body if needed
        BodyDef bdef = new BodyDef();
        bdef.position.set(startX, startY);
        bdef.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(bdef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(16 / PPM, 16 / PPM);
        body.createFixture(shape, 1.0f);
        shape.dispose();
    }

    private Animation<TextureRegion> loadAnimation(String path, int frameCount, float frameDuration) {
        Texture sheet = new Texture(Gdx.files.internal(path));
        TextureRegion[][] tmp = TextureRegion.split(sheet, sheet.getWidth() / frameCount, sheet.getHeight());
        TextureRegion[] frames = new TextureRegion[frameCount];

        for (int i = 0; i < frameCount; i++) {
            frames[i] = tmp[0][i];
        }
        return new Animation<>(frameDuration, frames);
    }

    public void setMoveInput(boolean left, boolean right, boolean up, boolean down) {
        if (currentState == State.ATTACK) return; // Don't override during attack

        if (left || right || up || down) {
            currentState = State.WALK;
            float vx = (left ? -speed : 0) + (right ? speed : 0);
            float vy = (up ? speed : 0) + (down ? -speed : 0);
            body.setLinearVelocity(vx / PPM, vy / PPM);

            // flip sprite based on direction
            if (vx < 0) facingLeft = true;
            else if (vx > 0) facingLeft = false;
        } else {
            currentState = State.IDLE;
            body.setLinearVelocity(0, 0);
        }
    }

    public void triggerAttack() {
        currentState = State.ATTACK;
        stateTime = 0; // Restart animation
    }

    public Body getBody() {
        return body;
    }
    public void update(float delta) {
        stateTime += delta;
        switch (currentState) {
            case WALK:
                currentFrame = walkAnimation.getKeyFrame(stateTime, true);
                break;
            case ATTACK:
                currentFrame = attackAnimation.getKeyFrame(stateTime, false);
                if (attackAnimation.isAnimationFinished(stateTime)) {
                    currentState = State.IDLE; // Return to idle
                }
                break;
            case IDLE:
            default:
                currentFrame = idleAnimation.getKeyFrame(stateTime, true);
                break;
        }
        // Flip depending on facing
        if (facingLeft && !currentFrame.isFlipX()) {
            currentFrame.flip(true, false);
        } else if (!facingLeft && currentFrame.isFlipX()) {
            currentFrame.flip(true, false);
        }
    }

    public void render(SpriteBatch batch) {
        batch.draw(currentFrame, body.getPosition().x * PPM - currentFrame.getRegionWidth(), body.getPosition().y * PPM - currentFrame.getRegionHeight(), 200,200);
    }

    public Vector2 getPosition() {
        return body.getPosition().scl(PPM); // Convert to pixel coordinates
    }
}



