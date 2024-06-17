package com.example.projektksiegarnia.viewmodels;

import com.example.projektksiegarnia.DataBaseManager;
import com.example.projektksiegarnia.SceneManager;
import com.example.projektksiegarnia.views.*;
import constants.AdminOptions;
import constants.Scenes;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class AdminViewModel {
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

    AdminOptions SelectedOption;

    private final List<TextField> textFieldList = new ArrayList<>();
    @FXML
    void initialize(){
        SetGatunkiComboBox();
        SetJezykiComboBox();
        SetWydawnictwaComboBox();
        SelectedOption = AdminOptions.View;
        //this is hard coded myb change later
        MainText.setText("Zalogowano jako Administrator");
    }


    @FXML
    void OnEditClick(){
        SelectedOption = AdminOptions.View;
        SetBooksBasedOnSelectedOptions(TytulName.getText(),GatunkiCB.getValue(),JezykiCB.getValue(),WydawnictwaCB.getValue(),SelectedOption);
    }
    @FXML
    void OnDeleteClick(){
        SelectedOption = AdminOptions.Delete;
        SetBooksBasedOnSelectedOptions(TytulName.getText(),GatunkiCB.getValue(),JezykiCB.getValue(),WydawnictwaCB.getValue(),SelectedOption);
    }
    @FXML
    void OnAddClick(){
        SelectedOption = AdminOptions.Add;
        AddKsiazkaTextField();
    }

    @FXML
    void OnLogoutClick(){
        SceneManager.LoadScene(Scenes.Login);
    }

    @FXML
    void OnSearchClick(){


        SetBooksBasedOnSelectedOptions(TytulName.getText(),GatunkiCB.getValue(),JezykiCB.getValue(),WydawnictwaCB.getValue(),SelectedOption);
    }

    void SetBooksBasedOnSelectedOptions(String Tytul, String Gatunek, String Jezyk, String Wydawnictwo, AdminOptions options){
        MainContainer.getChildren().clear();

        Session s = DataBaseManager.getSessionFactory().openSession();
        List<KsiazkaView> ksiazki = s.createQuery("FROM KsiazkaView").list();
        for (int i=0;i<ksiazki.size();i++){
            KsiazkaView k = ksiazki.get(i);

            if(!Tytul.equals("") && !k.getTytul().getNazwa().contains(Tytul))
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
            if(!SelectedOption.equals(AdminOptions.View)){
                Button b = new Button();
                b.setText(SelectedOption.name());
                int bookID = k.getId();
                b.setOnAction(_ -> DynamicButtonAction(bookID, SelectedOption));
                box.getChildren().add(b);
            }

            MainContainer.getChildren().add(box);
        }
        s.close();
    }
    void DynamicButtonAction(int ksiazkaID,AdminOptions option) {
        Session s = DataBaseManager.getSessionFactory().openSession();
        Transaction t = s.beginTransaction();
        KsiazkaView ksiazka = s.get(KsiazkaView.class, ksiazkaID);
        switch (option) {
            case Add-> {
                // Pobranie maksymalnego id z tabeli KsiazkaView
                Integer maxId = (Integer) s.createQuery("SELECT MAX(id) FROM KsiazkaView").uniqueResult();
                int nextId = (maxId == null) ? 1 : maxId + 1;
                KsiazkaView nowaKsiazka = new KsiazkaView();
                nowaKsiazka.setId(nextId);
                for (int i = 0; i < textFieldList.size(); i++) {
                    String value = textFieldList.get(i).getText();
                    switch (i) {
                        case 0:
                            TytulView tytul = new TytulView();
                            tytul.setNazwa(value);
                            nowaKsiazka.setTytul(tytul);
                            break;
                        case 1:
                            GatunekView gatunek = new GatunekView();
                            gatunek.setNazwa(value);
                            nowaKsiazka.setGatunek(gatunek);
                            break;
                        case 2:
                            WydawnictwoView wydawnictwo = new WydawnictwoView();
                            wydawnictwo.setNazwa(value);
                            nowaKsiazka.setWydawnictwo(wydawnictwo);
                            break;
                        case 3:
                            try {
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
                                Date rokWydania = sdf.parse(value);
                                nowaKsiazka.setRokWydania(rokWydania);
                            } catch (Exception e) {
                                System.out.println(e);
                                ;
                            }
                            break;
                        case 4:
                            JezykView jezyk = new JezykView();
                            jezyk.setNazwa(value);
                            nowaKsiazka.setJezyk(jezyk);
                            break;
                        case 5:
                            UzytkownikView uzytkownik = new UzytkownikView();
                            uzytkownik.setImie(value);
                            nowaKsiazka.setUzytkownik(uzytkownik);
                            break;
                    }
                }
                s.save(nowaKsiazka);
                t.commit();

            }
            case Delete -> {
                System.out.println("id usuwanej:"+ksiazka.getId());
                s.delete(ksiazka);
                t.commit();
                t = s.beginTransaction();

            }
            case Edit ->{

            }

        }

        s.merge(ksiazka);
        t.commit();
        s.close();
        switch (option){
            case Add    -> OnAddClick();
            case Edit   -> OnEditClick();
            case Delete -> OnDeleteClick();
        }
    }

    void AddKsiazkaTextField() {
        MainContainer.getChildren().clear();

        // Pobieranie pól zadeklarowanych w klasie KsiazkaView
        Field[] fields = KsiazkaView.class.getDeclaredFields();

        for (Field field : fields) {
            if (field.getName()=="id"){
                continue;
            }
            HBox box = new HBox();
            box.setSpacing(30d);
            Label label = new Label(field.getName());
            TextField textField = new TextField();
            textField.setId(field.getName());
            textFieldList.add(textField);
            box.getChildren().addAll(label, textField);
            MainContainer.getChildren().add(box);
        }
        // Dodanie przycisku do kontenera
        Button b = new Button("Zapisz");
        b.setOnAction(_ -> {
            StringBuilder bookInfo = new StringBuilder("Wprowadzone dane:\n");
            for (TextField textField : textFieldList) {
                bookInfo.append(textField.getId()).append(": ").append(textField.getText()).append("\n");
            }
            // Wyświetlanie wprowadzonego tekstu
            System.out.println(bookInfo.toString());
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Wprowadzone dane");
            alert.setHeaderText(null);
            alert.setContentText(bookInfo.toString());
            alert.showAndWait();
        });
        HBox box = new HBox();
        box.setSpacing(30d);
        box.getChildren().add(b);
        MainContainer.getChildren().add(box);
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
