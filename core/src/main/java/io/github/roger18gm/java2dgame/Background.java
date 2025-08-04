package io.github.roger18gm.java2dgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.*;

public class Background {
    private final World world;
    private final Texture[] textures;
    //    private final Texture dirtTileA, dirtTileB, grassTileA, grassTileB, mudTileA, mudTileB, buildingTileA,
//        stoneTileTopLeft, stoneTileTop, stoneTileTopRight, stoneTileLeft, stoneTileBotLeft, stoneTileBot,
//        stoneTileBotRight, stoneTileRight, stoneTileCenter, longHedge, shortHedge, log, flag, target;
    private int tileSize = 46;

    //     Draws each tile on the screen in the form of a grid
    private int[][] tileGrid = { // h 16 X w 22
        {3, 3, 4, 4, 3, 3, 3, 4, 4, 4, 3, 4, 3, 3, 4, 3, 4, 4, 4, 3, 4, 3},
        {4, 3, 4, 4, 3, 3, 3, 4, 4, 4, 3, 4, 3, 3, 4, 3, 4, 4, 4, 3, 3, 3},
        {4, 3, 4, 4, 3, 3, 3, 4, 4, 4, 3, 4, 3, 3, 4, 3, 4, 4, 4, 3, 4, 3},
        {3, 3, 4, 4, 3, 3, 3, 4, 4, 4, 3, 4, 3, 3, 4, 3, 4, 4, 4, 3, 3, 3},
        {4, 4, 3, 4, 4, 4, 3, 4, 3, 3, 4, 3, 4, 4, 4, 3, 3, 3, 4, 4, 4, 4},
        {4, 3, 4, 4, 3, 3, 3, 4, 4, 4, 3, 4, 3, 3, 4, 3, 4, 4, 3, 4, 3, 4},
        {3, 4, 4, 4, 3, 3, 3, 4, 4, 4, 3, 4, 3, 3, 4, 3, 4, 4, 4, 3, 3, 4},
        {3, 4, 4, 4, 3, 3, 3, 4, 4, 4, 3, 4, 3, 3, 4, 3, 4, 8, 9, 10, 3, 4},
        {4, 4, 3, 4, 3, 4, 3, 4, 3, 3, 3, 3, 3, 4, 4, 4, 4, 11, 12, 13, 3, 3},
        {3, 3, 4, 4, 3, 3, 3, 4, 4, 4, 3, 4, 3, 3, 4, 3, 4, 14, 15, 16, 3, 4},
        {4, 4, 3, 4, 4, 4, 3, 4, 3, 3, 4, 4, 3, 4, 3, 3, 4, 3, 4, 4, 4, 4},
        {3, 4, 4, 4, 3, 3, 3, 4, 4, 4, 3, 4, 3, 3, 4, 3, 4, 4, 4, 3, 4, 3},
        {4, 3, 4, 4, 3, 3, 3, 4, 4, 4, 3, 4, 3, 3, 4, 3, 4, 4, 4, 3, 4, 4},
        {4, 4, 4, 4, 3, 3, 3, 4, 4, 4, 3, 4, 3, 3, 4, 3, 4, 4, 4, 3, 3, 3},
        {3, 3, 4, 4, 3, 3, 3, 4, 4, 4, 3, 4, 3, 3, 4, 3, 4, 4, 4, 3, 4, 4},
        {3, 3, 4, 4, 3, 3, 3, 4, 4, 4, 3, 4, 3, 3, 4, 3, 4, 4, 4, 3, 4, 4 }

    };
    private int[][] propGrid = {
        {9, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 12},
        {14, 8, 0, 7, 0, 0, 0, 4, 0, 0, 0, 0, 0, 7, 0, 0, 0, 0, 0, 0, 0, 16},
        {14, 0, 0, 0, 0, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 4, 4, 0, 0, 0, 0, 16},
        {14, 5, 0, 7, 0, 0, 0, 4, 0, 0, 4, 0, 0, 7, 0, 0, 0, 0, 4, 0, 0, 16},
        {14, 0, 0, 0, 0, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 2, 0, 0, 7, 16},
        {14, 2, 0, 2, 0, 0, 0, 4, 0, 0, 4, 0, 2, 0, 2, 0, 0, 0, 0, 0, 0, 16},
        {14, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 16},
        {14, 0, 0, 0, 2, 0, 2, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 16},
        {14, 5, 7, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 4, 0, 0, 6, 0, 0, 16},
        {14, 0, 0, 0, 2, 0, 2, 0, 0, 0, 0, 0, 5, 0, 5, 0, 0, 0, 0, 0, 0, 16},
        {14, 2, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 4, 0, 0, 0, 0, 0, 16},
        {14, 0, 0, 0, 0, 0, 0, 0, 2, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 16},
        {14, 4, 0, 0, 7, 0, 0, 0, 0, 0, 0, 2, 0, 2, 0, 0, 0, 0, 0, 0, 0, 16},
        {14, 0, 0, 2, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 2, 0, 2, 0, 2, 16},
        {14, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 16},
        {10, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 11}
    };

