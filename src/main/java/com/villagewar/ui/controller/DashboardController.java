package com.villagewar.ui.controller;

import Controllers.VillageController;
import Game.ArmyUnit;
import Game.WorkForce;
import VillageElements.Building;
import VillageElements.Peasant;
import VillageElements.Recruitable;
import VillageElements.VillageMap;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import com.villagewar.ui.service.GameClientService;
import javafx.stage.Stage;
import com.villagewar.ui.util.EntityNodeFactory;
import com.villagewar.ui.util.FxmlLoaderHelper;
import com.villagewar.ui.util.UiMessages;
import com.villagewar.ui.viewmodel.VillageViewModel;

/**
 * Main game dashboard — stats, village map, building/worker/combat actions.
 */
public class DashboardController {

    @FXML
    private BorderPane root;

    @FXML
    private Label levelLabel;
    @FXML
    private Label goldLabel;
    @FXML
    private Label ironLabel;
    @FXML
    private Label lumberLabel;
    @FXML
    private Label populationLabel;
    @FXML
    private Label buildingCountLabel;
    @FXML
    private Label armySizeLabel;
    @FXML
    private Label foodLabel;
    @FXML
    private Label trophiesLabel;

    @FXML
    private FlowPane buildingsPane;
    @FXML
    private FlowPane workersPane;
    @FXML
    private FlowPane armyPane;
    @FXML
    private ScrollPane mapScroll;

    @FXML
    private Button createVillageButton;
    @FXML
    private Button refreshButton;
    @FXML
    private Button upgradeButton;
    @FXML
    private Button addBuildingButton;
    @FXML
    private Button addWorkerButton;
    @FXML
    private Button attackButton;
    @FXML
    private Button defendButton;
    @FXML
    private Button disconnectButton;

    @FXML
    private Label statusBar;
    @FXML
    private ProgressIndicator loadingIndicator;

    private Stage primaryStage;
    private GameClientService gameService;
    private Runnable onDisconnect;
    private final VillageViewModel viewModel = new VillageViewModel();

    public void initialize(Stage primaryStage, GameClientService gameService, Runnable onDisconnect) {
        this.primaryStage = primaryStage;
        this.gameService = gameService;
        this.onDisconnect = onDisconnect;
        bindViewModel();
        wireActions();
        loadingIndicator.visibleProperty().bind(gameService.busyProperty());
        loadingIndicator.managedProperty().bind(gameService.busyProperty());
        statusBar.textProperty().bind(gameService.statusMessageProperty());
    }

    @FXML
    private void initialize() {
        // FXML lifecycle — full init in initialize(Stage, ...)
    }

    private void bindViewModel() {
        levelLabel.textProperty().bind(viewModel.levelProperty().asString("Level: %d"));
        goldLabel.textProperty().bind(viewModel.goldProperty().asString("Gold: %d"));
        ironLabel.textProperty().bind(viewModel.ironProperty().asString("Iron: %d"));
        lumberLabel.textProperty().bind(viewModel.lumberProperty().asString("Wood: %d"));
        populationLabel.textProperty().bind(Bindings.format("Population: %d / %d",
                viewModel.populationProperty(), viewModel.maxPopulationProperty()));
        buildingCountLabel.textProperty().bind(viewModel.buildingCountProperty().asString("Buildings: %d"));
        armySizeLabel.textProperty().bind(viewModel.armySizeProperty().asString("Army: %d"));
        foodLabel.textProperty().bind(viewModel.foodProperty().asString("Food: %d"));
        trophiesLabel.textProperty().bind(viewModel.trophiesProperty().asString("Trophies: %d"));
    }

