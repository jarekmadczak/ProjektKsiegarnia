package com.example.projektksiegarnia;

import constants.Scenes;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ClientViewModel {
    @FXML
    Label MainText;

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
