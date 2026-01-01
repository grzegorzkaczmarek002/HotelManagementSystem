package Projekt.Hotelator;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

@Repository
public interface PokójRepo extends JpaRepository<Pokój, Integer> {
    List<Pokój> findByNumerpokoju(Integer numer_pokoju);
}
