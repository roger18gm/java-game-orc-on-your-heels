package io.github.roger18gm.java2dgame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.roger18gm.java2dgame.Main;

public class MenuScreen implements Screen {
    private Main main;
    private Music bgMusic;
    private Skin skin;
    private Stage stage;

    public MenuScreen(Main main) {
        this.main = main;
    }

    @Override
    public void show() {

        stage = new Stage(new ScreenViewport());
        bgMusic = Gdx.audio.newMusic(Gdx.files.internal("Sketchbook 2024-11-07.ogg"));
        bgMusic.setLooping(true);
        bgMusic.play();
        skin = new Skin(Gdx.files.internal("ui/skin/plain-james-ui.json"));

        TextButton playButton = new TextButton("Play", skin);
        TextButton exitButton = new TextButton("Exit", skin);

        // Add listeners to buttons
        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                bgMusic.stop();
                main.setScreen(new GameScreen(main));
            }
        });

        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        Image background = new Image(new Texture("OrcRunner.png"));
        background.setFillParent(true);
        stage.addActor(background);

        // Create a table to organize the UI elements
        Table table = new Table();
        table.setFillParent(true);
        table.center();
        table.add(playButton).padBottom(20).row();
        table.add(exitButton);
        // Add the table to the stage
        stage.addActor(table);
        // Let the stage handle input

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 1, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(delta);
        stage.draw();

        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            bgMusic.stop();
            main.setScreen(new GameScreen(main));
        }
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {
        // Make sure this screen no longer handles input
        Gdx.input.setInputProcessor(null);
    }
    @Override public void dispose() {
        bgMusic.dispose();
        stage.dispose();
        skin.dispose();
    }
}
