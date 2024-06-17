package com.example.projektksiegarnia.viewmodels;

import com.example.projektksiegarnia.DataBaseManager;
import com.example.projektksiegarnia.SceneManager;
import com.example.projektksiegarnia.views.*;
import constants.ClientOptions;
import constants.Scenes;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.hibernate.Session;
import org.hibernate.Transaction;

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

    ClientOptions SelectedOption;


    @FXML
    void initialize(){
        SetGatunkiComboBox();
        SetJezykiComboBox();
        SetWydawnictwaComboBox();
        SelectedOption = ClientOptions.View;
        //this is hard coded myb change later
        MainText.setText("Zalogowano jako uzytkownik 1");
    }


    @FXML
    void OnBorrowedClick(){
        SelectedOption = ClientOptions.View;
        SetBooksBasedOnSelectedOptions(TytulName.getText(),GatunkiCB.getValue(),JezykiCB.getValue(),WydawnictwaCB.getValue(),1L,SelectedOption);
    }
    @FXML
    void OnBorrowClick(){
        SelectedOption = ClientOptions.Borrow;
        SetBooksBasedOnSelectedOptions(TytulName.getText(),GatunkiCB.getValue(),JezykiCB.getValue(),WydawnictwaCB.getValue(),Long.MIN_VALUE,SelectedOption);
    }

    @FXML
    void OnReturnClick(){
        SelectedOption = ClientOptions.Return;
        SetBooksBasedOnSelectedOptions(TytulName.getText(),GatunkiCB.getValue(),JezykiCB.getValue(),WydawnictwaCB.getValue(),1L,SelectedOption);
    }

    @FXML
    void OnLogoutClick(){
        SceneManager.LoadScene(Scenes.Login);
    }

    @FXML
    void OnSearchClick(){
        Long id = (SelectedOption.equals(ClientOptions.View) || SelectedOption.equals(ClientOptions.Return))? 1L : Long.MIN_VALUE;
        System.out.println(id == Long.MIN_VALUE);
        SetBooksBasedOnSelectedOptions(TytulName.getText(),GatunkiCB.getValue(),JezykiCB.getValue(),WydawnictwaCB.getValue(),id,SelectedOption);
    }

    void SetBooksBasedOnSelectedOptions(String Tytul, String Gatunek, String Jezyk, String Wydawnictwo, Long UserID, ClientOptions options){
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

            HBox box = new HBox();
            box.setSpacing(30d);
            box.getChildren().add(new Label(k.GetNormalizedInfo()));
            if(!SelectedOption.equals(ClientOptions.View)){
                Button b = new Button();
                b.setText(SelectedOption.name());
                int bookID = k.getId();
                b.setOnAction(_ -> DynamicButtonAction(bookID, SelectedOption,1L));
                box.getChildren().add(b);
            }

            MainContainer.getChildren().add(box);
        }
        s.close();
    }
    void DynamicButtonAction(int ksiazkaID,ClientOptions option,Long UserId){
        Session s = DataBaseManager.getSessionFactory().openSession();
        Transaction t = s.beginTransaction();
        KsiazkaView ksiazka = s.get(KsiazkaView.class,ksiazkaID);

        switch (option){
            case Borrow -> {
                UzytkownikView uzytkownik = s.get(UzytkownikView.class,UserId);
                ksiazka.setUzytkownik(uzytkownik);
            }
            case Return -> {
                ksiazka.setUzytkownik(null);
            }
        }
        s.merge(ksiazka);
        t.commit();
        s.close();
        switch (option){
            case Borrow -> OnBorrowClick();
            case Return -> OnReturnClick();
        }
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
