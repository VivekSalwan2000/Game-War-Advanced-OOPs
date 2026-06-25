package com.villagewar.ui.controller;

import ChallengeDecision.ChallengeResult;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import com.villagewar.ui.service.GameClientService;
import javafx.stage.Modality;
import javafx.stage.Stage;
import com.villagewar.ui.util.UiMessages;
import com.villagewar.ui.viewmodel.CombatResultViewModel;
import com.villagewar.ui.viewmodel.VillageViewModel;

/**
 * Combat screen — attack random village or defend against random army.
 */
public class CombatController {

    @FXML
    private BorderPane root;

    @FXML
    private Label battleTypeLabel;

    @FXML
    private Label outcomeLabel;

    @FXML
    private Label armyInfoLabel;

    @FXML
    private TextArea lootArea;

    @FXML
    private Button fightButton;

    @FXML
    private Button closeButton;

    @FXML
    private ProgressIndicator progressIndicator;

    private Stage dialogStage;
    private final CombatResultViewModel combatViewModel = new CombatResultViewModel();

    @FXML
    private void initialize() {
        progressIndicator.setVisible(false);
        progressIndicator.setManaged(false);
        closeButton.setOnAction(e -> dialogStage.close());
    }

    public void show(Stage owner, GameClientService service, VillageViewModel villageViewModel,
                     boolean attackMode, Runnable onComplete) {
        dialogStage = new Stage();
        dialogStage.initOwner(owner);
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setTitle(attackMode ? "Attack" : "Defend");
        dialogStage.setScene(new javafx.scene.Scene(root, 520, 420));
        dialogStage.getScene().getStylesheets()
                .add(getClass().getResource("/javafx/css/villagewar.css").toExternalForm());

        String typeLabel = attackMode ? "Attack Random Village" : "Defend Against Random Army";
        battleTypeLabel.setText(typeLabel);
        armyInfoLabel.setText("Your army size: " + villageViewModel.armySizeProperty().get());

        outcomeLabel.textProperty().bind(combatViewModel.outcomeTextProperty());
        lootArea.textProperty().bind(combatViewModel.lootSummaryProperty());

        service.busyProperty().addListener((obs, o, busy) -> {
            progressIndicator.setVisible(busy);
            progressIndicator.setManaged(busy);
            fightButton.setDisable(busy);
        });

        fightButton.setText(attackMode ? "Launch Attack" : "Simulate Defense");
        fightButton.setOnAction(e -> {
            if (attackMode) {
                service.fightRandomVillage(
                        result -> showResult(result, typeLabel, onComplete),
                        err -> UiMessages.showError(dialogStage, "Attack failed", err.getMessage()));
            } else {
                service.randomAttackOnVillage(
                        result -> showResult(result, typeLabel, onComplete),
                        err -> UiMessages.showError(dialogStage, "Defense failed", err.getMessage()));
            }
        });

        closeButton.setOnAction(e -> {
            dialogStage.close();
            if (onComplete != null) {
                onComplete.run();
            }
        });

        dialogStage.showAndWait();
    }

    private void showResult(ChallengeResult result, String typeLabel, Runnable onComplete) {
        combatViewModel.updateFrom(result, typeLabel);
        outcomeLabel.getStyleClass().removeAll("victory", "defeat");
        outcomeLabel.getStyleClass().add(Boolean.TRUE.equals(result.getChallengeWon()) ? "victory" : "defeat");
        UiMessages.showInfo(dialogStage, "Battle complete",
                Boolean.TRUE.equals(result.getChallengeWon()) ? "Victory!" : "Defeat");
        if (onComplete != null) {
            onComplete.run();
        }
    }
}
