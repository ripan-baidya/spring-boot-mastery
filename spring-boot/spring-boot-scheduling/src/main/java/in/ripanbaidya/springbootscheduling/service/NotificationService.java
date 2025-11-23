package in.ripanbaidya.springbootscheduling.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);

    @Scheduled(cron = "0/5 * * * * *")
    public void sendNotification() {
        log.info("Notification Send at: {}", System.currentTimeMillis());
    }
}
