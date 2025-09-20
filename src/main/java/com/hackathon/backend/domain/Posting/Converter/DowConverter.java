package com.hackathon.backend.domain.Posting.Converter;

import com.hackathon.backend.domain.Posting.Model.Dow;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = false)
public class DowConverter implements AttributeConverter<Dow, Integer> {
    @Override
    public Integer convertToDatabaseColumn(Dow attribute) {
        return attribute == null ? null : attribute.code();
    }

    @Override
    public Dow convertToEntityAttribute(Integer dbData) {
        return dbData == null ? null : Dow.from(dbData);
    }
}