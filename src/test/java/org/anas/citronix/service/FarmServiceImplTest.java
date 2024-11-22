package org.anas.citronix.service;

import org.anas.citronix.domain.Farm;
import org.anas.citronix.domain.Field;
import org.anas.citronix.exceptions.FarmMaximumFieldsException;
import org.anas.citronix.exceptions.NullFarmException;
import org.anas.citronix.repository.FarmRepository;
import org.anas.citronix.service.dto.FarmDTO;
import org.anas.citronix.service.dto.FieldDTO;
import org.anas.citronix.service.dto.mapper.FarmMapper;
import org.anas.citronix.service.dto.mapper.FieldMapper;
import org.anas.citronix.service.implementation.FarmServiceImpl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;


@ExtendWith(MockitoExtension.class)
class FarmServiceImplTest {

    @Mock
    private FarmRepository farmRepository;

    @Mock
    private FarmMapper farmMapper;

    @Mock
    private FieldMapper fieldMapper;

    @Mock
    private FieldService fieldService;

    @InjectMocks
    private FarmServiceImpl farmService;

    @Test
    void createFarm_shouldThrowNullFarmException_whenFarmDTOIsNull() {
        // Arrange
        FarmDTO farmDTO = null;

        // Act & Assert
        NullFarmException exception = Assertions.assertThrows(NullFarmException.class, () -> {
            farmService.createFarm(farmDTO);
        });

        Assertions.assertEquals("Farm cannot be null", exception.getMessage());
    }

    @Test
    void createFarm_shouldNotSaveFields_whenFieldsAreNull() {
        // Arrange
        FarmDTO farmDTO = new FarmDTO();
        farmDTO.setFields(null);

        Farm farm = new Farm();
        Mockito.when(farmMapper.toEntity(Mockito.any(FarmDTO.class))).thenReturn(farm);
        Mockito.when(farmRepository.save(Mockito.any(Farm.class))).thenReturn(farm);
        Mockito.when(farmMapper.toDTO(Mockito.any(Farm.class))).thenReturn(new FarmDTO());

        // Act
        FarmDTO result = farmService.createFarm(farmDTO);

        // Assert
        Mockito.verify(fieldService, Mockito.never()).saveAll(Mockito.anyList());
        Assertions.assertNotNull(result, "Resulting FarmDTO should not be null");
    }

    @Test
    void createFarm_shouldNotSaveFields_whenFieldsAreEmpty() {
        FarmDTO farmDTO = new FarmDTO();
        farmDTO.setFields(Collections.emptyList());

        Farm farm = new Farm();
        FarmDTO expectedFarmDTO = new FarmDTO();
        Mockito.when(farmMapper.toEntity(farmDTO)).thenReturn(farm);
        Mockito.when(farmRepository.save(Mockito.any(Farm.class))).thenReturn(farm);
        Mockito.when(farmMapper.toDTO(Mockito.any(Farm.class))).thenReturn(expectedFarmDTO);

        // Act
        FarmDTO result = farmService.createFarm(farmDTO);

        // Assert
        Mockito.verify(fieldService, Mockito.never()).saveAll(Mockito.anyList());
        Assertions.assertNotNull(result, "Resulting FarmDTO should not be null");
        Assertions.assertEquals(expectedFarmDTO, result, "The result should match the expected FarmDTO");
    }

    @Test
    void createFarm_shouldThrowFarmMaximumFieldsException_whenFieldAreasExceedFarmTotalArea() {
        // Arrange
        FarmDTO farmDTO = new FarmDTO();
        farmDTO.setArea(10.0); // Farm total area
        farmDTO.setFields(List.of(
                new FieldDTO(8.0, null), // First field with area 8.0
                new FieldDTO(5.0, null)  // Second field with area 5.0
        ));

        Farm farm = new Farm();
        farm.setArea(10.0); // Farm area is 10

        // Mock the farmMapper to return the farm entity
        Mockito.when(farmMapper.toEntity(farmDTO)).thenReturn(farm);

        // Lenient stubbing for fieldMapper to avoid unnecessary stubbing error
        Field field1 = new Field();
        field1.setArea(8.0);
        Field field2 = new Field();
        field2.setArea(5.0);

        Mockito.lenient().when(fieldMapper.toEntity(farmDTO.getFields().get(0))).thenReturn(field1);
        Mockito.lenient().when(fieldMapper.toEntity(farmDTO.getFields().get(1))).thenReturn(field2);

        // Act & Assert
        FarmMaximumFieldsException exception = Assertions.assertThrows(FarmMaximumFieldsException.class, () -> {
            farmService.createFarm(farmDTO);
        });

        Assertions.assertEquals("The sum of the field areas exceeds the farm's total area", exception.getMessage());
    }

}
