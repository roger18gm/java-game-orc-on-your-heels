package io.github.roger18gm.java2dgame;

public class MoveNPC extends MovePerson {
    private NPC npc;

    public MoveNPC(NPC npc) {
        super(npc);
    }

    // Update class that moves the character in Render in Main
    public void update(float deltaTime) {
        // new addition
        if (movingLeft) {
            npc.body.setLinearVelocity(-SPEED, npc.body.getLinearVelocity().y);
            npc.facingLeft = true;
        }
        if (movingRight) {
            npc.body.setLinearVelocity(SPEED, npc.body.getLinearVelocity().y);
            npc.facingLeft = false;
        }
        if (movingUp) {
            npc.body.setLinearVelocity(npc.body.getLinearVelocity().x, SPEED);
        }
        if (movingDown) {
            npc.body.setLinearVelocity(npc.body.getLinearVelocity().x, -SPEED);
        }
        if (!movingLeft && !movingRight) {
            npc.body.setLinearVelocity(0, npc.body.getLinearVelocity().y);
        }
        if (!movingUp && !movingDown) {
            npc.body.setLinearVelocity(npc.body.getLinearVelocity().x, 0);
        }
    }
}
