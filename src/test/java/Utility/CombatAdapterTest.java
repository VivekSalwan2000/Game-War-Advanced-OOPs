package Utility;

import ChallengeDecision.ChallengeResult;
import Game.ArmyUnit;
import Models.Village;
import VillageElements.Archer;
import VillageElements.Soldier;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the Adapter layer that bridges the domain model to the {@code ChallengeDecision.Arbitrer}
 * combat engine.
 */
class CombatAdapterTest {

    @Test
    void attackAdapterExposesDamageAsPropertyAndHitPoints() {
        Soldier soldier = new Soldier();
        Attack_Entity_To_Challenge_Attack_Adapter adapter =
                new Attack_Entity_To_Challenge_Attack_Adapter(soldier);

        assertEquals(soldier.getDamage(), adapter.getProperty().intValue());
        assertEquals(soldier.getHitPoints(), adapter.getHitPoints().intValue());
    }

    @Test
    void defenceAdapterExposesDamageAsPropertyAndHitPoints() {
        Soldier soldier = new Soldier();
        Defence_Entity_To_Challenge_Defense_Adapter adapter =
                new Defence_Entity_To_Challenge_Defense_Adapter(soldier);

        assertEquals(soldier.getDamage(), adapter.getProperty().intValue());
        assertEquals(soldier.getHitPoints(), adapter.getHitPoints().intValue());
    }

    @Test
    void judgeAttackReturnsNonNullResult() {
        ArmyUnit army = new ArmyUnit();
        army.recruit(new Soldier());
        army.recruit(new Archer());

        ChallengeResult result = new ArbitrerAdapter().judgeAttack(army, new Village());

        assertNotNull(result, "judgeAttack must return a ChallengeResult, never null");
        assertNotNull(result.getChallengeWon());
    }
}
