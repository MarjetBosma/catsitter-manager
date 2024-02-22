package nl.novi.catsittermanager.services;

import nl.novi.catsittermanager.dtos.cat.CatDto;
import nl.novi.catsittermanager.dtos.cat.CatInputDto;
import nl.novi.catsittermanager.mappers.CatMapper;
import nl.novi.catsittermanager.models.Cat;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static nl.novi.catsittermanager.mappers.CatMapper.transferToDto;

@Service
public class CatServiceImplementation implements CatService {

//    private final CatRepository catRepos;
//
//    private final CustomerRepository customerRepos;
//
//    private final CustomerServiceImplementation customerService;

    private List<Cat> cats = new ArrayList<>(); // voor testen zonder database

//    public CatServiceImplementation(CatRepository catRepos, CustomerRepository customerRepos, CustomerServiceImplementation customerService) {
//        this.catRepos = catRepos;
//        this.customerRepos = customerRepos;
//        this.customerService = customerService;
//    }

    public CatServiceImplementation() { // Bedoeld voor testen zonder database
        cats.add(new Cat(1L, "Firsa", LocalDate.parse("2006-07-01"), "Europese Korthaar", "Vriendelijke, maar verlegen kat", true, true, "Dierenkliniek Zuilen", "030-2446933", "Thiafeline", "5mg 1dd", "Marjet Bosma"));
        cats.add(new Cat(2L, "Kees", LocalDate.parse("2009-04-10"), "Europese Korthaar", "Vertoont soms onverwachte agressie", true, true, "Dierenkliniek Hoevelaken", "038-4356712", null, null, "Marianne Bosma"));
    }

    @Override
    public List<CatDto> getAllCats() {
//        List<Cat> catList = catRepos.findAll(); // Deze is voor als de database gevuld is
        List<CatDto> catDtoList = new ArrayList<>();

        for (Cat cat : cats) {
            CatDto catDto = transferToDto(cat);
            catDtoList.add(catDto);
        }
        return catDtoList;
    }

    @Override
    public CatDto getCat(long idToFind) { // Nu alleen nog voor gebruik zonder database
        for (Cat cat : cats) {
            if (cat.getId() == idToFind) {
                return transferToDto(cat);
            }
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No cat found with this id.");
    }

    @Override
    public CatDto createCat(CatInputDto catInputDto) {
        Cat newCat = new Cat(
                cats.get(cats.size() - 1).getId(),
                catInputDto.name(),
                catInputDto.dateOfBirth(),
                catInputDto.breed(),
                catInputDto.generalInfo(),
                catInputDto.spayedOrNeutered(),
                catInputDto.vaccinated(),
                catInputDto.veterinarianName(),
                catInputDto.phoneVet(),
                catInputDto.medicationName(),
                catInputDto.medicationDose(),
                catInputDto.ownerName()
        );
        cats.add(newCat);
        return CatMapper.transferToDto(newCat);
    }

    @Override
    public CatDto editCat(long idToEdit, CatInputDto catInputDto) {
        for (Cat cat : cats) {
            if (cat.getId() == idToEdit) {
                if (catInputDto.name() != null) {
                    cat.setName(catInputDto.name());
                }
                if (catInputDto.dateOfBirth() != null) {
                    cat.setDateOfBirth(catInputDto.dateOfBirth());
                }
                if (catInputDto.breed() != null) {
                    cat.setBreed(catInputDto.breed());
                }
                if (catInputDto.generalInfo() != null) {
                    cat.setGeneralInfo(catInputDto.generalInfo());
                }
                if (catInputDto.spayedOrNeutered() != null) {
                    cat.setSpayedOrNeutered(catInputDto.spayedOrNeutered());
                }
                if (catInputDto.vaccinated() != null) {
                    cat.setVaccinated(catInputDto.vaccinated());
                }
                if (catInputDto.veterinarianName() != null) {
                    cat.setVeterinarianName(catInputDto.veterinarianName());
                }
                if (catInputDto.phoneVet() != null) {
                    cat.setPhoneVet(catInputDto.phoneVet());
                }
                if (catInputDto.medicationName() != null) { // mag eigenlijk wel null zijn indien kat geen medicatie heeft
                    cat.setMedicationName(catInputDto.medicationName());
                }
                if (catInputDto.medicationDose() != null) { // mag eigenlijk wel null zijn indien kat geen medicatie heeft
                    cat.setMedicationDose(catInputDto.medicationDose());
                }
                if (catInputDto.ownerName() != null) {
                    cat.setOwnerName(catInputDto.ownerName());
                }
                return CatMapper.transferToDto(cat);
            }
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No cat found with this id.");
    }

    @Override
    public void deleteCat(long idToRemove) {
        for (Cat cat : cats) {
            if (cat.getId() == idToRemove) {
                cats.remove(cat);
                return;
            }
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No cat found with this id");
    }
}


