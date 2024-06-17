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
import java.time.LocalDate;
import java.util.*;

public class AdminViewModel {
    @FXML
    VBox MainContainer;

    @FXML
    Button FooterButton;

    @FXML
    void initialize(){
        FooterButton.setOnAction(_->Logout());
        FooterButton.setText("Wyloguj");
    }

    @FXML
    void OnAddClicked(){
        ShowOptions(AdminOptions.Add);
    }
    @FXML
    void OnEditClicked(){
        ShowOptions(AdminOptions.Edit);
    }
    @FXML
    void OnRemoveClicked(){
        ShowOptions(AdminOptions.Remove);
    }



    void ShowOptions(AdminOptions options){
        MainContainer.getChildren().clear();
        FooterButton.setText("Powrót");
        FooterButton.setOnAction(_->Return());
        VBox box = new VBox();
        box.setSpacing(30d);
        Button[] buttons = new Button[6];

        for(int i=0;i<buttons.length;i++){
            buttons[i] = new Button();
        }

        buttons[0].setText("Gatunki");
        buttons[1].setText("Jezyki");
        buttons[2].setText("Ksiazki");
        buttons[3].setText("Tytuly");
        buttons[4].setText("Uzytkownicy");
        buttons[5].setText("Wydawnictwa");

        switch (options) {
            case Add -> {
                for(int i=0;i<buttons.length;i++){
                    Class type = GetDataType(buttons[i].getText());
                    buttons[i].setOnAction(_ -> AddData(type));
                }
            }
            case Edit -> {
                for(int i=0;i<buttons.length;i++){
                    Class type = GetDataType(buttons[i].getText());
                    buttons[i].setOnAction(_ -> EditData(type));
                }
            }
            case Remove -> {
                for(int i=0;i<buttons.length;i++){
                    Class type = GetDataType(buttons[i].getText());
                    buttons[i].setOnAction(_ -> RemoveData(type));
                }
            }
        }

        for(int i=0;i<buttons.length;i++){
            box.getChildren().add(buttons[i]);
        }


        MainContainer.getChildren().add(box);

    }

    void AddData(Class type){
        GenerateTextFields(type);
    }


    void EditData(Class type){
        if(type.equals(KsiazkaView.class)){
            //edit ksiazki
            System.out.println("wip");
            return;
        }
        System.out.println("Edit " + type.getName());

    }

    void RemoveData(Class type){
        MainContainer.getChildren().clear();
        Session s = DataBaseManager.getSessionFactory().openSession();
        List<Object> q = s.createQuery("FROM " + type.getSimpleName()).list();

        for(int i=0;i<q.size();i++){
            HBox box = new HBox();
            box.setSpacing(20d);
            Button b = new Button();
            Label t = new Label();
            b.setText("Remove");
            if(type.equals(GatunekView.class)){
                GatunekView g = (GatunekView)q.get(i);
                t.setText(g.GetNormalizedInfo());
                b.setOnAction(_->{
                    g.RemoveThis();
                    RemoveData(type);
                });
            }
            if(type.equals(JezykView.class)){
                JezykView g = (JezykView) q.get(i);
                t.setText(g.GetNormalizedInfo());
                b.setOnAction(_->{
                    g.RemoveThis();
                    RemoveData(type);
                });
            }
            if(type.equals(KsiazkaView.class)){
                KsiazkaView g = (KsiazkaView) q.get(i);
                t.setText(g.GetNormalizedInfo());
                b.setOnAction(_->{
                    g.RemoveThis();
                    RemoveData(type);
                });
            }
            if(type.equals(TytulView.class)){
                TytulView g = (TytulView) q.get(i);
                t.setText(g.GetNormalizedInfo());
                b.setOnAction(_->{
                    g.RemoveThis();
                    RemoveData(type);
                });
            }
            if(type.equals(UzytkownikView.class)){
                UzytkownikView g = (UzytkownikView) q.get(i);
                t.setText(g.GetNormalizedInfo());
                b.setOnAction(_->{
                    g.RemoveThis();
                    RemoveData(type);
                });
            }
            if(type.equals(WydawnictwoView.class)){
                WydawnictwoView g = (WydawnictwoView) q.get(i);
                t.setText(g.GetNormalizedInfo());
                b.setOnAction(_->{
                    g.RemoveThis();
                    RemoveData(type);
                });
            }
            box.getChildren().add(t);
            box.getChildren().add(b);
            MainContainer.getChildren().add(box);
        }
    }


