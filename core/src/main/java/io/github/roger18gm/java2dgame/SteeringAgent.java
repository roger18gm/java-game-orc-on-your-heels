package io.github.roger18gm.java2dgame;

import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public class SteeringAgent implements Steerable<Vector2> {
    private final Body body;
    private float maxSpeed = 10000;
    private float maxLinearAcceleration = 10000f;
    private float maxAngularSpeed = 500f;
    private float maxAngularAcceleration = 500f;
    private boolean tagged;

    public SteeringAgent(Body body) {
        this.body = body;
    }

    @Override
    public Vector2 getLinearVelocity() {
        return body.getLinearVelocity();
    }

    @Override
    public float getAngularVelocity() {
        return 0;
    }

    @Override
    public float getBoundingRadius() {
        return 1f;  // Adjust this value based on your character's size
    }

    @Override
    public boolean isTagged() {
        return tagged;
    }

    @Override
    public void setTagged(boolean tagged) {
        this.tagged = tagged;
    }

    @Override
    public float getZeroLinearSpeedThreshold() {
        return 0.001f;
    }

    @Override
    public void setZeroLinearSpeedThreshold(float v) {
        // Not needed for basic behaviors
    }

    @Override
    public float getMaxLinearSpeed() {
        return maxSpeed;
    }

    @Override
    public void setMaxLinearSpeed(float speed) {
        this.maxSpeed = speed;
    }

    @Override
    public float getMaxLinearAcceleration() {
        return maxLinearAcceleration;
    }

    @Override
    public void setMaxLinearAcceleration(float acceleration) {
        this.maxLinearAcceleration = acceleration;
    }

    @Override
    public float getMaxAngularSpeed() {
        return maxAngularSpeed;
    }

    @Override
    public void setMaxAngularSpeed(float speed) {
        this.maxAngularSpeed = speed;
    }

    @Override
    public float getMaxAngularAcceleration() {
        return maxAngularAcceleration;
    }

    @Override
    public void setMaxAngularAcceleration(float acceleration) {
        this.maxAngularAcceleration = acceleration;
    }

    @Override
    public Vector2 getPosition() {
        return body.getPosition();
    }

    @Override
    public float getOrientation() {
        Vector2 velocity = getLinearVelocity();
        if (velocity.isZero()) return body.getAngle();
        return MathUtils.atan2(velocity.y, velocity.x);

    }

    @Override
    public void setOrientation(float orientation) {
        body.setTransform(body.getPosition(), orientation);
    }

    @Override
    public float vectorToAngle(Vector2 vector2) {
        return MathUtils.atan2(vector2.y, vector2.x);
    }

    @Override
    public Vector2 angleToVector(Vector2 outVector, float angle) {
        outVector.set(MathUtils.cos(angle), MathUtils.sin(angle));
        return outVector;
    }

    @Override
    public Location<Vector2> newLocation() {
        return new SimpleLocation();
    }

    private static class SimpleLocation implements Location<Vector2> {
        private Vector2 position = new Vector2();
        private float orientation;

        @Override
        public Vector2 getPosition() {
            return position;
        }

        @Override
        public float getOrientation() {
            return orientation;
        }

        @Override
        public void setOrientation(float orientation) {
            this.orientation = orientation;
        }

        @Override
        public float vectorToAngle(Vector2 vector) {
            return MathUtils.atan2(vector.y, vector.x);
        }

        @Override
        public Vector2 angleToVector(Vector2 outVector, float angle) {
            outVector.set(MathUtils.cos(angle), MathUtils.sin(angle));
            return outVector;
        }

        @Override
        public Location<Vector2> newLocation() {
            return new SimpleLocation();
        }
    }
}
