package com.villagewar.ui.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;
import java.net.URL;

/**
 * Loads FXML from the classpath and returns the controller instance.
 */
public final class FxmlLoaderHelper {

    private FxmlLoaderHelper() {
    }

    @SuppressWarnings("unchecked")
    public static <T> T load(String resourcePath) {
        URL url = FxmlLoaderHelper.class.getResource(resourcePath);
        if (url == null) {
            throw new IllegalStateException("FXML resource not found: " + resourcePath);
        }
        FXMLLoader loader = new FXMLLoader(url);
        try {
            loader.load();
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load FXML: " + resourcePath, e);
        }
        return (T) loader.getController();
    }

    public static Parent loadRoot(String resourcePath) {
        URL url = FxmlLoaderHelper.class.getResource(resourcePath);
        if (url == null) {
            throw new IllegalStateException("FXML resource not found: " + resourcePath);
        }
        try {
            return FXMLLoader.load(url);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load FXML: " + resourcePath, e);
        }
    }
}
