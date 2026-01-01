package Projekt.Hotelator;

import jakarta.persistence.*;
import org.antlr.v4.runtime.misc.NotNull;

import java.util.*;


@Entity
    @Table(name = "klienci")
    public class Klient  {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
      private Long idKlient;


@OneToMany(mappedBy = "klient")
private Set<Rezerwacja> rezerwacje = new HashSet<Rezerwacja>();

    @OneToOne
    @JoinColumn(name="idOsoba",referencedColumnName = "idOsoba")
    private Osoba osoba;

    @Column(nullable = false)
    private  Date utworzenie;

    public Set<Rezerwacja> getRezerwacje() {
        return rezerwacje;
    }

    public void setRezerwacje(Set<Rezerwacja> rezerwacje) {
        this.rezerwacje = rezerwacje;
    }

    public Osoba getOsoba() {
        return osoba;
    }

    public void setOsoba(Osoba osoba) {
        this.osoba = osoba;
    }

    public Date getUtworzenie() {
        return utworzenie;
    }

    public Long getIdKlient() {
        return idKlient;
    }

    public Klient(Long idKlient, Osoba osoba) {
        this.idKlient = idKlient;
        this.rezerwacje = new HashSet<Rezerwacja>();
        this.osoba = osoba;
        this.utworzenie = new Date();
    }

    public Klient( Osoba osoba) {
        this.rezerwacje = new HashSet<Rezerwacja>();
        this.osoba = osoba;
        this.utworzenie = new Date();
    }

    public Klient() {
    }

    @Override
    public String toString() {
        return "" + idKlient ;
    }
}

