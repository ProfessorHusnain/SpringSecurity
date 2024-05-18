package com.jamia.jamiaakbira.event;

import com.jamia.jamiaakbira.entity.User;
import com.jamia.jamiaakbira.enumeration.EventType;
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
