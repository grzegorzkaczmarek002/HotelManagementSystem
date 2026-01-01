package Projekt.Hotelator;

import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;

import java.util.*;

@Entity
@Table(name = "logi")
public class Log {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idLog;

    @Column(nullable = false, length = 20)
    private String obiekt;
    @Column(nullable = false, length = 20)
    private String rodzaj;
    @Column(nullable = false, length = 80)
    private String opis;
    @Column(nullable = false)
    private Date czas;

    public String getObiekt() {
        return obiekt;
    }

    public void setObiekt(String obiekt) {
        this.obiekt = obiekt;
    }

    public String getRodzaj() {
        return rodzaj;
    }

    public void setRodzaj(String rodzaj) {
        this.rodzaj = rodzaj;
    }

    public String getOpis() {
        return opis;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

    public Date getCzas() {
        return czas;
    }

    public void setCzas(Date czas) {
        this.czas = czas;
    }

    public Long getIdLog() {
        return idLog;
    }

    public Log(String obiekt, String rodzaj, String opis) {
        this.obiekt = obiekt;
        this.rodzaj = rodzaj;
        this.opis = opis;
        this.czas = new Date();
    }

    public Log() {
    }
}








