package com.yferdin.pigeon_devis_back.user.model;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class RoleTypeConverter implements AttributeConverter<RoleType, String> {

    @Override
    public String convertToDatabaseColumn(RoleType attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.name();
    }

    @Override
    public RoleType convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        return RoleType.valueOf(dbData);
    }
} 