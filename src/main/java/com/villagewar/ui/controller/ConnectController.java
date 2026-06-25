package com.villagewar.ui.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import com.villagewar.ui.service.GameClientService;
import javafx.stage.Modality;
import javafx.stage.Stage;
import com.villagewar.ui.util.UiMessages;

/**
 * Connection dialog — collects host/port and delegates to {@link GameClientService#connect}.
 */
public class ConnectController {

    @FXML
    private VBox root;

    @FXML
    private TextField hostField;

    @FXML
    private TextField portField;

    @FXML
    private Button connectButton;

    @FXML
    private Button cancelButton;

    @FXML
    private Label statusLabel;

    @FXML
    private ProgressIndicator progressIndicator;

    private Stage dialogStage;
    private Runnable onConnected;

    @FXML
    private void initialize() {
        hostField.setText("localhost");
        portField.setText("8080");
        progressIndicator.setVisible(false);
        progressIndicator.setManaged(false);
        cancelButton.setOnAction(e -> {
            if (dialogStage != null) {
                dialogStage.close();
            }
        });
    }

    public void show(Stage owner, GameClientService service, Runnable onConnected) {
        this.onConnected = onConnected;
        dialogStage = new Stage();
        dialogStage.initOwner(owner);
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setTitle("Connect to Server");
        dialogStage.setScene(new javafx.scene.Scene(root, 420, 260));
        dialogStage.getScene().getStylesheets()
                .add(getClass().getResource("/javafx/css/villagewar.css").toExternalForm());

        service.statusMessageProperty().addListener((obs, o, n) -> statusLabel.setText(n));
        service.busyProperty().addListener((obs, o, busy) -> {
            progressIndicator.setVisible(busy);
            progressIndicator.setManaged(busy);
            connectButton.setDisable(busy);
        });

        connectButton.setOnAction(e -> {
            String host = hostField.getText().trim();
            int port;
            try {
                port = Integer.parseInt(portField.getText().trim());
            } catch (NumberFormatException ex) {
                UiMessages.showError(dialogStage, "Invalid port", "Enter a valid port number.");
                return;
            }
            service.connect(host, port,
                    vc -> {
                        dialogStage.close();
                        if (onConnected != null) {
                            onConnected.run();
                        }
                    },
                    err -> UiMessages.showError(dialogStage, "Connection failed", err.getMessage()));
        });

        dialogStage.showAndWait();
    }
}
