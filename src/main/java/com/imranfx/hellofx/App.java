package com.imranfx.hellofx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * JavaFX App
 */
public class App extends Application {
    
//    TODO
//    Procastinate
//    link picture, description into database
//    picture able to search
//    pictures generate video
    private static Scene scene;
    public static final int width = 1280;
    public static final int height = 720;

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("main"), width, height);
        stage.setScene(scene);
        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }
    

}