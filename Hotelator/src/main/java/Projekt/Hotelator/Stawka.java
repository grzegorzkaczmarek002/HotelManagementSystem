package Projekt.Hotelator;

import jakarta.persistence.*;

import java.util.*;

@Entity
@Table(name = "stawki")
public class Stawka {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idStawka;
    @Column(nullable = false)
    private Integer koszt_za_osobę;
    @Column(nullable = false)
    private Integer koszt_za_łóżko;
    @Column(nullable = false)
    private float żniżka_za_sezon;
    @Column(nullable = false)
    private float żniżka_za_dziecko;
    @Column(nullable = false)
    private float żniżka_za_grupę;
    @Column(nullable = false, length = 20)
    private String status;
    @Column(nullable = false)
    private Date utworzenie;
@OneToMany(mappedBy = "stawka")
private Set<Rezerwacja> rezerwacje = new HashSet<Rezerwacja>();

    public Long getIdStawka() {
        return idStawka;
    }

    public Integer getKoszt_za_osobę() {
        return koszt_za_osobę;
    }

    public void setKoszt_za_osobę(Integer koszt_za_osobę) {
        this.koszt_za_osobę = koszt_za_osobę;
    }

    public Integer getKoszt_za_łóżko() {
        return koszt_za_łóżko;
    }

    public void setKoszt_za_łóżko(Integer koszt_za_łóżko) {
        this.koszt_za_łóżko = koszt_za_łóżko;
    }

    public float getŻniżka_za_sezon() {
        return żniżka_za_sezon;
    }

    public void setŻniżka_za_sezon(float żniżka_za_sezon) {
        this.żniżka_za_sezon = żniżka_za_sezon;
    }

    public float getŻniżka_za_dziecko() {
        return żniżka_za_dziecko;
    }

    public void setŻniżka_za_dziecko(float żniżka_za_dziecko) {
        this.żniżka_za_dziecko = żniżka_za_dziecko;
    }

    public float getŻniżka_za_grupę() {
        return żniżka_za_grupę;
    }

    public void setŻniżka_za_grupę(float żniżka_za_grupę) {
        this.żniżka_za_grupę = żniżka_za_grupę;
    }

    public Set<Rezerwacja> getRezerwacje() {
        return rezerwacje;
    }

    public void setRezerwacje(Set<Rezerwacja> rezerwacje) {
        this.rezerwacje = rezerwacje;
    }

    public Date getUtworzenie() {
        return utworzenie;
    }

    public void setUtworzenie(Date utworzenie) {
        this.utworzenie = utworzenie;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Stawka(Integer koszt_za_osobę, Integer koszt_za_łóżko, float żniżka_za_sezon, float żniżka_za_dziecko, float żniżka_za_grupę) {
        this.koszt_za_osobę = koszt_za_osobę;
        this.koszt_za_łóżko = koszt_za_łóżko;
        this.żniżka_za_sezon = żniżka_za_sezon;
        this.żniżka_za_dziecko = żniżka_za_dziecko;
        this.żniżka_za_grupę = żniżka_za_grupę;
        this.rezerwacje = new HashSet<Rezerwacja>();
        this.status = "Nieaktywna";
    this.utworzenie = new Date();
    }

    public Stawka(Long idStawka, int koszt_za_osobę, int koszt_za_łóżko, float żniżka_za_sezon, float żniżka_za_dziecko, float żniżka_za_grupę) {
        this.idStawka = idStawka;
        this.koszt_za_osobę = koszt_za_osobę;
        this.koszt_za_łóżko = koszt_za_łóżko;
        this.żniżka_za_sezon = żniżka_za_sezon;
        this.żniżka_za_dziecko = żniżka_za_dziecko;
        this.żniżka_za_grupę = żniżka_za_grupę;
    this.rezerwacje = new HashSet<Rezerwacja>();
    this.utworzenie = new Date();
    this.status = "Nieaktywna";
    }

    public Stawka(){

}

    @Override
    public String toString() {
        return "" + idStawka ;
    }
}
