package nl.novi.catsittermanager.services;

import lombok.RequiredArgsConstructor;
import nl.novi.catsittermanager.exceptions.RecordNotFoundException;
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
                .orElseThrow(() -> new RecordNotFoundException("No cat found with this id."));
    }

    public Cat createCat(final Cat cat, final String ownerUsername) {
        Customer owner = customerService.getCustomer(ownerUsername);
        cat.setOwner(owner);
        return catRepository.save(cat);
    }

    public Cat editCat(final UUID idToEdit, final Cat cat, String ownerUsername) {
        if (catRepository.findById(idToEdit).isEmpty()) {
            throw new RecordNotFoundException(HttpStatus.NOT_FOUND, "No cat found with this id.");
        }
        Customer owner = customerService.getCustomer(ownerUsername);
        cat.setOwner(owner);
        return catRepository.save(cat);
    }

    public UUID deleteCat(UUID idToDelete) {
        if (!catRepository.existsById(idToDelete)) {
            throw new RecordNotFoundException("No cat found with this id.");
        }
        catRepository.deleteById(idToDelete);
        return idToDelete;
    }
}
