package nl.novi.catsittermanager.controllers;

import nl.novi.catsittermanager.dtos.catsitter.CatsitterDto;
import nl.novi.catsittermanager.dtos.catsitter.CatsitterInputDto;
import nl.novi.catsittermanager.dtos.customer.CustomerDto;
import nl.novi.catsittermanager.exceptions.RecordNotFoundException;
import nl.novi.catsittermanager.exceptions.ValidationException;
import nl.novi.catsittermanager.services.CatsitterServiceImplementation;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import static nl.novi.catsittermanager.controllers.ControllerHelper.checkForBindingResult;

// was verwijderd door Dennis???

@RestController
@RequestMapping("/catsitter")
public class CatsitterController {

    private final CatsitterServiceImplementation catsitterService;

    public CatsitterController(CatsitterServiceImplementation catsitterService) {
        this.catsitterService = catsitterService;
    }

    @GetMapping
    public ResponseEntity<List<CatsitterDto>> getAllCatSitters() {
        return ResponseEntity.ok(catsitterService.getAllCatsitters());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CatsitterDto> getCatSitter(@PathVariable("id") final UUID idToFind) {
            CatsitterDto catsitterDto = catsitterService.getCatsitter(idToFind);
            return ResponseEntity.ok(catsitterDto);
    }

    @PostMapping
    public ResponseEntity<CatsitterDto> addCatSitter(@RequestBody final CatsitterInputDto catsitterInputDto, final BindingResult br) {
        if (br.hasFieldErrors()) {
            throw new ValidationException(checkForBindingResult(br));
        } else {
            CatsitterDto savedCatsitter;
            savedCatsitter = catsitterService.createCatsitter(catsitterInputDto);
            URI uri = URI.create(
                    ServletUriComponentsBuilder
                            .fromCurrentRequest()
                            .path("/" + savedCatsitter).toUriString());
            return ResponseEntity.created(uri).body(savedCatsitter);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<CatsitterDto> editCatsitter(@PathVariable("id") final UUID idToEdit, @RequestBody final CatsitterInputDto catsitter) {
        CatsitterDto editedCatsitter = catsitterService.editCatsitter(idToEdit, catsitter);

        return ResponseEntity.ok().body(editedCatsitter);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteCatsitter(@PathVariable("id") final UUID idToDelete) {
        catsitterService.deleteCatsitter(idToDelete);
        return ResponseEntity.ok().body("Catsitter with id " + idToDelete +  " removed from database");
    }

//    @PutMapping("/{customerId}/{orderId}")
//    public CatsitterDto assignOrderToCatsitter(@PathVariable("customerId") Long catsitterId, @PathVariable("orderId") long orderId) {
//        return catsitterService.assignOrderToCatsitter(catsitterId, orderId);
//    }

}
