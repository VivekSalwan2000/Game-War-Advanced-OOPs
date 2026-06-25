package com.villagewar.ui.controller;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import com.villagewar.ui.service.GameClientService;
import javafx.stage.Stage;
import com.villagewar.ui.util.FxmlLoaderHelper;

/**
 * Main menu — entry point for console-equivalent client or server connection.
 */
public class MainMenuController {

    @FXML
    private BorderPane root;

    @FXML
    private Button startClientButton;

    @FXML
    private Button connectButton;

    @FXML
    private Button exitButton;

    private Stage primaryStage;
    private final GameClientService gameService = new GameClientService();

    @FXML
    private void initialize() {
        startClientButton.setOnAction(e -> openConnectDialog());
        connectButton.setOnAction(e -> openConnectDialog());
        exitButton.setOnAction(e -> {
            gameService.shutdown();
            primaryStage.close();
        });
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public Parent getRoot() {
        return root;
    }

    private void openConnectDialog() {
        ConnectController connect = FxmlLoaderHelper.load("/javafx/fxml/connect-dialog.fxml");
        connect.show(primaryStage, gameService, this::openDashboard);
    }

    private void openDashboard() {
        DashboardController dashboard = FxmlLoaderHelper.load("/javafx/fxml/dashboard.fxml");
        dashboard.initialize(primaryStage, gameService, this::returnToMenu);

        Scene scene = new Scene(dashboard.getRoot(), com.villagewar.ui.VillageWarApplication.DEFAULT_WIDTH,
                com.villagewar.ui.VillageWarApplication.DEFAULT_HEIGHT);
        scene.getStylesheets().add(getClass().getResource("/javafx/css/villagewar.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.setTitle("VillageWar — Dashboard");
    }

    private void returnToMenu() {
        gameService.disconnect(() -> {
            Scene scene = new Scene(getRoot(), com.villagewar.ui.VillageWarApplication.DEFAULT_WIDTH,
                    com.villagewar.ui.VillageWarApplication.DEFAULT_HEIGHT);
            scene.getStylesheets().add(getClass().getResource("/javafx/css/villagewar.css").toExternalForm());
            primaryStage.setScene(scene);
            primaryStage.setTitle("VillageWar");
        });
    }
}
