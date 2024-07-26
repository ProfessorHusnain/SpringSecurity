package com.spring.springsecurity.entity;

import com.fasterxml.jackson.annotation.*;
import com.spring.springsecurity.enumeration.NotificationChannel;
import com.spring.springsecurity.utils.OTPGenerator;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.UUID;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_DEFAULT;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "confirmations")
@JsonInclude(NON_DEFAULT)
public class Confirmation extends Auditable {
    @Column(name = "confirmation_key")
    private String key;
    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    @JsonProperty("user_id")
    private User user;

    public Confirmation(User user, NotificationChannel type){
        this.user = user;
        switch (type){
            case EMAIL->this.key= UUID.randomUUID().toString();
            case SMS->this.key= String.valueOf(OTPGenerator.generateOTP());
        }
    }
}
