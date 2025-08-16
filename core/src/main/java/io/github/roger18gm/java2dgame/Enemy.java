package io.github.roger18gm.java2dgame;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;

import static io.github.roger18gm.java2dgame.Player.PPM;

public class Enemy {
    enum State { IDLE, PATROL, CHASE, ATTACK }
    private State state = State.IDLE;
    private Body body;
    private Player player;
    private float speed = 5f;       // movement speed
    private float detectionRange = 5f; // distance (in Box2D meters)
    private float attackRange = 1f;    // attack trigger distance
    // Animations
    private Animation<TextureRegion> idleAnim;
    private Animation<TextureRegion> walkAnim;
    private Animation<TextureRegion> attackAnim;
    private Animation<TextureRegion> currentAnim;
    private float stateTime = 0f;
    private boolean facingLeft = false;

    public Enemy(World world, Player player, float x, float y,
                 Texture idleSheet, int idleCols, int idleRows,
                 Texture walkSheet, int walkCols, int walkRows,
                 Texture attackSheet, int attackCols, int attackRows) {
        this.player = player;

        // --- Create body ---
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x, y);
        body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(16 / PPM, 16 / PPM);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;
        fixtureDef.friction = 0.3f;
        body.createFixture(fixtureDef);
        shape.dispose();

        // --- Animations ---
        idleAnim   = buildAnimation(idleSheet, idleCols, idleRows, 0.2f);
        walkAnim   = buildAnimation(walkSheet, walkCols, walkRows, 0.1f);
        attackAnim = buildAnimation(attackSheet, attackCols, attackRows, 0.07f);

        currentAnim = idleAnim;
    }

    private Animation<TextureRegion> buildAnimation(Texture sheet, int cols, int rows, float frameDuration) {
        TextureRegion[][] tmp = TextureRegion.split(sheet,
            sheet.getWidth() / cols,
            sheet.getHeight() / rows);

        Array<TextureRegion> frames = new Array<>();
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                frames.add(tmp[r][c]);
            }
        }
        return new Animation<>(frameDuration, frames, Animation.PlayMode.LOOP);
    }

    public void update(float delta) {
        stateTime += delta;

        Vector2 enemyPos = body.getPosition();
        Vector2 playerPos = player.getBody().getPosition();
        float dist = enemyPos.dst(playerPos);

        switch(state) {
            case IDLE:
                body.setLinearVelocity(0, 0);
                currentAnim = idleAnim;
                if (dist < detectionRange) state = State.CHASE;
                break;

            case PATROL:
                patrol();
                currentAnim = walkAnim;
                if (dist < detectionRange) state = State.CHASE;
                break;

            case CHASE:
                chasePlayer(playerPos);
                currentAnim = walkAnim;
                if (dist > detectionRange) state = State.PATROL;
                if (dist < attackRange) state = State.ATTACK;
                break;

            case ATTACK:
                body.setLinearVelocity(0, 0);
                currentAnim = attackAnim;
                if (dist > attackRange) state = State.CHASE;
                break;
        }
    }

    private void chasePlayer(Vector2 playerPos) {
        Vector2 enemyPos = body.getPosition();
        Vector2 dir = playerPos.cpy().sub(enemyPos).nor();
        facingLeft = dir.x < 0; // flip if chasing left
        body.setLinearVelocity(dir.scl(speed));
    }

    private void patrol() {
        // Basic left-right patrol demo
        Vector2 vel = body.getLinearVelocity();
        if (Math.abs(vel.x) < 0.01f) {
            body.setLinearVelocity(speed, 0);
            facingLeft = false;
        }
    }

    public void draw(SpriteBatch batch) {
        TextureRegion frame = currentAnim.getKeyFrame(stateTime, true);

        if (facingLeft && !frame.isFlipX()) {
            frame.flip(true, false);
        } else if (!facingLeft && frame.isFlipX()) {
            frame.flip(true, false);
        }

        batch.draw(frame, body.getPosition().x * PPM - frame.getRegionWidth(), body.getPosition().y * PPM - frame.getRegionHeight(), 200,200);
    }

    public Body getBody() {
        return body;
    }
}