    public Background(World world){
        this.world = world;
        // Initializes the tiles
        textures = new Texture[] {

            new Texture("tiles/PNG/Tiles/grass-a.png"), // 1
            new Texture("tiles/PNG/Tiles/grass-b.png"), // 2
            new Texture("tiles/PNG/Tiles/dirt-a.png"), // 3
            new Texture("tiles/PNG/Tiles/dirt-b.png"), // 4
            new Texture("tiles/PNG/Tiles/mud-a.png"), // 5
            new Texture("tiles/PNG/Tiles/mud-b.png"), // 6
            new Texture("tiles/PNG/Buildings/Building_A_01.png"), // 7
            new Texture("tiles/PNG/Decor_Tiles/Decor_Tile_B_01.png"), // 8
            new Texture("tiles/PNG/Decor_Tiles/Decor_Tile_B_02.png"), // 9
            new Texture("tiles/PNG/Decor_Tiles/Decor_Tile_B_03.png"), // 10
            new Texture("tiles/PNG/Decor_Tiles/Decor_Tile_B_04.png"), // 11
            new Texture("tiles/PNG/Decor_Tiles/Decor_Tile_B_05.png"), // 12
            new Texture("tiles/PNG/Decor_Tiles/Decor_Tile_B_06.png"), // 13
            new Texture("tiles/PNG/Decor_Tiles/Decor_Tile_B_07.png"), // 14
            new Texture("tiles/PNG/Decor_Tiles/Decor_Tile_B_08.png"), // 15
            new Texture("tiles/PNG/Decor_Tiles/Decor_Tile_B_09.png"), // 16
            new Texture("tiles/PNG/Blocks/long-hedge.png"), // 17
            new Texture("tiles/PNG/Blocks/short-hedge.png"), // 18
            new Texture("tiles/PNG/Props/Log.png"), // 19
            new Texture("tiles/PNG/Props/Flag_A.png"), // 20
            new Texture("tiles/PNG/Props/Dot_A.png"), // 21
            new Texture("tiles/PNG/Hedges/Hedge_B_01.png"), // 22
            new Texture("tiles/PNG/Hedges/Hedge_B_02.png") // 23
        };
        createStaticBodies();
    }
    private void createStaticBodies() {
        for (int row = 0; row < propGrid.length; row++) {
            for (int col = 0; col < propGrid[row].length; col++) {
                if (propGrid[row][col] == (2)) { // the horizontal hedges
                    createStaticBody(col * tileSize, (propGrid.length - 1 - row) * tileSize, tileSize * 2, tileSize);
                }
                if (propGrid[row][col] == (4)) { // the vertical hedges
                    createStaticBody(col * tileSize, (propGrid.length - 1 - row) * tileSize, tileSize, tileSize * 2);
                }
                if (propGrid[row][col] == (5)) { // the horizontal logs
                    createStaticBody(col * tileSize - 45, (propGrid.length - 1 - row) * tileSize, tileSize * 2, tileSize);
                }
//                if (propGrid[row][col] == (6)) { // the flags
//                    createStaticBody(col * tileSize, (propGrid.length - 1 - row) * tileSize, tileSize * 2, tileSize * 2);
//                }

                if (propGrid[row][col] == (7)) { // the vertical logs
                    createStaticBody(col * tileSize, (propGrid.length - 1 - row) * tileSize, tileSize, tileSize * 2);
                }

                if (propGrid[row][col] == (14)) { // Left side border
                    createStaticBody(col * tileSize - 10, (propGrid.length - 1 - row) * tileSize - 25, tileSize / 3, tileSize * 2);
                }
                if (propGrid[row][col] == (16)) { // right side border
                    createStaticBody(col * tileSize + 30, (propGrid.length - 1 - row) * tileSize - 25, tileSize / 3, tileSize * 2);
                }
                if (propGrid[row][col] == (15)) { // bottom border
                    createStaticBody(col * tileSize - 20, (propGrid.length - 1 - row) * tileSize - 10, tileSize * 2, tileSize / 3);
                }
                if (propGrid[row][col] == (13)) { // top border
                    createStaticBody(col * tileSize - 25, (propGrid.length - 1 - row) * tileSize + 35, tileSize * 2, tileSize / 3);
                }
            }
        }
    }

    private void createStaticBody(float x, float y, float width, float height) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(x + width / 2, y + height / 2);

