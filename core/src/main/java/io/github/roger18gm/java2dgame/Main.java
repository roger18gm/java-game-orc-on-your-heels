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

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter {
    private Stage stage;
    private Skin skin;
    private World world;
    private Box2DDebugRenderer debugRenderer;
    private SpriteBatch batch;
    private Character soldier;
    private MovePerson movePerson;  // Add a MovePerson instance
    private Background background;
    private NPC npc;
    private MoveNPC moveNPC;
    private Character orc;
    private Enemy enemy;
    private Sound sound;
    private Sound attackSoundTwo;
    private Music music;
    // The default is that the soldier hasn't attacked yet
    private boolean attackSoundPlayed = false;
    private Inventory inventory; // inventory system
    public static boolean isPaused = false;
    private ArrayList<HealthItem> healthItems; // list of hearts on screen
    private Texture heartTexture; // heart texture
    private Texture flagTexture; // flag texture
    private float spawnTimer;
    ArrayList<WanderEnemy> wanderEnemies = new ArrayList<>();
    private Random random;

    private FlagItem flag; // single flag item

    private Rectangle dropZone; // Drop zone for flag
    private Texture menuBackgroundImg;


    @Override
    public void create() {

        menuBackgroundImg = new Texture(Gdx.files.internal("OrcRunner.png"));

        // UI components
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        skin = new Skin(Gdx.files.internal("ui/skin/plain-james-ui.json"));
//        skin = new Skin(Gdx.files.internal("ui/uiskin.json"));

        TextButton playButton = new TextButton("Play", skin);
        TextButton exitButton = new TextButton("Exit", skin);

        // Add listeners to buttons
        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Start the game
                startGame();
            }
        });

        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Exit the game
                Gdx.app.exit();
            }
        });

        // Create a table to organize the UI elements
        Table table = new Table();
        table.setFillParent(true);
        table.center();
        table.add(playButton).padBottom(20).row();
        table.add(exitButton);
        // Add the table to the stage
        stage.addActor(table);

        // physics
        world = new World(new Vector2(0,0), true);
        debugRenderer = new Box2DDebugRenderer();
        batch = new SpriteBatch();

        // Create MovePerson instance and pass the soldierWalking character to it
        soldier = new Character(world, "characters\\Characters(100x100)\\Soldier\\Soldier\\Soldier-Walk.png", 100, 50, 6, 5);
        movePerson = new MovePerson(soldier);

        background = new Background(world);

        orc = new Character(world, "characters\\Characters(100x100)\\Orc\\Orc\\Orc-Idle.png", 200, 200, 6, 5);
        enemy = new Enemy(orc);


        npc = new NPC(world, "characters\\Characters(100x100)\\Orc\\Orc\\Orc-Walk.png", 200, 200, 8, 5, soldier);
        moveNPC = new MoveNPC(npc);

        // Music
        sound = Gdx.audio.newSound((Gdx.files.internal("07_human_atk_sword_2.mp3")));
        attackSoundTwo = Gdx.audio.newSound((Gdx.files.internal("06_door_close_1.mp3")));
        music = Gdx.audio.newMusic(Gdx.files.internal("Sketchbook 2024-11-07.ogg"));;

        music.setVolume(0.2f);
        music.setLooping(true);
        music.play();


