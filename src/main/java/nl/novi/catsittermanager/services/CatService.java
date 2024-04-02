package nl.novi.catsittermanager.services;

import lombok.RequiredArgsConstructor;
import nl.novi.catsittermanager.dtos.cat.CatResponse;
import nl.novi.catsittermanager.dtos.cat.CatRequest;
import nl.novi.catsittermanager.exceptions.RecordNotFoundException;
import nl.novi.catsittermanager.mappers.CatMapper;
import nl.novi.catsittermanager.models.Cat;
import nl.novi.catsittermanager.models.Customer;
import nl.novi.catsittermanager.repositories.CatRepository;
import nl.novi.catsittermanager.repositories.CustomerRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CatService {

    private final CatRepository catRepository;
    private final CustomerService customerService;
    private final CustomerRepository customerRepository;
    private final CatMapper catMapper;

    public List<Cat> getAllCats() {
        return catRepository.findAll();
    }

    public Cat getCat(final UUID idToFind) {
        return catRepository.findById(idToFind)
                .orElseThrow(() -> new RecordNotFoundException("No cat found with this id."));
    }

    public Cat createCat(final Cat cat, final String ownerUsername) {
        Customer owner = customerService.getCustomer(ownerUsername);
        cat.setOwner(owner);

        return catRepository.save(cat);
    }

    public CatResponse editCat(UUID idToEdit, CatRequest catRequest) {
        Optional<Cat> optionalCat = catRepository.findById(idToEdit);

        if (optionalCat.isPresent()) {
            Cat cat = optionalCat.get();
            if (catRequest.name() != null) {
                cat.setName(catRequest.name());
            }
            if (catRequest.dateOfBirth() != null) {
                cat.setDateOfBirth(catRequest.dateOfBirth());
            }
            if (catRequest.gender() != null) {
                cat.setGender(catRequest.gender());
            }
            if (catRequest.breed() != null) {
                cat.setBreed(catRequest.breed());
            }
            if (catRequest.generalInfo() != null) {
                cat.setGeneralInfo(catRequest.generalInfo());
            }
            if (catRequest.spayedOrNeutered() != null) {
                cat.setSpayedOrNeutered(catRequest.spayedOrNeutered());
            }
            if (catRequest.vaccinated() != null) {
                cat.setVaccinated(catRequest.vaccinated());
            }
            if (catRequest.veterinarianName() != null) {
                cat.setVeterinarianName(catRequest.veterinarianName());
            }
            if (catRequest.phoneVet() != null) {
                cat.setPhoneVet(catRequest.phoneVet());
            }
            if (catRequest.medicationName() != null) {
                cat.setMedicationName(catRequest.medicationName());
            }
            if (catRequest.medicationDose() != null) {
                cat.setMedicationDose(catRequest.medicationDose());
            }
            if (catRequest.ownerUsername() != null) {
                Customer owner = customerRepository.findById(catRequest.ownerUsername())
                        .orElseThrow(() -> new RecordNotFoundException(HttpStatus.NOT_FOUND, "Owner not found"));
                cat.setOwner(owner);
            }
            catRepository.save(cat);
            return catMapper.CatToCatResponse(cat);
        } else {
            throw new RecordNotFoundException(HttpStatus.NOT_FOUND, "No cat found with this id.");
        }
    }

    public UUID deleteCat(UUID idToDelete) {
        if (!catRepository.existsById(idToDelete)) {
            throw new RecordNotFoundException(HttpStatus.NOT_FOUND, "No cat found with this id.");
        }
        catRepository.deleteById(idToDelete);
        return idToDelete;
    }
}
