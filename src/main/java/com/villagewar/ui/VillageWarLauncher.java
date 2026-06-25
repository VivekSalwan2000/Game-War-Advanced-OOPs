package com.villagewar.ui;

/**
 * Launcher class required for JavaFX in modular / Maven builds.
 * Do not call {@link javafx.application.Application#launch} from a subclass of Application.
 */
public final class VillageWarLauncher {
    private VillageWarLauncher() {
    }

    public static void main(String[] args) {
        VillageWarApplication.launch(VillageWarApplication.class, args);
    }
}
