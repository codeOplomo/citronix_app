package org.anas.citronix.service;

import org.anas.citronix.domain.HarvestDetail;

import java.util.List;
import java.util.UUID;

public interface HarvestDetailService {
    void saveAll(List<HarvestDetail> harvestDetails);

    void deleteHarvestDetail(UUID harvestDetailId);
}
