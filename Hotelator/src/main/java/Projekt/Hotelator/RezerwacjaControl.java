package Projekt.Hotelator;


import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.domain.Sort;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;




@Controller
public class RezerwacjaControl {
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
    @RequestMapping("/rezerwacje")
    public String lista_rezerwacje(
            @RequestParam("idpr") Integer idpr,
            Model model
    )
            throws Exception {
        Optional<Pracownik> p = pracownikRepo.findById(idpr);
        Pracownik pracownik = p.get();
        model.addAttribute("pracownik", pracownik);
        model.addAttribute("rezerwacje", rezerwacjaRepo.findAll(Sort.by("status")));
        return ("Rezerwacje");
    }
    @RequestMapping("/wyszukajrez")
    public String rezerwacje_wyszukaj(
            @RequestParam("idpr") Integer idpr,
            @RequestParam("idk") Integer idk,
            Model model)
            throws  Exception{
        Optional<Pracownik> p = pracownikRepo.findById(idpr);
        Pracownik pracownik = p.get();
        model.addAttribute("pracownik", pracownik);
        Optional<Klient> k = klientRepo.findById(idk);
        Integer nr_błędu;
        if(k.isEmpty()){
            nr_błędu = 1;
            model.addAttribute("rezerwacje", rezerwacjaRepo.findAll(Sort.by("status")));
        model.addAttribute("błąd",nr_błędu);
            return "Rezerwacje_błędy";
        }
        Klient klient = k.get();
        Set<Rezerwacja> rezerwacje = klient.getRezerwacje();
        model.addAttribute("rezerwacje",rezerwacje);
        return "Rezerwacje";
    }
    @RequestMapping("/kasujrezerwacje")
    public String rezerwacje_kasuj(
            @RequestParam("idpr") Integer idpr,
            @RequestParam("idr") Integer idr,
            Model model
    )
            throws Exception {
        Optional<Pracownik> p = pracownikRepo.findById(idpr);
        Pracownik pracownik = p.get();
        model.addAttribute("pracownik", pracownik);
        Optional<Rezerwacja> r = rezerwacjaRepo.findById(idr);
        Rezerwacja rezerwacja = r.get();
            Stawka a = rezerwacja.getStawka();
            Set<Rezerwacja> w = a.getRezerwacje();
            w.remove(rezerwacja);
            a.setRezerwacje(w);
            stawkaRepo.save(a);
            for (Pokój pp : rezerwacja.getPokoje()) {
                Set<Rezerwacja> rr = pp.getRezerwacje();
                rr.remove(rezerwacja);
                pp.setRezerwacje(rr);
                pokójRepo.save(pp);
            }
        String opis = "Pracownik o loginie "+pracownik.getLogin()+" usunął rezerwację o id " +rezerwacja.getIdRezerwacja();
        Log log = new Log("Rezerwacja","Usuwanie",opis);
        logRepo.save(log);
            rezerwacjaRepo.delete(rezerwacja);
        model.addAttribute("rezerwacje", rezerwacjaRepo.findAll(Sort.by("status")));
        return ("Rezerwacje");


    }




