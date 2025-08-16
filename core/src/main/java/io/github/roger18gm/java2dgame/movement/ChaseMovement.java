//package io.github.roger18gm.java2dgame.movement;
//
//import io.github.roger18gm.java2dgame.entities.Entity;
//import io.github.roger18gm.java2dgame.entities.Player;
//
//public class ChaseMovement implements Movement {
//    private Player player;
//
//    public ChaseMovement(Player player) {
//        this.player = player;
//    }
//
//    public void move(Entity entity) {
//        int dx = Integer.compare(player.getX(), entity.getX());
//        int dy = Integer.compare(player.getY(), entity.getY());
//        entity.setX(entity.getX() + dx * entity.getSpeed());
//        entity.setY(entity.getY() + dy * entity.getSpeed());
//    }
//}
