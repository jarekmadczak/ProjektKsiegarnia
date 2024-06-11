package com.example.projektksiegarnia;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginViewModel {
    @FXML
    TextField login;

    @FXML
    PasswordField password;

    @FXML
    void OnLoginClicked(){
        SceneManager.ShowAlert("T" , login.getText() + " --- " + password.getText());
    }

}
