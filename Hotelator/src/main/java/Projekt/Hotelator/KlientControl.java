package Projekt.Hotelator;


import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;


@Controller
public class KlientControl {
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
    @Autowired
    private AdresRepo adresRepo;
    @Autowired
    private OsobaRepo osobaRepo;

    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @RequestMapping("/klienci")
    public String lista_klienci(
            @RequestParam("idpr") Integer idpr,
            Model model
    )
            throws Exception {
        Optional<Pracownik> p = pracownikRepo.findById(idpr);
        Pracownik pracownik = p.get();
        model.addAttribute("pracownik", pracownik);
        model.addAttribute("klienci", klientRepo.findAll());
        return ("Klienci");
    }

@RequestMapping("/dodajkl")
public String klienci_dodanie(
        @RequestParam("idpr") Integer idpr,
Model model)
throws Exception{
        Optional<Pracownik> p = pracownikRepo.findById(idpr);
        Pracownik pracownik = p.get();
        model.addAttribute("pracownik",pracownik);
        return ("Klienci_dodanie");
}
@RequestMapping("/dodkl")
public String klienci_dod(
        @RequestParam("idpr") Integer idpr,
        @RequestParam("pesel") String pesel,
        @RequestParam("imię") String imię,
        @RequestParam("nazwisko") String nazwisko,
        @RequestParam("płeć") String płeć,
        @RequestParam("data_urodzenia") String data_urodzenia,
        @RequestParam("telefon") String telefon,
        @RequestParam("e_mail") String e_mail,
        @RequestParam("miejscowość") String miejscowośc,
        @RequestParam("ulica") String ulica,
        @RequestParam("nr_domu") String nr_domu,
        @RequestParam("nr_mieszkania") String nr_mieszkania,
        @RequestParam("kraj") String kraj,
        @RequestParam("kod_pocztowy") String kod_pocztowy,
        Model model
)
        throws Exception {
    Optional<Pracownik> p = pracownikRepo.findById(idpr);
    Pracownik pracownik = p.get();
    Integer nr_błędu;
    model.addAttribute("pracownik", pracownik);




        for (Klient kk : klientRepo.findAll()) {
            if(this.passwordEncoder.matches(pesel,kk.getOsoba().getPesel())){
                nr_błędu = 1;
                model.addAttribute("błąd", nr_błędu);
                return ("Klienci_dodanie_błędy");
            }
        }
    DateFormat odf = new SimpleDateFormat("yyyy-MM-dd");
        String[] data =  data_urodzenia.split("-");
        int rok = Integer.parseInt(data[0]);
        rok = rok + 18;
        data[0] = String.valueOf(rok);
        String potwierdzenie = "";
        potwierdzenie += data[0];
        potwierdzenie+="-";
        potwierdzenie+= data[1];
        potwierdzenie+="-";
        potwierdzenie+=data[2];
    Date pełnoletność = odf.parse(potwierdzenie);
    Date teraz = new Date();
    if(pełnoletność.after(teraz)){
        nr_błędu = 3;
        model.addAttribute("błąd", nr_błędu);
        return ("Klienci_dodanie_błędy");
    }
String pesell = this.passwordEncoder.encode(pesel);
    for(Osoba o : osobaRepo.findAll()) {
        if (this.passwordEncoder.matches(pesel, o.getPesel())) {
            Klient klient = new Klient(o);
            o.setKlient(klient);
            osobaRepo.save(o);
            klientRepo.save(klient);
            String opis = "Pracownik o loginie " + pracownik.getLogin() + " dodał klienta o id " + klient.getIdKlient();
            Log log = new Log("Klient", "Dodawanie", opis);
            logRepo.save(log);
            model.addAttribute("klienci", klientRepo.findAll());
            return ("Klienci");
        }
    }
        Integer adress = 0;
        Adres adres = null;
    if(nr_mieszkania.equals("")) {
        if (ulica.equals("")) {
            for (Adres a : adresRepo.findAll()) {
                if (a.getMiejscowość().equals(miejscowośc) && a.getKod_pocztowy().equals(kod_pocztowy) && a.getKraj().equals(kraj) && a.getNr_domu().equals(nr_domu) && a.getUlica().equals(" ") && a.getNr_mieszkania().equals(0)) {
                    adres = a;
                }
            }
            if (adres == null) {
                adres = new Adres(kod_pocztowy, miejscowośc, nr_domu, kraj);
                adress = 1;
            }
        }
        for (Adres a : adresRepo.findAll()) {
            if (a.getMiejscowość().equals(miejscowośc) && a.getKod_pocztowy().equals(kod_pocztowy) && a.getKraj().equals(kraj) && a.getNr_domu().equals(nr_domu) && a.getUlica().equals(ulica) && a.getNr_mieszkania().equals(0)) {
                adres = a;
            }
        }
        if (adres == null) {
            adres = new Adres(kod_pocztowy,miejscowośc,ulica,nr_domu,kraj);
            adress = 1;
        }
    }
    if(ulica.equals("") && nr_mieszkania != ""){
        Integer nr_mieszkaniaa = Integer.valueOf(nr_mieszkania);
        for (Adres a : adresRepo.findAll()) {
            if (a.getMiejscowość().equals(miejscowośc) && a.getKod_pocztowy().equals(kod_pocztowy) && a.getKraj().equals(kraj) && a.getNr_domu().equals(nr_domu) && a.getUlica().equals(" ") && a.getNr_mieszkania().equals(nr_mieszkaniaa)) {
                adres = a;
            }
        }
        if (adres == null) {
            adres = new Adres(kod_pocztowy, miejscowośc, nr_domu, nr_mieszkaniaa, kraj);
            adress = 1;
        }
    }
        if(adres == null){
            Integer nr_mieszkaniaa = Integer.valueOf(nr_mieszkania);
            for (Adres a : adresRepo.findAll()) {
                if (a.getMiejscowość().equals(miejscowośc) && a.getKod_pocztowy().equals(kod_pocztowy) && a.getKraj().equals(kraj) && a.getNr_domu().equals(nr_domu) && a.getUlica().equals(ulica) && a.getNr_mieszkania().equals(nr_mieszkaniaa)) {
                    adres = a;
                }
            }
            if (adres == null) {
                adres = new Adres(kod_pocztowy,miejscowośc,ulica,nr_domu,nr_mieszkaniaa,kraj);
                adress = 1;
            }
        }
    if(adress == 1){
        adresRepo.save(adres);
    }
        Osoba osoba = new Osoba(pesell,płeć,data_urodzenia,telefon,imię,nazwisko,e_mail,adres);
    osobaRepo.save(osoba);
        Klient klient = new Klient(osoba);

        if(adress == 1){
            Set<Osoba> lo = adres.getOsoby();
            lo.add(osoba);
            adres.setOsoby(lo);
            adresRepo.save(adres);
        }

klientRepo.save(klient);
    osoba.setKlient(klient);
    osobaRepo.save(osoba);
        String opis = "Pracownik o loginie "+pracownik.getLogin()+" dodał klienta o id "+klient.getIdKlient();
        Log log = new Log("Klient","Dodawanie",opis);
        logRepo.save(log);
        model.addAttribute("klienci", klientRepo.findAll());
        return ("Klienci");
    }


