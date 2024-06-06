package nl.novi.catsittermanager.mappers;

import nl.novi.catsittermanager.dtos.CatsitterRequestFactory;
import nl.novi.catsittermanager.dtos.catsitter.CatsitterRequest;
import nl.novi.catsittermanager.dtos.catsitter.CatsitterResponse;
import nl.novi.catsittermanager.models.Catsitter;
import nl.novi.catsittermanager.models.CatsitterFactory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CatsitterMapperTest {
    @Test
    void testCatsitterToCatsitterResponse() {

        // Arrange
        Catsitter catsitter = CatsitterFactory.randomCatsitter().build();

        // Act
        CatsitterResponse catsitterResponse = CatsitterMapper.CatsitterToCatsitterResponse(catsitter);

        // Assert
        assertEquals(catsitter.getUsername(), catsitterResponse.username());
        assertEquals(catsitter.getName(), catsitterResponse.name());;
        assertEquals(catsitter.getAddress(), catsitterResponse.address());
        assertEquals(catsitter.getEmail(), catsitterResponse.email());
        assertEquals(catsitter.getAbout(), catsitterResponse.about());
    }

    @Test
    void testCatsitterRequestToCatsitter() {

        // Arrange
        CatsitterRequest catsitterRequest = CatsitterRequestFactory.randomCatsitterRequest().build();

        // Act
        Catsitter catsitter = CatsitterMapper.CatsitterRequestToCatsitter(catsitterRequest);

        // Assert
        assertEquals(catsitterRequest.username(), catsitter.getUsername());
        assertEquals(catsitterRequest.name(), catsitter.getName());
        assertEquals(catsitterRequest.address(), catsitter.getAddress());
        assertEquals(catsitterRequest.email(), catsitter.getEmail());
    }
}