    @RequestMapping("/zmień_statusr")
    public String zmień_status_rezerwacja(
            @RequestParam("idpr") Integer idpr,
            @RequestParam("idr") Integer idr,
    @RequestParam("status") String status,
    Model model
    )
        throws Exception{
        Optional<Pracownik> p = pracownikRepo.findById(idpr);
        Pracownik pracownik = p.get();
        model.addAttribute("pracownik", pracownik);
        Optional<Rezerwacja> r = rezerwacjaRepo.findById(idr);
        Rezerwacja rezerwacja = r.get();
        Integer nr_błędu;


        if(status.equals("T")){
            for(Pokój pokój : rezerwacja.getPokoje()){
                if(pokój.getStatus().equals("Brudny") || pokój.getStatus().equals("Zajęty")){
                    nr_błędu = 2;
                    model.addAttribute("rezerwacje", rezerwacjaRepo.findAll(Sort.by("status")));
                    model.addAttribute("błąd",nr_błędu);
                    return "Rezerwacje_błędy";
                }
            }
            rezerwacja.setStatus("Trwająca");
            Date date = new Date();
            rezerwacja.setGodzina_przyjazdu(date);
            for(Pokój pokój : rezerwacja.getPokoje()){
                pokój.setStatus("Zajęty");
                pokójRepo.save(pokój);
            }
        rezerwacjaRepo.save(rezerwacja);
            String opis = "Pracownik o loginie "+pracownik.getLogin()+" zmienił status rezerwacji na Trwającą o id " +rezerwacja.getIdRezerwacja();
            Log log = new Log("Rezerwacja","Zmiana statusu",opis);
            logRepo.save(log);
            model.addAttribute("rezerwacje",rezerwacjaRepo.findAll(Sort.by("status")));
            return "Rezerwacje";
        }
   if(status.equals("Z")){
       rezerwacja.setStatus("Zakończona");
       for(Pokój pokój : rezerwacja.getPokoje()){
           pokój.setStatus("Brudny");
           pokójRepo.save(pokój);
       }
       rezerwacjaRepo.save(rezerwacja);
       String opis = "Pracownik o loginie "+pracownik.getLogin()+" zmienił status rezerwacji na Zakończoną o id " +rezerwacja.getIdRezerwacja();
       Log log = new Log("Rezerwacja","Zmiana statusu",opis);
       logRepo.save(log);
       model.addAttribute("rezerwacje",rezerwacjaRepo.findAll(Sort.by("status")));
       return "Rezerwacje";
   }
        model.addAttribute("rezerwacje",rezerwacjaRepo.findAll(Sort.by("status")));
   return "Rezerwacje";
    }

@RequestMapping("/aktualizujrezerwacje")
public String rezerwacje_aktualizuj_rezerwacje(
        @RequestParam("idpr") Integer idpr,
        @RequestParam("idr") Integer idr,
        Model model
)
        throws Exception{
Optional<Pracownik> p = pracownikRepo.findById(idpr);
Pracownik pracownik =  p.get();
model.addAttribute("pracownik",pracownik);
Optional<Rezerwacja> r = rezerwacjaRepo.findById(idr);
Rezerwacja rezerwacja = r.get();
model.addAttribute("rezerwacja",rezerwacja);
return "Rezerwacje_aktualizacja";
    }
    @RequestMapping("/aktrezerwacja")
    public String rezerwacje_akt_rezerwacje(
            @RequestParam("idr") Integer idr,
            @RequestParam("od") String od,
            @RequestParam("od_time") String od_time,
            @RequestParam("doo") String doo,
            @RequestParam("doo_time") String doo_time,
            @RequestParam("idpr") Integer idpr,
            Model model
    ) throws Exception {
        Optional<Rezerwacja> r = rezerwacjaRepo.findById(idr);
        Rezerwacja rezerwacja = r.get();
        Optional<Pracownik> p = pracownikRepo.findById(idpr);
        Pracownik pracownik = p.get();
        model.addAttribute("pracownik", pracownik);
        Integer nr_błędu;
        for(Pokój po : rezerwacja.getPokoje()){
            Set<Rezerwacja> lr = po.getRezerwacje();


            lr.remove(rezerwacja);
            po.setRezerwacje(lr);

            pokójRepo.save(po);
        }
        Set<Pokój> lp = rezerwacja.getPokoje();
        lp.clear();
        rezerwacja.setPokoje(lp);
        rezerwacjaRepo.save(rezerwacja);

        if (od.equals(doo)) {
            nr_błędu = 2;
            model.addAttribute("rezerwacja", rezerwacja);
            model.addAttribute("błąd", nr_błędu);
            return "Rezerwacje_aktualizacja_błędy";
        }

        String jutro = od;
        String[] jutroo = jutro.split("-");
        int rok = Integer.parseInt(jutroo[0]);
        int miesiąc = Integer.parseInt(jutroo[1]);
        int dzień = Integer.parseInt(jutroo[2]);
        for (int i = 0; i < 10; i++) {
            if (dzień == 31 && (miesiąc == 1 || miesiąc == 3 || miesiąc == 5 || miesiąc == 7 || miesiąc == 8 || miesiąc == 10 || miesiąc == 12)) {
                if (miesiąc == 12) {
                    rok = rok + 1;
                    miesiąc = 01;
                    dzień = 01;
                    i = 11;
                } else {
                    miesiąc = miesiąc + 1;
                    dzień = 01;
                    i = 11;
                }
            }
            if (dzień == 30 && (miesiąc == 4 || miesiąc == 6 || miesiąc == 9 || miesiąc == 11)) {
                miesiąc = miesiąc + 1;
                dzień = 01;
                i = 11;
            }
            if (rok % 4 == 0 && rok % 100 != 0 || rok % 400 == 0) {
                if (dzień == 29 && miesiąc == 2) {
                    miesiąc = miesiąc + 1;
                    dzień = 01;
                    i = 11;
                }
            } else {
                if (dzień == 28 && miesiąc == 2) {
                    miesiąc = miesiąc + 1;
                    dzień = 01;
                    i = 11;
                }
            }

            if (i != 11) {
                dzień = dzień + 1;
                i = 11;
            }
        }
        jutro = "";
        jutro += rok;
        jutro += '-';
        jutro += miesiąc;
        jutro += '-';
        jutro += dzień;
        jutro += ' ';
        jutro += od_time;

        od += ' ';
        od += od_time;
        DateFormat odf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date odd = odf.parse(od);
        Date jut = odf.parse(jutro);
        doo += ' ';
        doo += doo_time;
        DateFormat doof = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date dooo = doof.parse(doo);
        if (odd.equals(dooo)) {
            nr_błędu = 1;
            model.addAttribute("rezerwacja", rezerwacja);
            model.addAttribute("błąd", nr_błędu);
            return "Rezerwacje_aktualizacja_błędy";
        }
        Date d = new Date();
        if (odd.before(d) || dooo.before(d)) {
            nr_błędu = 3;
            model.addAttribute("rezerwacja", rezerwacja);
            model.addAttribute("błąd", nr_błędu);
            return "Rezerwacje_aktualizacja_błędy";
        }
        if (jut.after(dooo)) {
            nr_błędu = 2;
            model.addAttribute("rezerwacja", rezerwacja);
            model.addAttribute("błąd", nr_błędu);
            return "Rezerwacje_aktualizacja_błędy";
        }
        List<Pokój> pokójset = new ArrayList<>();
        for (Pokój pp : pokójRepo.findAll()) {
            pokójset.add(pp);
        }
        for (Rezerwacja rr : rezerwacjaRepo.findAll()) {


            if (rr.getDoo().before(dooo) && rr.getDoo().after(odd)) {
                for (Pokój pr : rr.getPokoje()) {
                    pokójset.remove(pr);
                }
            }
            if (rr.getOd().after(odd) && rr.getOd().before(dooo)) {
                for (Pokój pr : rr.getPokoje()) {
                    pokójset.remove(pr);
                }
            }
            if(rr.getDoo().after(dooo) && rr.getOd().before(odd)) {
                for (Pokój pr : rr.getPokoje()) {
                    pokójset.remove(pr);
                }
            }
            if (rr.getDoo().compareTo(dooo) == 0) {
                for (Pokój pr : rr.getPokoje()) {
                    pokójset.remove(pr);
                }
            }
            if (rr.getDoo().compareTo(odd) == 0) {
                for (Pokój pr : rr.getPokoje()) {
                    pokójset.remove(pr);
                }
            }
            if (rr.getOd().compareTo(odd) == 0) {
                for (Pokój pr : rr.getPokoje()) {
                    pokójset.remove(pr);
                }
            }
            if (rr.getOd().compareTo(dooo) == 0) {
                for (Pokój pr : rr.getPokoje()) {
                    pokójset.remove(pr);
                }
            }
        }


        if (pokójset.isEmpty()) {
            nr_błędu = 4;
            model.addAttribute("rezerwacja", rezerwacja);
            model.addAttribute("błąd", nr_błędu);
            return "Rezerwacje_aktualizacja_błędy";
        }

        Stawka ss = null;
        for (Stawka s : stawkaRepo.findAll()) {
            ss = s;
            if (s.getStatus().equals("Aktywna")) {
                break;
            }
        }

        rezerwacja.setStawka(ss);
        rezerwacja.setOd(odd);
        rezerwacja.setDoo(dooo);
        rezerwacja.setKoszt(0);
        rezerwacja.setLiczba_dzieci(0);
        rezerwacja.setLiczba_dorosłych(0);
        rezerwacja.setLiczba_extra_osób(0);
        rezerwacja.setLiczba_extra_łóżek(0);
        ss.getRezerwacje().add(rezerwacja);
        Klient k = rezerwacja.getKlient();
       k.getRezerwacje().add(rezerwacja);
       klientRepo.save(k);
        stawkaRepo.save(ss);
        rezerwacjaRepo.save(rezerwacja);
        String opis = "Pracownik o loginie "+pracownik.getLogin()+" aktualizował termin  rezerwacji o id " +rezerwacja.getIdRezerwacja();
        Log log = new Log("Rezerwacja","Aktualizowanie",opis);
        logRepo.save(log);
        model.addAttribute("rezerwacja",rezerwacja);
        model.addAttribute("pokoje", pokójset);
        return "Rezerwacje_akt";

    }
    @RequestMapping("/aktrez")
    public String aktrez(
            @RequestParam("numer") String numer,
            @RequestParam("idpr") Integer idpr,
            @RequestParam("idr") Integer idr,
            @RequestParam("liczba_dorosłych") String liczba_dorosłych,
            @RequestParam("liczba_dzieci") String liczba_dzieci,
            @RequestParam("lp") List<Pokój> lp,
            Model model
    )throws Exception {
        Pokój p;

        if(numer.length() > 2) {
            String[] s = numer.split(" ");
            System.out.println(s[0]);
            System.out.println(s[1]);
            Integer numer1 = Integer.valueOf(s[1]);
            List<Pokój> pp =  pokójRepo.findByNumerpokoju(numer1);
            p = pp.get(0);
        }
        else {
            Integer numer1 = Integer.valueOf(numer);
            List<Pokój> pp =  pokójRepo.findByNumerpokoju(numer1);
            p = pp.get(0);
        }
        Optional<Pracownik> opr = pracownikRepo.findById(idpr);
        Pracownik pr = opr.get();
        Optional<Rezerwacja> or = rezerwacjaRepo.findById(idr);
        Rezerwacja r = or.get();
        model.addAttribute("pracownik", pr);

        Integer nr_błędu;
        Integer poz = 0;
        if(liczba_dorosłych.equals("0")){
            nr_błędu = 2;
            model.addAttribute("błąd", nr_błędu);
            model.addAttribute("pokoje", lp);
            model.addAttribute("rezerwacja", r);
            return "Rezerwacje_akt_błędy";
        }
        Integer liczba_dorosłych1 = Integer.valueOf(liczba_dorosłych);
        Integer liczba_dzieci1 = Integer.valueOf(liczba_dzieci);
        Integer liczba = liczba_dorosłych1 + liczba_dzieci1;
        if (liczba > p.getMax_miejsce()) {
            nr_błędu = 1;
            model.addAttribute("błąd", nr_błędu);
            model.addAttribute("pokoje", lp);
            model.addAttribute("rezerwacja", r);
            return "Rezerwacje_akt_błędy";
        }

        if (liczba > p.getMiejsce()) {
            poz = liczba - p.getMiejsce();
            Integer extraosoby = poz + r.getLiczba_extra_osób();
            r.setLiczba_extra_osób(extraosoby);
            Integer extrałóżka = poz + r.getLiczba_extra_łóżek();
            r.setLiczba_extra_łóżek(extrałóżka);
        }
        Integer dorosły = r.getLiczba_dorosłych() + liczba_dorosłych1;
        Integer dzieci = r.getLiczba_dzieci() + liczba_dzieci1;
        r.setLiczba_dorosłych(dorosły);
        r.setLiczba_dzieci(dzieci);
        float stawka_dzieci = 1 - r.getStawka().getŻniżka_za_dziecko();
        float stawka_sezon = 1 - r.getStawka().getŻniżka_za_sezon();
        float cenadorosłych = liczba_dorosłych1 * p.getCena();
        float cenadzieci = liczba_dzieci1 * p.getCena();
        cenadzieci = cenadzieci * stawka_dzieci;
        float cenaextraosób = poz * r.getStawka().getKoszt_za_osobę();
        float cenaextrałóżko = poz * r.getStawka().getKoszt_za_łóżko();
        float koszt = cenadorosłych + cenadzieci + cenaextraosób + cenaextrałóżko;
        koszt = koszt * stawka_sezon;
        koszt = koszt + r.getKoszt();
        koszt = Math.round(koszt);
        r.setKoszt(koszt);
        r.getPokoje().add(p);
        p.getRezerwacje().add(r);
        pokójRepo.save(p);
        rezerwacjaRepo.save(r);
        String opis = "Pracownik o loginie "+pr.getLogin()+" dodał pokój o id "+p.getIdPokój()+" do rezerwacji o id " +r.getIdRezerwacja();
        Log log = new Log("Rezerwacja","Aktualizowanie",opis);
        logRepo.save(log);
        model.addAttribute("rezerwacja", r);
        return "Rezerwacje_akt_podsumowanie";
    }
    @RequestMapping("/koniec_rezerwacji1")
    public String  koniec_rezerwacji1(
            @RequestParam("idpr") Integer idpr,
            @RequestParam("idr") Integer idr,
            Model model)
            throws Exception{
        Optional<Pracownik> op = pracownikRepo.findById(idpr);
        Pracownik p = op.get();
        Optional<Rezerwacja> or = rezerwacjaRepo.findById(idr);
        Rezerwacja r = or.get();
        model.addAttribute("rezerwacje",rezerwacjaRepo.findAll(Sort.by("status")));
        model.addAttribute("pracownik",p);
        Integer liczba = r.getLiczba_dorosłych() + r.getLiczba_dzieci();
        if(liczba > 10){
            float źniżka_grupa = 1 - r.getStawka().getŻniżka_za_grupę();
            float koszt = r.getKoszt() * źniżka_grupa;
            koszt = Math.round(koszt);
            r.setKoszt(koszt);
        }
        for(Pokój po : r.getPokoje()){
            Set<Rezerwacja> orr= po.getRezerwacje();
            orr.add(r);
            po.setRezerwacje(orr);
            pokójRepo.save(po);
        }
        Stawka s =  r.getStawka();
        Set<Rezerwacja> rezerwacje = s.getRezerwacje();
        rezerwacje.add(r);
        s.setRezerwacje(rezerwacje);
        Klient k =   r.getKlient();
        k.getRezerwacje().add(r);
        stawkaRepo.save(s);
        rezerwacjaRepo.save(r);
        klientRepo.save(k);
        return "Rezerwacje";
    }
    @RequestMapping("/kolejny_pokój1")
    public String kolejny_pokój1(
            @RequestParam("idpr") Integer idpr,
            @RequestParam("idr") Integer idr,
            Model model
    ) throws Exception {
        Optional<Pracownik> p = pracownikRepo.findById(idpr);
        Pracownik pracownik = p.get();
        Optional<Rezerwacja> or = rezerwacjaRepo.findById(idr);
        Rezerwacja rezerwacja = or.get();
        Date dooo = rezerwacja.getDoo();
        Date odd = rezerwacja.getOd();
        model.addAttribute("pracownik", pracownik);
        Integer nr_błędu;

        List<Pokój> pokójset = new ArrayList<>();
        for (Pokój pp : pokójRepo.findAll()) {
            pokójset.add(pp);
        }
        for (Rezerwacja rr : rezerwacjaRepo.findAll()) {


            if (rr.getDoo().before(dooo) && rr.getDoo().after(odd)) {
                for (Pokój pr : rr.getPokoje()) {
                    pokójset.remove(pr);
                }
            }
            if (rr.getOd().after(odd) && rr.getOd().before(dooo)) {
                for (Pokój pr : rr.getPokoje()) {
                    pokójset.remove(pr);
                }
            }
            if(rr.getDoo().after(dooo) && rr.getOd().before(odd)) {
                for (Pokój pr : rr.getPokoje()) {
                    pokójset.remove(pr);
                }
            }
            if (rr.getDoo().compareTo(dooo) == 0) {
                for (Pokój pr : rr.getPokoje()) {
                    pokójset.remove(pr);
                }
            }
            if (rr.getDoo().compareTo(odd) == 0) {
                for (Pokój pr : rr.getPokoje()) {
                    pokójset.remove(pr);
                }
            }
            if (rr.getOd().compareTo(odd) == 0) {
                for (Pokój pr : rr.getPokoje()) {
                    pokójset.remove(pr);
                }
            }
            if (rr.getOd().compareTo(dooo) == 0) {
                for (Pokój pr : rr.getPokoje()) {
                    pokójset.remove(pr);
                }
            }
        }


        if (pokójset.isEmpty()) {
            nr_błędu = 1;
            model.addAttribute("błąd", nr_błędu);
            return "Rezerwacje_akt_podsumowanie_błędy";
        }
        model.addAttribute("rezerwacja",rezerwacja);
        model.addAttribute("pokoje", pokójset);
        return "Rezerwacje_akt";

    }

