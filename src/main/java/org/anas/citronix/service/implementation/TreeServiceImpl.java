package org.anas.citronix.service.implementation;

import org.anas.citronix.domain.Field;
import org.anas.citronix.domain.Tree;
import org.anas.citronix.exceptions.FieldNotFoundException;
import org.anas.citronix.exceptions.TreeDensityExceededException;
import org.anas.citronix.repository.TreeRepository;
import org.anas.citronix.service.FieldService;
import org.anas.citronix.service.TreeService;
import org.anas.citronix.service.dto.TreeDTO;
import org.anas.citronix.service.dto.mapper.TreeMapper;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class TreeServiceImpl implements TreeService {

    private final FieldService fieldService;
    private final TreeMapper treeMapper;
    private final TreeRepository treeRepository;

    public TreeServiceImpl(FieldService fieldService, TreeMapper treeMapper, TreeRepository treeRepository) {
        this.fieldService = fieldService;
        this.treeMapper = treeMapper;
        this.treeRepository = treeRepository;
    }

    public TreeDTO addTreeToField(TreeDTO treeDTO) {
        UUID fieldId = treeDTO.getFieldId();
        Field field = fieldService.findById(fieldId)
                .orElseThrow(() -> new FieldNotFoundException("Field with ID " + fieldId + " not found"));

        Tree tree = treeMapper.toEntity(treeDTO);

        validateTreeDensity(field);
        tree.setField(field);
        tree.validate();

        Tree savedTree = treeRepository.save(tree);

        return treeMapper.toDTO(savedTree);
    }

    private void validateTreeDensity(Field field) {
        double fieldArea = field.getArea();
        long treeCount = treeRepository.countByField(field);

        double maxDensity = 100 * fieldArea;
        if (treeCount >= maxDensity) {
            throw new TreeDensityExceededException("This field has reached the maximum tree density of " + maxDensity + " trees.");
        }
    }
}