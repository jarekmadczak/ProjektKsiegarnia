package com.example.projektksiegarnia.viewmodels;

import com.example.projektksiegarnia.DataBaseManager;
import com.example.projektksiegarnia.SceneManager;
import com.example.projektksiegarnia.views.KsiazkaView;
import constants.Constants;
import constants.Scenes;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.hibernate.Session;
import java.util.List;

public class LoginViewModel {
    @FXML
    TextField login;

    @FXML
    PasswordField password;

    @FXML
    void initialize(){
        Session session = DataBaseManager.getSessionFactory().openSession();
        session.beginTransaction();
        String msg = "";
        List<KsiazkaView> ksiazki = session.createQuery("FROM KsiazkaView").list();
        for(int i=0;i<ksiazki.size();i++){
            msg += "t";
            KsiazkaView k = ksiazki.get(i);
            msg += k.getId() + " - " + k.getGatunek().getNazwa() + " - " + k.getJezyk().getNazwa() + " -U: " + k.getUzytkownik().getImie() ;
        }
        SceneManager.DEBUG = msg;


    }


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
        else if (l.equals("debug") && p.equals("debug"))
            SceneManager.ShowAlert("T",SceneManager.DEBUG);
        else
            SceneManager.ShowAlert("T","ZLy login lub haslo: " + l + " / " + p);

    }

}
