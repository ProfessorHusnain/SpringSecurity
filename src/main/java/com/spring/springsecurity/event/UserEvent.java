package com.spring.springsecurity.event;

import com.spring.springsecurity.entity.User;
import com.spring.springsecurity.enumeration.EventType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
public class UserEvent {
    private User user;
    private EventType eventType;
    private Map<?, ?> data;
}