    @RequestMapping("/dodajrezerwacjekl")
    public String klienci_dodaj_rezerwacje(
            @RequestParam("idpr") Integer idpr,
            @RequestParam("idk") Integer idk,
            Model model
    )
            throws Exception {

        Optional<Pracownik> p = pracownikRepo.findById(idpr);
        Pracownik pracownik = p.get();
        Optional<Klient> k = klientRepo.findById(idk);
        Klient klient = k.get();
        model.addAttribute("pracownik", pracownik);
        model.addAttribute("klient", klient);
        return ("Klienci_dodaj_rezerwacje");
    }

    @RequestMapping("/dodajrezerkl")
    public String klienci_dodaj_rezer(
            @RequestParam("idk") Integer idk,
            @RequestParam("od") String od,
            @RequestParam("od_time") String od_time,
            @RequestParam("doo") String doo,
            @RequestParam("doo_time") String doo_time,
            @RequestParam("idpr") Integer idpr,
            Model model
    ) throws Exception {
        Optional<Klient> k = klientRepo.findById(idk);
        Klient klient = k.get();
        Optional<Pracownik> p = pracownikRepo.findById(idpr);
        Pracownik pracownik = p.get();
        model.addAttribute("pracownik", pracownik);
        Integer nr_błędu;
        if (od.equals(doo)) {
            nr_błędu = 2;
            model.addAttribute("klient", klient);
            model.addAttribute("błąd", nr_błędu);
            return "Klienci_dodaj_rezerwacje_błędy";
        }

        String jutro = od;
        String[] jutroo = jutro.split("-");
        int rok = Integer.parseInt(jutroo[0]);
        int miesiąc = Integer.parseInt(jutroo[1]);
        int dzień = Integer.parseInt(jutroo[2]);
        for (int i = 0; i < 10; i++) {
            if (dzień == 31 && (miesiąc == 1 || miesiąc == 3 || miesiąc == 5 || miesiąc == 7 || miesiąc == 8 || miesiąc == 10 || miesiąc == 12)) {
                if (miesiąc == 12) {
                    rok = rok + 1;
                    miesiąc = 01;
                    dzień = 01;
                    i = 11;
                } else {
                    miesiąc = miesiąc + 1;
                    dzień = 01;
                    i = 11;
                }
            }
            if (dzień == 30 && (miesiąc == 4 || miesiąc == 6 || miesiąc == 9 || miesiąc == 11)) {
                miesiąc = miesiąc + 1;
                dzień = 01;
                i = 11;
            }
            if (rok % 4 == 0 && rok % 100 != 0 || rok % 400 == 0) {
                if (dzień == 29 && miesiąc == 2) {
                    miesiąc = miesiąc + 1;
                    dzień = 01;
                    i = 11;
                }
            } else {
                if (dzień == 28 && miesiąc == 2) {
                    miesiąc = miesiąc + 1;
                    dzień = 01;
                    i = 11;
                }
            }

            if (i != 11) {
                dzień = dzień + 1;
                i = 11;
            }
        }
        jutro = "";
        jutro += rok;
        jutro += '-';
        jutro += miesiąc;
        jutro += '-';
        jutro += dzień;
        jutro += ' ';
        jutro += od_time;

        od += ' ';
        od += od_time;
        DateFormat odf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date odd = odf.parse(od);
        Date jut = odf.parse(jutro);
        doo += ' ';
        doo += doo_time;
        DateFormat doof = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date dooo = doof.parse(doo);
        if (odd.equals(dooo)) {
            nr_błędu = 1;
            model.addAttribute("klient", klient);
            model.addAttribute("błąd", nr_błędu);
            return "Klienci_dodaj_rezerwacje_błędy";
        }
        Date d = new Date();
        if (odd.before(d) || dooo.before(d)) {
            nr_błędu = 3;
            model.addAttribute("klient", klient);
            model.addAttribute("błąd", nr_błędu);
            return "Klienci_dodaj_rezerwacje_błędy";
        }
        if (jut.after(dooo)) {
            nr_błędu = 2;
            model.addAttribute("klient", klient);
            model.addAttribute("błąd", nr_błędu);
            return "Klienci_dodaj_rezerwacje_błędy";
        }
        List<Pokój> pokójset = new ArrayList<>();
        for (Pokój pp : pokójRepo.findAll()) {
            pokójset.add(pp);
        }
        for (Rezerwacja rr : rezerwacjaRepo.findAll()) {


            if (rr.getDoo().before(dooo) && rr.getDoo().after(odd)) {
                for (Pokój pr : rr.getPokoje()) {
                    pokójset.remove(pr);
                }
            }
            if (rr.getOd().after(odd) && rr.getOd().before(dooo)) {
                for (Pokój pr : rr.getPokoje()) {
                    pokójset.remove(pr);
                }
            }
            if(rr.getDoo().after(dooo) && rr.getOd().before(odd)) {
                for (Pokój pr : rr.getPokoje()) {
                    pokójset.remove(pr);
                }
            }
            if (rr.getDoo().compareTo(dooo) == 0) {
                for (Pokój pr : rr.getPokoje()) {
                    pokójset.remove(pr);
                }
            }
            if (rr.getDoo().compareTo(odd) == 0) {
                for (Pokój pr : rr.getPokoje()) {
                    pokójset.remove(pr);
                }
            }
            if (rr.getOd().compareTo(odd) == 0) {
                for (Pokój pr : rr.getPokoje()) {
                    pokójset.remove(pr);
                }
            }
            if (rr.getOd().compareTo(dooo) == 0) {
                for (Pokój pr : rr.getPokoje()) {
                    pokójset.remove(pr);
                }
            }
        }


        if (pokójset.isEmpty()) {
            nr_błędu = 4;
            model.addAttribute("klient", klient);
            model.addAttribute("błąd", nr_błędu);
            return "Klienci_dodaj_rezerwacje_błędy";
        }

        Stawka ss = null;
        for (Stawka s : stawkaRepo.findAll()) {
            ss = s;
            if (s.getStatus().equals("Aktywna")) {
                break;
            }
        }

        Rezerwacja rezerwacja = new Rezerwacja(klient, "Oczekująca", odd, dooo, 0, 0, 0, 0, ss, 0);
        ss.getRezerwacje().add(rezerwacja);
        rezerwacjaRepo.save(rezerwacja);
        klient.getRezerwacje().add(rezerwacja);
        model.addAttribute("rezerwacja",rezerwacja);
        String opis = "Pracownik o loginie "+pracownik.getLogin()+" dodał termin do rezerwacji o id " +rezerwacja.getIdRezerwacja()+ " dla klienta o id "+klient.getIdKlient();
        Log log = new Log("Klient","Dodawanie rezerwacji",opis);
        logRepo.save(log);
        model.addAttribute("pokoje", pokójset);
        return "Klienci_dod_rezerwacje";

    }
 @RequestMapping("/dodrezerkl")
 public String dodrezerkl(
         @RequestParam("numer") String numer,
         @RequestParam("idpr") Integer idpr,
         @RequestParam("idr") Integer idr,
         @RequestParam("liczba_dorosłych") String liczba_dorosłych,
         @RequestParam("liczba_dzieci") String liczba_dzieci,
         @RequestParam("lp") List<Pokój> lp,
         Model model
 )throws Exception {
  Pokój p;
        if(numer.length() > 2) {
      String[] s = numer.split(" ");
      System.out.println(s[0]);
      System.out.println(s[1]);
      Integer numer1 = Integer.valueOf(s[1]);
            List<Pokój> pp =  pokójRepo.findByNumerpokoju(numer1);
            p = pp.get(0);;
  }
    else {
        Integer numer1 = Integer.valueOf(numer);
            List<Pokój> pp =  pokójRepo.findByNumerpokoju(numer1);
            p = pp.get(0);
  }

     Optional<Pracownik> opr = pracownikRepo.findById(idpr);
     Pracownik pr = opr.get();
     Optional<Rezerwacja> or = rezerwacjaRepo.findById(idr);
     Rezerwacja r = or.get();
     model.addAttribute("pracownik", pr);

     Integer nr_błędu;
     if(liczba_dorosłych.equals("0")) {
         nr_błędu = 2;
         model.addAttribute("błąd", nr_błędu);
         model.addAttribute("pokoje", lp);
         model.addAttribute("rezerwacja", r);
         return "Klienci_dod_rezerwacje_błędy";
     }
     Integer liczba_dorosłych1 = Integer.valueOf(liczba_dorosłych);
     Integer liczba_dzieci1 = Integer.valueOf(liczba_dzieci);
     Integer liczba = liczba_dorosłych1 + liczba_dzieci1;
     if (liczba > p.getMax_miejsce()) {
         nr_błędu = 1;
         model.addAttribute("błąd", nr_błędu);
         model.addAttribute("pokoje", lp);
         model.addAttribute("rezerwacja", r);
         return "Klienci_dod_rezerwacje_błędy";
     }
   Integer poz = 0;
     if (liczba > p.getMiejsce()) {
          poz = liczba - p.getMiejsce();
         Integer extraosoby = poz + r.getLiczba_extra_osób();
          r.setLiczba_extra_osób(extraosoby);
          Integer extrałóżka = poz + r.getLiczba_extra_łóżek();
         r.setLiczba_extra_łóżek(extrałóżka);
     }

     Integer dorosły = r.getLiczba_dorosłych() + liczba_dorosłych1;
     Integer dzieci = r.getLiczba_dzieci() + liczba_dzieci1;

     r.setLiczba_dorosłych(dorosły);
     r.setLiczba_dzieci(dzieci);
     float stawka_dzieci = 1 - r.getStawka().getŻniżka_za_dziecko();
     float stawka_sezon = 1 - r.getStawka().getŻniżka_za_sezon();
     float cenadorosłych = liczba_dorosłych1 * p.getCena();
     float cenadzieci = liczba_dzieci1 * p.getCena();
     cenadzieci = cenadzieci * stawka_dzieci;
     float cenaextraosób = poz * r.getStawka().getKoszt_za_osobę();
     float cenaextrałóżko = poz * r.getStawka().getKoszt_za_łóżko();
     float koszt = cenadorosłych + cenadzieci + cenaextraosób + cenaextrałóżko;
     koszt = koszt * stawka_sezon;
     koszt = koszt + r.getKoszt();
     koszt = Math.round(koszt);
     r.setKoszt(koszt);
     r.getPokoje().add(p);
     p.getRezerwacje().add(r);
     pokójRepo.save(p);
     rezerwacjaRepo.save(r);
     model.addAttribute("rezerwacja", r);
     String opis = "Pracownik o loginie "+pr.getLogin()+" dodał pokój o id " +p.getIdPokój()+ " do rezerwacji o id "+p.getIdPokój()+ " dla klienta o id "+r.getKlient();
     Log log = new Log("Klient","Dodawanie rezerwacji",opis);
     logRepo.save(log);
     return "Klienci_dod_rezerwacje_podsumowanie";
 }
@RequestMapping("/koniec_rezerwacji")
public String  koniec_rezerwacji(
        @RequestParam("idpr") Integer idpr,
     @RequestParam("idr") Integer idr,
     Model model)
throws Exception{
    Optional<Pracownik> op = pracownikRepo.findById(idpr);
    Pracownik p = op.get();
    Optional<Rezerwacja> or = rezerwacjaRepo.findById(idr);
    Rezerwacja r = or.get();
    model.addAttribute("klienci",klientRepo.findAll());
    model.addAttribute("pracownik",p);
    Integer liczba = r.getLiczba_dorosłych() + r.getLiczba_dzieci();
    if(liczba > 10){
       float stawka_grupa = 1 - r.getStawka().getŻniżka_za_grupę();
        float koszt = r.getKoszt() * stawka_grupa;
        koszt = Math.round(koszt);
        r.setKoszt(koszt);
    }
    for(Pokój po : r.getPokoje()){
        Set<Rezerwacja> orr= po.getRezerwacje();
        orr.add(r);
        po.setRezerwacje(orr);
        pokójRepo.save(po);
    }
    Stawka s =  r.getStawka();
    Set<Rezerwacja> rezerwacje = s.getRezerwacje();
    rezerwacje.add(r);
    s.setRezerwacje(rezerwacje);
  Klient k =   r.getKlient();
  k.getRezerwacje().add(r);
    stawkaRepo.save(s);
    rezerwacjaRepo.save(r);
klientRepo.save(k);
        return "Klienci";
     }
    @RequestMapping("/kolejny_pokój")
    public String kolejny_pokój(
            @RequestParam("idpr") Integer idpr,
            @RequestParam("idr") Integer idr,
            Model model
    ) throws Exception {
        Optional<Pracownik> p = pracownikRepo.findById(idpr);
        Pracownik pracownik = p.get();
        Optional<Rezerwacja> or = rezerwacjaRepo.findById(idr);
        Rezerwacja rezerwacja = or.get();
        Date dooo = rezerwacja.getDoo();
        Date odd = rezerwacja.getOd();
        model.addAttribute("pracownik", pracownik);
Integer nr_błędu;

        List<Pokój> pokójset = new ArrayList<>();
        for (Pokój pp : pokójRepo.findAll()) {
            pokójset.add(pp);
        }
        for (Rezerwacja rr : rezerwacjaRepo.findAll()) {


            if (rr.getDoo().before(dooo) && rr.getDoo().after(odd)) {
                for (Pokój pr : rr.getPokoje()) {
                    pokójset.remove(pr);
                }
            }
            if (rr.getOd().after(odd) && rr.getOd().before(dooo)) {
                for (Pokój pr : rr.getPokoje()) {
                    pokójset.remove(pr);
                }
            }
            if(rr.getDoo().after(dooo) && rr.getOd().before(odd)) {
                for (Pokój pr : rr.getPokoje()) {
                    pokójset.remove(pr);
                }
            }
            if (rr.getDoo().compareTo(dooo) == 0) {
                for (Pokój pr : rr.getPokoje()) {
                    pokójset.remove(pr);
                }
            }
            if (rr.getDoo().compareTo(odd) == 0) {
                for (Pokój pr : rr.getPokoje()) {
                    pokójset.remove(pr);
                }
            }
            if (rr.getOd().compareTo(odd) == 0) {
                for (Pokój pr : rr.getPokoje()) {
                    pokójset.remove(pr);
                }
            }
            if (rr.getOd().compareTo(dooo) == 0) {
                for (Pokój pr : rr.getPokoje()) {
                    pokójset.remove(pr);
                }
            }
        }


        if (pokójset.isEmpty()) {
            nr_błędu = 1;
            model.addAttribute("błąd", nr_błędu);
            return "Klienci_dod_rezerwacje_podsumowanie_błędy";
        }
        model.addAttribute("rezerwacja",rezerwacja);

        model.addAttribute("pokoje", pokójset);
        return "Klienci_dod_rezerwacje";

    }
    @RequestMapping("/dodajrezerwacje")
    public String dodaj_rezerwacje(
            @RequestParam("idpr") Integer idpr,
            @RequestParam("idk") Integer idk,
            Model model
    )
            throws Exception {
Integer nr_błędu;
        Optional<Pracownik> p = pracownikRepo.findById(idpr);
        Pracownik pracownik = p.get();
        model.addAttribute("pracownik", pracownik);
        Optional<Klient> k = klientRepo.findById(idk);
        if(k.isEmpty()){
            nr_błędu = 1;
            model.addAttribute("błąd",nr_błędu);
            model.addAttribute("rezerwacje",rezerwacjaRepo.findAll(Sort.by("status")));
                    return "Rezerwacje_błędy";
        }
        Klient klient = k.get();
        model.addAttribute("klient", klient);
        return ("Rezerwacje_dodanie");
    }

