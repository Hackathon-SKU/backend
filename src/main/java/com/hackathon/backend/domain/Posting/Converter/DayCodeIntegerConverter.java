// DayCode <-> Integer
package com.hackathon.backend.jpa.Converter;

import com.hackathon.backend.domain.Posting.Model.DayCode;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = false)
public class DayCodeIntegerConverter implements AttributeConverter<DayCode, Integer> {
    @Override public Integer convertToDatabaseColumn(DayCode attribute) {
        return attribute == null ? null : attribute.code();
    }
    @Override public DayCode convertToEntityAttribute(Integer dbData) {
        return dbData == null ? null : DayCode.fromCode(dbData);
    }
}
