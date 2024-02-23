package nl.novi.catsittermanager.controllers;

import lombok.extern.log4j.Log4j2;
import nl.novi.catsittermanager.dtos.cat.CatDto;
import nl.novi.catsittermanager.dtos.cat.CatInputDto;
import nl.novi.catsittermanager.exceptions.RecordNotFoundException;
import nl.novi.catsittermanager.exceptions.ValidationException;
import nl.novi.catsittermanager.services.CatServiceImplementation;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

import static nl.novi.catsittermanager.controllers.ControllerHelper.checkForBindingResult;

@RestController
@RequestMapping("/cat")
@Log4j2
public class CatController {

    private final CatServiceImplementation catService;

    public CatController(CatServiceImplementation catService) {
        this.catService = catService;
    }

    @GetMapping
    public ResponseEntity<List<CatDto>> getAllCats() {
        log.info("GET all cats");
        return ResponseEntity.ok(catService.getAllCats());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CatDto> getCat(@PathVariable("id") long idToFind) {
        if (idToFind > 0) {
            CatDto catDto = catService.getCat(idToFind);
            return ResponseEntity.ok(catDto);
        } else {
            throw new RecordNotFoundException("No cat found with this id");
        }
    }

    @PostMapping
    public ResponseEntity<CatDto> createCat(@RequestBody CatInputDto catInputDto, BindingResult br) {
        if (br.hasFieldErrors()) {
            throw new ValidationException(checkForBindingResult(br));
        } else {
            CatDto savedCat = catService.createCat(catInputDto);
            URI uri = URI.create(
                    ServletUriComponentsBuilder
                            .fromCurrentRequest()
                            .path("/" + savedCat).toUriString());
            return ResponseEntity.created(uri).body(savedCat);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<CatDto> editCat(@PathVariable("id") long idToEdit, @RequestBody CatInputDto cat) {
        CatDto editedCat = catService.editCat(idToEdit, cat);

        return ResponseEntity.ok().body(editedCat);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteCat(@PathVariable("id") long idToDelete) {
        catService.deleteCat(idToDelete);
        return ResponseEntity.noContent().build();
    }

//    @PutMapping("/{id}/customer")
//    public ResponseEntity<Object> assignCustomerToCat(@PathVariable("id") Long id,@Valid @RequestBody IdInputDto input) {
//        catService.assignCustomerToCat(id, input.id);
//        return ResponseEntity.noContent().build();
//    }

}
