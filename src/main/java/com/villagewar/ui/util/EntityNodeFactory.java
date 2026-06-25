package com.villagewar.ui.util;

import VillageElements.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;

/**
 * Builds styled nodes for village entities on the visual map.
 */
public final class EntityNodeFactory {

    private EntityNodeFactory() {
    }

    public static StackPane createEntityNode(VillageEntity entity) {
        EntityStyle style = styleFor(entity);
        Rectangle tile = new Rectangle(72, 72);
        tile.getStyleClass().add("entity-tile");
        tile.setStyle("-fx-fill: " + style.color() + ";");

        Label icon = new Label(style.icon());
        icon.getStyleClass().add("entity-icon");

        Label level = new Label("Lv " + entity.getLevel());
        level.getStyleClass().add("entity-level");

        VBox content = new VBox(2, icon, level);
        content.setAlignment(Pos.CENTER);

        StackPane pane = new StackPane(tile, content);
        pane.setPadding(new Insets(4));
        pane.setAlignment(Pos.CENTER);

        Tooltip tooltip = new Tooltip(style.name() + "\nLevel: " + entity.getLevel()
                + "\nHP: " + entity.getHitPoints()
                + "\nCost: " + entity.getProductionCost());
        Tooltip.install(pane, tooltip);
        return pane;
    }

    public static StackPane createVillageHallNode(VillageMap.VillageHall hall) {
        return createEntityNode(hall);
    }

    private static EntityStyle styleFor(VillageEntity entity) {
        if (entity instanceof VillageMap.VillageHall) {
            return new EntityStyle("Village Hall", "🏰", "#8B4513");
        }
        if (entity instanceof Farm) {
            return new EntityStyle("Farm", "🌾", "#6B8E23");
        }
        if (entity instanceof GoldMine) {
            return new EntityStyle("Gold Mine", "💰", "#DAA520");
        }
        if (entity instanceof LumberMill) {
            return new EntityStyle("Lumber Mill", "🪵", "#556B2F");
        }
        if (entity instanceof IronMine) {
            return new EntityStyle("Iron Mine", "⛏️", "#708090");
        }
        if (entity instanceof ArcherTower) {
            return new EntityStyle("Archer Tower", "🏹", "#4682B4");
        }
        if (entity instanceof Cannon) {
            return new EntityStyle("Cannon", "💣", "#2F4F4F");
        }
        if (entity instanceof Catapult) {
            return new EntityStyle("Catapult", "🎯", "#8B0000");
        }
        if (entity instanceof Soldier) {
            return new EntityStyle("Soldier", "⚔️", "#CD853F");
        }
        if (entity instanceof Knight) {
            return new EntityStyle("Knight", "🛡️", "#B8860B");
        }
        if (entity instanceof Archer) {
            return new EntityStyle("Archer", "🏹", "#5F9EA0");
        }
        if (entity instanceof Farmer) {
            return new EntityStyle("Farmer", "👨‍🌾", "#9ACD32");
        }
        if (entity instanceof Miner) {
            return new EntityStyle("Miner", "⛏️", "#A9A9A9");
        }
        if (entity instanceof Builder) {
            return new EntityStyle("Builder", "🔨", "#BC8F8F");
        }
        if (entity instanceof Collector) {
            return new EntityStyle("Collector", "📦", "#D2B48C");
        }
        return new EntityStyle(entity.getClass().getSimpleName(), "🏠", "#696969");
    }

    private record EntityStyle(String name, String icon, String color) {
    }
}
