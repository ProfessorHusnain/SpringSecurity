package com.jamia.jamiaakbira.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_DEFAULT;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "devotee")
@JsonInclude(NON_DEFAULT)
public class Devotee extends Auditable {
    private String fullName;
    private String fatherName;
    private String phone;
    private String email;
    private String occupation;
    private String city;
    private String cast;
    private String address;

}
