package Game;

import AbstractFactory.*;
import AbstractFactory.WorkerFactory;
import ConcurrentAbstractFactory.InvalidVillageEntityTypeException;
import Controllers.VillageController;
import Models.Village;
import Views.VillageView;
import VillageElements.*;

import java.io.Serializable;
import java.security.SecureRandom;
import java.util.Random;

import static AbstractFactory.WorkerFactory.*;


/**
 * This Game engine controls all game actions and has all the logic for the game. It controls all the attacks, upgrades,
 * constructions, termination time for upgrades and production. It also calculates the success of attacks, calculates the
 * loot and generate possible villages to attack.
 */
public class GameEngine implements Serializable {
    private final AbstractFactory workerFactory, buildingFactory;

    public GameEngine() {
        workerFactory = new WorkerFactory();
        buildingFactory = new BuildingFactory();
    }


    /**
     * This method returns a random unit of army
     *
     * @param size size of required army
     * @return random army unit.
     */
    public ArmyUnit getRandomArmyUnit(int size) {
        ArmyUnit armyUnit = new ArmyUnit();


        Random random = new SecureRandom();

        for (int i = 0; i < size; i++) {
            int randomChoice = random.nextInt(1, 4);
            switch (randomChoice) {
                case 1 -> {
                    try {
                        armyUnit.recruit((Recruitable) workerFactory.getVillageEntity(ARCHER));
                    } catch (ConcurrentAbstractFactory.InvalidVillageEntityTypeException |
                             InvalidBuildingTypeException e) {
                        throw new RuntimeException(e);
                    }
                }

                case 2 -> {
                    try {
                        armyUnit.recruit((Recruitable) workerFactory.getVillageEntity(SOLDIER));
                    } catch (ConcurrentAbstractFactory.InvalidVillageEntityTypeException |
                             InvalidBuildingTypeException e) {
                        throw new RuntimeException(e);
                    }
                }

                case 3 -> {
                    try {
                        armyUnit.recruit((Recruitable) workerFactory.getVillageEntity(KNIGHT));
                    } catch (ConcurrentAbstractFactory.InvalidVillageEntityTypeException |
                             InvalidBuildingTypeException e) {
                        throw new RuntimeException(e);
                    }
                }

            }
        }

        return armyUnit;
    }

    /**
     * This method creates a new village controller with a random village
     * @param level level of village required
     * @return village controller
     */
    public VillageController getRandomVillage(int level)  {
        Random random = new SecureRandom();
        VillageController randomVillage = new VillageController(new Village(level), new VillageView());
        CollectedResources randomCollectedResource =
                new CollectedResources(random.nextInt(10000,50000),
                random.nextInt(10000,50000),
                random.nextInt(10000,50000));
        randomVillage.setTreasuryAvailable(randomCollectedResource);

        randomVillage.setTotalTrophies(random.nextInt(500));

        String[] buildingTypes = {
                BuildingFactory.FARM, BuildingFactory.LUMBER_MILL, BuildingFactory.IRON_MINE,
                BuildingFactory.GOLD_MINE, BuildingFactory.ARCHER_TOWER, BuildingFactory.CANNON,
                BuildingFactory.CATAPULT
        };
        for (String buildingType : buildingTypes) {
            addRandomBuildings(randomVillage, buildingType, random.nextInt(0, level + 1));
        }

        return randomVillage;
    }

    /**
     * Adds {@code count} buildings of the given type to the supplied village.
     * @param village village to populate
     * @param buildingType building type key (see {@link BuildingFactory})
     * @param count number of buildings to add
     */
    private void addRandomBuildings(VillageController village, String buildingType, int count) {
        for (int i = 0; i < count; i++) {
            try {
                village.addBuilding((Building) buildingFactory.getVillageEntity(buildingType));
            } catch (InvalidVillageEntityTypeException | InvalidBuildingTypeException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
