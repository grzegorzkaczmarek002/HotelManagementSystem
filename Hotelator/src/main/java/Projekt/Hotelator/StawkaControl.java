package Projekt.Hotelator;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.Optional;
import java.util.Set;

@Controller
public class StawkaControl {
    @Autowired
    private KlientRepo klientRepo;
    @Autowired
    private PracownikRepo pracownikRepo;
    @Autowired
    private RezerwacjaRepo rezerwacjaRepo;
    @Autowired
    private PokójRepo pokójRepo;
    @Autowired
    private StawkaRepo stawkaRepo;
    @Autowired
    private LogRepo logRepo;
    @RequestMapping("/stawki")
    public String lista_stawki(
            @RequestParam("idpr") Integer idpr,
            Model model
    )
            throws Exception {
        Optional<Pracownik> p = pracownikRepo.findById(idpr);
        Pracownik pracownik = p.get();
        model.addAttribute("pracownik", pracownik);
        model.addAttribute("stawki", stawkaRepo.findAll(Sort.by("status")));
        return ("Stawki");
    }


    @RequestMapping("/zmień_statuss")
    public String zmień_status_stawka(
            @RequestParam("idpr") Integer idpr,
            @RequestParam("ids") Integer ids,
            @RequestParam("status") String status,
            Model model
    )
            throws Exception{
        Optional<Pracownik> p = pracownikRepo.findById(idpr);
        Pracownik pracownik = p.get();
        model.addAttribute("pracownik", pracownik);
        Optional<Stawka> st = stawkaRepo.findById(ids);
        Stawka stawka = st.get();
for(Stawka staw : stawkaRepo.findAll()){
    staw.setStatus("Nieaktywna");
    stawkaRepo.save(staw);
}

        if(status.equals("A")){
            stawka.setStatus("Aktywna");
            stawkaRepo.save(stawka);
            String opis = "Pracownik o loginie "+pracownik.getLogin()+" zmienił status stawki na Aktywną o id " +stawka.getIdStawka();
            Log log = new Log("Stawka","Zmiana statusu",opis);
            logRepo.save(log);
            model.addAttribute("stawki",stawkaRepo.findAll(Sort.by("status")));
            return "Stawki";
        }
        model.addAttribute("stawki",stawkaRepo.findAll(Sort.by("status")));
        return "Stawki";
    }
    @RequestMapping("/kasujstawke")
    public String stawki_kasowanie(
            @RequestParam("idpr") Integer idpr,
            @RequestParam("ids") Integer ids,
            Model model
    )
            throws Exception {
        Optional<Pracownik> p = pracownikRepo.findById(idpr);
        Pracownik pracownik = p.get();
        model.addAttribute("pracownik", pracownik);
        Optional<Stawka> st = stawkaRepo.findById(ids);
        Stawka stawka = st.get();
for(Stawka staw : stawkaRepo.findAll()) {
    if (staw.getStatus().equals("Aktywna")) {
        for (Rezerwacja rezerwacja : rezerwacjaRepo.findAll()) {
            rezerwacja.setStawka(staw);
     rezerwacjaRepo.save(rezerwacja);
        }
    }
}
        String opis = "Pracownik o loginie "+pracownik.getLogin()+" usunął stawke o id " +stawka.getIdStawka();
        Log log = new Log("Stawka","Usuwanie",opis);
        logRepo.save(log);
        stawkaRepo.delete(stawka);

        model.addAttribute("stawki",stawkaRepo.findAll(Sort.by("status")));
        return "Stawki";
    }
    @RequestMapping("/wyszukajstawke")
    public String rezerwacje_stawka(
            @RequestParam("idpr") Integer idpr,
            @RequestParam("idr") Integer idr,
            Model model)
            throws  Exception{
        Optional<Pracownik> p = pracownikRepo.findById(idpr);
        Pracownik pracownik = p.get();
        model.addAttribute("pracownik", pracownik);
        Optional<Rezerwacja> r = rezerwacjaRepo.findById(idr);
        Integer nr_błędu;
        if(r.isEmpty()){
            nr_błędu = 1;
            model.addAttribute("stawki",stawkaRepo.findAll(Sort.by("status")));
            model.addAttribute("błąd",nr_błędu);
            return "Stawki_błędy";
        }
        Rezerwacja rezerwacja = r.get();
        Stawka stawkaa = rezerwacja.getStawka();
        model.addAttribute("stawki",stawkaa);
        return "Stawki";
    }
    @RequestMapping("/dodajstawke")
    public String dodaj_stawka(
            @RequestParam("idpr") Integer idpr,
            Model model
    )
            throws Exception {
        Optional<Pracownik> p = pracownikRepo.findById(idpr);
        Pracownik pracownik = p.get();
        model.addAttribute("pracownik", pracownik);
        return ("Stawki_dodanie");
    }
    @RequestMapping("/dodstawka")
    public String dodanie_stawki(
            @RequestParam("idpr") Integer idpr,
            @RequestParam("koszt_za_osobę")String koszt_za_osobę,
            @RequestParam("koszt_za_łóżko") String koszt_za_łóżko,
            @RequestParam("żniżka_za_dziecko") String żniżka_za_dziecko,
            @RequestParam("żniżka_za_grupę") String żniżka_za_grupę,
            @RequestParam("żniżka_za_sezon") String żniżka_za_sezon,
            Model model
    )
            throws Exception{
        Integer koszt_za_osobę1 = Integer.valueOf(koszt_za_osobę);
        Integer koszt_za_łóżko1 = Integer.valueOf(koszt_za_łóżko);
        Integer żniżka_za_dziecko1 = Integer.valueOf(żniżka_za_dziecko);
        float dziecko = (float) (żniżka_za_dziecko1 * 0.01);
        Integer żniżka_za_grupę1 = Integer.valueOf(żniżka_za_grupę);
float grupa = (float) (żniżka_za_grupę1 * 0.01);
        Integer żniżka_za_sezon1 = Integer.valueOf(żniżka_za_sezon);
float sezon = (float) (żniżka_za_sezon1 * 0.01);
        Optional<Pracownik> p = pracownikRepo.findById(idpr);
        Pracownik pracownik = p.get();
        model.addAttribute("pracownik", pracownik);
        Stawka stawka = new Stawka(koszt_za_osobę1,koszt_za_łóżko1,sezon,dziecko,grupa);
        stawkaRepo.save(stawka);
        String opis = "Pracownik o loginie "+pracownik.getLogin()+" dodał stawke o id " +stawka.getIdStawka();
        Log log = new Log("Stawka","Dodawanie",opis);
        logRepo.save(log);
        model.addAttribute("stawki",stawkaRepo.findAll(Sort.by("status")));
        return "Stawki";
    }

