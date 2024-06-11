package com.example.projektksiegarnia;

import constants.Scenes;
import javafx.application.Application;
import javafx.stage.Stage;


public class Boot extends Application {
    public static void main(String[] args) {
        //launch is calling method start
        launch();
    }

    @Override
    public void start(Stage stage) {
        SceneManager.PrimaryStage = stage;
        SceneManager.PrimaryStage.setTitle("ProjektKsiegarnia");

        //this have to be called here because after exiting start method we're losing all references to stage
        SceneManager.LoadScene(Scenes.Boot);
    }
}