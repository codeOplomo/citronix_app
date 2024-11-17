package org.anas.citronix.service.dto.mapper;

import org.anas.citronix.domain.Tree;
import org.anas.citronix.service.dto.TreeDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TreeMapper {
    @Mapping(target = "age", expression = "java(tree.calculateAge())")
    @Mapping(target = "productivity", expression = "java(tree.calculateProductivity())")
    @Mapping(target = "fieldId", source = "field.id")
    TreeDTO toDTO(Tree tree);

    @Mapping(target = "id", ignore = true) // ID is generated
    @Mapping(target = "field", ignore = true) // Set manually
    Tree toEntity(TreeDTO treeDTO);
}

