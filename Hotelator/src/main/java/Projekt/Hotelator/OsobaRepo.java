package Projekt.Hotelator;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OsobaRepo extends JpaRepository<Osoba,Integer> {



}
