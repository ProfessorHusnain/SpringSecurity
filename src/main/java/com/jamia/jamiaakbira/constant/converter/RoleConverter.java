package com.jamia.jamiaakbira.constant.converter;

import com.jamia.jamiaakbira.enumeration.Roles;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.stream.Stream;
@Converter(autoApply = true)
public class RoleConverter implements AttributeConverter<Roles, String> {

    @Override
    public String convertToDatabaseColumn(Roles roles) {
        if (roles == null) {
            return null;
        }
        return roles.getRole();
    }

    @Override
    public Roles convertToEntityAttribute(String code) {
        if (code == null) {
            return null;
        }
        return Stream.of(Roles.values())
                .filter(c -> c.getRole().equals(code))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
