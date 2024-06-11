package com.example.projektksiegarnia;

import constants.Constants;
import constants.Scenes;
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
        String l = login.getText();
        String p = password.getText();

        //i fucking hate java .equals my balls
        if(l.equals(Constants.ClientLogin) && p.equals(Constants.ClientPassword))
            SceneManager.LoadScene(Scenes.Client);
        else if (l.equals(Constants.ModLogin) && p.equals(Constants.ModPassword))
            SceneManager.LoadScene(Scenes.Moderator);
        else if (l.equals(Constants.AdminLogin) && p.equals(Constants.AdminPassword))
            SceneManager.LoadScene(Scenes.Admin);
        else
            SceneManager.ShowAlert("T","ZLy login lub haslo: " + l + " / " + p);

    }

}
