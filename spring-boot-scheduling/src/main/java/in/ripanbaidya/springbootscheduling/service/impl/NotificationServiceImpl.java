package in.ripanbaidya.springbootscheduling.service.impl;

import in.ripanbaidya.springbootscheduling.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class NotificationServiceImpl implements NotificationService {

    private static final Logger log = LoggerFactory.getLogger(NotificationServiceImpl.class);

    @Override
    @Scheduled(fixedDelayString = "${app.notification.time}")
    public void sendNotification() {

        log.info("Notification sent at {}", System.currentTimeMillis());
    }
}