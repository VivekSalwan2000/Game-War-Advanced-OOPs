package com.villagewar.ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import com.villagewar.ui.controller.MainMenuController;
import com.villagewar.ui.util.FxmlLoaderHelper;

/**
 * Root JavaFX application — loads the main menu scene.
 */
public class VillageWarApplication extends Application {

    public static final int DEFAULT_WIDTH = 1024;
    public static final int DEFAULT_HEIGHT = 720;

    @Override
    public void start(Stage primaryStage) {
        MainMenuController controller = FxmlLoaderHelper.load("/javafx/fxml/main-menu.fxml");
        Scene scene = new Scene(controller.getRoot(), DEFAULT_WIDTH, DEFAULT_HEIGHT);
        scene.getStylesheets().add(getClass().getResource("/javafx/css/villagewar.css").toExternalForm());

        primaryStage.setTitle("VillageWar");
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(900);
        primaryStage.setMinHeight(600);
        primaryStage.show();

        controller.setPrimaryStage(primaryStage);
    }
}
