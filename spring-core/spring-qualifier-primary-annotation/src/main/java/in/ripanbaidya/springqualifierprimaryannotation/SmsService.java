package in.ripanbaidya.springqualifierprimaryannotation;

import org.springframework.stereotype.Component;

@Component(value = "smsService")
public class SmsService implements  MessageService {

    @Override
    public String sendMessage() {
        return "SMS sent!";
    }
}
