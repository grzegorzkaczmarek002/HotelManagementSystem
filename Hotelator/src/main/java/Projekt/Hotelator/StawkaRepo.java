package Projekt.Hotelator;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StawkaRepo extends JpaRepository<Stawka,Integer> {
}
