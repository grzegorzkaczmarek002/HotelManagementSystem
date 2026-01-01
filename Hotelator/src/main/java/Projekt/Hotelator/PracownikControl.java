package Projekt.Hotelator;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;


@Controller
public class PracownikControl {
    @Autowired
    private PracownikRepo pracownikRepo;
    @Autowired
    private KlientRepo klientRepo;
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


    @RequestMapping("/aplikacja")
    public String początek(
@RequestParam("login") String login,
@RequestParam("hasło") String hasło,
@RequestParam("licznik_prób") Integer licznik_prób,
Model model)
        throws Exception {
            if (licznik_prób == 4) {
                String opis = "Zablokowanie logowania";
                Log log = new Log("Logowanie", "Próby logowania", opis);
                logRepo.save(log);
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd-HH-mm");
                String blokada = df.format(new Date());
                String[] blokadaa = blokada.split("-");
                int rok = Integer.parseInt(blokadaa[0]);
                int miesiąc = Integer.parseInt(blokadaa[1]);
                int dzień = Integer.parseInt(blokadaa[2]);
                int godzina = Integer.parseInt(blokadaa[3]);
                int minuta = Integer.parseInt(blokadaa[4]);
                minuta = minuta + 15;
                if (minuta >= 60) {
                    int różnica = minuta - 60;
                    minuta = 00;
                    minuta = minuta + różnica;
                    godzina = godzina + 1;
                }
                if (godzina == 24) {
                    godzina = 00;
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
                }
                blokada = "";
                blokada += rok;
                blokada += "-";
                blokada += miesiąc;
                blokada += "-";
                blokada += dzień;
                blokada += " ";
                blokada += godzina;
                blokada += ":";
                blokada += minuta;
                DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                Date blokada1 = df1.parse(blokada);
                model.addAttribute("blokada", blokada);
                return "Widok2";
            }
            for (Pracownik pracownik : pracownikRepo.findAll()) {

                if (pracownik.getLogin().equals(login) && this.passwordEncoder.matches(hasło,pracownik.getHasło()) ) {
                    String opis = "Pracownik o loginie " + pracownik.getLogin() + " zalogował się";
                    Log log = new Log("Pracownik", "Logowanie", opis);
                    logRepo.save(log);

                    if (pracownik.getOsoba().getPłeć().equals("Męzczyzna")) {
                        pracownik.setStatus("Zalogowany");
                    }
                    if (pracownik.getOsoba().getPłeć().equals("Kobieta")) {
                        pracownik.setStatus("Zalogowana");
                    }
                    if (pracownik.getStanowisko().equals("Recepcjonista") || pracownik.getStanowisko().equals("Recepcjonistka")) {
                        pracownikRepo.save(pracownik);
                        model.addAttribute("pracownik", pracownik);
                        return "Recepcjonista";
                    }
                    if (pracownik.getStanowisko().equals("Sprzątacz") || pracownik.getStanowisko().equals("Sprzątaczka")) {
                        pracownikRepo.save(pracownik);
                        model.addAttribute("pracownik", pracownik);
                        return "Sprzątacz";
                    }
                    if (pracownik.getStanowisko().equals("Kierownik") || pracownik.getStanowisko().equals("Kierowniczka")) {
                        pracownikRepo.save(pracownik);
                        model.addAttribute("pracownik", pracownik);
                        return "Kierownik";
                    }
                    if (pracownik.getStanowisko().equals("Administrator") || pracownik.getStanowisko().equals("Administratorka")) {
                        pracownikRepo.save(pracownik);
                        model.addAttribute("pracownik", pracownik);
                        return "Administrator";
                    }
                }

            }
            licznik_prób = licznik_prób + 1;
            String opis = "Nieudana próba logowania";
            Log log = new Log("Logowanie", "Próby logowania", opis);
            logRepo.save(log);
            model.addAttribute("licznik_prób", licznik_prób);
            return "Widok1";

}


    @RequestMapping("/powrót")
    public String powrót(
    @RequestParam("blokada") String blokada,
    Model model
            ) throws Exception{
        Date teraz = new Date();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date blokadaa = df.parse(blokada);
        if(teraz.after(blokadaa)){
            Integer licznik_prób = 0;
            model.addAttribute("licznik_prób",licznik_prób);
            return "Widok";
        }
    model.addAttribute("blokada",blokada);
        return "Widok2";
    }




