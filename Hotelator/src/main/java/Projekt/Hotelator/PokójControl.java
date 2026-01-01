package Projekt.Hotelator;

import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;


@Controller
public class PokójControl {
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

    @RequestMapping("/pokojer")
    public String lista_pokojer(
            @RequestParam("idpr") Integer idpr,
            Model model
    )
            throws Exception {
        Optional<Pracownik> p = pracownikRepo.findById(idpr);
        Pracownik pracownik = p.get();
        model.addAttribute("pracownik", pracownik);
        model.addAttribute("pokoje", pokójRepo.findAll(Sort.by("status")));
        return ("Pokojer");
    }
    @RequestMapping("/pokojes")
    public String lista_pokojes(
            @RequestParam("idpr") Integer idpr,
            Model model
    )
            throws Exception {
        Optional<Pracownik> p = pracownikRepo.findById(idpr);
        Pracownik pracownik = p.get();
        model.addAttribute("pracownik", pracownik);
        model.addAttribute("pokoje", pokójRepo.findAll(Sort.by("status")));
        return ("Pokojes");
    }
    @RequestMapping("/pokoje")
    public String lista_pokoje(
            @RequestParam("idpr") Integer idpr,
            Model model
    )
            throws Exception {
        Optional<Pracownik> p = pracownikRepo.findById(idpr);
        Pracownik pracownik = p.get();
        model.addAttribute("pracownik", pracownik);
        model.addAttribute("pokoje", pokójRepo.findAll(Sort.by("status")));
        return ("Pokoje");
    }
    @RequestMapping("/zmień_statusp")
    public String zmień_status_pokój(
            @RequestParam("idpr") Integer idpr,
            @RequestParam("idp") Integer idp,
            @RequestParam("status") String status,
            Model model
    )
            throws Exception{
        Optional<Pracownik> p = pracownikRepo.findById(idpr);
        Pracownik pracownik = p.get();
        model.addAttribute("pracownik", pracownik);
        Optional<Pokój> po = pokójRepo.findById(idp);
        Pokój pokój = po.get();



        if(status.equals("W")){
            pokój.setStatus("Wolny");
            pokójRepo.save(pokój);
            model.addAttribute("pokoje",pokójRepo.findAll(Sort.by("status")));
            return "Pokojes";
        }
        String opis = "Pracownik o loginie "+pracownik.getLogin()+" zmienił status pokoju na Wolny o id "+pokój.getIdPokój();
        Log log = new Log("Pokój","Zmiana statusu",opis);
        logRepo.save(log);
        model.addAttribute("pokoje",pokójRepo.findAll(Sort.by("status")));
        return "Pokojes";
    }
    @RequestMapping("/kasujpokój")
    public String pokoje_kasuj(
            @RequestParam("idpr") Integer idpr,
            @RequestParam("idp") Integer idp,
            Model model
    )
            throws Exception {
        Optional<Pracownik> p = pracownikRepo.findById(idpr);
        Pracownik pracownik = p.get();
        model.addAttribute("pracownik", pracownik);
        Optional<Pokój> po = pokójRepo.findById(idp);
        Pokój pokój = po.get();
        String idrezerwacji = null;
        for (Rezerwacja r : pokój.getRezerwacje()) {
            Stawka a = r.getStawka();
            Set<Rezerwacja> w = a.getRezerwacje();
            w.remove(r);
            a.setRezerwacje(w);
            stawkaRepo.save(a);
            Klient k = r.getKlient();
            Set<Rezerwacja> lrr = k.getRezerwacje();
            lrr.remove(r);
            k.setRezerwacje(lrr);
            klientRepo.save(k);
            for (Pokój pp : r.getPokoje()) {
                if (pp.equals(pokój)) {

                } else {
                    Set<Rezerwacja> rr = pp.getRezerwacje();
                    rr.remove(r);
                    pp.setRezerwacje(rr);
                    pokójRepo.save(pp);
                }
            }
            idrezerwacji = " "+r.getIdRezerwacja();
            rezerwacjaRepo.delete(r);
        }
        String opis = "Pracownik o loginie "+pracownik.getLogin()+" usunał pokój o id "+pokój.getIdPokój()+" i rezerwacje o id "+idrezerwacji;
        Log log = new Log("Pokój","Usuwanie",opis);
        logRepo.save(log);
        pokójRepo.delete(pokój);
        model.addAttribute("pokoje", pokójRepo.findAll(Sort.by("status")));
        return "Pokoje";
    }
    @RequestMapping("/wyszukajpokój")
    public String rezerwacje_pokój(
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
            model.addAttribute("pokoje", pokójRepo.findAll(Sort.by("status")));
            model.addAttribute("błąd",nr_błędu);
            return "Pokoje_błędy";
        }
        Rezerwacja rezerwacja = r.get();
        Set<Pokój> pokojes = rezerwacja.getPokoje();
        model.addAttribute("pokoje",pokojes);
        return "Pokoje";
    }
    @RequestMapping("/dodajpokój")
    public String dodaj_pokój(
            @RequestParam("idpr") Integer idpr,
            Model model
    )
            throws Exception {
        Optional<Pracownik> p = pracownikRepo.findById(idpr);
        Pracownik pracownik = p.get();
        model.addAttribute("pracownik", pracownik);
        return ("Pokoje_dodanie");
    }
    @RequestMapping("/dodpokój")
    public String dodanie_pokoju(
            @RequestParam("idpr") Integer idpr,
            @RequestParam("numer_pokoju")String numer_pokoju,
            @RequestParam("miejsce") String miejsce,
            @RequestParam("max_miejsce") String max_miejsce,
            @RequestParam("balkon") String balkon,
            @RequestParam("piętro") String piętro,
            @RequestParam("cena") String cena,
            @RequestParam("opis") String opis,
            Model model
            )
