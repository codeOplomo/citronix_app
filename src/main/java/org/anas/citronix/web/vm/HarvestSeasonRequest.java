package org.anas.citronix.web.vm;

import org.anas.citronix.domain.enums.Season;

public class HarvestSeasonRequest {
    private Season season;

    // Getters and Setters
    public Season getSeason() {
        return season;
    }

    public void setSeason(Season season) {
        this.season = season;
    }
}

