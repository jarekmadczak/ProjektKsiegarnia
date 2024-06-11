package com.example.projektksiegarnia;

import constants.Constants;
import constants.Scenes;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class SceneManager {
    public static Stage PrimaryStage;
    public static Scene CurrentScene;

    public static void LoadScene(Scenes scene) {
        FXMLLoader fxmlLoader = null;
        switch (scene){
            case Boot -> fxmlLoader = new FXMLLoader(Boot.class.getResource(Constants.BootFXML));

            case Login -> fxmlLoader = new FXMLLoader(Boot.class.getResource(Constants.LoginFXML));

            case Client -> fxmlLoader = new FXMLLoader(Boot.class.getResource(Constants.ClientFXML));

            case Moderator -> fxmlLoader = new FXMLLoader(Boot.class.getResource(Constants.ModeratorFXML));

            case Admin -> fxmlLoader = new FXMLLoader(Boot.class.getResource(Constants.AdminFXML));
        }



        if(fxmlLoader == null){
            ShowAlert("Error when loading scene.","fxml was null");
            return;
        }

        try{
            if(CurrentScene != null)
                CurrentScene = new Scene(fxmlLoader.load(),CurrentScene.getWidth(),CurrentScene.getHeight());
            else
                CurrentScene = new Scene(fxmlLoader.load(), 1280, 720);

        }catch (IOException e){
            ShowAlert("Error when loading scene.",e.getMessage());
            return;
        }catch (Exception e){
            ShowAlert("Error when loading scene.", "Resources for: " + scene.name() + " scene dont exist");
            return;
        }

        SceneManager.PrimaryStage.setScene(CurrentScene);
        SceneManager.PrimaryStage.show();

        //perform callback
        OnSceneLoaded(scene);

    }

    public static void ShowAlert(String title, String content){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    static void OnSceneLoaded(Scenes scene){
        switch (scene){
            case Boot -> LoadScene(Scenes.Login);

            case Login -> {

            }

            case Client -> {
                //fxmlLoader = new FXMLLoader(Boot.class.getResource(Constants.BootFXML));
            }
            case Moderator -> {
                //fxmlLoader = new FXMLLoader(Boot.class.getResource(Constants.BootFXML));
            }
            case Admin -> {
                //fxmlLoader = new FXMLLoader(Boot.class.getResource(Constants.BootFXML));
            }
        }
    }
    public static boolean resourceExists(String resourcePath) {
        return Boot.class.getResource(resourcePath) != null;
    }
}