    @RequestMapping("/dodajrezer")
    public String rezerwacje_dod(
            @RequestParam("idk") Integer idk,
            @RequestParam("od") String od,
            @RequestParam("od_time") String od_time,
            @RequestParam("doo") String doo,
            @RequestParam("doo_time") String doo_time,
            @RequestParam("idpr") Integer idpr,
            Model model
    ) throws Exception {
        Optional<Klient> k = klientRepo.findById(idk);
        Klient klient = k.get();
        Optional<Pracownik> p = pracownikRepo.findById(idpr);
        Pracownik pracownik = p.get();
        model.addAttribute("pracownik", pracownik);
        Integer nr_błędu;
        if (od.equals(doo)) {
            nr_błędu = 2;
            model.addAttribute("klient", klient);
            model.addAttribute("błąd", nr_błędu);
            return "Rezerwacje_dod_błędy";
        }

        String jutro = od;
        String[] jutroo = jutro.split("-");
        int rok = Integer.parseInt(jutroo[0]);
        int miesiąc = Integer.parseInt(jutroo[1]);
        int dzień = Integer.parseInt(jutroo[2]);
        for (int i = 0; i < 10; i++) {
            if (dzień == 31 && (miesiąc == 1 || miesiąc == 3 || miesiąc == 5 || miesiąc == 7 || miesiąc == 8 || miesiąc == 10 || miesiąc == 12)) {
                if (miesiąc == 12) {
                    rok = rok + 1;
                    miesiąc = 01;
                    dzień = 01;
                    i = 11;
                } else {
                    miesiąc = miesiąc + 1;
                    dzień = 01;
                    i = 11;
                }
            }
            if (dzień == 30 && (miesiąc == 4 || miesiąc == 6 || miesiąc == 9 || miesiąc == 11)) {
                miesiąc = miesiąc + 1;
                dzień = 01;
                i = 11;
            }
            if (rok % 4 == 0 && rok % 100 != 0 || rok % 400 == 0) {
                if (dzień == 29 && miesiąc == 2) {
                    miesiąc = miesiąc + 1;
                    dzień = 01;
                    i = 11;
                }
            } else {
                if (dzień == 28 && miesiąc == 2) {
                    miesiąc = miesiąc + 1;
                    dzień = 01;
                    i = 11;
                }
            }

            if (i != 11) {
                dzień = dzień + 1;
                i = 11;
            }
        }
        jutro = "";
        jutro += rok;
        jutro += '-';
        jutro += miesiąc;
        jutro += '-';
        jutro += dzień;
        jutro += ' ';
        jutro += od_time;

        od += ' ';
        od += od_time;
        DateFormat odf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date odd = odf.parse(od);
        Date jut = odf.parse(jutro);
        doo += ' ';
        doo += doo_time;
        DateFormat doof = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date dooo = doof.parse(doo);
        if (odd.equals(dooo)) {
            nr_błędu = 1;
            model.addAttribute("klient", klient);
            model.addAttribute("błąd", nr_błędu);
            return "Rezerwacje_dodanie_błędy";
        }
        Date d = new Date();
        if (odd.before(d) || dooo.before(d)) {
            nr_błędu = 3;
            model.addAttribute("klient", klient);
            model.addAttribute("błąd", nr_błędu);
            return "Rezerwacje_dodanie_błędy";
        }
        if (jut.after(dooo)) {
            nr_błędu = 2;
            model.addAttribute("klient", klient);
            model.addAttribute("błąd", nr_błędu);
            return "Rezerwacje_dodanie_błędy";
        }
        List<Pokój> pokójset = new ArrayList<>();
        for (Pokój pp : pokójRepo.findAll()) {
            pokójset.add(pp);
        }
        for (Rezerwacja rr : rezerwacjaRepo.findAll()) {


            if (rr.getDoo().before(dooo) && rr.getDoo().after(odd)) {
                for (Pokój pr : rr.getPokoje()) {
                    pokójset.remove(pr);
                }
            }
            if (rr.getOd().after(odd) && rr.getOd().before(dooo)) {
                for (Pokój pr : rr.getPokoje()) {
                    pokójset.remove(pr);
                }
            }
            if(rr.getDoo().after(dooo) && rr.getOd().before(odd)) {
                for (Pokój pr : rr.getPokoje()) {
                    pokójset.remove(pr);
                }
            }
            if (rr.getDoo().compareTo(dooo) == 0) {
                for (Pokój pr : rr.getPokoje()) {
                    pokójset.remove(pr);
                }
            }
            if (rr.getDoo().compareTo(odd) == 0) {
                for (Pokój pr : rr.getPokoje()) {
                    pokójset.remove(pr);
                }
            }
            if (rr.getOd().compareTo(odd) == 0) {
                for (Pokój pr : rr.getPokoje()) {
                    pokójset.remove(pr);
                }
            }
            if (rr.getOd().compareTo(dooo) == 0) {
                for (Pokój pr : rr.getPokoje()) {
                    pokójset.remove(pr);
                }
            }
        }


        if (pokójset.isEmpty()) {
            nr_błędu = 4;
            model.addAttribute("klient", klient);
            model.addAttribute("błąd", nr_błędu);
            return "Rezerwacje_dodanie_błędy";
        }

        Stawka ss = null;
        for (Stawka s : stawkaRepo.findAll()) {
            ss = s;
            if (s.getStatus().equals("Aktywna")) {
                break;
            }
        }

        Rezerwacja rezerwacja = new Rezerwacja(klient, "Oczekująca", odd, dooo, 0, 0, 0, 0, ss, 0);
        ss.getRezerwacje().add(rezerwacja);
        rezerwacjaRepo.save(rezerwacja);
        klient.getRezerwacje().add(rezerwacja);
        String opis = "Pracownik o loginie "+pracownik.getLogin()+" dodał termin do rezerwacji o id "+rezerwacja.getIdRezerwacja();
        Log log = new Log("Rezerwacja","Dodawanie",opis);
        logRepo.save(log);
        model.addAttribute("rezerwacja",rezerwacja);
        model.addAttribute("pokoje", pokójset);
        return "Rezerwacje_dod";

    }
    @RequestMapping("/dodrezer")
    public String dodrezer(
            @RequestParam("numer") String numer,
            @RequestParam("idpr") Integer idpr,
            @RequestParam("idr") Integer idr,
            @RequestParam("liczba_dorosłych") String liczba_dorosłych,
            @RequestParam("liczba_dzieci") String liczba_dzieci,
            @RequestParam("lp") List<Pokój> lp,
            Model model
    )throws Exception {
        Pokój p;
        if(numer.length() > 2) {
            String[] s = numer.split(" ");
            System.out.println(s[0]);
            System.out.println(s[1]);
            Integer numer1 = Integer.valueOf(s[1]);
            List<Pokój> pp =  pokójRepo.findByNumerpokoju(numer1);
            p = pp.get(0);
        }
        else {
            Integer numer1 = Integer.valueOf(numer);
            List<Pokój> pp =  pokójRepo.findByNumerpokoju(numer1);
            p = pp.get(0);
        }

        Optional<Pracownik> opr = pracownikRepo.findById(idpr);
        Pracownik pr = opr.get();
        Optional<Rezerwacja> or = rezerwacjaRepo.findById(idr);
        Rezerwacja r = or.get();
        model.addAttribute("pracownik", pr);

        Integer nr_błędu;
        if(liczba_dorosłych.equals("0") ) {
            nr_błędu = 2;
            model.addAttribute("błąd", nr_błędu);
            model.addAttribute("pokoje", lp);
            model.addAttribute("rezerwacja", r);
            return "Klienci_dod_błędy";
        }
        Integer liczba_dorosłych1 = Integer.valueOf(liczba_dorosłych);
        Integer liczba_dzieci1 = Integer.valueOf(liczba_dzieci);
        Integer liczba = liczba_dorosłych1 + liczba_dzieci1;
        if (liczba > p.getMax_miejsce()) {
            nr_błędu = 1;
            model.addAttribute("błąd", nr_błędu);
            model.addAttribute("pokoje", lp);
            model.addAttribute("rezerwacja", r);
            return "Klienci_dod_błędy";
        }
        Integer poz = 0;
        if (liczba > p.getMiejsce()) {
            poz = liczba - p.getMiejsce();
            Integer extraosoby = poz + r.getLiczba_extra_osób();
            r.setLiczba_extra_osób(extraosoby);
            Integer extrałóżka = poz + r.getLiczba_extra_łóżek();
            r.setLiczba_extra_łóżek(extrałóżka);
        }

        Integer dorosły = r.getLiczba_dorosłych() + liczba_dorosłych1;
        Integer dzieci = r.getLiczba_dzieci() + liczba_dzieci1;
        r.setLiczba_dorosłych(dorosły);
        r.setLiczba_dzieci(dzieci);
        float stawka_dzieci = 1 - r.getStawka().getŻniżka_za_dziecko();
        float stawka_sezon = 1 - r.getStawka().getŻniżka_za_sezon();
        float cenadorosłych = liczba_dorosłych1 * p.getCena();
        float cenadzieci = liczba_dzieci1 * p.getCena();
        cenadzieci = cenadzieci * stawka_dzieci;
        float cenaextraosób = poz * r.getStawka().getKoszt_za_osobę();
        float cenaextrałóżko = poz * r.getStawka().getKoszt_za_łóżko();
        float koszt = cenadorosłych + cenadzieci + cenaextraosób + cenaextrałóżko;
        koszt = koszt * stawka_sezon;
        koszt = koszt + r.getKoszt();
        koszt = Math.round(koszt);
        r.setKoszt(koszt);
        r.getPokoje().add(p);
        p.getRezerwacje().add(r);
        pokójRepo.save(p);
        rezerwacjaRepo.save(r);
        String opis = "Pracownik o loginie "+pr.getLogin()+" dodał pokój o id "+p.getIdPokój()+" do rezerwacji o id "+r.getIdRezerwacja();
        Log log = new Log("Rezerwacja","Dodawanie",opis);
        logRepo.save(log);
        model.addAttribute("rezerwacja", r);
        return "Rezerwacje_dod_podsumowanie";
    }
    @RequestMapping("/koniec_rezerwacji2")
    public String  koniec_rezerwacji2(
            @RequestParam("idpr") Integer idpr,
            @RequestParam("idr") Integer idr,
            Model model)
            throws Exception{
        Optional<Pracownik> op = pracownikRepo.findById(idpr);
        Pracownik p = op.get();
        Optional<Rezerwacja> or = rezerwacjaRepo.findById(idr);
        Rezerwacja r = or.get();
        model.addAttribute("rezerwacje",rezerwacjaRepo.findAll(Sort.by("status")));
        model.addAttribute("pracownik",p);
        Integer liczba = r.getLiczba_dorosłych() + r.getLiczba_dzieci();
        if(liczba > 10){
            float żniżka_grupa = 1 - r.getStawka().getŻniżka_za_grupę();
            float koszt = r.getKoszt() * żniżka_grupa;
            koszt = Math.round(koszt);
            r.setKoszt(koszt);
        }
        for(Pokój po : r.getPokoje()){
            Set<Rezerwacja> orr= po.getRezerwacje();
            orr.add(r);
            po.setRezerwacje(orr);
            pokójRepo.save(po);
        }
        Stawka s =  r.getStawka();
        Set<Rezerwacja> rezerwacje = s.getRezerwacje();
        rezerwacje.add(r);
        s.setRezerwacje(rezerwacje);
        Klient k =   r.getKlient();
        k.getRezerwacje().add(r);
        stawkaRepo.save(s);
        rezerwacjaRepo.save(r);
        klientRepo.save(k);
        return "Rezerwacje";
    }
    @RequestMapping("/kolejny_pokój2")
    public String kolejny_pokój2(
            @RequestParam("idpr") Integer idpr,
            @RequestParam("idr") Integer idr,
            Model model
    ) throws Exception {
        Optional<Pracownik> p = pracownikRepo.findById(idpr);
        Pracownik pracownik = p.get();
        Optional<Rezerwacja> or = rezerwacjaRepo.findById(idr);
        Rezerwacja rezerwacja = or.get();
        Date dooo = rezerwacja.getDoo();
        Date odd = rezerwacja.getOd();
        model.addAttribute("pracownik", pracownik);
        Integer nr_błędu;

        List<Pokój> pokójset = new ArrayList<>();
        for (Pokój pp : pokójRepo.findAll()) {
            pokójset.add(pp);
        }
        for (Rezerwacja rr : rezerwacjaRepo.findAll()) {


            if (rr.getDoo().before(dooo) && rr.getDoo().after(odd)) {
                for (Pokój pr : rr.getPokoje()) {
                    pokójset.remove(pr);
                }
            }
            if (rr.getOd().after(odd) && rr.getOd().before(dooo)) {
                for (Pokój pr : rr.getPokoje()) {
                    pokójset.remove(pr);
                }
            }
            if(rr.getDoo().after(dooo) && rr.getOd().before(odd)) {
                for (Pokój pr : rr.getPokoje()) {
                    pokójset.remove(pr);
                }
            }
            if (rr.getDoo().compareTo(dooo) == 0) {
                for (Pokój pr : rr.getPokoje()) {
                    pokójset.remove(pr);
                }
            }
            if (rr.getDoo().compareTo(odd) == 0) {
                for (Pokój pr : rr.getPokoje()) {
                    pokójset.remove(pr);
                }
            }
            if (rr.getOd().compareTo(odd) == 0) {
                for (Pokój pr : rr.getPokoje()) {
                    pokójset.remove(pr);
                }
            }
            if (rr.getOd().compareTo(dooo) == 0) {
                for (Pokój pr : rr.getPokoje()) {
                    pokójset.remove(pr);
                }
            }
        }


        if (pokójset.isEmpty()) {
            nr_błędu = 1;
            model.addAttribute("błąd", nr_błędu);
            return "Rezerwacje_dod_podsumowanie_błędy";
        }
        model.addAttribute("rezerwacja",rezerwacja);

        model.addAttribute("pokoje", pokójset);
        return "Rezerwacje_dod";

    }
 }




