package in.ripanbaidya.generatecsvfile;

import in.ripanbaidya.generatecsvfile.entity.Student;
import in.ripanbaidya.generatecsvfile.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class SpringBootGenerateCsvFileApplication {

    private final StudentRepository studentRepository;

    @Autowired
    public SpringBootGenerateCsvFileApplication(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public static void main(String[] args) {
        SpringApplication.run(SpringBootGenerateCsvFileApplication.class, args);
    }

    @Bean
    public CommandLineRunner insertStudentsData() {
        return args -> {
            // Insert sample students data
            Student s1 = new Student("Ripan", "12345", "rb@gmail.com");
            Student s2 = new Student("Torsha", "12345", "td@gmail.com");
            Student s3 = new Student("Sayan", "12345", "sm@gmail.com");
            Student s4 = new Student("Raghav", "12345", "ry@gmail.com");

            studentRepository.saveAll(List.of(s1, s2, s3, s4));
        };
    }

}
