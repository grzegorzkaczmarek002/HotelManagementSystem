package Projekt.Hotelator;

import jakarta.persistence.*;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
@Entity
@Table(name = "adresy")
public class Adres {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idAdres;
    @Column(nullable = false , length = 20)
    private  String kod_pocztowy;
    @Column(nullable = false, length = 60)
    private  String miejscowość;
    @Column(nullable = true, length = 60)
    private  String ulica;
    @Column(nullable = false, length = 10)
    private  String nr_domu;
    @Column(nullable = true)
    private  Integer nr_mieszkania;
    @Column(nullable = false, length = 60)
    private  String kraj;
    @Column(nullable = false)
    private Date utworzenie;
    @OneToMany(mappedBy = "adres")
    private Set<Osoba> osoby = new HashSet<Osoba>();

    public String getKod_pocztowy() {
        return kod_pocztowy;
    }

    public void setKod_pocztowy(String kod_pocztowy) {
        this.kod_pocztowy = kod_pocztowy;
    }

    public String getMiejscowość() {
        return miejscowość;
    }

    public void setMiejscowość(String miejscowość) {
        this.miejscowość = miejscowość;
    }

    public String getUlica() {
        return ulica;
    }

    public void setUlica(String ulica) {
        this.ulica = ulica;
    }

    public String getNr_domu() {
        return nr_domu;
    }

    public void setNr_domu(String nr_domu) {
        this.nr_domu = nr_domu;
    }

    public Integer getNr_mieszkania() {
        return nr_mieszkania;
    }

    public void setNr_mieszkania(Integer nr_mieszkania) {
        this.nr_mieszkania = nr_mieszkania;
    }

    public String getKraj() {
        return kraj;
    }

    public void setKraj(String kraj) {
        this.kraj = kraj;
    }

    public Set<Osoba> getOsoby() {
        return osoby;
    }

    public void setOsoby(Set<Osoba> osoby) {
        this.osoby = osoby;
    }

    public Long getIdAdres() {
        return idAdres;
    }

    public Date getUtworzenie() {
        return utworzenie;
    }

    public Adres(String kod_pocztowy, String miejscowość, String ulica, String nr_domu, Integer nr_mieszkania, String kraj) {
        this.kod_pocztowy = kod_pocztowy;
        this.miejscowość = miejscowość;
        this.ulica = ulica;
        this.nr_domu = nr_domu;
        this.nr_mieszkania = nr_mieszkania;
        this.kraj = kraj;
        this.osoby = new HashSet<Osoba>();
        this.utworzenie = new Date();
    }

    public Adres(String kod_pocztowy, String miejscowość, String nr_domu, String kraj) {
        this.kod_pocztowy = kod_pocztowy;
        this.miejscowość = miejscowość;
        this.nr_domu = nr_domu;
        this.nr_mieszkania = 0;
        this.ulica = " ";
        this.kraj = kraj;
        this.osoby = new HashSet<Osoba>();
        this.utworzenie = new Date();
    }

    public Adres(String kod_pocztowy, String miejscowość, String nr_domu, Integer nr_mieszkania, String kraj) {
        this.kod_pocztowy = kod_pocztowy;
        this.miejscowość = miejscowość;
        this.nr_domu = nr_domu;
        this.nr_mieszkania = nr_mieszkania;
        this.ulica = " ";
        this.kraj = kraj;
        this.osoby = new HashSet<Osoba>();
        this.utworzenie = new Date();
    }

    public Adres(String kod_pocztowy, String miejscowość, String ulica, String nr_domu, String kraj) {
        this.kod_pocztowy = kod_pocztowy;
        this.miejscowość = miejscowość;
        this.ulica = ulica;
        this.nr_domu = nr_domu;
        this.nr_mieszkania = 0;
        this.kraj = kraj;
        this.osoby = new HashSet<Osoba>();
        this.utworzenie = new Date();
    }

    public Adres() {
    }

    @Override
    public String toString() {
        return ", miejscowość='" + miejscowość + '\'' +
                ", ulica='" + ulica + '\'' +
                ", nr_domu='" + nr_domu + '\'' +
                ", nr_mieszkania=" + nr_mieszkania +
                "kod_pocztowy='" + kod_pocztowy + '\'' +
                ", kraj='" + kraj + '\'' +" ";
    }
}
