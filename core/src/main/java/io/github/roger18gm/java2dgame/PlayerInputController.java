package io.github.roger18gm.java2dgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;

public class PlayerInputController {
    private Player player;
    private Sound attackSoundOne;
    private boolean attackSoundPlayed = false;

    public PlayerInputController(Player player) {
        this.player = player;
        attackSoundOne = Gdx.audio.newSound((Gdx.files.internal("07_human_atk_sword_2.mp3")));
    }

    public void update() {
        boolean left  = Gdx.input.isKeyPressed(Input.Keys.A);
        boolean right = Gdx.input.isKeyPressed(Input.Keys.D);
        boolean up    = Gdx.input.isKeyPressed(Input.Keys.W);
        boolean down  = Gdx.input.isKeyPressed(Input.Keys.S);

        player.setMoveInput(left, right, up, down);

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            player.triggerAttack();
            attackSoundOne.play(1.0f); // Play sound only once per click
            attackSoundPlayed = true; // Because the soldier has attacked once it won't repeat the sound
        }
    }
}

