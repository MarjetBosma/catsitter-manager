package nl.novi.catsittermanager.services;

import lombok.RequiredArgsConstructor;
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

    public Cat editCat(UUID id, Cat cat, String ownerUsername) {
        if (catRepository.findById(id).isEmpty()) {
            throw new RecordNotFoundException(HttpStatus.NOT_FOUND, "No catToUpdate found with this id.");
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
