package com.villagewar.ui.controller;

import ConcurrentAbstractFactory.WorkerFactoryConcurrent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.VBox;
import com.villagewar.ui.service.GameClientService;
import javafx.stage.Modality;
import javafx.stage.Stage;
import com.villagewar.ui.util.UiMessages;
import VillageElements.Worker;

import java.util.function.Consumer;

/**
 * Dialog for recruiting workers/troops through the existing TCP request flow.
 */
public class WorkerDialogController {

    @FXML
    private VBox root;

    @FXML
    private ComboBox<String> workerTypeCombo;

    @FXML
    private Button recruitButton;

    @FXML
    private Button cancelButton;

    @FXML
    private ProgressIndicator progressIndicator;

    private Stage dialogStage;

    @FXML
    private void initialize() {
        workerTypeCombo.getItems().addAll(
                WorkerFactoryConcurrent.FARMER,
                WorkerFactoryConcurrent.MINER,
                WorkerFactoryConcurrent.BUILDER,
                WorkerFactoryConcurrent.COLLECTOR,
                WorkerFactoryConcurrent.SOLDIER,
                WorkerFactoryConcurrent.KNIGHT,
                WorkerFactoryConcurrent.ARCHER
        );
        workerTypeCombo.getSelectionModel().selectFirst();
        progressIndicator.setVisible(false);
        progressIndicator.setManaged(false);
        cancelButton.setOnAction(e -> dialogStage.close());
    }

    public void show(Stage owner, GameClientService service, Consumer<Worker> onAdded) {
        dialogStage = new Stage();
        dialogStage.initOwner(owner);
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setTitle("Recruit Worker");
        dialogStage.setScene(new javafx.scene.Scene(root, 400, 220));
        dialogStage.getScene().getStylesheets()
                .add(getClass().getResource("/javafx/css/villagewar.css").toExternalForm());

        service.busyProperty().addListener((obs, o, busy) -> {
            progressIndicator.setVisible(busy);
            progressIndicator.setManaged(busy);
            recruitButton.setDisable(busy);
        });

        recruitButton.setOnAction(e -> {
            String type = workerTypeCombo.getSelectionModel().getSelectedItem();
            if (type == null) {
                UiMessages.showError(dialogStage, "Selection required", "Choose a worker type.");
                return;
            }
            service.addWorker(type,
                    worker -> {
                        dialogStage.close();
                        UiMessages.showSuccess(owner, "Recruited", type + " joined your village.");
                        if (onAdded != null) {
                            onAdded.accept(worker);
                        }
                    },
                    err -> UiMessages.showError(dialogStage, "Failed to recruit", err.getMessage()));
        });

        dialogStage.showAndWait();
    }
}
