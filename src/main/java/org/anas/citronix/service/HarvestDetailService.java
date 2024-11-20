package org.anas.citronix.service;

import org.anas.citronix.domain.HarvestDetail;

import java.util.List;

public interface HarvestDetailService {
    void saveAll(List<HarvestDetail> harvestDetails);
}
