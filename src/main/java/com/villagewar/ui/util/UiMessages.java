package com.villagewar.ui.util;

import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * Shared alert helpers and status-bar factory.
 */
public final class UiMessages {

    private UiMessages() {
    }

    public static void showError(Stage owner, String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        if (owner != null) {
            alert.initOwner(owner);
        }
        alert.showAndWait();
    }

    public static void showInfo(Stage owner, String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        if (owner != null) {
            alert.initOwner(owner);
        }
        alert.showAndWait();
    }

    public static void showSuccess(Stage owner, String title, String message) {
        showInfo(owner, title, message);
    }

    public static Label createStatusLabel() {
        Label label = new Label("Ready");
        label.getStyleClass().add("status-bar");
        label.setMaxWidth(Double.MAX_VALUE);
        return label;
    }
}