    void GenerateTextFields(Class type){
        MainContainer.getChildren().clear();
        VBox box = new VBox();
        box.setSpacing(30d);

        Label text = new Label();
        //td usunac view z konca
        text.setText("Dodawanie: " + type.getSimpleName());
        box.getChildren().add(text);

        if(type.equals(KsiazkaView.class)){
            HBox[] hboxs = new HBox[6];
            for (int i=0;i<hboxs.length;i++){
                hboxs[i] = new HBox();
                hboxs[i].setSpacing(20d);
            }

            ComboBox<String> tids = new ComboBox<String>();
            ComboBox<String> gids = new ComboBox<String>();
            ComboBox<String> wids = new ComboBox<String>();
            TextField dt = new TextField();
            ComboBox<String> jids = new ComboBox<String>();
            ComboBox<String> uids = new ComboBox<String>();

            tids.getItems().addAll(GetAllIds(TytulView.class.getSimpleName()));
            gids.getItems().addAll(GetAllIds(GatunekView.class.getSimpleName()));
            wids.getItems().addAll(GetAllIds(WydawnictwoView.class.getSimpleName()));
            jids.getItems().addAll(GetAllIds(JezykView.class.getSimpleName()));
            uids.getItems().addAll(GetAllIds(UzytkownikView.class.getSimpleName()));

            Label tl = new Label("ID Tytulu");
            Label gl = new Label("ID Gatunku");
            Label wl = new Label("ID Wydawnictwa");
            Label dl = new Label("Data Wydania");
            Label jl = new Label("ID Jezyka");
            Label ul = new Label("ID Uzytkownika");

            hboxs[0].getChildren().add(tl);
            hboxs[0].getChildren().add(tids);
            hboxs[1].getChildren().add(gl);
            hboxs[1].getChildren().add(gids);
            hboxs[2].getChildren().add(wl);
            hboxs[2].getChildren().add(wids);
            hboxs[3].getChildren().add(dl);
            hboxs[3].getChildren().add(dt);
            hboxs[4].getChildren().add(jl);
            hboxs[4].getChildren().add(jids);
            hboxs[5].getChildren().add(ul);
            hboxs[5].getChildren().add(uids);

            for (int i=0;i<hboxs.length;i++){
                box.getChildren().add(hboxs[i]);
            }

            Button b = new Button();
            b.setText("Wykonaj");
            b.setOnAction(_->ExecuteAddData(type, Arrays.asList(tids.getValue(),gids.getValue(),wids.getValue(),dt.getText(),jids.getValue(),uids.getValue())));
            box.getChildren().add(b);
        }
        else{
            Field[] f = type.getDeclaredFields();
            ArrayList<TextField> data = new ArrayList<>();
            for (int i=0;i<f.length;i++){
                HBox boxh = new HBox();
                boxh.setSpacing(20d);

                Label l = new Label();
                l.setText(f[i].getName());

                TextField t = new TextField();
                if(f[i].getName().equals("id")){
                    t.setEditable(false);
                    t.setText("auto uzupelniane");
                }

                data.add(t);

                boxh.getChildren().add(l);
                boxh.getChildren().add(t);
                box.getChildren().add(boxh);
            }
            Button b = new Button();
            b.setText("Wykonaj");
            b.setOnAction(_->ExecuteAddData(type,data.stream().map(e->e.getText()).toList()));
            box.getChildren().add(b);
        }

        MainContainer.getChildren().add(box);
    }

    void ExecuteAddData(Class type, List<String> values){
        if(type.equals(GatunekView.class)){
            GatunekView.AddNew(values.get(1));
            return;
        }
        if(type.equals(JezykView.class)){
            JezykView.AddNew(values.get(1));
            return;
        }
        if(type.equals(KsiazkaView.class)){
            Long uid = (values.get(5) == null ||values.get(5).equals("")) ? Long.MIN_VALUE : Long.parseLong(values.get(5));
            LocalDate data = LocalDate.parse(values.get(3));
            KsiazkaView.AddNew(Long.parseLong(values.get(0)),Long.parseLong(values.get(1)),Long.parseLong(values.get(2)),data,Long.parseLong(values.get(4)),uid);
            return;
        }
        if(type.equals(TytulView.class)){
            TytulView.AddNew(values.get(1));
            return;
        }
        if(type.equals(UzytkownikView.class)){
            UzytkownikView.AddNew(values.get(1),values.get(2),values.get(3));
            return;
        }
        if(type.equals(WydawnictwoView.class)){
            WydawnictwoView.AddNew(values.get(1));
            return;
        }
    }

    List<String> GetAllIds(String typename){
        Session s = DataBaseManager.getSessionFactory().openSession();
        Transaction t = s.beginTransaction();
        return s.createQuery("SELECT id FROM "+typename).list().stream().map(e->e.toString()).toList();
    }

    //void RemoveData(Long )

    void Logout(){
        SceneManager.LoadScene(Scenes.Login);
    }

    void Return(){
        MainContainer.getChildren().clear();
        FooterButton.setOnAction(_->Logout());
        FooterButton.setText("Wyloguj");
    }

    Class GetDataType(String DataName){
        switch (DataName){
            case "Gatunki" -> {
                return GatunekView.class;
            }
            case "Jezyki" ->{
                return JezykView.class;
            }
            case "Ksiazki"->{
                return KsiazkaView.class;
            }
            case "Tytuly"->{
                return TytulView.class;
            }
            case "Uzytkownicy"->{
                return UzytkownikView.class;
            }
            case "Wydawnictwa"->{
                return WydawnictwoView.class;
            }
            default -> {
                System.out.println("Literowka");
                return null;
            }
        }
    }
}
