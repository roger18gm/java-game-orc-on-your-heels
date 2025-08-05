package io.github.roger18gm.java2dgame;

import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

public class Character implements Location<Vector2> {
    protected Texture characterSheet;
    private Animation<TextureRegion> animation;
    private float stateTime;
    private int x, y;
    private int frameCols;
    private static final int FRAME_ROWS = 1;
    private String filePath;
    protected boolean facingLeft = false;  // To track if the character is facing left
    protected Body body;
    private World world;

    private int lifePoints;

    // Constructor
    public Character(World world, String filepath, int x, int y, int frameCols, int lifePoints) {
        this.frameCols = frameCols;
        this.filePath = filepath;
        this.lifePoints = lifePoints;
        characterSheet = new Texture(Gdx.files.absolute(filepath));
        createAnimationFrames();

        // Create Box2D body
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x, y);
        body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(15, 15); // Adjust size as needed
//        shape.setAsBox(10, 15); // Adjust size as needed

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1.0f;
        fixtureDef.friction = 0.3f;

        body.createFixture(fixtureDef);
        shape.dispose();
    }

    // Creates the animation frames based on the texture and number of columns
    private void createAnimationFrames() {
        TextureRegion[][] tempFrames = TextureRegion.split(characterSheet,
            characterSheet.getWidth() / frameCols,
            characterSheet.getHeight() / FRAME_ROWS);

        TextureRegion[] animationFrames = new TextureRegion[frameCols * FRAME_ROWS];
        int index = 0;
        for (int i = 0; i < FRAME_ROWS; i++) {
            for (int j = 0; j < frameCols; j++) {
                animationFrames[index++] = tempFrames[i][j];
            }
        }
        // **Change the frame duration for hurt animation specifically**
        if (filePath.contains("Hurt")) {  // Check if it's the hurt animation
            animation = new Animation<>(0.2f, animationFrames); // Slow down hurt animation to 0.2s per frame
        } else {
            animation = new Animation<>(0.1f, animationFrames); // Normal speed for other animations
        }
//        animation = new Animation<>(0.1f, animationFrames); // Create the animation with frame time of 0.1f
        stateTime = 0f; // Reset state time for animation
    }

    // Update the state time (should be called every frame)
    public void update(float deltaTime) {
        stateTime += deltaTime;
    }

    // Render the current frame of the animation
    public void render(SpriteBatch batch) {
        TextureRegion currentFrame = animation.getKeyFrame(stateTime, true); // Get current frame in animation

        // Flip the animation if facing left
        if (facingLeft) {
            currentFrame.flip(true, false);  // Flip horizontally
        }

        batch.draw(currentFrame, body.getPosition().x - currentFrame.getRegionWidth() - 10 , body.getPosition().y - currentFrame.getRegionHeight() - 10, 220, 220); // Draw the current frame of the character
//        batch.draw(currentFrame, body.getPosition().x - currentFrame.getRegionWidth() , body.getPosition().y - currentFrame.getRegionHeight() , 200, 200); // Draw the current frame of the character
//        batch.draw(currentFrame, x, y, 200, 200); // Draw the current frame of the character

        // Reset flip to avoid affecting other frames
        if (facingLeft) {
            currentFrame.flip(true, false);  // Flip back to normal
        }
    }

    public int getLifePoints(){
        return this.lifePoints;
    }
    public void setLifePoints(int lifePoints) {
        this.lifePoints = lifePoints;
    }
    public Body getBody() {
        return body;
    }

    // Dispose the character sheet (texture) to free resources
    public void dispose() {
        characterSheet.dispose();
    }

    // Getter and Setter for position
    public int GetX() {
        return x;
    }

    public void SetX(int x) {
        this.x = x;
    }

    public int GetY() {
        return y;
    }

    public void SetY(int y) {
        this.y = y;
    }

    // Getter and Setter for the file path (also reloads texture and animation frames)
    public String GetFilePath() {
        return this.filePath;
    }

    public void SetFilePath(String filePath, int frameCols) {
        if (!this.filePath.equals(filePath)) {
            this.filePath = filePath;
            this.frameCols = frameCols;

            // Dispose of the old texture if it exists
            if (characterSheet != null) {
                characterSheet.dispose();
            }

            // Load the new texture
            characterSheet = new Texture(Gdx.files.absolute(filePath));

            // Recreate the animation frames with the new texture
            createAnimationFrames();

        }
    }

    // Set the direction the character is facing
    public void setFacingLeft(boolean facingLeft) {
        this.facingLeft = facingLeft;
    }


    // Implement the Location interface methods
    @Override
    public Vector2 getPosition() {
        return body.getPosition();
    }

    @Override
    public float getOrientation() {
        return body.getAngle();
    }

    @Override
    public void setOrientation(float orientation) {
        body.setTransform(body.getPosition(), orientation);
    }

    @Override
    public Location<Vector2> newLocation() {
        return new Character(world, filePath, x, y, frameCols, lifePoints);
    }

    @Override
    public float vectorToAngle(Vector2 vector) {
        return (float) Math.atan2(-vector.x, vector.y);
    }

    @Override
    public Vector2 angleToVector(Vector2 outVector, float angle) {
        outVector.x = -(float) Math.sin(angle);
        outVector.y = (float) Math.cos(angle);
        return outVector;
    }
}

