package Projekt.Hotelator;

import jakarta.persistence.*;

import java.util.*;


@Entity
    @Table(name = "rezerwacje")
    public class Rezerwacja  {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long idRezerwacja;
@ManyToOne
@JoinColumn(name = "idKlient",referencedColumnName = "idKlient")
        private  Klient klient;
    @Column(nullable = false, length = 20)
    private String status;
    @Column(nullable = false)
    private Date od;
    @Column(nullable = false)
    private  Date doo;
    private Date godzina_przyjazdu;
    @ManyToMany

    private Set<Pokój> pokoje = new HashSet<Pokój>();
    @Column(nullable = false)
    private Integer liczba_dorosłych;
    @Column(nullable = false)
    private  Integer liczba_dzieci;
    @Column(nullable = false)
    private Integer liczba_extra_łóżek;
    @Column(nullable = false)
    private Integer liczba_extra_osób;

    @ManyToOne
    @JoinColumn(name="id_stawka",referencedColumnName = "idStawka")
    private Stawka stawka;
    @Column(nullable = false)
    private float koszt;
    @Column(nullable = false)
    private Date utworzenie;



    public Long getIdRezerwacja() {
        return idRezerwacja;
    }

    public Klient getKlient() {
        return klient;
    }

    public void setKlient(Klient klient) {
        this.klient = klient;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getOd() {
        return od;
    }

    public void setOd(Date od) {
        this.od = od;
    }

    public Date getDoo() {
        return doo;
    }

    public void setDoo(Date doo) {
        this.doo = doo;
    }

    public Date getGodzina_przyjazdu() {
        return godzina_przyjazdu;
    }

    public void setGodzina_przyjazdu(Date godzina_przyjazdu) {
        this.godzina_przyjazdu = godzina_przyjazdu;
    }

    public Set<Pokój> getPokoje() {
        return pokoje;
    }

    public void setPokoje(Set<Pokój> pokoje) {
        this.pokoje = pokoje;
    }

    public Integer getLiczba_dorosłych() {
        return liczba_dorosłych;
    }

    public void setLiczba_dorosłych(Integer liczba_dorosłych) {
        this.liczba_dorosłych = liczba_dorosłych;
    }

    public Integer getLiczba_dzieci() {
        return liczba_dzieci;
    }

    public void setLiczba_dzieci(Integer liczba_dzieci) {
        this.liczba_dzieci = liczba_dzieci;
    }

    public Integer getLiczba_extra_łóżek() {
        return liczba_extra_łóżek;
    }

    public void setLiczba_extra_łóżek(Integer liczba_extra_łóżek) {
        this.liczba_extra_łóżek = liczba_extra_łóżek;
    }

    public Integer getLiczba_extra_osób() {
        return liczba_extra_osób;
    }

    public void setLiczba_extra_osób(Integer liczba_extra_osób) {
        this.liczba_extra_osób = liczba_extra_osób;
    }

    public Stawka getStawka() {
        return stawka;
    }

    public void setStawka(Stawka stawka) {
        this.stawka = stawka;
    }

    public float getKoszt() {
        return koszt;
    }

    public void setKoszt(float koszt) {
        this.koszt = koszt;
    }

    public Date getUtworzenie() {
        return utworzenie;
    }

    public void setUtworzenie(Date utworzone) {
        this.utworzenie = utworzone;
    }

    public Rezerwacja(Klient klient, String status, Date od, Date doo, Date godzina_przyjazdu, Integer liczba_dorosłych, Integer liczba_dzieci, Integer liczba_extra_łóżek, Integer liczba_extra_osób, Stawka stawka) {
        this.klient = klient;
        this.status = status;
        this.od = od;
        this.doo = doo;
        this.godzina_przyjazdu = godzina_przyjazdu;
        this.pokoje = new HashSet<Pokój>();
        this.liczba_dorosłych = liczba_dorosłych;
        this.liczba_dzieci = liczba_dzieci;
        this.liczba_extra_łóżek = liczba_extra_łóżek;
        this.liczba_extra_osób = liczba_extra_osób;
        this.stawka = stawka;
        this.utworzenie = new Date();
    }
public Rezerwacja(){

}

    public Rezerwacja(Long idRezerwacja, Klient klient, String status, Date od, Date doo, Date godzina_przyjazdu, int liczba_dorosłych, int liczba_dzieci, int liczba_extra_łóżek, int liczba_extra_osób, Stawka stawka) {
        this.idRezerwacja = idRezerwacja;
        this.klient = klient;
        this.pokoje = new HashSet<Pokój>();
        this.status = status;
        this.od = od;
        this.doo = doo;
        this.godzina_przyjazdu = godzina_przyjazdu;
        this.liczba_dorosłych = liczba_dorosłych;
        this.liczba_dzieci = liczba_dzieci;
        this.liczba_extra_łóżek = liczba_extra_łóżek;
        this.liczba_extra_osób = liczba_extra_osób;
        this.stawka = stawka;
        this.utworzenie = new Date();
    }

    public Rezerwacja(Klient klient, String status, Date od, Date doo, Integer liczba_dorosłych, Integer liczba_dzieci, Integer liczba_extra_łóżek, Integer liczba_extra_osób, Stawka stawka, float koszt) {
        this.klient = klient;
        this.pokoje = new HashSet<Pokój>();
        this.status = status;
        this.od = od;
        this.doo = doo;
        this.liczba_dorosłych = liczba_dorosłych;
        this.liczba_dzieci = liczba_dzieci;
        this.liczba_extra_łóżek = liczba_extra_łóżek;
        this.liczba_extra_osób = liczba_extra_osób;
        this.stawka = stawka;
        this.utworzenie = new Date();
        this.koszt = koszt;
    }

    @Override
    public String toString() {
        return "" + idRezerwacja ;}
    }

