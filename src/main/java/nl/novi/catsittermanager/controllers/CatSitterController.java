package nl.novi.catsittermanager.controllers;

import nl.novi.catsittermanager.dtos.catsitter.CatSitterDto;
import nl.novi.catsittermanager.dtos.catsitter.CatSitterInputDto;
import nl.novi.catsittermanager.exceptions.RecordNotFoundException;
import nl.novi.catsittermanager.exceptions.ValidationException;
import nl.novi.catsittermanager.services.CatSitterServiceImplementation;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

import static nl.novi.catsittermanager.controllers.ControllerHelper.checkForBindingResult;

@RestController
@RequestMapping("/catsitter")
public class CatSitterController {

    private final CatSitterServiceImplementation catSitterService;

    public CatSitterController(CatSitterServiceImplementation catSitterService) {
        this.catSitterService = catSitterService;
    }

    @GetMapping
    public ResponseEntity<List<CatSitterDto>> getAllCatSitters() {
        return ResponseEntity.ok(catSitterService.getAllCatSitters());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CatSitterDto> getCatSitter(@PathVariable("id") Long idToFind) {
        if (idToFind > 0) {
            CatSitterDto catSitterDto = catSitterService.getCatSitter(idToFind);
            return ResponseEntity.ok(catSitterDto);
        } else {
            throw new RecordNotFoundException("No catsitter found with this id");
        }
    }

    @PostMapping
    public ResponseEntity<CatSitterDto> addCatSitter(@RequestBody CatSitterInputDto catSitterInputDto, BindingResult br) {
        if (br.hasFieldErrors()) {
            throw new ValidationException(checkForBindingResult(br));
        } else {
            CatSitterDto savedCatSitter;
            savedCatSitter = catSitterService.createCatSitter(catSitterInputDto);
            URI uri = URI.create(
                    ServletUriComponentsBuilder
                            .fromCurrentRequest()
                            .path("/" + savedCatSitter).toUriString());
            return ResponseEntity.created(uri).body(savedCatSitter);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<CatSitterDto> editCatSitter(@PathVariable("id") long idToEdit, @RequestBody CatSitterInputDto catSitter) {
        CatSitterDto editedCatSitter= catSitterService.editCatSitter(idToEdit, catSitter);

        return ResponseEntity.ok().body(editedCatSitter);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteCatSitter(@PathVariable("id") Long idToDelete) {
        catSitterService.deleteCatSitter(idToDelete);
        return ResponseEntity.noContent().build();
    }

//    @PutMapping("/{id}/customer")
//    public ResponseEntity<Object> assignCustomerToCatSitter(@PathVariable("id") Long id,@Valid @RequestBody IdInputDto input) {
//        catSitterService.assignCustomerToCatSitter(id, input.id);
//        return ResponseEntity.noContent().build();
//    }

//    @PutMapping("/{id}/order")
//    public ResponseEntity<Object> assignOrderToCatSitter(@PathVariable("id") Long id,@Valid @RequestBody IdInputDto input) {
//        catSitterService.assignOrderToCatSitter(id, input.id);
//        return ResponseEntity.noContent().build();
//    }

}
