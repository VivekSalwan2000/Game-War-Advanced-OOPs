package Game;

import Controllers.VillageController;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for {@link GameEngine} random-content generation.
 */
class GameEngineTest {

    @Test
    void randomArmyHasRequestedSize() {
        ArmyUnit army = new GameEngine().getRandomArmyUnit(10);
        assertNotNull(army);
        assertEquals(10, army.getArmyUnit().size());
    }

    @Test
    void randomArmyOfZeroIsEmpty() {
        ArmyUnit army = new GameEngine().getRandomArmyUnit(0);
        assertNotNull(army);
        assertTrue(army.getArmyUnit().isEmpty());
    }

    @Test
    void randomVillageHasRequestedLevel() {
        VillageController controller = new GameEngine().getRandomVillage(3);
        assertNotNull(controller);
        assertNotNull(controller.getVillage());
        assertEquals(3, controller.getLevel());
    }

    @Test
    void randomVillageHasTreasuryWithinExpectedBounds() {
        VillageController controller = new GameEngine().getRandomVillage(2);
        int gold = controller.getTreasuryAvailable().getGold();
        assertTrue(gold >= 10000 && gold < 50000, "treasury should be seeded in the documented range");
    }
}
