package Projekt.Hotelator;

import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;

import java.util.*;

@Entity
@Table(name = "pokoje")
public class Pokój {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPokój;

    @Column(nullable = false)
    private Integer numerpokoju;
    @Column(nullable = false)
    private Integer miejsce;
    @Column(nullable = false)
    private Integer max_miejsce;
    @Column(nullable = false)
    private Integer extra_łóżko;
    @Column(nullable = false)
    private Integer piętro;
    @Column(nullable = false, length = 20)
    private String balkon;
    @Column(nullable = false)
    private String opis;
    @Column(nullable = false)
    private float cena;
    @Column(nullable = false, length = 20)
    private String status;
    @Column(nullable = false)
    private Date utworzenie;
    @ManyToMany(mappedBy = "pokoje")
    private Set<Rezerwacja> rezerwacje = new HashSet<Rezerwacja>();

    public Integer getNumerpokoju() {
        return numerpokoju;
    }

    public void setNumerpokoju(Integer numer_pokoju) {
        this.numerpokoju = numer_pokoju;
    }

    public Integer getMiejsce() {
        return miejsce;
    }

    public void setMiejsce(Integer miejsce) {
        this.miejsce = miejsce;
    }

    public Integer getMax_miejsce() {
        return max_miejsce;
    }

    public void setMax_miejsce(Integer max_miejsce) {
        this.max_miejsce = max_miejsce;
    }

    public Integer getExtra_łóżko() {
        return extra_łóżko;
    }

    public void setExtra_łóżko(Integer extra_łóżko) {
        this.extra_łóżko = extra_łóżko;
    }

    public Integer getPiętro() {
        return piętro;
    }

    public void setPiętro(Integer piętro) {
        this.piętro = piętro;
    }

    public String isBalkon() {
        return balkon;
    }

    public void setBalkon(String balkon) {
        this.balkon = balkon;
    }

    public String getOpis() {
        return opis;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

    public float getCena() {
        return cena;
    }

    public void setCena(float cena) {
        this.cena = cena;
    }

    public Set<Rezerwacja> getRezerwacje() {
        return rezerwacje;
    }

    public void setRezerwacje(Set<Rezerwacja> rezerwacje) {
        this.rezerwacje = rezerwacje;
    }

    public Long getIdPokój() {
        return idPokój;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBalkon() {
        return balkon;
    }

    public void setUtworzenie(Date utworzenie) {
        this.utworzenie = utworzenie;
    }

    public Date getUtworzenie() {
        return utworzenie;
    }

    public Pokój(Integer numerpokoju, Integer miejsce, Integer max_miejsce, Integer extra_łóżko, Integer piętro, String balkon, String opis, float cena ) {
        this.numerpokoju = numerpokoju;
        this.miejsce = miejsce;
        this.max_miejsce = max_miejsce;
        this.extra_łóżko = extra_łóżko;
        this.piętro = piętro;
        this.balkon = balkon;
        this.opis = opis;
        this.cena = cena;
        this.status = "Wolny";
        this.utworzenie = new Date();
 this.rezerwacje = new HashSet<Rezerwacja>();
    }

    public Pokój(Long idPokój, Integer numerpokoju, Integer miejsce, Integer max_miejsce, Integer extra_łóżko, Integer piętro, String balkon, String opis, float cena) {
        this.idPokój = idPokój;
        this.numerpokoju = numerpokoju;
        this.miejsce = miejsce;
        this.max_miejsce = max_miejsce;
        this.extra_łóżko = extra_łóżko;
        this.piętro = piętro;
        this.balkon = balkon;
        this.opis = opis;
        this.cena = cena;
        this.utworzenie = new Date();
        this.status = "Wolny";
        this.rezerwacje = new HashSet<Rezerwacja>();
    }

    public Pokój(){

    }

    @Override
    public String toString() {
        return "" + idPokój ;
    }
}