throws Exception{
    Integer nr_błędu;
   Integer numer_pokoju1 = Integer.valueOf(numer_pokoju);
   Integer miejsce1 = Integer.valueOf(miejsce);
   Integer max_miejsce1 = Integer.valueOf(max_miejsce);
   Integer piętro1 = Integer.valueOf(piętro);
        Optional<Pracownik> p = pracownikRepo.findById(idpr);
        Pracownik pracownik = p.get();
        model.addAttribute("pracownik", pracownik);
    for(Pokój pokój : pokójRepo.findAll()){
        if(pokój.getNumerpokoju().equals(numer_pokoju1)){
            nr_błędu = 1;
            model.addAttribute("błąd",nr_błędu);
            return "Pokoje_dodanie_błędy";
        }
    }
 if(max_miejsce1 < miejsce1){
     nr_błędu = 2;
     model.addAttribute("błąd",nr_błędu);
     return "Pokoje_dodanie_błędy";
 }
 Integer extra_łóżko = max_miejsce1 - miejsce1;
 float koszt = Float.parseFloat(cena);
Pokój pokój = new Pokój(numer_pokoju1,miejsce1,max_miejsce1,extra_łóżko,piętro1,balkon,opis,koszt);
pokójRepo.save(pokój);
        String opiss = "Pracownik o loginie "+pracownik.getLogin()+" dodał pokój o id "+pokój.getIdPokój();
        Log log = new Log("Pokój","Dodawanie",opiss);
        logRepo.save(log);
        model.addAttribute("pokoje", pokójRepo.findAll(Sort.by("status")));
return "Pokoje";
    }

    @RequestMapping("/aktualizujpokój")
    public String pokój_aktualizuj(
            @RequestParam("idpr") Integer idpr,
            @RequestParam("idp") Integer idp,
            Model model
    )
            throws Exception{
        Optional<Pracownik> p = pracownikRepo.findById(idpr);
        Pracownik pracownik =  p.get();
        model.addAttribute("pracownik",pracownik);
        Optional<Pokój> po = pokójRepo.findById(idp);
        Pokój pokój = po.get();
        model.addAttribute("pokój",pokój);
        return "Pokoje_aktualizacja";
    }
