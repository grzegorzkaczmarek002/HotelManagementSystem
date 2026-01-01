package Projekt.Hotelator;



import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@SpringBootApplication
public class HotelatorApplication {

	public static void main(String[] args) {
		SpringApplication.run(HotelatorApplication.class, args);
	}




}
