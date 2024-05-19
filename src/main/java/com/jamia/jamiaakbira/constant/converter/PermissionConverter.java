package com.jamia.jamiaakbira.constant.converter;

import com.jamia.jamiaakbira.enumeration.Permissions;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.stream.Stream;

@Converter(autoApply = true)
public class PermissionConverter implements AttributeConverter<Permissions, String> {
    @Override
    public String convertToDatabaseColumn(Permissions permissions) {
        if (permissions == null) {
            return null;
        }
        return permissions.getPermission();
    }

    @Override
    public Permissions convertToEntityAttribute(String code) {
        if (code == null) {
            return null;
        }
        return Stream.of(Permissions.values())
                .filter(c -> c.getPermission().equals(code))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