    @RequestMapping("/wyszukajkl")
    public String klienci_wyszukaj(
            @RequestParam("idpr") Integer idpr,
            @RequestParam("pesel") String pesel ,
            Model model)
    throws  Exception{
        Optional<Pracownik> p = pracownikRepo.findById(idpr);
        Pracownik pracownik = p.get();
        model.addAttribute("pracownik", pracownik);
Integer nr_błędu;
for(Klient k : klientRepo.findAll()){
    if(this.passwordEncoder.matches(pesel,k.getOsoba().getPesel())){
        model.addAttribute("klienci",k);
        return "Klienci";
    }
}
nr_błędu = 1;
model.addAttribute("błąd",nr_błędu);
model.addAttribute("klienci",klientRepo.findAll());
        return "Klienci_błędy";
    }




    @RequestMapping("/aktualizujkl")
    public String klienci_aktualizacja(
            @RequestParam("idpr") Integer idpr,
            @RequestParam("idk") Integer idk,
            Model model
    )
            throws Exception {
        Optional<Pracownik> p = pracownikRepo.findById(idpr);
        Pracownik pracownik = p.get();
        model.addAttribute("pracownik", pracownik);
        Optional<Klient> k = klientRepo.findById(idk);
        Klient klient = k.get();
        model.addAttribute("klient", klient);
        return ("Klienci_aktualizacja");
    }

