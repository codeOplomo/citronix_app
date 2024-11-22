package org.anas.citronix.service;

import org.anas.citronix.domain.Field;
import org.anas.citronix.service.dto.TreeDTO;
import org.anas.citronix.domain.Tree;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TreeService {
    TreeDTO addTreeToField(TreeDTO treeDTO);

    Optional<Tree> findById(UUID treeId);

    TreeDTO updateTree(UUID id, TreeDTO treeDTO);

    List<Tree> findAllByField(Field field);

    List<Tree> findAllByIds(List<UUID> treeIds);

    void deleteTree(UUID treeId);

    TreeDTO restoreTree(UUID treeId);

    void saveTree(Tree tree);

    Page<TreeDTO> findAllTrees(boolean includeRemoved, Pageable pageable);

}
