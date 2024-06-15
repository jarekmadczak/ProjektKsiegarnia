package com.example.projektksiegarnia.viewmodels;

import com.example.projektksiegarnia.DataBaseManager;
import com.example.projektksiegarnia.SceneManager;
import com.example.projektksiegarnia.views.GatunekView;
import com.example.projektksiegarnia.views.JezykView;
import com.example.projektksiegarnia.views.KsiazkaView;
import com.example.projektksiegarnia.views.WydawnictwoView;
import constants.Scenes;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import org.hibernate.Session;

import java.util.List;

public class ClientViewModel {
    @FXML
    Label MainText;

    @FXML
    ComboBox<String> GatunkiCB;

    @FXML
    ComboBox<String> JezykiCB;

    @FXML
    ComboBox<String> WydawnictwaCB;

    @FXML
    VBox MainContainer;

    @FXML
    TextField TytulName;

    boolean OnlyUserBooks;

    @FXML
    void initialize(){
        SetGatunkiComboBox();
        SetJezykiComboBox();
        SetWydawnictwaComboBox();
        OnlyUserBooks = false;
        //this is hard coded myb change later
        MainText.setText("Zalogowano jako uzytkownik 1");
    }


    @FXML
    void OnBorrowedClick(){
        OnlyUserBooks = true;
        SetBooksBasedOnSelectedOptions(TytulName.getText(),GatunkiCB.getValue(),JezykiCB.getValue(),WydawnictwaCB.getValue(),1L);
    }
    @FXML
    void OnBorrowClick(){
        OnlyUserBooks = false;
        SetBooksBasedOnSelectedOptions(TytulName.getText(),GatunkiCB.getValue(),JezykiCB.getValue(),WydawnictwaCB.getValue(),Long.MIN_VALUE);
    }

    @FXML
    void OnReturnClick(){
        OnlyUserBooks = true;
        SetBooksBasedOnSelectedOptions(TytulName.getText(),GatunkiCB.getValue(),JezykiCB.getValue(),WydawnictwaCB.getValue(),1L);
    }

    @FXML
    void OnLogoutClick(){
        SceneManager.LoadScene(Scenes.Login);
    }

    @FXML
    void OnSearchClick(){
        Long id = OnlyUserBooks? 1L : Long.MIN_VALUE;
        System.out.println(id == Long.MIN_VALUE);
        SetBooksBasedOnSelectedOptions(TytulName.getText(),GatunkiCB.getValue(),JezykiCB.getValue(),WydawnictwaCB.getValue(),id);
    }

    void SetBooksBasedOnSelectedOptions(String Tytul, String Gatunek, String Jezyk, String Wydawnictwo, Long UserID){
        MainContainer.getChildren().clear();
        Session s = DataBaseManager.getSessionFactory().openSession();
        List<KsiazkaView> ksiazki = s.createQuery("FROM KsiazkaView").list();
        for (int i=0;i<ksiazki.size();i++){
            KsiazkaView k = ksiazki.get(i);

            if(!Tytul.equals("") && !k.getTytul().getNazwa().contains(Tytul))
                continue;
            if(!k.getUzytkownik().getId().equals(UserID))
                continue;
            if(!Gatunek.equals("-") && !k.getGatunek().getNazwa().equals(Gatunek))
                continue;
            if(!Jezyk.equals("-") && !k.getJezyk().getNazwa().equals(Jezyk))
                continue;
            if(!Wydawnictwo.equals("-") && !k.getWydawnictwo().getNazwa().equals(Wydawnictwo))
                continue;

            MainContainer.getChildren().add(new Label(k.GetNormalizedInfo()));
        }
        s.close();
    }

    void SetGatunkiComboBox(){
        Session s = DataBaseManager.getSessionFactory().openSession();
        s.beginTransaction();
        List<GatunekView> gatunki = s.createQuery("FROM GatunekView").list();
        GatunkiCB.getItems().clear();
        GatunkiCB.getItems().add("-");

        for (int i=0;i<gatunki.size();i++){
            GatunkiCB.getItems().add(gatunki.get(i).getNazwa());
        }
        GatunkiCB.setValue("-");
        s.close();
    }

    void SetJezykiComboBox(){
        Session s = DataBaseManager.getSessionFactory().openSession();
        s.beginTransaction();
        List<JezykView> jezyki = s.createQuery("FROM JezykView").list();
        JezykiCB.getItems().clear();
        JezykiCB.getItems().add("-");

        for (int i=0;i<jezyki.size();i++){
            JezykiCB.getItems().add(jezyki.get(i).getNazwa());
        }
        JezykiCB.setValue("-");
        s.close();
    }

    void SetWydawnictwaComboBox(){
        Session s = DataBaseManager.getSessionFactory().openSession();
        s.beginTransaction();
        List<WydawnictwoView> wydawnictwa = s.createQuery("FROM WydawnictwoView").list();
        WydawnictwaCB.getItems().clear();
        WydawnictwaCB.getItems().add("-");
        for (int i=0;i<wydawnictwa.size();i++){
            WydawnictwaCB.getItems().add(wydawnictwa.get(i).getNazwa());
        }
        WydawnictwaCB.setValue("-");
        s.close();
    }
}
