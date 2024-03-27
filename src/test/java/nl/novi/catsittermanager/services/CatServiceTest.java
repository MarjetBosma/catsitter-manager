package nl.novi.catsittermanager.services;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import nl.novi.catsittermanager.dtos.cat.CatDto;
import nl.novi.catsittermanager.mappers.CatMapper;
import nl.novi.catsittermanager.models.Cat;
import nl.novi.catsittermanager.models.Customer;
import nl.novi.catsittermanager.repositories.CatRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Getter
@Setter
@ExtendWith(MockitoExtension.class)
public class CatServiceTest {

    @Mock
    private CatRepository catRepos;

    @Mock
    private CatMapper catMapper;

    @InjectMocks
    private CatService catService;

    @Test
    void testGetAllCats_shouldFetchAllCatsOnTheList() {

        // Arrange
        Customer owner1 = new Customer();
        owner1.setUsername("marjet_bosma");

        Customer owner2 = new Customer();
        owner2.setUsername("marianne_bosma");

        List<Cat> catList = new ArrayList<>();
        catList.add(new Cat(UUID.randomUUID(), "Firsa", LocalDate.of(2006, 7, 1), "V", "Europese Korthaar", "Vriendelijke, maar verlegen kat", true, true, "Dierenkliniek Zuilen", "030-2446933", "Thiafeline", "5mg 1dd", owner1));
        catList.add(new Cat(UUID.randomUUID(), "Kees", LocalDate.of(2009, 9, 1), "M", "Europese Korthaar", "Kan onverwacht agressief worden", true, true, "Dierenkliniek Hoevelaken", "038-4567891", null, null, owner2));

        // Mock behavior of the repository
        when(catRepos.findAll()).thenReturn(catList);

        // Act
        List<CatDto> catDtoList = catService.getAllCats();

        // Assert
        assertEquals(2, catDtoList.size());
    }

    @Test
    void testGetCat_shouldFetchCatWithSpecificId() {
        // Arrange
        Customer owner1 = new Customer();
        owner1.setUsername("marjet_bosma");

        UUID catId = UUID.randomUUID();
        Cat cat = new Cat(catId, "Firsa", LocalDate.of(2006, 7, 1), "V", "Europese Korthaar", "Vriendelijke, maar verlegen kat", true, true, "Dierenkliniek Zuilen", "030-2446933", "Thiafeline", "5mg 1dd", owner1);

        CatDto expectedCatDto = new CatDto(catId, "Firsa", LocalDate.of(2006, 7, 1), "V", "Europese Korthaar", "Vriendelijke, maar verlegen kat", true, true, "Dierenkliniek Zuilen", "030-2446933", "Thiafeline", "5mg 1dd", owner1.getUsername());

        // Mock behavior of the repository
        when(catRepos.findById(catId)).thenReturn(Optional.of(cat));

        // Mock behavior of the mapper
        when(catMapper.transferToDto(cat)).thenReturn(expectedCatDto);

        // Act
        CatDto actualCatDto = catService.getCat(catId);

        // Assert
        assertEquals(expectedCatDto, actualCatDto);
    }
}