    @RequestMapping("/aktualizujstawke")
    public String stawka_aktualizuj(
            @RequestParam("idpr") Integer idpr,
            @RequestParam("ids") Integer ids,
            Model model
    )
            throws Exception{
        Optional<Pracownik> p = pracownikRepo.findById(idpr);
        Pracownik pracownik =  p.get();
        model.addAttribute("pracownik",pracownik);
        Optional<Stawka> st = stawkaRepo.findById(ids);
        Stawka stawka = st.get();
        model.addAttribute("stawka",stawka);
        return "Stawki_aktualizacja";
    }
    @RequestMapping("/aktstawka")
    public String stawka_akt(
            @RequestParam("idpr") Integer idpr,
            @RequestParam("ids") Integer ids,
            @RequestParam("koszt_za_osobę")String koszt_za_osobę,
            @RequestParam("koszt_za_łóżko") String koszt_za_łóżko,
            @RequestParam("żniżka_za_dziecko") String żniżka_za_dziecko,
            @RequestParam("żniżka_za_grupę") String żniżka_za_grupę,
            @RequestParam("żniżka_za_sezon") String żniżka_za_sezon,
            Model model
    )
            throws Exception{
        Optional<Pracownik> p = pracownikRepo.findById(idpr);
        Pracownik pracownik =  p.get();
        model.addAttribute("pracownik",pracownik);
        Optional<Stawka> st = stawkaRepo.findById(ids);
        Stawka stawka = st.get();
       if(koszt_za_osobę != ""){
           Integer koszt_za_osobę1 = Integer.valueOf(koszt_za_osobę);
           stawka.setKoszt_za_osobę(koszt_za_osobę1);
       }
    if(koszt_za_łóżko != ""){
        Integer koszt_za_łóżko1 = Integer.valueOf(koszt_za_łóżko);
        stawka.setKoszt_za_łóżko(koszt_za_łóżko1);
    }
    if(żniżka_za_dziecko != ""){
        Integer żniżka_za_dziecko1 = Integer.valueOf(żniżka_za_dziecko);
        float dziecko = (float) (żniżka_za_dziecko1 * 0.01);
        stawka.setŻniżka_za_dziecko(dziecko);
    }
        if(żniżka_za_grupę != ""){
            Integer żniżka_za_grupę1 = Integer.valueOf(żniżka_za_grupę);
            float grupa = (float) (żniżka_za_grupę1 * 0.01);
            stawka.setŻniżka_za_grupę(grupa);
        }
        if(żniżka_za_sezon != ""){
            Integer żniżka_za_sezon1 = Integer.valueOf(żniżka_za_sezon);
            float sezon = (float) (żniżka_za_sezon1 * 0.01);
            stawka.setŻniżka_za_sezon(sezon);
        }
    for(Rezerwacja rezerwacja : stawka.getRezerwacje()){
        rezerwacja.setStawka(stawka);
        rezerwacjaRepo.save(rezerwacja);
    }
    stawkaRepo.save(stawka);
        String opis = "Pracownik o loginie "+pracownik.getLogin()+" aktualizował stawke o id " +stawka.getIdStawka();
        Log log = new Log("Stawka","Aktualizowanie",opis);
        logRepo.save(log);
        model.addAttribute("stawki",stawkaRepo.findAll(Sort.by("status")));
        return "Stawki";
    }
}


