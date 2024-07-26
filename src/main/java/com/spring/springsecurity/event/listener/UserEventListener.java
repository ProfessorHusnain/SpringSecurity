package com.spring.springsecurity.event.listener;

import com.spring.springsecurity.event.UserEvent;
import com.spring.springsecurity.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserEventListener {

    private final EmailService emailService;
    @EventListener
    public void onUserEvent(UserEvent event) {
        switch (event.getEventType()) {
            case REGISTRATION ->
                emailService.sendNewAccountEmail(event.getUser().getFirstName(), event.getUser().getEmail(),
                        event.getData().get("key").toString());
            case PASSWORD_RESET-> emailService.sendPasswordResetEmail(event.getUser().getFirstName(),
                    event.getUser().getEmail(),event.getData().get("key").toString());
            default -> {
            }
        }
    }
}
