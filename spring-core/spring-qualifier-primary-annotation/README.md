## @Qualifier Annotation in Spring Boot

**@Qualifier** is used to resolve ambiguity when Spring finds multiple beans of the same type. It tells Spring which specific bean to inject when multiple candidates are available.

## When to Use @Qualifier:

When you have multiple implementations of the same interface, and you need to specify which one to inject.

## Simple Example:

### 1. Interface Definition
```java
public interface MessageService {
    String sendMessage();
}
```

### 2. Multiple Implementations
```java
@Component("emailService")  // Named bean
public class EmailService implements MessageService {
    @Override
    public String sendMessage() {
        return "Email sent!";
    }
}

@Component("smsService")  // Named bean
public class SMSService implements MessageService {
    @Override
    public String sendMessage() {
        return "SMS sent!";
    }
}
```

### 3. Using @Qualifier to Inject Specific Bean
```java
@Service
public class NotificationService {
    
    private final MessageService messageService;
    
    // Constructor injection with @Qualifier
    public NotificationService(@Qualifier("emailService") MessageService messageService) {
        this.messageService = messageService;
    }
    
    public String notifyUser() {
        return messageService.sendMessage();
    }
}
```

### 4. Alternative: Field Injection with @Qualifier
```java
@Service
public class NotificationService {
    
    @Autowired
    @Qualifier("smsService")  // Injects SMS Service
    private MessageService messageService;
    
    public String notifyUser() {
        return messageService.sendMessage();
    }
}
```

### 5. Setter Injection with @Qualifier
```java
@Service
public class NotificationService {
    
    private MessageService messageService;
    
    @Autowired
    public void setMessageService(@Qualifier("emailService") MessageService messageService) {
        this.messageService = messageService;
    }
}
```

## More Practical Example with Database Services:

```java
// Interface
public interface DataSource {
    String connect();
}

// Implementations
@Component("mysqlDataSource")
public class MySQLDataSource implements DataSource {
    @Override
    public String connect() {
        return "Connected to MySQL Database";
    }
}

@Component("postgresDataSource")
public class PostgresDataSource implements DataSource {
    @Override
    public String connect() {
        return "Connected to PostgreSQL Database";
    }
}

// Service using specific data source
@Service
public class UserService {
    
    private final DataSource dataSource;
    
    @Autowired
    public UserService(@Qualifier("mysqlDataSource") DataSource dataSource) {
        this.dataSource = dataSource;
    }
    
    public String getConnectionStatus() {
        return dataSource.connect();
    }
}
```

## Using @Primary with @Qualifier:

When you want a default bean but occasionally need a different one:

```java
@Component
@Primary  // This will be injected by default
public class EmailService implements MessageService {
    // implementation
}

@Component
public class SMSService implements MessageService {
    // implementation
}

@Service
public class NotificationService {
    
    // EmailService injected by default (due to @Primary)
    @Autowired
    private MessageService primaryService;
    
    // Explicitly choose SMS Service
    @Autowired
    @Qualifier("smsService")
    private MessageService secondaryService;
}
```

## Key Points:

1. **@Qualifier** has higher priority than **@Primary**
2. You can use bean names directly as qualifiers
3. Can be used with:
    - Constructor injection
    - Field injection
    - Setter injection
    - Method parameters

## Common Use Cases:
- Multiple database configurations
- Different payment gateways
- Various notification services (email, SMS, push)
- Different cache implementations
- Multiple API clients for the same service