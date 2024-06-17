package com.example.projektksiegarnia.views;

import com.example.projektksiegarnia.DataBaseManager;
import jakarta.persistence.*;
import org.hibernate.Session;
import org.hibernate.Transaction;

@Entity
@Table(name="uzytkownicy")
public class UzytkownikView {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String imie;

    @Column(nullable = false)
    private String nazwisko;

    @Column(nullable = false)
    private String email;

    public static void AddNew(String imie, String nazwisko, String email){
        Session s = DataBaseManager.getSessionFactory().openSession();
        Transaction t = s.beginTransaction();

        UzytkownikView user = new UzytkownikView();
        user.setImie(imie);
        user.setNazwisko(nazwisko);
        user.setEmail(email);

        s.merge(user);
        t.commit();
        s.close();
    }
    public void RemoveThis(){
        Session s = DataBaseManager.getSessionFactory().openSession();
        Transaction t = s.beginTransaction();
        s.remove(this);
        t.commit();
        s.close();
    }
    public String GetNormalizedInfo(){
        return getId() + "\t\t\t" + getImie() + "\t\t\t" + getNazwisko() + "\t\t\t" + getEmail();
    }

    public UzytkownikView(){
        this.id = Long.MIN_VALUE;
        this.imie = "null";
        this.nazwisko = "null";
        this.email = "null";
    }
    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImie() {
        return imie;
    }

    public void setImie(String imie) {
        this.imie = imie;
    }

    public String getNazwisko() {
        return nazwisko;
    }

    public void setNazwisko(String nazwisko) {
        this.nazwisko = nazwisko;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
