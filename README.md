# Orc On Your Heels

A 2D top-down action game built with LibGDX where players control a soldier character navigating a tile-based world filled with orc enemies, NPCs, and objectives.

## Game Overview

**Core Gameplay:**
- Control a soldier character using WASD movement and spacebar for attacks
- Navigate a physics-based world with Box2D collision detection
- Collect health items that spawn periodically throughout the world 
- Complete flag capture and delivery objectives
- Engage in combat with AI-controlled orc enemies

## Technical Architecture

### Core Technologies
- **LibGDX Framework**: Cross-platform game development 
- **Box2D Physics**: 2D physics simulation for movement and collisions
- **Scene2D UI**: User interface management for menus and inventory
- **LWJGL3**: Desktop platform deployment 

### Key Components
- **Character System**: Base character class with animation and physics integration 
- **AI System**: Wandering enemy behavior using steering algorithms
- **Tile-Based World**: Grid-based background with collision boundaries 
- **Item System**: Health items and flag objectives with pickup mechanics 

## Getting Started

### Prerequisites
- Java Development Kit (JDK) 8 or higher
- Gradle build system

### Running the Game
```bash
# Run the desktop version
./gradlew lwjgl3:run

# Build executable JAR
./gradlew lwjgl3:jar
```

The built JAR can be found at `lwjgl3/build/libs/`.

### Project Structure
- `core/`: Main game logic shared across platforms
- `lwjgl3/`: Desktop platform launcher using LWJGL3

## Game Features

### Character Control
- **Player Character**: Soldier with WASD movement and spacebar attacks
- **Enemy Characters**: Manual orc control with arrow keys and M for attack 
- **AI Entities**: Wandering orcs with autonomous movement patterns 

### Audio System
- Background music with volume control and looping 
- Combat sound effects for attacks and interactions

### Game Mechanics
- **Health System**: Collectible heart items that restore player health 
- **Objective System**: Flag pickup and delivery to designated drop zones
- **Combat System**: Distance-based damage calculation between characters

## Development

### Useful Gradle Commands
- `./gradlew build`: Build all projects
- `./gradlew clean`: Remove build folders 
- `./gradlew test`: Run unit tests

### Resources
- [LibGDX Documentation](https://libgdx.com/dev/)
- [YouTube Tutorials](https://www.youtube.com)

https://free-game-assets.itch.io/free-battle-location-top-down-2d-tileset?download

## Future Development

The project aims to expand into a fully-featured game with enhanced combat mechanics, item collection systems, and win conditions.

## Future plans
We plan to continue working on this projects and turn it into a fully functioning game. This includes adding adding aspects of "hurt" and creating the ability to pick up items to win the game.
