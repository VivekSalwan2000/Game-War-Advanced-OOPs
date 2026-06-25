package com.villagewar.ui.controller;

import ConcurrentAbstractFactory.BuildingFactoryConcurrent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.VBox;
import com.villagewar.ui.service.GameClientService;
import javafx.stage.Modality;
import javafx.stage.Stage;
import com.villagewar.ui.util.UiMessages;
import VillageElements.Building;

import java.util.function.Consumer;

/**
 * Dialog for adding a building through the existing TCP request flow.
 */
public class BuildingDialogController {

    @FXML
    private VBox root;

    @FXML
    private ComboBox<String> buildingTypeCombo;

    @FXML
    private Button addButton;

    @FXML
    private Button cancelButton;

    @FXML
    private ProgressIndicator progressIndicator;

    private Stage dialogStage;

    @FXML
    private void initialize() {
        buildingTypeCombo.getItems().addAll(
                BuildingFactoryConcurrent.FARM,
                BuildingFactoryConcurrent.LUMBER_MILL,
                BuildingFactoryConcurrent.IRON_MINE,
                BuildingFactoryConcurrent.GOLD_MINE,
                BuildingFactoryConcurrent.ARCHER_TOWER,
                BuildingFactoryConcurrent.CANNON,
                BuildingFactoryConcurrent.CATAPULT
        );
        buildingTypeCombo.getSelectionModel().selectFirst();
        progressIndicator.setVisible(false);
        progressIndicator.setManaged(false);
        cancelButton.setOnAction(e -> dialogStage.close());
    }

    public void show(Stage owner, GameClientService service, Consumer<Building> onAdded) {
        dialogStage = new Stage();
        dialogStage.initOwner(owner);
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setTitle("Add Building");
        dialogStage.setScene(new javafx.scene.Scene(root, 400, 220));
        dialogStage.getScene().getStylesheets()
                .add(getClass().getResource("/javafx/css/villagewar.css").toExternalForm());

        service.busyProperty().addListener((obs, o, busy) -> {
            progressIndicator.setVisible(busy);
            progressIndicator.setManaged(busy);
            addButton.setDisable(busy);
        });

        addButton.setOnAction(e -> {
            String type = buildingTypeCombo.getSelectionModel().getSelectedItem();
            if (type == null) {
                UiMessages.showError(dialogStage, "Selection required", "Choose a building type.");
                return;
            }
            service.addBuilding(type,
                    building -> {
                        dialogStage.close();
                        UiMessages.showSuccess(owner, "Building added", type + " added to your village.");
                        if (onAdded != null) {
                            onAdded.accept(building);
                        }
                    },
                    err -> UiMessages.showError(dialogStage, "Failed to add building", err.getMessage()));
        });

        dialogStage.showAndWait();
    }
}
