package Projekt.Hotelator;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "pracownicy")
public class Pracownik  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPracownik;

    @OneToOne
    @JoinColumn(name="idOsoba",referencedColumnName = "idOsoba")
    private Osoba osoba;
    @Column(nullable = false, length = 20)
private String login;
    @Column(nullable = false, length = 100)
private  String hasło;
    @Column(nullable = false, length = 40)
private String stanowisko;

    @Column(nullable = false, length = 20)
    private String status;
    @Column(nullable = false)
    private  Date utworzenie;

    public Osoba getOsoba() {
        return osoba;
    }

    public void setOsoba(Osoba osoba) {
        this.osoba = osoba;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getHasło() {
        return hasło;
    }

    public void setHasło(String hasło) {
        this.hasło = hasło;
    }

    public String getStanowisko() {
        return stanowisko;
    }

    public void setStanowisko(String stanowisko) {
        this.stanowisko = stanowisko;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getIdPracownik() {
        return idPracownik;
    }

    public Date getUtworzenie() {
        return utworzenie;
    }

    public Pracownik(Osoba osoba, String login, String hasło, String stanowisko) {
        this.osoba = osoba;
        this.login = login;
        this.hasło = hasło;
        this.utworzenie = new Date();
 if(osoba.getPłeć().equals("Kobieta")) {
     if (stanowisko == "Recepcjonista") {
         this.stanowisko = "Recepcjonistka";
     }
     if (stanowisko == "Sprzątacz") {
         this.stanowisko = "Sprzątaczka";
     }
     if (stanowisko == "Kierownik") {
         this.stanowisko = "Kierowniczka";
     }
     if (stanowisko == "Administrator") {
         this.stanowisko = "Administratorka";
     }
 }
if(osoba.getPłeć().equals("Mężczyzna")){
    this.stanowisko = stanowisko;
}

 if(osoba.getPłeć().equals("Kobieta")){
     this.status = "Nie Zalogowana";
 }
 else {
     this.status = "Nie Zalogowany";
 }

    }

    public Pracownik() {
    }

    @Override
    public String toString() {
        return "Pracownik{" +
                "osoba=" + osoba +
                ", login='" + login + '\'' +
                ", hasło='" + hasło + '\'' +
                ", stanowisko='" + stanowisko + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}

