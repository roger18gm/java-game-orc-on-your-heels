package io.github.roger18gm.java2dgame;

import com.badlogic.gdx.*;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import io.github.roger18gm.java2dgame.screens.MenuScreen;

public class Main extends Game {

    public SpriteBatch batch;  // Shared among screens for drawing

    @Override
    public void create() {
        batch = new SpriteBatch();

        // Start with the menu screen
        setScreen(new MenuScreen(this));
    }

    @Override
    public void render() {
        super.render(); // Delegates render/update calls to the active screen
    }

    @Override
    public void dispose() {
        batch.dispose();
        getScreen().dispose(); // Dispose current screen's resources
    }
}

