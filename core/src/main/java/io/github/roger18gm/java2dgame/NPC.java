package io.github.roger18gm.java2dgame;
import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.steer.SteeringAcceleration;
import com.badlogic.gdx.ai.steer.SteeringBehavior;
import com.badlogic.gdx.ai.steer.behaviors.Arrive;
import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;


public class NPC extends Character implements Steerable<Vector2> {
    private final SteeringBehavior<Vector2> steeringBehavior;
    private final SteeringAcceleration<Vector2> steeringOutput;

    protected float linearSpeed;
    protected float linearAcceleration;

    public NPC(World world, String filepath, int x, int y, int frameCols, int lifePoints,  Character target) {
        super(world, filepath, x, y, frameCols, lifePoints);

        // Create a steering behavior to follow the target character
        steeringBehavior = new Arrive<>(this, target)
            .setTimeToTarget(0.1f)
            .setArrivalTolerance(0.001f)
            .setDecelerationRadius(1);

        steeringOutput = new SteeringAcceleration<>(new Vector2());

        // Set maximum speeds and accelerations

        this.linearSpeed = 60000;
        this.linearAcceleration = 50000;
        setMaxLinearSpeed(linearSpeed);
        setMaxLinearAcceleration(linearAcceleration);
    }


    public void update(float deltaTime) {
        super.update(deltaTime);

        // Calculate the steering output
        steeringBehavior.calculateSteering(steeringOutput);

        // Apply the steering output to the NPC's body
        body.applyForceToCenter(steeringOutput.linear, true);
    }

    public void render(SpriteBatch batch) {
        super.render(batch);
    }

    public void dispose() {
        characterSheet.dispose();
    }

    // Implement the Steerable interface methods
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
    public float vectorToAngle(Vector2 vector2) {
        return 0;
    }

    @Override
    public Vector2 angleToVector(Vector2 vector2, float v) {
        return null;
    }

    @Override
    public Location<Vector2> newLocation() {
        return null;
    }

    @Override
    public Vector2 getLinearVelocity() {
        return body.getLinearVelocity();
    }

    @Override
    public float getAngularVelocity() {
        return body.getAngularVelocity();
    }

    @Override
    public float getBoundingRadius() {
        return 1.0f;
    }

    @Override
    public boolean isTagged() {
        return false;
    }

    @Override
    public void setTagged(boolean tagged) {
    }

    @Override
    public float getZeroLinearSpeedThreshold() {
        return 0.001f;
    }

    @Override
    public void setZeroLinearSpeedThreshold(float value) {
    }

    @Override
    public float getMaxLinearSpeed() {
        return linearSpeed;
    }

    @Override
    public void setMaxLinearSpeed(float maxLinearSpeed) {
        this.linearSpeed = maxLinearSpeed;
    }

    @Override
    public float getMaxLinearAcceleration() {
        return linearAcceleration;
    }

    @Override
    public void setMaxLinearAcceleration(float maxLinearAcceleration) {
        this.linearAcceleration = maxLinearAcceleration;
    }

    @Override
    public float getMaxAngularSpeed() {
        return 30.0f;
    }

    @Override
    public void setMaxAngularSpeed(float maxAngularSpeed) {
    }

    @Override
    public float getMaxAngularAcceleration() {
        return 10.0f;
    }

    @Override
    public void setMaxAngularAcceleration(float maxAngularAcceleration) {
    }

//    @Override
    public Vector2 getLinearAcceleration() {
        return steeringOutput.linear;
    }

//    @Override
    public float getAngularAcceleration() {
        return steeringOutput.angular;
    }
}
