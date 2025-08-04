package io.github.roger18gm.java2dgame;
//package items;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.roger18gm.java2dgame.Main;

import java.util.ArrayList;
import java.util.HashMap;


public class Inventory {
    private Stage stage;
    private Window inventoryWindow;
    private boolean isVisible; // Tracks visibility
    private ArrayList<BaseItem> items;
    private Table itemTable; // Table to display items
    private Skin skin;
    private int selectedIndex = -1;

    public Inventory() {
        stage = new Stage(new ScreenViewport());
        items = new ArrayList<>();
        skin = new Skin(Gdx.files.internal("uiskin.json"));
        TextButton useButton = new TextButton("Use", skin);
        TextButton dropButton = new TextButton("Drop", skin);

        // Load a skin for UI elements
//        Skin skin = new Skin(Gdx.files.internal("uiskin.json"));

        // Creates inventory window
        inventoryWindow = new Window("Inventory", skin);
        inventoryWindow.setSize(300, 400);
        inventoryWindow.setPosition(0, 0);

        // Creates table to organize the items
        itemTable = new Table();
        updateItemTable(skin); // Populate with current items
        inventoryWindow.add(itemTable).padBottom(20); // Add item table to the window

        Table buttonTable = new Table();
        buttonTable.add(useButton).pad(10);
        buttonTable.add(dropButton).pad(10);

        inventoryWindow.row(); // Move to the next row
        inventoryWindow.add(buttonTable);

        // Initially hidden
        inventoryWindow.setVisible(false);
        isVisible = false;

        // Add the inventory window to the stage
        stage.addActor(inventoryWindow);

        // Listeners 'listen' for button presses
        useButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                useSelectedItem();
            }
        });

        dropButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                dropSelectedItem();
            }
        });

        // Enable input for the stage
        Gdx.input.setInputProcessor(stage);
    }

    private void updateItemTable(Skin skin) {
        itemTable.clear(); // Clear the table before adding items

        if (items.isEmpty()) {
            Label emptyLabel = new Label("No items in inventory", skin);
            itemTable.add(emptyLabel).center().pad(10);
            return;
        }

        for (int i = 0; i < items.size(); i++) {
            final int index = i; // For use inside the listener
            BaseItem item = items.get(i);
            Label label = new Label(item.getName(), skin);

            // Highlight the selected item
            if (index == selectedIndex) {
                label.setColor(0, 1, 0, 1); // Green color for the selected item
            } else {
                label.setColor(1, 1, 1, 1); // White color for non-selected items
            }

            // Add a ClickListener to allow selection
            label.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    selectedIndex = index; // Set the selected index
                    updateItemTable(skin); // Refresh the table to show the highlight
                }
            });

            itemTable.add(label).pad(5);
            itemTable.row();
        }
    }

    public void useSelectedItem() {
        if (selectedIndex >= 0 && selectedIndex < items.size()) {
            BaseItem selectedItem = items.get(selectedIndex);
            if (selectedItem.isConsumable()) {
                System.out.println("Using item: " + selectedItem.getName());
                items.remove(selectedIndex); // Remove the item after using
                selectedIndex = -1; // Reset the selection
                updateItemTable(skin); // Refresh the table
            } else {
                System.out.println(selectedItem.getName() + " cannot be used.");
            }
        } else {
            System.out.println("No item selected to use.");
        }
    }

    public void dropSelectedItem() {
        if (selectedIndex >= 0 && selectedIndex < items.size()) {
            BaseItem selectedItem = items.get(selectedIndex);
            System.out.println("Dropping item: " + selectedItem.getName());
            items.remove(selectedIndex); // Remove the item from inventory
            selectedIndex = -1; // Reset the selection
            updateItemTable(skin); // Refresh the table
        } else {
            System.out.println("No item selected to drop.");
        }
    }

    public void addItem(BaseItem item) {
        items.add(item);
        updateItemTable(skin); // Update the UI when a new item is added
    }

    public void removeItem(BaseItem item) {
        items.remove(item);
        updateItemTable(skin); // Update the UI when an item is removed
    }

    public ArrayList<BaseItem> getItems() {
        return items;
    }

    public boolean toggleVisibility() {
        isVisible = !isVisible;
        inventoryWindow.setVisible(isVisible);
//        Main.isPaused = isVisible;
        if (!isVisible) {
            selectedIndex = -1;// Pause the game when inventory is visible
        }
        return isVisible;
    }

    public void handleNavigation() {
        if (!isVisible || items.isEmpty()) return; // Skip if inventory is not visible or empty

        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            selectedIndex = (selectedIndex > 0) ? selectedIndex - 1 : items.size() - 1; // Wrap around
            updateItemTable(skin);
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
            selectedIndex = (selectedIndex < items.size() - 1) ? selectedIndex + 1 : 0; // Wrap around
            updateItemTable(skin);
        }
    }

    public void handleInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.I)) {
            toggleVisibility();
        }
    }

    public void render(float delta) {
        handleInput(); // Check for keypress
        stage.act(delta);
        stage.draw();
    }

    public void dispose() {
        stage.dispose();
    }

    public Stage getStage() {
        return stage;
    }
}
