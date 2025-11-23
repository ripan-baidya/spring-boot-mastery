package in.ripanbaidya.springbootscheduling;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
// Use this annotation to enable scheduling
@EnableScheduling
public class SpringBootSchedulingApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootSchedulingApplication.class, args);
    }

}
