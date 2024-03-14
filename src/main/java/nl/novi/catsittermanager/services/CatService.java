package nl.novi.catsittermanager.services;

import lombok.RequiredArgsConstructor;
import nl.novi.catsittermanager.dtos.cat.CatDto;
import nl.novi.catsittermanager.dtos.cat.CatInputDto;
import nl.novi.catsittermanager.mappers.CatMapper;
import nl.novi.catsittermanager.models.Cat;
import nl.novi.catsittermanager.models.Customer;
import nl.novi.catsittermanager.repositories.CatRepository;
import nl.novi.catsittermanager.repositories.CustomerRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CatService {

    private final CatRepository catRepos;
    private final CustomerRepository customerRepository;

    public List<CatDto> getAllCats() {
        return catRepos.findAll().stream()
                .map(CatMapper::transferToDto)
                .collect(Collectors.toList());
    }

    public CatDto getCat(UUID idToFind) {
        return catRepos.findById(idToFind)
                .map(CatMapper::transferToDto)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No cat found with this id."));
    }

    public CatDto createCat(@RequestBody CatInputDto catInputDto) {
        Cat newCat = CatMapper.transferFromDto(catInputDto);

        //TODO: check if owner exists and do a nice catch
        Customer owner = customerRepository.findById(catInputDto.ownerUsername()).orElseThrow();

        newCat.setOwner(owner);

        catRepos.save(newCat);
        return CatMapper.transferToDto(newCat);
    }

    public CatDto editCat(UUID idToEdit, CatInputDto catInputDto) {
        Optional<Cat> optionalCat = catRepos.findById(idToEdit);

        if (optionalCat.isPresent()) {
            Cat cat = optionalCat.get();
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

            //TODO: check if owner is updated

            catRepos.save(cat);
            return CatMapper.transferToDto(cat);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No cat found with this id.");
        }
    }

    public UUID deleteCat(UUID idToDelete) {
        catRepos.deleteById(idToDelete);
        return idToDelete;
    }
}
