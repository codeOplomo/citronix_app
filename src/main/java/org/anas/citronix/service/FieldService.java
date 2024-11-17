package org.anas.citronix.service;

import org.anas.citronix.domain.Farm;
import org.anas.citronix.domain.Field;
import org.anas.citronix.service.dto.FieldDTO;

public interface FieldService {
    FieldDTO assignField(FieldDTO fieldDTO, Farm farm);
}
