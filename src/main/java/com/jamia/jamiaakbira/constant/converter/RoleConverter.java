package com.jamia.jamiaakbira.constant.converter;

import com.jamia.jamiaakbira.enumeration.Authority;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.stream.Stream;
@Converter(autoApply = true)
public class RoleConverter implements AttributeConverter<Authority, String> {

    @Override
    public String convertToDatabaseColumn(Authority authority) {
        if (authority == null) {
            return null;
        }
        return authority.getAuthority();
    }

    @Override
    public Authority convertToEntityAttribute(String code) {
        if (code == null) {
            return null;
        }
        return Stream.of(Authority.values())
                .filter(c -> c.getAuthority().equals(code))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