        @RequestMapping("/aplikacja1")
        public String początek(
                @RequestParam("idpr") Integer idpr,
                Model model)
        throws Exception{
Optional<Pracownik> p = pracownikRepo.findById(idpr);
Pracownik pracownik = p.get();
                    if(pracownik.getStanowisko().equals("Recepcjonista") || pracownik.getStanowisko().equals("Recepcjonistka")){
                        model.addAttribute("pracownik",pracownik);
                        return "Recepcjonista";
                    }
                    if(pracownik.getStanowisko().equals("Sprzątacz") || pracownik.getStanowisko().equals("Sprzątaczka")){
                        model.addAttribute("pracownik",pracownik);
                        return "Sprzątacz";
                    }
                    if(pracownik.getStanowisko().equals("Kierownik") || pracownik.getStanowisko().equals("Kierowniczka")){
                        model.addAttribute("pracownik",pracownik);
                        return "Kierownik";
                    }
                    if(pracownik.getStanowisko().equals("Administrator")|| pracownik.getStanowisko().equals("Administratorka")){
                        model.addAttribute("pracownik",pracownik);
                        return "Administrator";
                    }
         return "Widok";
                }






    @RequestMapping("/wylogowanie")
    public String wylogowanie(
   @RequestParam("idpr") Integer idpr,
    Model model)
            throws Exception{
        Optional<Pracownik> p = pracownikRepo.findById(idpr);
        Pracownik pracownik = p.get();
                if(pracownik.getOsoba().getPłeć().equals("Męzczyzna")){
                    pracownik.setStatus("Nie Zalogowany");
                    pracownikRepo.save(pracownik);
                }
                if(pracownik.getOsoba().getPłeć().equals("Kobieta")){
                    pracownik.setStatus("Nie Zalogowana");
                    pracownikRepo.save(pracownik);
                }

        String opis = "Pracownik o loginie "+pracownik.getLogin()+" wylogował się ";
        Log log = new Log("Pracownik","Wylogowanie",opis);
        logRepo.save(log);
        Integer licznik_prób = 0;
        model.addAttribute("licznik_prób",licznik_prób);
        return ("Widok");}
    @RequestMapping("/pracownicy")
    public String lista_pracownicy(
            @RequestParam("idpr") Integer idpr,
            Model model
    )
            throws Exception {
        Optional<Pracownik> p = pracownikRepo.findById(idpr);
        Pracownik pracownik = p.get();
        model.addAttribute("pracownik", pracownik);
        model.addAttribute("pracownicy", pracownikRepo.findAll());
        return ("Pracownicy");
    }


