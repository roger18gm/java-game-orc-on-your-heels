package io.github.roger18gm.java2dgame.screens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.roger18gm.java2dgame.*;

import java.util.ArrayList;
import java.util.Random;

public class GameScreen implements Screen {
    private final Main main;
    private Player player;
    private PlayerInputController inputController;
    private OrthographicCamera camera;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;
    private World world;
    private Box2DDebugRenderer debugRenderer;
    private float tileSize = 48f;
    private Stage stage;
    private Inventory inventory; // inventory system
    public static boolean isPaused = false;
    private ArrayList<HealthItem> healthItems; // list of hearts on screen
    private Texture heartTexture; // heart texture
    private Texture flagTexture; // flag texture
    private float spawnTimer;
    private Random random;
    Array<Enemy> enemies = new Array<>();
    private FlagItem flag; // single flag item

    private Rectangle dropZone; // Drop zone for flag
    public GameScreen(Main main) {
        this.main = main;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1056 / tileSize, 768 / tileSize); // match your tile units
        stage = new Stage(new ScreenViewport());
        world = new World(new Vector2(0, 0), true);
        player = new Player(world, 1, 1);
        inputController = new PlayerInputController(player);

        Texture idleSheet = new Texture(Gdx.files.internal("characters\\Characters(100x100)\\Orc\\Orc\\Orc-Idle.png"));
        Texture walkSheet = new Texture(Gdx.files.internal("characters\\Characters(100x100)\\Orc\\Orc\\Orc-Walk.png"));
        Texture attackSheet = new Texture(Gdx.files.internal("characters\\Characters(100x100)\\Orc\\Orc\\Orc-Attack02.png"));
        Texture hurtSheet = new Texture(Gdx.files.internal("characters\\Characters(100x100)\\Orc\\Orc\\Orc-Hurt.png"));

        // Create some enemies with different positions and animations
        enemies.add(new Enemy(world, player, 5, 5, idleSheet, 6, 1, walkSheet, 8, 1, attackSheet, 6, 1));
        enemies.add(new Enemy(world, player, 12, 10, idleSheet, 6, 1, walkSheet, 8, 1, attackSheet, 6, 1));
        enemies.add(new Enemy(world, player, 20, 3, idleSheet, 6, 1, walkSheet, 8, 1, attackSheet, 6, 1));

        // Initialize the inventory system
        inventory = new Inventory();
    }


    @Override // This method is called when the screen is first shown. Only once
    public void show() {
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(stage); // stage UI gets priority
        multiplexer.addProcessor(new InputAdapter() {
            @Override
            public boolean keyDown(int keycode) {
                return false; // you can add raw game inputs here if needed
            }
        });
        multiplexer.addProcessor(inventory.getStage());
        Gdx.input.setInputProcessor(multiplexer);
        debugRenderer = new Box2DDebugRenderer();
        map = new TmxMapLoader().load("maps/orcsLvlOneV2.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1 / tileSize); // scale tiles to world units
        createCollisionsFromMap();

        // Initialize health items
        healthItems = new ArrayList<>();
        heartTexture = new Texture("heart pixel art 16x16.png"); // Replace with actual heart texture path
        flagTexture = new Texture("tiles/PNG/Props/Flag_A.png");

        // Spawn some initial hearts
        for (int i = 0; i < 5; i++) {
            healthItems.add(HeartSpawner.createRandomHeart(heartTexture, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        }

        flag = FlagItem.createFlag(flagTexture); // Create a flag item

        isPaused = false; // Game starts unpaused
        spawnTimer = 0;   // Initialize spawn timer
    }

    /**
     * Creates static collision bodies from the "Collisions" layer in the Tiled map.
     * Each rectangle in the layer will be converted to a Box2D static body.
     */
    private void createCollisionsFromMap() {
        MapLayer collisionLayer = map.getLayers().get("Collisions");
        if (collisionLayer == null) return;

        for (MapObject object : collisionLayer.getObjects()) {
            if (object instanceof RectangleMapObject) {
                Rectangle rect = ((RectangleMapObject) object).getRectangle();

                BodyDef bodyDef = new BodyDef();
                bodyDef.type = BodyDef.BodyType.StaticBody;
                Body body = world.createBody(bodyDef);

                PolygonShape shape = new PolygonShape();
                shape.setAsBox(rect.width / 2 / tileSize, rect.height / 2 / tileSize,
                    new Vector2((rect.x + rect.width / 2) / tileSize,
                        (rect.y + rect.height / 2) / tileSize),
                    0);

                body.createFixture(shape, 0.0f);
                shape.dispose();
            }
        }
    }

    @Override // This method is called every frame to render the game.
    public void render(float delta) {
        world.step(1/60f, 6, 2);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Update player logic
        inputController.update();
        player.update(delta);
        for (Enemy enemy : enemies) {
            enemy.update(delta);
        }
        spawnTimer += delta;
        if (spawnTimer > 5) { // Spawn a new heart every 5 seconds
            healthItems.add(HeartSpawner.createRandomHeart(heartTexture, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
            spawnTimer = 0; // Reset spawn timer
        }

        // Update stage (UI elements)
        stage.act(delta);

        // --- Draw game world ---
        camera.update();
        renderer.setView(camera);
        renderer.render();
        debugRenderer.render(world, camera.combined);
        main.batch.begin();

        player.render(main.batch);
        for (Enemy enemy : enemies) {
            enemy.draw(main.batch);
        }
        // Render health items
        for (HealthItem heart : healthItems) {
            heart.render(main.batch);
        }
        flag.render(main.batch);
        main.batch.end();

        // --- Draw UI stage ---
        stage.draw();
        inventory.render(delta);
        // Pause example
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            main.setScreen(new MenuScreen(main));
        }
    }

    @Override public void resize(int w, int h) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
    @Override
    public void dispose() {
        map.dispose();
        renderer.dispose();
        world.dispose();
        debugRenderer.dispose();

        // Dispose of health item textures
        for (HealthItem heart : healthItems) {
            heart.dispose();
        }
        heartTexture.dispose();
        flagTexture.dispose();
        inventory.dispose();
    }
}