        Body body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width / 2, height / 2);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1.0f;
        fixtureDef.friction = 0.0f;

        body.createFixture(fixtureDef);
        shape.dispose();
    }
    public void render(SpriteBatch batch){
        for (int row = 0; row < tileGrid.length; row++){
            for (int col = 0; col < tileGrid[row].length; col++){

                int x = col * tileSize;
                int y = (tileGrid.length - 1 - row) * tileSize;
                if (tileGrid[row][col] != 0) {
                    batch.draw(textures[tileGrid[row][col] - 1], x, y, tileSize, tileSize);
//                    batch.draw(textures[propGrid[row][col] - 1], x, y, tileSize, tileSize); // this no workie
                }
            }
        }


        // Prop map grid rules
        for (int row = 0; row < tileGrid.length; row++){

            Texture buildingTileA = textures[6];
            Texture longHedge = textures[16];
            Texture shortHedge = textures[17];
            Texture log = textures[18];
            Texture flag = textures[19];
            Texture target = textures[20];
            Texture corner = textures[21];
            Texture border = textures[22];

            for (int col = 0; col < tileGrid[row].length; col++){

                int x = col * tileSize;
                int y = (tileGrid.length - 1 - row) * tileSize;

                if (propGrid[row][col] == 1)
                    batch.draw(buildingTileA, x, y, tileSize, tileSize);
                else if(propGrid[row][col] == 2)
                    batch.draw(longHedge, x, y, tileSize * 2, tileSize);
                else if (propGrid[row][col] == 3)
                    batch.draw(shortHedge, x, y, tileSize, tileSize);
                else if (propGrid[row][col] == 4){
                    batch.draw(longHedge,
                        x, y,
                        tileSize / 2f, tileSize / 2f,
                        tileSize * 2, tileSize,
                        1f, 1f,
                        90,
                        0, 0,
                        longHedge.getWidth(), longHedge.getHeight(),
                        false, false);
                }
//                else if (propGrid[row][col] == 5) // horizontal logs
//                    batch.draw(log, x , y - (tileSize / 2) - 20, tileSize * 2, tileSize * 3);

                else if (propGrid[row][col] == 5){ // vertical logs
                    batch.draw(log,
                        x, y + 45,
                        tileSize / 2f, tileSize / 2f,
                        tileSize * 2, tileSize * 3,
                        1f, 1f,
                        180,
                        0, 0,
                        log.getWidth(), log.getHeight(),
                        false, false);
                }

                else if (propGrid[row][col] == 7){ // vertical logs
                    batch.draw(log,
                        x + (tileSize / 2) + 20, y,
                        tileSize / 2f, tileSize / 2f,
                        tileSize * 2, tileSize * 3,
                        1f, 1f,
                        90,
                        0, 0,
                        log.getWidth(), log.getHeight(),
                        false, false);
                }
                // flag and target
                else if (propGrid[row][col] == 6)
                    batch.draw(flag, x, y, tileSize * 2, tileSize * 2);
                else if (propGrid[row][col] == 8)
                    batch.draw(target, x, y, tileSize * 2, tileSize * 2);

                    // Hedge border
                else if (propGrid[row][col] == 9)
                    batch.draw(corner, x, y, tileSize, tileSize);
                else if (propGrid[row][col] == 10)
                    batch.draw(corner,
                        x, y,
                        tileSize / 2f, tileSize / 2f,
                        tileSize, tileSize,
                        1f, 1f,
                        90,
                        0, 0,
                        corner.getWidth(), corner.getHeight(),
                        false, false);
                else if (propGrid[row][col] == 11)
                    batch.draw(corner,
                        x, y,
                        tileSize / 2f - 5, tileSize / 2f,
                        tileSize, tileSize,
                        1f, 1f,
                        180,
                        0, 0,
                        corner.getWidth(), corner.getHeight(),
                        false, false);
                else if (propGrid[row][col] == 12)
                    batch.draw(corner,
                        x, y,
                        tileSize / 2f - 5, tileSize / 2f + 5,
                        tileSize, tileSize,
                        1f, 1f,
                        270,
                        0, 0,
                        corner.getWidth(), corner.getHeight(),
                        false, false);
                    // Border
                else if (propGrid[row][col] == 13)
                    batch.draw(border, x, y, tileSize, tileSize);
                else if (propGrid[row][col] == 14)
                    batch.draw(border,
                        x, y,
                        tileSize / 2f, tileSize / 2f,
                        tileSize, tileSize,
                        1f, 1f,
                        90,
                        0, 0,
                        corner.getWidth(), corner.getHeight(),
                        false, false);
                else if (propGrid[row][col] == 15)
                    batch.draw(border,
                        x, y,
                        tileSize / 2f, tileSize / 2f,
                        tileSize, tileSize,
                        1f, 1f,
                        180,
                        0, 0,
                        corner.getWidth(), corner.getHeight(),
                        false, false);
                else if (propGrid[row][col] == 16)
                    batch.draw(border,
                        x, y,
                        tileSize / 2f - 5, tileSize / 2f + 5,
                        tileSize, tileSize,
                        1f, 1f,
                        270,
                        0, 0,
                        corner.getWidth(), corner.getHeight(),
                        false, false);

            }
        }
    }

    public void dispose() {
        for (Texture texture : textures){
            texture.dispose();
        }
    }
}
