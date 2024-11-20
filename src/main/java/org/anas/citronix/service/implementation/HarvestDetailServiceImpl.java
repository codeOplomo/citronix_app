package org.anas.citronix.service.implementation;

import org.anas.citronix.domain.Harvest;
import org.anas.citronix.domain.HarvestDetail;
import org.anas.citronix.domain.Tree;
import org.anas.citronix.exceptions.HarvestNotFoundException;
import org.anas.citronix.exceptions.TreeNotFoundException;
import org.anas.citronix.repository.HarvestDetailRepository;
import org.anas.citronix.repository.HarvestRepository;
import org.anas.citronix.service.HarvestDetailService;
import org.anas.citronix.service.TreeService;
import org.anas.citronix.service.dto.HarvestDetailDTO;
import org.anas.citronix.service.dto.mapper.HarvestMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class HarvestDetailServiceImpl implements HarvestDetailService {

    private final HarvestDetailRepository harvestDetailRepository;

    public HarvestDetailServiceImpl(HarvestDetailRepository harvestDetailRepository) {
        this.harvestDetailRepository = harvestDetailRepository;
    }

    @Override
    public void saveAll(List<HarvestDetail> harvestDetails) {
        harvestDetailRepository.saveAll(harvestDetails);
    }

//    @Override
//    public HarvestDetailDTO addHarvestDetail(UUID harvestId, HarvestDetailDTO harvestDetailDTO) {
//        // Retrieve the harvest and tree entities using the provided IDs
//        Harvest harvest = harvestRepository.findById(harvestId)
//                .orElseThrow(() -> new HarvestNotFoundException("Harvest with ID " + harvestId + " not found"));
//
//        Tree tree = treeService.findById(harvestDetailDTO.getTreeId())
//                .orElseThrow(() -> new TreeNotFoundException("Tree with ID " + harvestDetailDTO.getTreeId() + " not found"));
//
//        // Create and save the HarvestDetail entity
//        HarvestDetail harvestDetail = harvestMapper
//                new HarvestDetail();
//        harvestDetail.setQuantity(harvestDetailDTO.getQuantity());
//        harvestDetail.setHarvest(harvest);
//        harvestDetail.setTree(tree);
//
//        HarvestDetail savedDetail = harvestDetailRepository.save(harvestDetail);
//
//        // Return the saved detail as a DTO
//        return new HarvestDetailDTO(savedDetail);
//    }
}
