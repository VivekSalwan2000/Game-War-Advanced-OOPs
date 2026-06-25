package com.villagewar.ui.viewmodel;

import ChallengeDecision.ChallengeResource;
import ChallengeDecision.ChallengeResult;
import javafx.beans.property.*;

import java.util.List;

/**
 * Observable combat outcome for the attack/defense screen.
 */
public class CombatResultViewModel {

    private final BooleanProperty won = new SimpleBooleanProperty(false);
    private final StringProperty outcomeText = new SimpleStringProperty("");
    private final StringProperty lootSummary = new SimpleStringProperty("");
    private final StringProperty battleType = new SimpleStringProperty("");

    public void updateFrom(ChallengeResult result, String typeLabel) {
        if (result == null) {
            won.set(false);
            outcomeText.set("No result");
            lootSummary.set("");
            battleType.set(typeLabel);
            return;
        }
        battleType.set(typeLabel);
        won.set(Boolean.TRUE.equals(result.getChallengeWon()));
        outcomeText.set(won.get() ? "Victory" : "Defeat");
        lootSummary.set(formatLoot(result.getLoot()));
    }

    private String formatLoot(List<ChallengeResource<Double, Double>> loot) {
        if (loot == null || loot.isEmpty()) {
            return "No loot";
        }
        StringBuilder sb = new StringBuilder();
        for (ChallengeResource<Double, Double> item : loot) {
            sb.append("Resource: ")
                    .append(String.format("%.1f", item.getProperty().doubleValue()))
                    .append("  |  Hit: ")
                    .append(String.format("%.1f", item.getHitPoints().doubleValue()))
                    .append("\n");
        }
        return sb.toString().trim();
    }

    public BooleanProperty wonProperty() {
        return won;
    }

    public StringProperty outcomeTextProperty() {
        return outcomeText;
    }

    public StringProperty lootSummaryProperty() {
        return lootSummary;
    }

    public StringProperty battleTypeProperty() {
        return battleType;
    }
}
