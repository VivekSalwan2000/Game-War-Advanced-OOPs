package com.villagewar.ui.viewmodel;

import Controllers.VillageController;
import Game.ArmyUnit;
import Game.WorkForce;
import VillageElements.Building;
import VillageElements.CollectedResources;
import VillageElements.VillageMap;
import javafx.beans.property.*;

/**
 * Observable snapshot of the player's village for dashboard and map binding.
 */
public class VillageViewModel {

    private final IntegerProperty level = new SimpleIntegerProperty(0);
    private final IntegerProperty gold = new SimpleIntegerProperty(0);
    private final IntegerProperty iron = new SimpleIntegerProperty(0);
    private final IntegerProperty lumber = new SimpleIntegerProperty(0);
    private final IntegerProperty population = new SimpleIntegerProperty(0);
    private final IntegerProperty maxPopulation = new SimpleIntegerProperty(0);
    private final IntegerProperty buildingCount = new SimpleIntegerProperty(0);
    private final IntegerProperty armySize = new SimpleIntegerProperty(0);
    private final IntegerProperty food = new SimpleIntegerProperty(0);
    private final IntegerProperty trophies = new SimpleIntegerProperty(0);
    private final BooleanProperty hasVillage = new SimpleBooleanProperty(false);

    private VillageController controller;

    public void updateFrom(VillageController villageController) {
        this.controller = villageController;
        if (villageController == null) {
            hasVillage.set(false);
            return;
        }
        hasVillage.set(true);
        level.set(villageController.getLevel());

        CollectedResources treasury = villageController.getTreasuryAvailable();
        if (treasury != null) {
            gold.set(treasury.getGold());
            iron.set(treasury.getIron());
            lumber.set(treasury.getLumber());
        }

        population.set(villageController.getCurrentPopulation());
        maxPopulation.set(villageController.getMaxPopulationAllowed());
        food.set(villageController.getFoodAvailable());
        trophies.set(villageController.getTotalTrophies());

        VillageMap map = villageController.getVillageMap();
        if (map != null && map.getBuildings() != null) {
            buildingCount.set(map.getBuildings().size());
        } else {
            buildingCount.set(0);
        }

        ArmyUnit army = villageController.getArmyUnit();
        armySize.set(army != null && army.getArmyUnit() != null ? army.getArmyUnit().size() : 0);
    }

    public VillageController getController() {
        return controller;
    }

    public IntegerProperty levelProperty() {
        return level;
    }

    public IntegerProperty goldProperty() {
        return gold;
    }

    public IntegerProperty ironProperty() {
        return iron;
    }

    public IntegerProperty lumberProperty() {
        return lumber;
    }

    public IntegerProperty populationProperty() {
        return population;
    }

    public IntegerProperty maxPopulationProperty() {
        return maxPopulation;
    }

    public IntegerProperty buildingCountProperty() {
        return buildingCount;
    }

    public IntegerProperty armySizeProperty() {
        return armySize;
    }

    public IntegerProperty foodProperty() {
        return food;
    }

    public IntegerProperty trophiesProperty() {
        return trophies;
    }

    public BooleanProperty hasVillageProperty() {
        return hasVillage;
    }
}
