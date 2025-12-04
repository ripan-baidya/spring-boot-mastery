package in.ripanbaidya.springqualifierprimaryannotation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

// In this class, we will see how we can use @Qualifier annotation to resolve the ambiguity
// among multiple beans of same type. It tells Spring which specific bean to inject when
// multiple candidates are available.
@Service
public class NotificationService {

    private final MessageService messageService;

    // Here, we are using field injection with @Qualifier
    // @Autowired
    // @Qualifier("smsService")
    // private MessageService messageService;

    // Note: Here "emailService" bean will be injected by default because it is marked with @Primary annotation.
    // private MessageService messageService;

    // Constructor injection with @Qualifier
    // Note: If we never specify which bean to use then Spring will throw an exception
    // because it will not know which bean to inject.
    public NotificationService(@Qualifier("emailService") MessageService messageService) {
        this.messageService = messageService;
    }
}
