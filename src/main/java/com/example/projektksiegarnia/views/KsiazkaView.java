package com.example.projektksiegarnia.views;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name="ksiazki")
public class KsiazkaView {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "tytul_id")
    private TytulView tytul;

    @ManyToOne
    @JoinColumn(name = "gatunek_id")
    private GatunekView gatunek;

    @ManyToOne
    @JoinColumn(name = "wydawnictwo_id")
    private WydawnictwoView wydawnictwo;

    @Column(name = "rok_wydania")
    private Date rokWydania;

    @ManyToOne
    @JoinColumn(name = "jezyk_id")
    private JezykView jezyk;

    @ManyToOne
    @JoinColumn(name = "uzytkownik_id")
    private UzytkownikView uzytkownik;

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public TytulView getTytul() {
        return tytul;
    }

    public void setTytul(TytulView tytul) {
        this.tytul = tytul;
    }

    public GatunekView getGatunek() {
        return gatunek;
    }

    public void setGatunek(GatunekView gatunek) {
        this.gatunek = gatunek;
    }

    public WydawnictwoView getWydawnictwo() {
        return wydawnictwo;
    }

    public void setWydawnictwo(WydawnictwoView wydawnictwo) {
        this.wydawnictwo = wydawnictwo;
    }

    public Date getRokWydania() {
        return rokWydania;
    }

    public void setRokWydania(Date rokWydania) {
        this.rokWydania = rokWydania;
    }

    public JezykView getJezyk() {
        return jezyk;
    }

    public void setJezyk(JezykView jezyk) {
        this.jezyk = jezyk;
    }

    public UzytkownikView getUzytkownik() {
        if(uzytkownik == null)
            return new UzytkownikView();
        return uzytkownik;
    }

    public void setUzytkownik(UzytkownikView uzytkownik) {
        this.uzytkownik = uzytkownik;
    }

    public String GetNormalizedInfo(){
        return getTytul().getNazwa() + "\t\t\t" + getGatunek().getNazwa() + "\t\t\t" + getJezyk().getNazwa() + "\t\t\t" + getRokWydania().toString() + "\t\t\t" + getWydawnictwo().getNazwa();
    }

}
