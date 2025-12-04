package in.ripanbaidya.springqualifierprimaryannotation;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component(value = "emailService")
// @Primary annotation is used to specify a default bean when multiple beans of same type are available
// Note: Even if we never use @Qualifier annotation, @Primary annotation will still work. it will inject
// the bean marked with @Primary annotation.
@Primary
public class EmailService implements MessageService {

    @Override
    public String sendMessage() {
        return "Email sent!";
    }
}
