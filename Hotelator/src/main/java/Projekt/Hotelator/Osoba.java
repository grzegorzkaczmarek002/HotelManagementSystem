package Projekt.Hotelator;

import jakarta.persistence.*;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
@Entity
@Table(name = "osoby")
public class Osoba {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idOsoba;
    @Column(nullable = false, length = 80)
    private String pesel;
    @Column(nullable = false, length = 20)
    private  String płeć;
    @Column(nullable = false , length = 20)
    private String data_urodzenia;
    @Column(nullable = false , length = 20)
    private String nr_telefonu;
    @Column(nullable = false, length = 40)
    private  String imię;
    @Column(nullable = false, length = 40)
    private  String nazwisko;
    @Column(nullable = false, length = 60)
    private  String e_mail;
    @OneToOne
    private Klient klient;
    @OneToOne
    private Pracownik pracownik;
    @ManyToOne
    @JoinColumn(name="idAdres",referencedColumnName = "idAdres")
    private Adres adres;
    @Column(nullable = false)
    private Date utworzenie;

    public String getPesel() {
        return pesel;
    }

    public void setPesel(String pesel) {
        this.pesel = pesel;
    }

    public String getPłeć() {
        return płeć;
    }

    public void setPłeć(String płeć) {
        this.płeć = płeć;
    }

    public String getData_urodzenia() {
        return data_urodzenia;
    }

    public void setData_urodzenia(String data_urodzenia) {
        this.data_urodzenia = data_urodzenia;
    }

    public String getNr_telefonu() {
        return nr_telefonu;
    }

    public void setNr_telefonu(String nr_telefonu) {
        this.nr_telefonu = nr_telefonu;
    }

    public String getImię() {
        return imię;
    }

    public void setImię(String imię) {
        this.imię = imię;
    }

    public String getNazwisko() {
        return nazwisko;
    }

    public void setNazwisko(String nazwisko) {
        this.nazwisko = nazwisko;
    }

    public String getE_mail() {
        return e_mail;
    }

    public void setE_mail(String e_mail) {
        this.e_mail = e_mail;
    }

    public Klient getKlient() {
        return klient;
    }

    public void setKlient(Klient klient) {
        this.klient = klient;
    }

    public Pracownik getPracownik() {
        return pracownik;
    }

    public void setPracownik(Pracownik pracownik) {
        this.pracownik = pracownik;
    }

    public Adres getAdres() {
        return adres;
    }

    public void setAdres(Adres adres) {
        this.adres = adres;
    }

    public Long getIdOsoba() {
        return idOsoba;
    }

    public Date getUtworzenie() {
        return utworzenie;
    }

    public Osoba(String pesel, String płeć, String data_urodzenia, String nr_telefonu, String imię, String nazwisko, String e_mail, Adres adres) {
        this.pesel = pesel;
        this.płeć = płeć;
        this.data_urodzenia = data_urodzenia;
        this.nr_telefonu = nr_telefonu;
        this.imię = imię;
        this.nazwisko = nazwisko;
        this.e_mail = e_mail;
        this.adres = adres;
        this.utworzenie = new Date();
       this.klient = null;
       this.pracownik = null;
    }

    public Osoba() {
    }

    @Override
    public String toString() {
        return "pesel='" + pesel + '\'' +
                ", imię='" + imię + '\'' +
                ", nazwisko='" + nazwisko + '\'' +
                ", płeć='" + płeć + '\'' +
                ", data_urodzenia='" + data_urodzenia + '\'' +
                ", nr_telefonu='" + nr_telefonu + '\'' +
                ", e_mail='" + e_mail + '\'' +
                ", adres=" + adres +" ";
    }
}