    private void wireActions() {
        createVillageButton.setOnAction(e -> gameService.createVillage(vc -> {
                    onVillageUpdated(vc);
                    UiMessages.showSuccess(primaryStage, "Village created", "Your new village is ready.");
                },
                err -> UiMessages.showError(primaryStage, "Create village failed", err.getMessage())));

        refreshButton.setOnAction(e -> gameService.refreshVillage(this::onVillageUpdated,
                err -> UiMessages.showError(primaryStage, "Refresh failed", err.getMessage())));

        upgradeButton.setOnAction(e -> gameService.upgradeVillage(vc -> {
            if (vc == null) {
                UiMessages.showError(primaryStage, "Upgrade failed", "Insufficient funds or max level reached.");
            } else {
                onVillageUpdated(vc);
                UiMessages.showSuccess(primaryStage, "Upgrade", "Village hall upgraded!");
            }
        }, err -> UiMessages.showError(primaryStage, "Upgrade failed", err.getMessage())));

        addBuildingButton.setOnAction(e -> openBuildingDialog());
        addWorkerButton.setOnAction(e -> openWorkerDialog());

        attackButton.setOnAction(e -> openCombatScreen(true));
        defendButton.setOnAction(e -> openCombatScreen(false));

        disconnectButton.setOnAction(e -> {
            if (onDisconnect != null) {
                onDisconnect.run();
            }
        });
    }

    private void onVillageUpdated(VillageController controller) {
        viewModel.updateFrom(controller);
        renderVillageMap(controller);
    }

    private void renderVillageMap(VillageController controller) {
        buildingsPane.getChildren().clear();
        workersPane.getChildren().clear();
        armyPane.getChildren().clear();

        if (controller == null) {
            return;
        }

        VillageMap map = controller.getVillageMap();
        if (map != null) {
            buildingsPane.getChildren().add(EntityNodeFactory.createVillageHallNode(map.getVillageHall()));
            if (map.getBuildings() != null) {
                for (Building building : map.getBuildings()) {
                    if (building instanceof VillageMap.VillageHall) {
                        continue;
                    }
                    buildingsPane.getChildren().add(EntityNodeFactory.createEntityNode(building));
                }
            }
        }

        WorkForce workForce = controller.getWorkForce();
        if (workForce != null && workForce.getWorkers() != null) {
            for (Peasant worker : workForce.getWorkers()) {
                if (worker instanceof VillageElements.VillageEntity entity) {
                    workersPane.getChildren().add(EntityNodeFactory.createEntityNode(entity));
                }
            }
        }

        ArmyUnit army = controller.getArmyUnit();
        if (army != null && army.getArmyUnit() != null) {
            for (Recruitable unit : army.getArmyUnit()) {
                if (unit instanceof VillageElements.VillageEntity entity) {
                    armyPane.getChildren().add(EntityNodeFactory.createEntityNode(entity));
                }
            }
        }

        GridPane mapGrid = new GridPane();
        mapGrid.setHgap(8);
        mapGrid.setVgap(8);
        mapGrid.setPadding(new Insets(12));
        mapGrid.add(new Label("Buildings"), 0, 0);
        mapGrid.add(buildingsPane, 0, 1);
        mapGrid.add(new Label("Workers"), 1, 0);
        mapGrid.add(workersPane, 1, 1);
        mapGrid.add(new Label("Army"), 2, 0);
        mapGrid.add(armyPane, 2, 1);
        mapScroll.setContent(mapGrid);
    }

    private void openBuildingDialog() {
        BuildingDialogController dialog = FxmlLoaderHelper.load("/javafx/fxml/building-dialog.fxml");
        dialog.show(primaryStage, gameService, building ->
                gameService.refreshVillage(this::onVillageUpdated,
                        err -> UiMessages.showError(primaryStage, "Refresh failed", err.getMessage())));
    }

    private void openWorkerDialog() {
        WorkerDialogController dialog = FxmlLoaderHelper.load("/javafx/fxml/worker-dialog.fxml");
        dialog.show(primaryStage, gameService, worker ->
                gameService.refreshVillage(this::onVillageUpdated,
                        err -> UiMessages.showError(primaryStage, "Refresh failed", err.getMessage())));
    }

    private void openCombatScreen(boolean attackMode) {
        CombatController combat = FxmlLoaderHelper.load("/javafx/fxml/combat.fxml");
        combat.show(primaryStage, gameService, viewModel, attackMode, () -> {
            gameService.refreshVillage(vc -> {
                viewModel.updateFrom(vc);
                renderVillageMap(vc);
            }, err -> UiMessages.showError(primaryStage, "Refresh failed", err.getMessage()));
        });
    }

    public Parent getRoot() {
        return root;
    }
}
