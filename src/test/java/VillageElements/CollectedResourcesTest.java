package VillageElements;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the {@link CollectedResources} value object (treasury / cost arithmetic).
 */
class CollectedResourcesTest {

    @Test
    void constructorStoresAllThreeResources() {
        CollectedResources r = new CollectedResources(10, 20, 30);
        assertEquals(10, r.getLumber());
        assertEquals(20, r.getIron());
        assertEquals(30, r.getGold());
    }

    @Test
    void defaultConstructorIsAllZero() {
        CollectedResources r = new CollectedResources();
        assertEquals(0, r.getLumber());
        assertEquals(0, r.getIron());
        assertEquals(0, r.getGold());
    }

    @Test
    void subCostReducesEachResourceInPlace() {
        CollectedResources treasury = new CollectedResources(100, 100, 100);
        CollectedResources snapshot = treasury.subCost(new CollectedResources(40, 30, 20));

        assertEquals(60, treasury.getLumber());
        assertEquals(70, treasury.getIron());
        assertEquals(80, treasury.getGold());
        // returned snapshot mirrors the mutated state
        assertEquals(60, snapshot.getLumber());
        assertEquals(70, snapshot.getIron());
        assertEquals(80, snapshot.getGold());
    }

    @Test
    void addCostIncreasesEachResourceInPlace() {
        CollectedResources treasury = new CollectedResources(100, 100, 100);
        treasury.addCost(new CollectedResources(1, 2, 3));

        assertEquals(101, treasury.getLumber());
        assertEquals(102, treasury.getIron());
        assertEquals(103, treasury.getGold());
    }

    @Test
    void compareToReturnsZeroWhenAllResourcesEqual() {
        assertEquals(0, new CollectedResources(5, 5, 5).compareTo(new CollectedResources(5, 5, 5)));
    }

    @Test
    void compareToReturnsNegativeWhenOtherHasMoreOfAnyResource() {
        // documented affordability semantics: cost <= treasury yields a non-positive result
        assertEquals(-1, new CollectedResources(0, 0, 0).compareTo(new CollectedResources(1, 1, 1)));
    }

    @Test
    void compareToReturnsPositiveWhenOtherHasLessOfEveryResource() {
        assertEquals(1, new CollectedResources(10, 10, 10).compareTo(new CollectedResources(1, 1, 1)));
    }
}
