package nl.novi.catsittermanager.services;

import lombok.RequiredArgsConstructor;
import nl.novi.catsittermanager.exceptions.RecordNotFoundException;
import nl.novi.catsittermanager.exceptions.UsernameNotFoundException;
import nl.novi.catsittermanager.models.Cat;
import nl.novi.catsittermanager.models.Customer;
import nl.novi.catsittermanager.repositories.CatRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CatService {

    private final CatRepository catRepository;
    private final CustomerService customerService;

    public List<Cat> getAllCats() {
        return catRepository.findAll();
    }

    public Cat getCat(final UUID idToFind) {
        return catRepository.findById(idToFind)
                .orElseThrow(() -> new RecordNotFoundException(HttpStatus.NOT_FOUND, "No cat found with this id."));
    }

    public Cat createCat(final Cat cat, final String ownerUsername) {
        Customer owner = customerService.getCustomer(ownerUsername);
        if (owner == null) {
            throw new UsernameNotFoundException("Owner not found");
        }
        cat.setOwner(owner);
        return catRepository.save(cat);
    }

    public Cat editCat(final UUID idToEdit, final Cat updatedCat, String ownerUsername) {
        Cat existingCat = catRepository.findById(idToEdit)
                .orElseThrow(() -> new RecordNotFoundException(HttpStatus.NOT_FOUND, "No cat found with this id."));

        existingCat.setName(updatedCat.getName());
        existingCat.setDateOfBirth(updatedCat.getDateOfBirth());
        existingCat.setGender(updatedCat.getGender());
        existingCat.setBreed(updatedCat.getBreed());
        existingCat.setGeneralInfo(updatedCat.getGeneralInfo());
        existingCat.setSpayedOrNeutered(updatedCat.getSpayedOrNeutered());
        existingCat.setVaccinated(updatedCat.getVaccinated());
        existingCat.setVeterinarianName(updatedCat.getVeterinarianName());
        existingCat.setPhoneVet(updatedCat.getPhoneVet());
        existingCat.setMedicationName(updatedCat.getMedicationName());
        existingCat.setMedicationDose(updatedCat.getMedicationDose());
        existingCat.setOwner(updatedCat.getOwner());
        existingCat.setImage(updatedCat.getImage());

        Customer owner = updatedCat.getOwner();
        if (owner != null) {
            Customer existingOwner = customerService.getCustomer(ownerUsername);
            existingCat.setOwner(existingOwner);
        }
        return catRepository.save(updatedCat);
    }

    public UUID deleteCat(UUID idToDelete) {
        if (!catRepository.existsById(idToDelete)) {
            throw new RecordNotFoundException("No cat found with this id.");
        }
        catRepository.deleteById(idToDelete);
        return idToDelete;
    }
}
