package io.github.roger18gm.java2dgame;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.ScreenUtils;
import items.Inventory;
import items.HealthItem;
import items.HeartSpawner;
import items.FlagItem;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.graphics.Texture;

import java.util.ArrayList;
import java.util.Iterator;

public class Main extends ApplicationAdapter {
    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer; // For visualizing drop zone
    private Character soldier;
    private MovePerson movePerson; // Character movement instance
    private Background background; // Background rendering
    private Inventory inventory; // Inventory system
    public static boolean isPaused = false;

    private ArrayList<HealthItem> healthItems; // List of hearts on screen
    private Texture heartTexture; // Heart texture
    private Rectangle playerBox; // Player's collision box
    private Texture flagTexture; // Flag texture
    private FlagItem flag; // The single flag item
    private Rectangle dropZone; // Drop zone for flag
    private World world;

    private float spawnTimer; // Timer for heart spawning

    @Override
    public void create() {
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer(); // Initialize ShapeRenderer
        World world = new World(new Vector2(0, 0), true); // No gravity, allow sleeping

        // Initialize player character
        soldier = new Character(world, "C:\\Users\\austi\\IdeaProjects\\java-game\\assets\\characters\\Characters(100x100)\\Soldier\\Soldier\\Soldier-Walk.png", 100, 50, 6, 100);

        // Initialize background
        background = new Background(world);

        // Initialize inventory system
        Skin skin = new Skin(Gdx.files.internal("C:\\Users\\austi\\IdeaProjects\\java-game\\assets\\ui\\uiskin.json"));
        inventory = new Inventory();

        // Initialize player movement logic
        movePerson = new MovePerson(soldier);

        // Initialize health items
        healthItems = new ArrayList<>();
        heartTexture = new Texture("C:\\Users\\austi\\IdeaProjects\\java-game\\core\\src\\main\\java\\items\\heart pixel art 16x16.png");
        flagTexture = new Texture("C:\\Users\\austi\\IdeaProjects\\java-game\\assets\\tiles\\PNG\\Props\\Flag_A.png");

        // Initialize the flag at a specific location
        flag = FlagItem.createFlag(flagTexture);

        // Initialize player's collision box
        playerBox = new Rectangle(100, 100, 50, 50); // Example player position and size

        // Initialize the drop zone
        dropZone = new Rectangle(17, 436, 27, 27); // Example position and size for the drop zone

        // Set up input multiplexer for inventory and player movement
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(inventory.getStage());
        multiplexer.addProcessor(movePerson);
        Gdx.input.setInputProcessor(multiplexer);

        isPaused = false; // Game starts unpaused
        spawnTimer = 0;   // Initialize spawn timer
    }

    @Override
    public void render() {
        float deltaTime = Gdx.graphics.getDeltaTime();

        // Update spawn timer and spawn hearts periodically
        spawnTimer += deltaTime;
        if (spawnTimer > 5) { // Spawn a new heart every 5 seconds
            healthItems.add(HeartSpawner.createRandomHeart(heartTexture, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
            spawnTimer = 0; // Reset spawn timer
        }

        // Collision detection: Check for player pickup (hearts)
        Iterator<HealthItem> iterator = healthItems.iterator();
        while (iterator.hasNext()) {
            HealthItem heart = iterator.next();
            if (heart.isColliding(playerBox)) {
                System.out.println("Collision detected with heart at: " + heart.getPickupBox());
                heart.applyEffect(); // Apply health effect
                iterator.remove();   // Remove heart after pickup
            }
        }

        // Flag interaction logic
        if (flag != null) {
            // Flag pickup logic
            if (flag.isColliding(playerBox) && !flag.isCarried()) {
                System.out.println("Flag picked up!");
                flag.setCarried(true); // Set flag to be carried
            }

            // If the flag is carried, move it with the player
            if (flag.isCarried()) {
                flag.carry(playerBox.x, playerBox.y); // Attach flag to player position
            }

            // Drop the flag if player is in the drop zone and presses SPACE
            if (flag.isCarried() && playerBox.overlaps(dropZone)) {
                if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
                    System.out.println("Flag successfully dropped in the zone!");
                    flag.drop(); // Change flag state to "not carried"
                }
            }
        }

        // Clear screen
        ScreenUtils.clear(1.0f, 0.75f, 0.80f, 1.0f); // RGB (255, 192, 203)

        batch.begin();
        background.render(batch);

        // Render health items
        for (HealthItem heart : healthItems) {
            heart.render(batch);
        }

        // Render the flag (if it exists)
        if (flag != null) {
            flag.render(batch);
        }

        // Render soldier if not paused
        if (!isPaused) {
            soldier.render(batch);
        }
        batch.end();

        // Render the drop zone using ShapeRenderer
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(0, 1, 0, 1); // Green outline
        shapeRenderer.rect(dropZone.x, dropZone.y, dropZone.width, dropZone.height);
        shapeRenderer.end();

        // Update game elements only when not paused
        if (!isPaused) {
            movePerson.update(deltaTime);
            //background.update(deltaTime);
            playerBox.setPosition(playerBox.x + 1, playerBox.y); // Example movement
        }

        // Render inventory elements
        inventory.render(deltaTime);

        // Render the drop zone using ShapeRenderer
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line); // Begin drawing a line shape
        shapeRenderer.setColor(0, 1, 0, 1); // Set color to green (R=0, G=1, B=0, Alpha=1)
        shapeRenderer.rect(dropZone.x, dropZone.y, dropZone.width, dropZone.height); // Draw the rectangle
        shapeRenderer.end(); // Finish drawing
    }

    @Override
    public void dispose() {
        batch.dispose();
        shapeRenderer.dispose(); // Dispose of ShapeRenderer
        soldier.dispose();
        background.dispose();
        inventory.dispose();
        heartTexture.dispose();
        flagTexture.dispose();
        world.dispose();

        // Dispose health item textures
        for (HealthItem heart : healthItems) {
            heart.dispose();
        }

        // Dispose of the flag
        if (flag != null) {
            flag.dispose();
        }
    }
}