//        Skin skin = new Skin(Gdx.files.internal("ui\\uiskin.json"));

        // Initialize health items
        healthItems = new ArrayList<>();
        heartTexture = new Texture("heart pixel art 16x16.png"); // Replace with actual heart texture path
        flagTexture = new Texture("tiles/PNG/Props/Flag_A.png");


    }
    void startGame() {
        // Start the game
        System.out.println("Game started!");
        stage.clear(); // Clear the stage

        // Initialize the inventory system
        inventory = new Inventory();

        // Spawn some initial hearts
        for (int i = 0; i < 5; i++) {
            healthItems.add(HeartSpawner.createRandomHeart(heartTexture, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        }

        // Create a drop zone for the flag
        flag = FlagItem.createFlag(flagTexture); // Create a flag item

        // Create a SteeringAgent for the player
        SteeringAgent playerAgent = new SteeringAgent(soldier.getBody());

        random = new Random();
        // Create an enemy character
        for (int e=0; e < random.nextInt(50); e++) {
            wanderEnemies.add(new WanderEnemy(world, 6, "characters\\Characters(100x100)\\Orc\\Orc\\Orc-Idle.png", random.nextInt(950), random.nextInt(700), playerAgent));
        }

        // Add the MovePerson and Enemy InputProcessors to the InputMultiplexer
        InputMultiplexer inputMultiplexer = new InputMultiplexer();

        inputMultiplexer.addProcessor(enemy);
        inputMultiplexer.addProcessor(movePerson);
        inputMultiplexer.addProcessor(inventory.getStage());
        Gdx.input.setInputProcessor(inputMultiplexer);
        Gdx.app.log("Main", "InputProcessors added: MovePerson & Enemy");

        isPaused = false; // Game starts unpaused
        spawnTimer = 0;   // Initialize spawn timer
    }


    @Override
    public void render() {
        ScreenUtils.clear(1.0f, 0.75f, 0.80f, 1.0f); // RGB (255, 192, 203) -> (1.0, 0.75, 0.80)

        if (Gdx.input.getInputProcessor() == stage) {

            batch.begin();
            batch.draw(menuBackgroundImg, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            batch.end();

            // Draw the stage
            stage.act();
            stage.draw();

        } else {
            float deltaTime = Gdx.graphics.getDeltaTime();
            // Call MovePerson's Move() method to update the character's position
            movePerson.update(deltaTime);
            npc.update(deltaTime);
            enemy.update(deltaTime);

//            // Collision detection: Check for player pickup
//            Iterator<HealthItem> iterator = healthItems.iterator();
//            while (iterator.hasNext()) {
//                HealthItem heart = iterator.next();
//                if (heart.isColliding(playerBox)) {
//                    System.out.println("Collision detected with heart at: " +heart.getPickupBox());
//                    heart.applyEffect(); // Apply health effect
//                    iterator.remove();   // Remove heart after pickup
//                }
//            }
//

// Update game elements only when not paused
            if (!isPaused) {

                // Move_person damage
                if (enemy.attack) {
                    float distance = Math.abs(movePerson.GetX() - enemy.GetX());
                    if (distance < 56) {
                        if (movePerson.faceLeft && !enemy.faceLeft || !movePerson.faceLeft && enemy.faceLeft) {
                            movePerson.damage = true;
                        }
                    }
                } else {
                    movePerson.damage = false;
                }

                // Simplified if statement for enemy damage
                if (movePerson.attack) {
                    float distance = Math.abs(enemy.GetX() - movePerson.GetX());
                    if (distance < 56) {
                        if (enemy.faceLeft && !movePerson.faceLeft || !enemy.faceLeft && movePerson.faceLeft) {
                            enemy.damage = true;
                        }
                    }
                } else {
                    enemy.damage = false;
                }

                // Plays sword sound only once per click
                if (movePerson.isAttacking() && !attackSoundPlayed) {
                    sound.play(1.0f); // Play sound only once per click
                    attackSoundPlayed = true; // Because the soldier has attacked once it won't repeat the sound
                } else if (!movePerson.isAttacking()) {
                    attackSoundPlayed = false; // Reset so sound can play on the next attack
                }
//                if (enemy.attack && !attackSoundPlayed) {
//                    attackSoundTwo.play(1.0f); // Play sound only once per click
//                    attackSoundPlayed = true; // Because the soldier has attacked once it won't repeat the sound
//                } else if (!enemy.attack) {
//                    attackSoundPlayed = false; // Reset so sound can play on the next attack
//                }

                for (WanderEnemy e: wanderEnemies) {
                    e.update(deltaTime);
                }
//            THIS IS A NO-NO~~~~~~moveNPC.update(deltaTime);~~~~~~~~

//                if (movePerson.GetX() <= enemy.GetX() && enemy.attack) {
//                    movePerson.damage = true;
//                } else {
//                    movePerson.damage = false;
//                }

                // Update spawn timer and spawn hearts periodically
                spawnTimer += deltaTime;
                if (spawnTimer > 5) { // Spawn a new heart every 5 seconds
                    healthItems.add(HeartSpawner.createRandomHeart(heartTexture, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
                    spawnTimer = 0; // Reset spawn timer
                }

                Rectangle soldierRectangle = new Rectangle(soldier.getPosition().x, soldier.getPosition().y, 1, 1);

                // Flag interaction logic
                if (flag != null) {
                    // Flag pickup logic
                    if (flag.isColliding(soldierRectangle) && !flag.isCarried()) {
                        System.out.println("Flag picked up!");
                        flag.setCarried(true); // Set flag to be carried
                    }

                    // If the flag is carried, move it with the player
                    if (flag.isCarried()) {
                        flag.carry(soldier.getPosition().x, soldier.getPosition().y); // Attach flag to player position
                    }


                    // Drop the flag if player is in the drop zone and presses SPACE
                    if (flag.isCarried() && soldierRectangle.overlaps(dropZone)) {
                        if (Gdx.input.isKeyJustPressed(Input.Keys.X)) {
                            System.out.println("Flag successfully dropped in the zone!");
                            flag.drop(); // Change flag state to "not carried"
                        }
                    }
                }

            }

// Render game objects
            batch.begin();
            background.render(batch);
            if (!isPaused) {
                soldier.render(batch); // Render soldier only when not paused
                orc.render(batch);
                npc.render(batch);
            }

            for (WanderEnemy e: wanderEnemies) {
                e.render(batch);
            }

            // Render health items
            for (HealthItem heart : healthItems) {
                heart.render(batch);
            }
            batch.end();
            // Render inventory elements
            inventory.render(deltaTime);

            world.step(1 / 60f, 6, 2); // Step the physics world
//            debugRenderer.render(world, batch.getProjectionMatrix());
        }
    }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
        batch.dispose();
        soldier.dispose();
        orc.dispose();
        background.dispose();
        world.dispose();
        debugRenderer.dispose();
        music.dispose();
        sound.dispose();
        npc.dispose();
//        enemy.dispose();
//        movePerson.dispose();
//        moveNPC.dispose();
        inventory.dispose();

        for (WanderEnemy e: wanderEnemies   ) {
            e.dispose();
        }

        // Dispose of health item textures
        for (HealthItem heart : healthItems) {
            heart.dispose();
        }
        heartTexture.dispose();
        flagTexture.dispose();
        menuBackgroundImg.dispose();


    }
}
