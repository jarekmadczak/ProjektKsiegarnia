package com.example.projektksiegarnia.viewmodels;

import com.example.projektksiegarnia.SceneManager;
import constants.Scenes;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ClientViewModel {
    @FXML
    Label MainText;

//    @FXML
//    ComboBox<String> t;
//
//    @FXML
//    void initialize(){
//        t.getItems().clear();
//        t.getItems().add("string 1");
//        t.getItems().add("string 2");
//        t.getItems().add("string 3");
//        SceneManager.ShowAlert("T","ini");
//    }
    @FXML
    void OnBorrowedClick(){
        MainText.setText("WypożyczoneKsiążki");
    }
    @FXML
    void OnBorrowClick(){
        MainText.setText("WypżyczKsiążke");
    }
    @FXML
    void OnReturnClick(){
        MainText.setText("Zwróć książke");
    }

    @FXML
    void OnLogoutClick(){
        SceneManager.LoadScene(Scenes.Login);
    }
}
