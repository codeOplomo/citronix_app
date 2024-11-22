package org.anas.citronix.service;

import org.anas.citronix.domain.Farm;
import org.anas.citronix.domain.Field;
import org.anas.citronix.service.dto.FieldDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FieldService {
    FieldDTO assignField(FieldDTO fieldDTO, Farm farm);

    Optional<Field> findById(UUID fieldId);

    FieldDTO createField(FieldDTO fieldDTO);

    void saveAll(List<Field> fields);

    void deleteField(UUID fieldId);

    void restoreField(UUID fieldId) ;

    Page<Field> findAll(Pageable pageable);

    Page<Field> findAllByRemoved(boolean removed, Pageable pageable);

}
