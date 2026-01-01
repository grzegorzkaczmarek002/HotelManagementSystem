package Projekt.Hotelator;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RezerwacjaRepo extends JpaRepository<Rezerwacja,Integer> {
}