    @RequestMapping("/aktualizacjakl")
    public String klienci_aktualizuj(
            @RequestParam("idpr") Integer idpr,
            @RequestParam("idk") Integer idk,

            @RequestParam("pesel") String pesel,
            @RequestParam("imię") String imię,
            @RequestParam("nazwisko") String nazwisko,
            @RequestParam("płeć") String płeć,
            @RequestParam("data_urodzenia") String data_urodzenia,
            @RequestParam("telefon") String telefon,
            @RequestParam("e_mail") String e_mail,
            @RequestParam("miejscowość") String miejscowośc,
            @RequestParam("ulica") String ulica,
            @RequestParam("nr_domu") String nr_domu,
            @RequestParam("nr_mieszkania") String nr_mieszkania,
            @RequestParam("kraj") String kraj,
            @RequestParam("kod_pocztowy") String kod_pocztowy,
            Model model
    )
            throws Exception {
        Optional<Klient> k = klientRepo.findById(idk);
        Klient klientzm = k.get();
        Optional<Pracownik> p = pracownikRepo.findById(idpr);
        Pracownik pracownik = p.get();
        Integer nr_błędu;
        model.addAttribute("pracownik", pracownik);
        Integer peselsp = 0;
        Integer datasp = 0;

        if (!Objects.equals(płeć, klientzm.getOsoba().getPłeć()) && imię == "") {
            model.addAttribute("klient", klientzm);
            nr_błędu = 2;
            model.addAttribute("błąd", nr_błędu);
            return ("Klienci_aktualizacja_błędy");
        }
        if (data_urodzenia != "") {
            DateFormat odf = new SimpleDateFormat("yyyy-MM-dd");
            String[] data =  data_urodzenia.split("-");
            int rok = Integer.parseInt(data[0]);
            rok = rok + 18;
            data[0] = String.valueOf(rok);
            String potwierdzenie = "";
            potwierdzenie += data[0];
            potwierdzenie+="-";
            potwierdzenie+= data[1];
            potwierdzenie+="-";
            potwierdzenie+=data[2];
            Date pełnoletność = odf.parse(potwierdzenie);
            Date teraz = new Date();
            if(pełnoletność.after(teraz)){
                model.addAttribute("klient", klientzm);
                nr_błędu = 3;
                model.addAttribute("błąd", nr_błędu);
                return ("Klienci_aktualizacja_błędy");
            }
        datasp =  1;
        }


        if (pesel != "") {
            for (Klient kk : klientRepo.findAll()) {
                if(this.passwordEncoder.matches(pesel,kk.getOsoba().getPesel())) {
                    model.addAttribute("klient", klientzm);
                    nr_błędu = 1;
                    model.addAttribute("błąd", nr_błędu);
                    return ("Klienci_aktualizacja_błędy");
                }
peselsp = 1;
            }


        }
        if (datasp == 1) {
            klientzm.getOsoba().setData_urodzenia(data_urodzenia);
        }

        if (peselsp == 1) {
            String pesell = this.passwordEncoder.encode(pesel);
            klientzm.getOsoba().setPesel(pesell);
        }

        if (imię != "") {
            klientzm.getOsoba().setImię(imię);
            if (!Objects.equals(płeć, klientzm.getOsoba().getPłeć())) {
                klientzm.getOsoba().setPłeć(płeć);
            }
        }
        if (nazwisko != "") {
            klientzm.getOsoba().setNazwisko(nazwisko);
        }


        if (telefon != "") {
            klientzm.getOsoba().setNr_telefonu(telefon);
        }
        if (e_mail != "") {
            klientzm.getOsoba().setE_mail(e_mail);
        }
        Adres adres =  klientzm.getOsoba().getAdres();
        String kod_pocztowyy = "";
        String miejscowość = "";
        String ulicaa = "";
        String nr_domuu = "";
        Integer nr_mieszkaniaa = 0 ;
        String krajj = "";
        if (kod_pocztowy != "") {
            kod_pocztowyy = kod_pocztowy;
        }
        if (miejscowośc != "") {
            miejscowość = miejscowośc;
        }
        if (ulica != "") {
            ulicaa = ulica;
        }
        if (nr_domu != "") {
            nr_domuu = nr_domu;
        }
        if (nr_mieszkania != "") {
            nr_mieszkaniaa = Integer.valueOf(nr_mieszkania);
        }
        if (kraj != "") {
            krajj = kraj;
        }
        Adres adres1 = new Adres(kod_pocztowyy,miejscowość,ulicaa,nr_domuu,nr_mieszkaniaa,krajj);
        if(adres1.getKod_pocztowy().equals("")){
            adres1.setKod_pocztowy(adres.getKod_pocztowy());
        }
        if(adres1.getMiejscowość().equals("")){
            adres1.setMiejscowość(adres.getMiejscowość());
        }
        if(adres1.getUlica().equals("")){
            adres1.setUlica(adres.getUlica());
        }
        if (adres1.getNr_domu().equals("")) {
            adres1.setNr_domu(adres.getNr_domu());
        }
        if(adres1.getKraj().equals("")){
            adres1.setKraj(adres.getKraj());
        }
        osobaRepo.save(klientzm.getOsoba());
        Integer powtórzenie = 0;
        for(Adres a : adresRepo.findAll()){
            if(a.getKraj().equals(adres1.getKraj()) && a.getUlica().equals(adres1.getUlica()) && a.getMiejscowość().equals(adres1.getMiejscowość()) && a.getNr_domu().equals(adres1.getNr_domu()) && a.getNr_mieszkania().equals(adres1.getNr_mieszkania()) && a.getKod_pocztowy().equals(adres1.getKod_pocztowy())){
                Adres adres2 = klientzm.getOsoba().getAdres();
                Set<Osoba> lo1 = adres2.getOsoby();
                lo1.remove(klientzm.getOsoba());
                adres2.setOsoby(lo1);
                adresRepo.save(adres2);
                klientzm.getOsoba().setAdres(a);
                Set<Osoba> lo  = a.getOsoby();
                lo.add(klientzm.getOsoba());
                a.setOsoby(lo);
                adresRepo.save(a);
                powtórzenie = 1;
            }
        }
        if(powtórzenie == 0){
            klientzm.getOsoba().setAdres(adres1);
            adres1.getOsoby().add(klientzm.getOsoba());
            adresRepo.save(adres1);
        }
            for (Rezerwacja r : klientzm.getRezerwacje()) {
                r.setKlient(klientzm);
                rezerwacjaRepo.save(r);
            }
            klientRepo.save(klientzm);
        String opis = "Pracownik o loginie "+pracownik.getLogin()+" aktualizował klienta o id "+klientzm.getIdKlient();
        Log log = new Log("Klient","Aktualizowanie",opis);
        logRepo.save(log);
            model.addAttribute("klienci", klientRepo.findAll());
            return ("Klienci");
        }
    @RequestMapping("/kasujkl")
    public String klienci_kasuj(
            @RequestParam("idpr") Integer idpr,
            @RequestParam("idk") Integer idk,
            Model model
    )
            throws Exception {
        String idrezerwacji = null;
        Optional<Pracownik> p = pracownikRepo.findById(idpr);
        Pracownik pracownik = p.get();
        model.addAttribute("pracownik", pracownik);
        Optional<Klient> k = klientRepo.findById(idk);
        Klient klient = k.get();
        for (Rezerwacja r : klient.getRezerwacje()) {
            Stawka a = r.getStawka();
            Set<Rezerwacja> w = a.getRezerwacje();
            w.remove(r);
            a.setRezerwacje(w);
            stawkaRepo.save(a);
            for (Pokój pp : r.getPokoje()) {
                Set<Rezerwacja> rr = pp.getRezerwacje();
                rr.remove(r);
                pp.setRezerwacje(rr);
                pokójRepo.save(pp);
            }
            idrezerwacji = " "+r.getIdRezerwacja();
            rezerwacjaRepo.delete(r);
        }
        Osoba osoba = klient.getOsoba();
        osoba.setKlient(null);
        Integer przynależność = 0;
        for(Pracownik pr : pracownikRepo.findAll()){
            if(pr.equals(osoba.getKlient())){
                przynależność = 1;
            }
        }
        if(przynależność == 0) {
            Adres a = osoba.getAdres();
            Set<Osoba> lo = a.getOsoby();
            lo.remove(osoba);
            if (lo.isEmpty()) {
                klientRepo.delete(klient);
                osobaRepo.delete(osoba);
                adresRepo.delete(a);
            } else {
                a.setOsoby(lo);
                adresRepo.save(a);
                klientRepo.delete(klient);
                osobaRepo.delete(osoba);
            }
        }
        if(przynależność == 1) {
            klientRepo.delete(klient);
            osobaRepo.save(klient.getOsoba());
        }


        String opis = "Pracownik o loginie "+pracownik.getLogin()+" usunął klienta o id "+klient.getIdKlient()+" i rezerwacje o id "+idrezerwacji;
        Log log = new Log("Klient","Usuwanie",opis);
        logRepo.save(log);
        klientRepo.delete(klient);
        model.addAttribute("klienci", klientRepo.findAll());
        return ("Klienci");


    }
    }