@RequestMapping("/aktpokój")
    public String pokój_akt(
        @RequestParam("idpr") Integer idpr,
        @RequestParam("idp") Integer idp,
        @RequestParam("numer_pokoju")String numer_pokoju,
        @RequestParam("miejsce") String miejsce,
        @RequestParam("max_miejsce") String max_miejsce,
        @RequestParam("balkon") String balkon,
        @RequestParam("piętro") String piętro,
        @RequestParam("cena") String cena,
        @RequestParam("opis") String opis,
        Model model
)
throws Exception{
    Optional<Pracownik> p = pracownikRepo.findById(idpr);
    Pracownik pracownik =  p.get();
    model.addAttribute("pracownik",pracownik);
Integer nr_błędu;
    Optional<Pokój> po = pokójRepo.findById(idp);
Pokój pokój = po.get();
Integer nrpokoju = 0;
Integer m = 0;
Integer mm = 0;
    if (numer_pokoju != "") {
        Integer numer_pokoju1 = Integer.valueOf(numer_pokoju);
        for(Pokój pok : pokójRepo.findAll()){
    if(pok.getNumerpokoju().equals(numer_pokoju1)){
        nr_błędu = 1;
        model.addAttribute("błąd",nr_błędu);
        model.addAttribute("pokój",pokój);
        return "Pokoje_aktualizacja_błędy";
    }
        }
    nrpokoju = 1;
    }
if(miejsce != ""){
    Integer miejsce1 = Integer.valueOf(miejsce);
    if(max_miejsce != ""){
        Integer max_miejsce1 = Integer.valueOf(max_miejsce);
        if(max_miejsce1 < miejsce1){
            nr_błędu = 2;
            model.addAttribute("błąd",nr_błędu);
            model.addAttribute("pokój",pokój);
            return "Pokoje_aktualizacja_błędy";
        }
    m = 1;
        mm = 1;
    }
if(pokój.getMax_miejsce() < miejsce1){
    nr_błędu = 2;
    model.addAttribute("błąd",nr_błędu);
    model.addAttribute("pokój",pokój);
    return "Pokoje_aktualizacja_błędy";
}
m = 1;
}
if(max_miejsce != ""){
    Integer max_miejsce1 = Integer.valueOf(max_miejsce);
    if(miejsce != ""){
        Integer miejsce1 = Integer.valueOf(miejsce);
        if(max_miejsce1 < miejsce1){
            nr_błędu = 2;

            model.addAttribute("błąd",nr_błędu);
            model.addAttribute("pokój",pokój);
            return "Pokoje_aktualizacja_błędy";
        }
        m = 1;
        mm = 1;
    }
    if(max_miejsce1 < pokój.getMiejsce()){
        nr_błędu = 2;
        model.addAttribute("błąd",nr_błędu);
        model.addAttribute("pokój",pokój);
        return "Pokoje_aktualizacja_błędy";
    }
    mm = 1;
}
if(balkon != ""){
    pokój.setBalkon(balkon);
}
if(cena != ""){
    float koszt = Float.parseFloat(cena);
    pokój.setCena(koszt);
}
if(nrpokoju == 1){
    Integer numer_pokoju1 = Integer.valueOf(numer_pokoju);
    pokój.setNumerpokoju(numer_pokoju1);
}
if(opis != ""){
    pokój.setOpis(opis);
}
if(piętro != ""){
    Integer piętro1 = Integer.valueOf(piętro) ;
    pokój.setPiętro(piętro1);
}
if(m == 1){
    Integer miejsce1 = Integer.valueOf(miejsce);
    pokój.setMiejsce(miejsce1);
}
if(mm == 1){
    Integer max_miejsce1 = Integer.valueOf(max_miejsce);
    pokój.setMax_miejsce(max_miejsce1);
}
Integer extra_łóżko = pokój.getMax_miejsce() - pokój.getMiejsce();
pokój.setExtra_łóżko(extra_łóżko);
for(Rezerwacja r : pokój.getRezerwacje()){
    Set<Pokój> lp = r.getPokoje();
    lp.add(pokój);
    r.setPokoje(lp);
    rezerwacjaRepo.save(r);
}
pokójRepo.save(pokój);
    String opiss = "Pracownik o loginie "+pracownik.getLogin()+" aktualizował pokój o id "+pokój.getIdPokój();
    Log log = new Log("Pokój","Aktualizowanie",opiss);
    logRepo.save(log);
    model.addAttribute("pokoje",pokójRepo.findAll(Sort.by("status")));
    return "Pokoje";
    }
}
