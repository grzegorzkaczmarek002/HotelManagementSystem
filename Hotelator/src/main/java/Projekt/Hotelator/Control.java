package Projekt.Hotelator;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.web.server.csrf.CsrfToken;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;


@Controller
public class Control {
    @Autowired
    private PracownikRepo pracownikRepo;
    @Autowired
    private KlientRepo klientRepo;
    @Autowired
    private PokójRepo pokójRepo;
    @Autowired
    private RezerwacjaRepo rezerwacjaRepo;
    @Autowired
    private StawkaRepo stawkaRepo;
    @RequestMapping("/")
    public String getHomePage(
            Model model
    )throws Exception {

        Integer licznik_prób = 0;
model.addAttribute("licznik_prób",licznik_prób);
        return "Widok";
    }


}
