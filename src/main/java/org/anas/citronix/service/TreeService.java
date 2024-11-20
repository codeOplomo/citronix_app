package org.anas.citronix.service;

import org.anas.citronix.domain.Field;
import org.anas.citronix.service.dto.TreeDTO;
import org.anas.citronix.domain.Tree;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TreeService {
    TreeDTO addTreeToField(TreeDTO treeDTO);

    Optional<Tree> findById(UUID treeId);

    List<Tree> findAllByField(Field field);
}
