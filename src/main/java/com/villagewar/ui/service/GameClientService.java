package com.villagewar.ui.service;

import ChallengeDecision.ChallengeResult;
import Controllers.VillageController;
import VillageElements.Building;
import VillageElements.Worker;
import connection.Clients.Client;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

/**
 * Async façade over {@link Client}. All socket I/O runs off the JavaFX thread;
 * callbacks are marshalled back with {@link Platform#runLater}.
 */
public class GameClientService {

    private final ExecutorService networkExecutor = Executors.newSingleThreadExecutor(r -> {
        Thread t = new Thread(r, "villagewar-network");
        t.setDaemon(true);
        return t;
    });

    private Client client;
    private final BooleanProperty busy = new SimpleBooleanProperty(false);
    private final StringProperty statusMessage = new SimpleStringProperty("Not connected");

    public BooleanProperty busyProperty() {
        return busy;
    }

    public StringProperty statusMessageProperty() {
        return statusMessage;
    }

    public boolean isConnected() {
        return client != null && client.isConnected();
    }

    public Client getClient() {
        return client;
    }

    public void connect(String host, int port, Consumer<VillageController> onSuccess, Consumer<Throwable> onError) {
        runAsync(() -> {
            client = new Client(port, host);
            client.connect();
            setStatus("Connected to " + host + ":" + port);
            return client.getVillageController();
        }, onSuccess, onError);
    }

    public void disconnect(Runnable onComplete) {
        runAsync(() -> {
            if (client != null) {
                client.disconnect();
                client = null;
            }
            setStatus("Disconnected");
            return null;
        }, ignored -> {
            if (onComplete != null) {
                onComplete.run();
            }
        }, throwable -> {
            if (onComplete != null) {
                onComplete.run();
            }
        });
    }

    public void createVillage(Consumer<VillageController> onSuccess, Consumer<Throwable> onError) {
        runAsync(() -> {
            requireClient();
            VillageController created = client.createVillage();
            if (created == null) {
                throw new IllegalStateException("Server could not create a village");
            }
            client.setVillageController(created);
            client.setVillageAvailable(true);
            setStatus("Village created");
            return created;
        }, onSuccess, onError);
    }

    public void refreshVillage(Consumer<VillageController> onSuccess, Consumer<Throwable> onError) {
        runAsync(() -> {
            requireClient();
            if (!client.isVillageAvailable()) {
                throw new IllegalStateException("No village exists yet — create one first");
            }
            VillageController updated = client.getVillage();
            client.setVillageController(updated);
            setStatus("Village refreshed");
            return updated;
        }, onSuccess, onError);
    }

    public void upgradeVillage(Consumer<VillageController> onSuccess, Consumer<Throwable> onError) {
        runAsync(() -> {
            requireClient();
            VillageController upgraded = client.upgradeVillage();
            if (upgraded != null) {
                client.setVillageController(upgraded);
                setStatus("Village upgraded");
            } else {
                setStatus("Upgrade failed — insufficient funds");
            }
            return upgraded;
        }, onSuccess, onError);
    }

    public void addBuilding(String buildingType, Consumer<Building> onSuccess, Consumer<Throwable> onError) {
        runAsync(() -> {
            requireClient();
            Building building = client.addBuilding(buildingType);
            if (building == null) {
                throw new IllegalStateException("Could not add building: " + buildingType);
            }
            VillageController refreshed = client.getVillage();
            client.setVillageController(refreshed);
            setStatus("Added " + buildingType);
            return building;
        }, onSuccess, onError);
    }

    public void addWorker(String workerType, Consumer<Worker> onSuccess, Consumer<Throwable> onError) {
        runAsync(() -> {
            requireClient();
            Worker worker = client.addWorker(workerType);
            if (worker == null) {
                throw new IllegalStateException("Could not add worker: " + workerType);
            }
            VillageController refreshed = client.getVillage();
            client.setVillageController(refreshed);
            setStatus("Recruited " + workerType);
            return worker;
        }, onSuccess, onError);
    }

    public void fightRandomVillage(Consumer<ChallengeResult> onSuccess, Consumer<Throwable> onError) {
        runAsync(() -> {
            requireClient();
            setStatus("Attacking random village…");
            ChallengeResult result = client.fightRandomVillage();
            if (result == null) {
                throw new IllegalStateException("Attack could not be processed");
            }
            setStatus(result.getChallengeWon() ? "Victory!" : "Defeat");
            return result;
        }, onSuccess, onError);
    }

    public void randomAttackOnVillage(Consumer<ChallengeResult> onSuccess, Consumer<Throwable> onError) {
        runAsync(() -> {
            requireClient();
            setStatus("Defending against random army…");
            ChallengeResult result = client.randomAttackOnYourVillage();
            if (result == null) {
                throw new IllegalStateException("Defense could not be processed");
            }
            setStatus(result.getChallengeWon() ? "Defense held!" : "Village raided");
            return result;
        }, onSuccess, onError);
    }

    public void shutdown() {
        networkExecutor.shutdownNow();
    }

    private void requireClient() {
        if (client == null || !client.isConnected()) {
            throw new IllegalStateException("Not connected to server");
        }
    }

    private void setStatus(String message) {
        Platform.runLater(() -> statusMessage.set(message));
    }

    private <T> void runAsync(
            NetworkTask<T> task,
            Consumer<T> onSuccess,
            Consumer<Throwable> onError) {
        busy.set(true);
        CompletableFuture
                .supplyAsync(() -> {
                    try {
                        return task.call();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }, networkExecutor)
                .whenComplete((result, error) -> Platform.runLater(() -> {
                    busy.set(false);
                    if (error != null) {
                        Throwable cause = error.getCause() != null ? error.getCause() : error;
                        statusMessage.set("Error: " + cause.getMessage());
                        if (onError != null) {
                            onError.accept(cause);
                        }
                    } else if (onSuccess != null) {
                        onSuccess.accept(result);
                    }
                }));
    }

    @FunctionalInterface
    private interface NetworkTask<T> {
        T call() throws IOException, ClassNotFoundException;
    }
}
