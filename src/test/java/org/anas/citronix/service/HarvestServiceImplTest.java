package org.anas.citronix.service;

import org.anas.citronix.domain.Field;
import org.anas.citronix.domain.Harvest;
import org.anas.citronix.domain.Tree;
import org.anas.citronix.domain.enums.Season;
import org.anas.citronix.exceptions.FieldNotFoundException;
import org.anas.citronix.exceptions.HarvestAlreadyExistsException;
import org.anas.citronix.repository.HarvestRepository;
import org.anas.citronix.service.implementation.HarvestServiceImpl;
import org.anas.citronix.utils.HarvestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.mockito.*;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class HarvestServiceImplTest {

    @Mock
    private TreeService treeService;
    @Mock
    private FieldService fieldService;
    @Mock
    private HarvestRepository harvestRepository;
    @Mock
    private HarvestUtils harvestUtils;
    @Mock
    private HarvestDetailService harvestDetailService;  // Add this mock for HarvestDetailService

    @InjectMocks
    private HarvestServiceImpl harvestService;

    private UUID fieldId;
    private LocalDate harvestDate;
    private List<UUID> treeIds;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        fieldId = UUID.randomUUID();
        harvestDate = LocalDate.of(2024, 5, 15); // Example: May 15, 2024
        treeIds = Arrays.asList(UUID.randomUUID(), UUID.randomUUID());
    }

    @Test
    void testCreateHarvest_DifferentYear_SameSeason_ShouldPass() {
        Field field = mock(Field.class);
        Season season = mock(Season.class);
        LocalDate differentYearHarvestDate = LocalDate.of(2025, 5, 10);  // A different year in the same season

        when(fieldService.findById(eq(fieldId))).thenReturn(java.util.Optional.of(field));

        when(harvestRepository.existsByFieldAndSeasonAndYear(eq(field), any(Season.class), eq(2025))).thenReturn(false);

        List<Tree> trees = Arrays.asList(mock(Tree.class), mock(Tree.class));
        when(treeService.findAllByField(eq(field))).thenReturn(trees);

        when(harvestUtils.validateAndFetchTrees(eq(fieldId), eq(treeIds))).thenReturn(trees);

        doNothing().when(harvestDetailService).saveAll(anyList());

        Harvest harvest = mock(Harvest.class);
        when(harvestRepository.save(any(Harvest.class))).thenReturn(harvest);

        Harvest createdHarvest = harvestService.createHarvest(fieldId, differentYearHarvestDate, treeIds);

        assertNotNull(createdHarvest);
        verify(harvestRepository).save(any(Harvest.class));
        verify(harvestDetailService).saveAll(anyList());
    }

    @Test
    void testCreateHarvest_SameYear_SameSeason_ShouldThrowException() {
        Field field = mock(Field.class);
        Season mockSeason = mock(Season.class);
        LocalDate sameYearHarvestDate = LocalDate.of(2024, 5, 10);  // Same year, same season

        when(fieldService.findById(eq(fieldId))).thenReturn(java.util.Optional.of(field));

        when(harvestUtils.determineSeason(eq(sameYearHarvestDate))).thenReturn(mockSeason);

        when(harvestRepository.existsByFieldAndSeasonAndYear(eq(field), eq(mockSeason), eq(2024)))
                .thenReturn(true);

        doNothing().when(harvestDetailService).saveAll(anyList());

        assertThrows(HarvestAlreadyExistsException.class, () -> {
            harvestService.createHarvest(fieldId, sameYearHarvestDate, treeIds);
        });

        verify(harvestRepository, never()).save(any(Harvest.class));
        verify(harvestDetailService, never()).saveAll(anyList());
    }

    @Test
    void testCreateHarvest_FieldNotFound_ShouldThrowException() {
        when(fieldService.findById(eq(fieldId))).thenReturn(java.util.Optional.empty());

        assertThrows(FieldNotFoundException.class, () -> {
            harvestService.createHarvest(fieldId, harvestDate, treeIds);
        });

        verify(harvestRepository, never()).save(any(Harvest.class));
        verify(harvestDetailService, never()).saveAll(anyList());
    }

}



