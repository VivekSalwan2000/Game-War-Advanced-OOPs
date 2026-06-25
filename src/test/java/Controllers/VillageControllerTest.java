package Controllers;

import Models.Village;
import VillageElements.CollectedResources;
import VillageElements.Farm;
import VillageElements.UpgradeCancelledDueToMaxedOutException;
import Views.VillageView;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for {@link VillageController} orchestration logic, including the regression fix
 * for the previously no-op {@code setLevel}.
 */
class VillageControllerTest {

    private VillageController newController() {
        return new VillageController(new Village(), new VillageView());
    }

    @Test
    void setLevelActuallyUpdatesLevel() {
        VillageController controller = newController();
        controller.setLevel(7);
        assertEquals(7, controller.getLevel(), "setLevel must persist the new level (regression fix)");
    }

    @Test
    void newVillageStartsAtLevelOne() {
        assertEquals(1, newController().getLevel());
    }

    @Test
    void addingFarmIncreasesFoodAndMaxPopulation() {
        VillageController controller = newController();
        int foodBefore = controller.getFoodAvailable();
        int maxPopBefore = controller.getMaxPopulationAllowed();

        Farm farm = new Farm();
        assertTrue(controller.addBuilding(farm), "A fresh village should accept a first farm");

        assertEquals(foodBefore + farm.getQuantity(), controller.getFoodAvailable());
        assertEquals(maxPopBefore + farm.getSizeFedPerFarm(), controller.getMaxPopulationAllowed());
    }

    @Test
    void upgradeSucceedsWithSufficientFunds() throws UpgradeCancelledDueToMaxedOutException {
        VillageController controller = newController();
        int levelBefore = controller.getLevel();

        assertTrue(controller.upgradeVillage());
        assertEquals(levelBefore + 1, controller.getLevel());
    }

    @Test
    void upgradeFailsWithInsufficientFunds() throws UpgradeCancelledDueToMaxedOutException {
        VillageController controller = newController();
        controller.setTreasuryAvailable(new CollectedResources(50, 50, 50));

        assertFalse(controller.upgradeVillage());
        assertEquals(1, controller.getLevel(), "Level must not change when the upgrade is unaffordable");
    }
}
