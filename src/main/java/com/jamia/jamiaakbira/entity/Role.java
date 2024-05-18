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
@Table(name = "roles")
@JsonInclude(NON_DEFAULT)
public class Role extends Auditable{
    private String name;
    private String description;
    private String authorities; // todo: create the jpa entity for authorities
    private boolean active;
}