    @RequestMapping("/dodajpracownika")
    public String pracownicy_dodanie(
            @RequestParam("idpr") Integer idpr,
            Model model)
            throws Exception{
        Optional<Pracownik> p = pracownikRepo.findById(idpr);
        Pracownik pracownik = p.get();
        model.addAttribute("pracownik",pracownik);
        return ("Pracownicy_dodanie");
    }
    @RequestMapping("/dodpr")
    public String pracownicy_dod(
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
            @RequestParam("login") String login,
            @RequestParam("hasło") String hasło,
            @RequestParam("stanowisko") String stanowisko,
            Model model
    )
            throws Exception {
        Optional<Pracownik> p = pracownikRepo.findById(idpr);
        Pracownik pracownik = p.get();
        Integer nr_błędu;
        model.addAttribute("pracownik", pracownik);


if(płeć.equals("Kobieta")){
    if(stanowisko.equals("Recepcjonista")){
        stanowisko = "Recepcjonistka";
    }
if(stanowisko.equals("Sprzątacz")){
    stanowisko = "Sprzątaczka";
}
if(stanowisko.equals("Kierownik")){
    stanowisko = "Kierowniczka";
}
}

        for (Pracownik pp : pracownikRepo.findAll()) {
            if (this.passwordEncoder.matches(pesel,pp.getOsoba().getPesel())) {
                nr_błędu = 1;
                model.addAttribute("błąd", nr_błędu);
                return ("Pracownicy_dodanie_błędy");
            }
            if (pp.getLogin().equals(login)) {
                nr_błędu = 2;
                model.addAttribute("błąd", nr_błędu);
                return ("Pracownicy_dodanie_błędy");
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
            return ("Pracownicy_dodanie_błędy");
        }
String hasłoo = this.passwordEncoder.encode(hasło);
        String pesell = this.passwordEncoder.encode(pesel);
for(Osoba o : osobaRepo.findAll()) {
    if (this.passwordEncoder.matches(pesel, o.getPesel())) {
        Pracownik pracownik1 = new Pracownik(o, login, hasłoo, stanowisko);
    o.setPracownik(pracownik1);
        osobaRepo.save(o);
        pracownikRepo.save(pracownik1);
        String opis = "Pracownik o loginie " + pracownik.getLogin() + " dodał pracownika o id " + pracownik1.getIdPracownik();
        Log log = new Log("Pracownik", "Dodawanie", opis);
        logRepo.save(log);
        model.addAttribute("pracownicy", pracownikRepo.findAll());
        return ("Pracownicy");
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
   Pracownik pracownik1 = new Pracownik(osoba,login,hasłoo,stanowisko);

        if(adress == 1){
            Set<Osoba> lo = adres.getOsoby();
            lo.add(osoba);
            adres.setOsoby(lo);
            adresRepo.save(adres);
        }
        pracownikRepo.save(pracownik1);
        osoba.setPracownik(pracownik1);
        osobaRepo.save(osoba);


        String opis = "Pracownik o loginie "+pracownik.getLogin()+" dodał pracownika o id "+pracownik1.getIdPracownik();
        Log log = new Log("Pracownik","Dodawanie",opis);
        logRepo.save(log);
            model.addAttribute("pracownicy", pracownikRepo.findAll());
            return ("Pracownicy");
    }
    @RequestMapping("/wyszukajpracownika")
    public String pracownicy_wyszukaj(
            @RequestParam("idpr") Integer idpr,
            @RequestParam("pesel") String pesel ,
            Model model)
            throws  Exception{
        Optional<Pracownik> p = pracownikRepo.findById(idpr);
        Pracownik pracownik = p.get();
        model.addAttribute("pracownik", pracownik);
Integer nr_błędu;
        for(Pracownik pr : pracownikRepo.findAll()){
            if(this.passwordEncoder.matches(pesel,pr.getOsoba().getPesel())){
                model.addAttribute("pracownicy",pr);
                return "Pracownicy";
            }
        }
  nr_błędu = 1;
        model.addAttribute("błąd",nr_błędu);
        model.addAttribute("pracownicy",pracownikRepo.findAll());
        return "Pracownicy_błędy";
    }




    @RequestMapping("/aktualizujpracownika")
    public String pracownicy_aktualizacja(
            @RequestParam("idpr") Integer idpr,
            @RequestParam("idpr1") Integer idpr1,
            Model model
    )
            throws Exception {
        Optional<Pracownik> p = pracownikRepo.findById(idpr);
        Pracownik pracownik = p.get();
        model.addAttribute("pracownik", pracownik);
        Optional<Pracownik> pp = pracownikRepo.findById(idpr1);
        Pracownik pracownik1 = pp.get();
        model.addAttribute("pracownik1", pracownik1);
        return ("Pracownicy_aktualizacja");
    }

    @RequestMapping("/aktualizacjapr")
    public String pracownicy_aktualizuj(
            @RequestParam("idpr") Integer idpr,
            @RequestParam("idpr1") Integer idpr1,
            @RequestParam("login") String login,
            @RequestParam("hasło") String hasło,
            @RequestParam("stanowisko") String stanowisko,
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
        Optional<Pracownik> pp = pracownikRepo.findById(idpr1);
        Pracownik pracownik1 = pp.get();
        Optional<Pracownik> p = pracownikRepo.findById(idpr);
        Pracownik pracownik = p.get();
        Integer nr_błędu;
        model.addAttribute("pracownik", pracownik);
        Integer peselsp = 0;
        Integer datasp = 0;
        Integer loginsp = 0;

        if (!Objects.equals(płeć, pracownik1.getOsoba().getPłeć()) && imię == "") {
            model.addAttribute("pracownik1", pracownik1);
            nr_błędu = 4;
            model.addAttribute("błąd", nr_błędu);
            return ("Pracownicy_aktualizacja_błędy");
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
                model.addAttribute("pracownik1", pracownik1);
                nr_błędu = 3;
                model.addAttribute("błąd", nr_błędu);
                return ("Pracownicy_aktualizacja_błędy");
            }
            datasp =  1;
        }


        if (pesel != "") {
            for (Pracownik prac : pracownikRepo.findAll()) {
                if (this.passwordEncoder.matches(pesel,prac.getOsoba().getPesel())) {
                    model.addAttribute("pracownik1", pracownik1);
                    nr_błędu = 1;
                    model.addAttribute("błąd", nr_błędu);
                    return ("Pracownicy_aktualizacja_błędy");
                }
                peselsp = 1;
            }
        }
            if (login != "") {
                for (Pracownik prac1 : pracownikRepo.findAll()) {
                    if (prac1.getLogin().equals(login)) {
                        model.addAttribute("pracownik1", pracownik1);
                        nr_błędu = 2;
                        model.addAttribute("błąd", nr_błędu);
                        return ("Pracownicy_aktualizacja_błędy");
                    }
                    loginsp = 1;
                }
            }
          Adres adres =  pracownik1.getOsoba().getAdres();
        String kod_pocztowyy = "";
        String miejscowość = "";
        String ulicaa = "";
        String nr_domuu = "";
        Integer nr_mieszkaniaa = 0 ;
        String krajj = "";

        if (datasp == 1) {
            pracownik1.getOsoba().setData_urodzenia(data_urodzenia);
        }

        if (peselsp == 1) {
            String pesell = this.passwordEncoder.encode(pesel);
            pracownik1.getOsoba().setPesel(pesell);
        }
            if (loginsp == 1) {
                pracownik1.setLogin(login);
            }
        if (imię != "") {
            pracownik1.getOsoba().setImię(imię);
            if (!Objects.equals(płeć, pracownik1.getOsoba().getPłeć())) {
                pracownik1.getOsoba().setPłeć(płeć);
            }
        }
        if (nazwisko != "") {
            pracownik1.getOsoba().setNazwisko(nazwisko);
        }


        if (telefon != "") {
            pracownik1.getOsoba().setNr_telefonu(telefon);
        }
        if (e_mail != "") {
            pracownik1.getOsoba().setE_mail(e_mail);
        }

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

if(płeć != "" & stanowisko != "") {

    if (płeć.equals("Kobieta")) {
        if (stanowisko.equals("Recepcjonista")) {
          pracownik1.setStanowisko("Recepcjonista");
        }
        if (stanowisko.equals("Sprzątacz")) {
            pracownik1.setStanowisko("Sprzątacz");
        }
        if (stanowisko.equals("Kierownik")) {
            pracownik1.setStanowisko("Kierownik");
        }
    }
if(płeć.equals("Męzczyzna")){
    pracownik1.setStanowisko(stanowisko);
}
}
if(hasło != ""){
    String hasłoo = this.passwordEncoder.encode(hasło);
pracownik1.setHasło(hasłoo);
}

osobaRepo.save(pracownik1.getOsoba());
        Integer powtórzenie = 0;
        for(Adres a : adresRepo.findAll()){
            if(a.getKraj().equals(adres1.getKraj()) && a.getUlica().equals(adres1.getUlica()) && a.getMiejscowość().equals(adres1.getMiejscowość()) && a.getNr_domu().equals(adres1.getNr_domu()) && a.getNr_mieszkania().equals(adres1.getNr_mieszkania()) && a.getKod_pocztowy().equals(adres1.getKod_pocztowy())){
                Adres adres2 = pracownik1.getOsoba().getAdres();
                Set<Osoba> lo1 = adres2.getOsoby();
                lo1.remove(pracownik1.getOsoba());
                adres2.setOsoby(lo1);
                adresRepo.save(adres2);
                pracownik1.getOsoba().setAdres(a);
                Set<Osoba> lo  = a.getOsoby();
                lo.add(pracownik1.getOsoba());
                a.setOsoby(lo);
                adresRepo.save(a);
                powtórzenie = 1;
            }
        }
        if(powtórzenie == 0){
            pracownik1.getOsoba().setAdres(adres1);
            adres1.getOsoby().add(pracownik1.getOsoba());
adresRepo.save(adres1);
        }
        pracownikRepo.save(pracownik1);
        String opis = "Pracownik o loginie "+pracownik.getLogin()+" aktualizował pracownika o id "+pracownik1.getIdPracownik();
        Log log = new Log("Pracownik","Aktualizowanie",opis);
        logRepo.save(log);
        model.addAttribute("pracownicy", pracownikRepo.findAll());
        return ("Pracownicy");
    }
    @RequestMapping("/kasujpracownika")
    public String pracownicy_kasuj(
            @RequestParam("idpr") Integer idpr,
            @RequestParam("idpr1") Integer idpr1,
            Model model
    )
            throws Exception {
        Optional<Pracownik> p = pracownikRepo.findById(idpr);
        Pracownik pracownik = p.get();
        model.addAttribute("pracownik", pracownik);
        Optional<Pracownik> pp = pracownikRepo.findById(idpr1);
        Pracownik pracownik1 = pp.get();
        Osoba osoba = pracownik1.getOsoba();
        osoba.setPracownik(null);
        Integer przynależność = 0;
  for(Klient k : klientRepo.findAll()){
            if(k.equals(osoba.getKlient())){
                przynależność = 1;
            }
        }
        if(przynależność == 0) {
            Adres a = osoba.getAdres();
            Set<Osoba> lo = a.getOsoby();
            lo.remove(osoba);
            if (lo.isEmpty()) {
                pracownikRepo.delete(pracownik1);
                osobaRepo.delete(osoba);
                adresRepo.delete(a);
            } else {
                a.setOsoby(lo);
                adresRepo.save(a);

                pracownikRepo.delete(pracownik1);
                osobaRepo.delete(osoba);
            }
        }
        if(przynależność == 1) {
            pracownikRepo.delete(pracownik1);
            osobaRepo.save(pracownik.getOsoba());
        }

        String opis = "Pracownik o loginie "+pracownik.getLogin()+" usunął pracownika o id "+pracownik1.getIdPracownik();
        Log log = new Log("Pracownik","Usuwanie",opis);
        logRepo.save(log);
        model.addAttribute("pracownicy", pracownikRepo.findAll());
        return ("Pracownicy");


    }
}




