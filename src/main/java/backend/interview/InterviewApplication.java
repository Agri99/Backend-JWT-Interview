package backend.interview;

import java.util.ArrayList;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import backend.interview.models.Item;
import backend.interview.models.User;
import backend.interview.services.UserService;

@SpringBootApplication
public class InterviewApplication {

	public static void main(String[] args) {
		SpringApplication.run(InterviewApplication.class, args);
	}

	@Bean
	CommandLineRunner run(UserService userService) {
		return args -> {
            userService.saveUser(new User(null, "honda", "1234", "honda@mail.com", new ArrayList<>(), false));
			userService.saveUser(new User(null, "yamaha", "1234", "yamaha@mail.com", new ArrayList<>(), false));
			userService.saveUser(new User(null, "suzuki", "1234", "suzuki@mail.com", new ArrayList<>(), false));

			userService.saveItem(new Item(null, "CBR 150R"));
			userService.saveItem(new Item(null, "R25"));
			userService.saveItem(new Item(null, "GSX 150R"));

			userService.addItemToUser("honda", "CBR 150R");
			userService.addItemToUser("yamaha", "R25");
			userService.addItemToUser("suzuki", "GSX 150R");
        };
	}

}
